package com.example.phasmatic.extras;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class NotificationSender {

    private static final String FCM_URL = "https://fcm.googleapis.com/fcm/send";
    private static final String SERVER_KEY = "ΤΟ_SERVER_KEY_ΑΠΟ_FIREBASE_CONSOLE";

    public static void send(String toToken, String senderName, String messageText) {
        new Thread(() -> {
            try {
                JSONObject notification = new JSONObject();
                notification.put("title", senderName);
                notification.put("body", messageText);

                JSONObject payload = new JSONObject();
                payload.put("to", toToken);
                payload.put("notification", notification);
                payload.put("priority", "high");

                URL url = new URL(FCM_URL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                os.write(payload.toString().getBytes("UTF-8"));
                os.flush();
                os.close();

                int code = conn.getResponseCode();
                Log.d("FCM", "Response code: " + code);
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}