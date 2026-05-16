package com.example.decyra.data.ai.clients;

import com.example.decyra.data.ai.callbacks.RouterCallback;

import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.*;
import java.io.IOException;

public class RouterClient {
    private final OkHttpClient client;
    private final String apiKey;


    public RouterClient(OkHttpClient client, String apiKey) {
        this.client = client;
        this.apiKey = apiKey;
    }

    public void classifyIntent(String userMessage, RouterCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("model", "gpt-4o-mini");

            JSONArray messages = new JSONArray();
            messages.put(new JSONObject()
                    .put("role", "system")
                    .put("content", "You are an AI router. " +
                            "Determine if the user message is a NEW SEARCH for universities or a FOLLOW-UP " +
                            "question/analysis about previously mentioned options. " +
                            "Respond ONLY with 'SEARCH' or 'FOLLOW_UP'.")
            );
            messages.put(new JSONObject().put("role", "user").put("content", userMessage));

            body.put("messages", messages);
            body.put("max_tokens", 5);
            body.put("temperature", 0);

            Request request = new Request.Builder()
                    .url("https://api.openai.com/v1/chat/completions")
                    .addHeader("Authorization", "Bearer " + apiKey)
                    .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) { callback.onResult(false); }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        String content = new JSONObject(response.body().string())
                                .getJSONArray("choices").getJSONObject(0)
                                .getJSONObject("message").getString("content").trim();

                        callback.onResult(content.contains("FOLLOW_UP"));
                    } catch (Exception e) { callback.onResult(false); }
                }
            });
        } catch (Exception e) { callback.onResult(false); }
    }
}