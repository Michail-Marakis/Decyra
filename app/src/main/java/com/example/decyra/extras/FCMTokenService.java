package com.example.decyra.extras;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.decyra.R;
import com.example.decyra.frontend.Chat.chat.ChatActivity;
import com.example.decyra.frontend.conference.general_conference.GeneralConferenceActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * The type Fcm token service.
 */
public class FCMTokenService extends FirebaseMessagingService {

    private static final String CHAT_CHANNEL_ID = "chat_messages_channel";
    private static final String MEETING_CHANNEL_ID = "meeting_notifications_channel";

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);

        String uid = FirebaseAuth.getInstance().getCurrentUser() != null
                ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                : null;

        if (uid != null) {
            FirebaseDatabase.getInstance(
                    "https://mega-5a5b4-default-rtdb.europe-west1.firebasedatabase.app"
            ).getReference("users").child(uid).child("fcmToken").setValue(token);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String type = "new_message";
        if (remoteMessage.getData() != null && !remoteMessage.getData().isEmpty()) {
            String incomingType = remoteMessage.getData().get("type");
            if (incomingType != null && !incomingType.isEmpty()) {
                type = incomingType;
            }
        }

        String title = "Notification";
        String body = "";

        if (remoteMessage.getNotification() != null) {
            if (remoteMessage.getNotification().getTitle() != null) {
                title = remoteMessage.getNotification().getTitle();
            }
            if (remoteMessage.getNotification().getBody() != null) {
                body = remoteMessage.getNotification().getBody();
            }
        }

        createNotificationChannels();

        Intent intent;
        String channelId;
        int requestCode;

        if ("new_meeting".equals(type)) {
            intent = new Intent(this, GeneralConferenceActivity.class);
            channelId = MEETING_CHANNEL_ID;
            requestCode = 2;
        } else {
            intent = new Intent(this, ChatActivity.class);
            channelId = CHAT_CHANNEL_ID;
            requestCode = 1;
        }

        intent.putExtra("notification_type", type);
        intent.putExtra("notification_title", title);
        intent.putExtra("notification_body", body);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                requestCode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        notificationManager.notify((int) System.currentTimeMillis(), builder.build());
    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager == null) return;

            NotificationChannel chatChannel = new NotificationChannel(
                    CHAT_CHANNEL_ID,
                    "Chat Messages",
                    NotificationManager.IMPORTANCE_HIGH
            );
            chatChannel.setDescription("Notifications for incoming chat messages");

            NotificationChannel meetingChannel = new NotificationChannel(
                    MEETING_CHANNEL_ID,
                    "Meetings",
                    NotificationManager.IMPORTANCE_HIGH
            );
            meetingChannel.setDescription("Notifications for meeting invitations and updates");

            manager.createNotificationChannel(chatChannel);
            manager.createNotificationChannel(meetingChannel);
        }
    }
}