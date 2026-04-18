package com.example.phasmatic.extras;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FCMTokenService extends FirebaseMessagingService {

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
    }
}