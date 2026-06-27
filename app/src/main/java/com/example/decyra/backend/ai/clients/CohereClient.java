package com.example.decyra.backend.ai.clients;

import com.example.decyra.backend.ai.callbacks.RerankCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.TimeUnit;

import okhttp3.*;

/**
 * The type Cohere client.
 */
public class CohereClient {
    private static final String API_KEY = "YOUR_API_KEY";
    private final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build();

    /**
     * Rerank.
     *
     * @param query    the query
     * @param matches  the matches
     * @param callback the callback
     */
    public void rerank(String query, JSONArray matches, RerankCallback callback) {
        try {
            JSONObject body = new JSONObject();
            body.put("model", "rerank-multilingual-v3.0");
            body.put("query", query);
            body.put("top_n", 5);

            JSONArray docs = new JSONArray();
            for (int i = 0; i < matches.length(); i++) {
                JSONObject meta = matches.getJSONObject(i).getJSONObject("metadata");
                docs.put(meta.toString());
            }
            body.put("documents", docs);

            Request request = new Request.Builder()
                    .url("https://api.cohere.ai/v1/rerank")
                    .addHeader("Authorization", "Bearer " + API_KEY)
                    .post(RequestBody.create(body.toString(), MediaType.get("application/json")))
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, java.io.IOException e) { callback.onSuccess(formatRaw(matches)); }

                @Override
                public void onResponse(Call call, Response response) throws java.io.IOException {
                    try {
                        if (!response.isSuccessful()) { callback.onSuccess(formatRaw(matches)); return; }
                        JSONObject res = new JSONObject(response.body().string());
                        JSONArray results = res.getJSONArray("results");
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < results.length(); i++) {
                            int idx = results.getJSONObject(i).getInt("index");
                            sb.append(formatSingle(matches.getJSONObject(idx), i + 1));
                        }
                        callback.onSuccess(sb.toString());
                    } catch (Exception e) { callback.onSuccess(formatRaw(matches)); }
                }
            });
        } catch (Exception e) { callback.onSuccess(formatRaw(matches)); }
    }

    private String formatSingle(JSONObject match, int rank) throws Exception {
        JSONObject meta = match.getJSONObject("metadata");
        StringBuilder sb = new StringBuilder("ΕΠΙΛΟΓΗ " + rank + ":\n");
        java.util.Iterator<String> keys = meta.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            sb.append("- ").append(key).append(": ").append(meta.optString(key)).append("\n");
        }
        return sb.append("\n").toString();
    }

    private String formatRaw(JSONArray matches) {
        try {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < matches.length(); i++) sb.append(formatSingle(matches.getJSONObject(i), i + 1));
            return sb.toString();
        } catch (Exception e) { return ""; }
    }
}