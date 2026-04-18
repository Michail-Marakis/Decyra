package com.example.phasmatic.extras;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class NotificationSender {

    private static final String FUNCTION_URL =
            "https://sbzxqcwvbbgbpykyvmfa.supabase.co/functions/v1/send-email";

    public static void send(String toToken, String senderName, String messageText) {
        new Thread(() -> {
            try {
                Log.d("SUPABASE_FCM", "Step 1 - start");
                URL url = new URL(FUNCTION_URL);

                Log.d("SUPABASE_FCM", "Step 2 - url ok: " + FUNCTION_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();

                Log.d("SUPABASE_FCM", "Step 3 - connection opened");
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);

                JSONObject payload = new JSONObject();
                payload.put("token", toToken);
                payload.put("title", senderName);
                payload.put("body", messageText);

                Log.d("SUPABASE_FCM", "Step 4 - payload: " + payload);

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();

                Log.d("SUPABASE_FCM", "Step 5 - payload sent");

                int code = conn.getResponseCode();
                Log.d("SUPABASE_FCM", "Response code: " + code);

                InputStream is = (code >= 200 && code < 300)
                        ? conn.getInputStream()
                        : conn.getErrorStream();

                if (is != null) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is));
                    String line;
                    StringBuilder sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                    br.close();
                    Log.d("SUPABASE_FCM", "Response body: " + sb.toString());
                }

                conn.disconnect();

            } catch (Exception e) {
                Log.e("SUPABASE_FCM", "Error sending notification", e);
                e.printStackTrace();
            }
        }).start();
    }
}