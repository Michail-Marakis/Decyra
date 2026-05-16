package com.example.phasmatic.data.ai.clients;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.phasmatic.data.ai.callbacks.ChatCallback;
import com.example.phasmatic.data.ai.callbacks.EmbeddingCallback;
import com.example.phasmatic.data.ai.callbacks.MessagesCallback;
import com.example.phasmatic.data.ai.callbacks.PineconeCallback;
import com.example.phasmatic.data.ai.callbacks.RerankCallback;
import com.example.phasmatic.data.model.MessageLLM;
import com.example.phasmatic.extras.ProgramType;
import com.example.phasmatic.extras.LLMRules;
import com.google.firebase.database.*;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.*;

public class OpenAIChatClient {

    private static final String OPENAI_URL =
            "https://api.openai.com/v1/chat/completions";

    private static final MediaType JSON =
            MediaType.get("application/json; charset=utf-8");

    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    private final FirebaseDatabase firebaseDb;
    private final PineconeClient pineconeClient = new PineconeClient();
    private final CohereClient reranker = new CohereClient();

    private static final String AWS_API_KEY_URL =
            "https://koa1qztjbi.execute-api.eu-north-1.amazonaws.com/prod/apikey";

    private interface ApiKeyCallback {
        void onSuccess(String apiKey);
        void onError(String error);
    }

    public OpenAIChatClient(Context context) {
        firebaseDb = FirebaseDatabase.getInstance(
                "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
        );
    }

    private void getApiKeyFromAws(ApiKeyCallback callback) {
        try {
            Log.d("AWS_API_DEBUG", "Calling URL: " + AWS_API_KEY_URL);

            Request request = new Request.Builder()
                    .url(AWS_API_KEY_URL)
                    .get()
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    String type;
                    if (e instanceof SocketTimeoutException) {
                        type = "TIMEOUT_ERROR";
                    } else {
                        type = e.getMessage();
                    }
                    Log.e("AWS_API_DEBUG", "Failure: " + type);
                    callback.onError(type);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try {
                        String body = response.body().string();
                        Log.d("AWS_API_DEBUG", "HTTP " + response.code() + " body: " + body);

                        if (!response.isSuccessful()) {
                            callback.onError("HTTP " + response.code() + ": " + body);
                            return;
                        }

                        JSONObject json = new JSONObject(body);
                        String apiKey = json.getString("apiKey");
                        callback.onSuccess(apiKey);

                    } catch (Exception e) {
                        Log.e("AWS_API_DEBUG", "Parse error: " + e.getMessage());
                        callback.onError(e.getMessage());
                    }
                }
            });
        } catch (Exception e) {
            Log.e("AWS_API_DEBUG", "Outer error: " + e.getMessage());
            callback.onError(e.getMessage());
        }
    }

    public void getEmbedding(String text, EmbeddingCallback callback) {

        getApiKeyFromAws(new ApiKeyCallback() {
            @Override
            public void onSuccess(String apiKey) {
                try {
                    JSONObject body = new JSONObject();
                    body.put("model", "text-embedding-3-small");
                    body.put("input", text);

                    Request request = new Request.Builder()
                            .url("https://api.openai.com/v1/embeddings")
                            .addHeader("Authorization", "Bearer " + apiKey)
                            .post(RequestBody.create(body.toString(), JSON))
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(@NonNull Call call, @NonNull IOException e) {
                            String type;
                            if (e instanceof SocketTimeoutException) {
                                type = "TIMEOUT_ERROR";
                            } else {
                                type = e.getMessage();
                            }
                            callback.onError(type);
                        }

                        @Override
                        public void onResponse(@NonNull Call call, @NonNull Response response) {
                            try {
                                JSONObject json = new JSONObject(response.body().string());
                                JSONArray emb = json.getJSONArray("data")
                                        .getJSONObject(0)
                                        .getJSONArray("embedding");

                                float[] vector = new float[emb.length()];
                                for (int i = 0; i < emb.length(); i++) {
                                    vector[i] = (float) emb.getDouble(i);
                                }

                                callback.onSuccess(vector);
                            } catch (Exception e) {
                                callback.onError(e.getMessage());
                            }
                        }
                    });

                } catch (Exception e) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private MessageLLM saveMessage(String conversationId, String role, String content) {

        DatabaseReference ref = firebaseDb.getReference("messages_llm").push();

        MessageLLM msg = new MessageLLM(
                ref.getKey(),
                conversationId,
                role,
                content,
                System.currentTimeMillis()
        );

        ref.setValue(msg);

        return msg;
    }

    private void getLastMessages(
            String conversationId,
            int limit,
            MessagesCallback callback
    ) {

        firebaseDb.getReference("messages_llm")
                .orderByChild("conversationId")
                .equalTo(conversationId)
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        ArrayList<MessageLLM> list = new ArrayList<>();

                        for (DataSnapshot ds : snapshot.getChildren()) {

                            MessageLLM m = ds.getValue(MessageLLM.class);
                            if (m != null) list.add(m);
                        }

                        list.sort((a, b) ->
                                Long.compare(a.getTimestamp(), b.getTimestamp())
                        );

                        if (list.size() > limit) {
                            list = new ArrayList<>(
                                    list.subList(list.size() - limit, list.size())
                            );
                        }

                        callback.onSuccess(list);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callback.onError(error.getMessage());
                    }
                });
    }

    public void sendMessage(
            int num,
            String conversationId,
            String userMessage,
            String userFullName,
            ChatCallback callback
    ) {
        MessageLLM userMsg = saveMessage(conversationId, "user", userMessage);

        getApiKeyFromAws(new ApiKeyCallback() {
            @Override
            public void onSuccess(String apiKey) {
                RouterClient router = new RouterClient(client, apiKey);

                router.classifyIntent(userMessage, isFollowUp -> {
                    if (isFollowUp) {
                        Log.d("ROUTER_LOGIC", "Follow-up detected. Using History.");
                        buildPrompt(apiKey, conversationId, userMessage, "", callback, userFullName, getProgramType(num));
                    } else {
                        Log.d("ROUTER_LOGIC", "Search detected. Starting RAG flow.");
                        getEmbedding(userMessage, new EmbeddingCallback() {
                            @Override
                            public void onSuccess(float[] embedding) {
                                pineconeClient.upsertChatHistory(
                                        embedding,
                                        userMsg.getId(),
                                        userFullName,
                                        "user",
                                        userMessage,
                                        conversationId,
                                        userMsg.getTimestamp()
                                );
                                runRag(num, embedding, conversationId, userMessage, callback, userFullName);
                            }

                            @Override
                            public void onError(String error) {
                                runRag(num, null, conversationId, userMessage, callback, userFullName);
                            }
                        });
                    }
                });
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private void runRag(
            int num,
            float[] embedding,
            String conversationId,
            String userMessage,
            ChatCallback callback,
            String userFullName
    ) {

        getApiKeyFromAws(new ApiKeyCallback() {
            @Override
            public void onSuccess(String apiKey) {

                String[] namespaces;
                String[] indexes;
                ProgramType programType;

                if (num == 0) {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-AUEB"};
                    indexes = new String[]{"Education"};
                } else if (num == 1) {
                    programType = ProgramType.master;
                    namespaces = new String[]{"europe-master"};
                    indexes = new String[]{"master"};
                } else if (num == 2) {
                    programType = ProgramType.career;
                    namespaces = new String[]{"main-career"};
                    indexes = new String[]{"career"};
                } else if (num == 4) {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-THESSALY"};
                    indexes = new String[]{"Education"};
                } else if (num == 5) {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-ARISTOTLE"};
                    indexes = new String[]{"Education"};
                } else if (num == 6) {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-EKPA"};
                    indexes = new String[]{"Education"};
                } else if (num == 7) {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-CRETE"};
                    indexes = new String[]{"Education"};
                } else if (num == 8) {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-PAPEI"};
                    indexes = new String[]{"Education"};
                } else if (num == 9) {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-PELLOPONESE"};
                    indexes = new String[]{"Education"};
                } else if (num == 10) {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-HAROKOPIO"};
                    indexes = new String[]{"Education"};
                } else {
                    programType = ProgramType.erasmus;
                    namespaces = new String[]{"erasmus-IONIAN"};
                    indexes = new String[]{"Education"};
                }

                if (embedding == null) {
                    buildPrompt(apiKey, conversationId, userMessage, "", callback, userFullName, programType);
                    return;
                }

                queryNext(0, namespaces, indexes, embedding, new JSONArray(),
                        apiKey, conversationId, userMessage, callback, programType, userFullName);
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private void queryNext(
            int index,
            String[] namespaces,
            String[] indexes,
            float[] embedding,
            JSONArray allMatches,
            String apiKey,
            String conversationId,
            String userMessage,
            ChatCallback callback,
            ProgramType programType,
            String userFullName
    ) {

        if (index == namespaces.length) {
            reranker.rerank(userMessage, allMatches, new RerankCallback() {
                @Override
                public void onSuccess(String rankedContext) {
                    buildPrompt(apiKey, conversationId, userMessage, rankedContext, callback, userFullName, programType);
                }
            });
            return;
        }

        pineconeClient.queryIndex(
                embedding,
                namespaces[index],
                indexes[index],
                new PineconeCallback() {

                    @Override
                    public void onSuccess(JSONArray matches) {
                        try {
                            for (int i = 0; i < matches.length(); i++) {
                                allMatches.put(matches.getJSONObject(i));
                            }
                        } catch (Exception e) {
                            Log.e("RAG_DEBUG", "Error merging matches: " + e.getMessage());
                        }

                        queryNext(index + 1, namespaces, indexes, embedding,
                                allMatches, apiKey, conversationId, userMessage, callback, programType, userFullName);
                    }

                    @Override
                    public void onError(String error) {
                        queryNext(index + 1, namespaces, indexes, embedding,
                                allMatches, apiKey, conversationId, userMessage, callback, programType, userFullName);
                    }
                }
        );
    }

    private void buildPrompt(
            String apiKey,
            String conversationId,
            String userMessage,
            String ragContext,
            ChatCallback callback,
            String userFullName,
            ProgramType programType
    ) {

        //limit = 6 gia to posa msgs na vlepei
        getLastMessages(conversationId, 6, new MessagesCallback() {

            @Override
            public void onSuccess(ArrayList<MessageLLM> history) {

                try {

                    JSONArray messages = new JSONArray();

                    String systemPrompt;

                    systemPrompt = LLMRules.buildBasePrompt(programType);

                    messages.put(new JSONObject()
                            .put("role", "system")
                            .put("content", systemPrompt)
                    );

                    if (!ragContext.isEmpty()) {
                        messages.put(new JSONObject()
                                .put("role", "system")
                                .put("content", "RAG CONTEXT:\n" + ragContext));
                    } else {
                        messages.put(new JSONObject()
                                .put("role", "system")
                                .put("content", "Note: User is asking about previous recommendations. Use history."));
                    }

                    for (MessageLLM m : history) {
                        messages.put(new JSONObject()
                                .put("role", m.getRole())
                                .put("content", m.getContent()));
                    }

                    messages.put(new JSONObject()
                            .put("role", "user")
                            .put("content", userMessage));

                    callOpenAI(apiKey, messages, conversationId, callback, userFullName);

                } catch (Exception e) {
                    callback.onError(e.getMessage());
                }
            }

            @Override
            public void onError(String error) {
                callback.onError(error);
            }
        });
    }

    private void callOpenAI(
            String apiKey,
            JSONArray messages,
            String conversationId,
            ChatCallback callback,
            String userFullName
    ) {

        try {

            JSONObject body = new JSONObject();
            body.put("model", "gpt-4.1");
            body.put("messages", messages);

            Request request = new Request.Builder()
                    .url(OPENAI_URL)
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .post(RequestBody.create(body.toString(), JSON))
                    .build();

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    String type = "";
                    if (e instanceof SocketTimeoutException) {
                        type = "TIMEOUT_ERROR";
                    } else {
                        type = e.getMessage();
                    }
                    callback.onError(type);
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {

                    try {

                        String content = new JSONObject(response.body().string())
                                .getJSONArray("choices")
                                .getJSONObject(0)
                                .getJSONObject("message")
                                .getString("content");

                        MessageLLM assistant = saveMessage(conversationId, "assistant", content);

                        getEmbedding(content, new EmbeddingCallback() {
                            @Override
                            public void onSuccess(float[] embeddingVector) {

                                pineconeClient.upsertChatHistory(
                                        embeddingVector,
                                        assistant.getId(),
                                        userFullName + "-SystemResp",
                                        "assistant",
                                        content,
                                        conversationId,
                                        Long.valueOf(String.valueOf(assistant.getTimestamp()))
                                );
                            }

                            @Override
                            public void onError(String error) {
                            }
                        });

                        callback.onSuccess(content);

                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                }
            });

        } catch (Exception e) {
            callback.onError(e.getMessage());
        }
    }

    private ProgramType getProgramType(int num) {
        if (num == 1) return ProgramType.master;
        if (num == 2) return ProgramType.career;
        return ProgramType.erasmus;
    }
}