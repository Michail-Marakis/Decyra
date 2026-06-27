package com.example.decyra.frontend.Profile_Menu;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import androidx.appcompat.widget.PopupMenu;

import com.example.decyra.R;
import com.example.decyra.frontend.Profile_Menu.account_settings.AccountActivity;
import com.example.decyra.frontend.conference.general_conference.GeneralConferenceActivity;
import com.example.decyra.frontend.Chat.users_to_chat.UsersActivity;
import com.example.decyra.frontend.login.LoginActivity;
import com.example.decyra.frontend.notes.Notes.NotesActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * The type Profile menu helper.
 */
public class ProfileMenuHelper {

    private final Activity activity;
    private final DatabaseReference usersRef;

    private String userId;
    private String userFullName;
    private String userEmail;
    private String userPhone;
    private Long log;

    /**
     * Instantiates a new Profile menu helper.
     *
     * @param activity     the activity
     * @param userId       the user id
     * @param userFullName the user full name
     * @param userEmail    the user email
     * @param userPhone    the user phone
     */
    public ProfileMenuHelper(Activity activity,
                             String userId,
                             String userFullName,
                             String userEmail,
                             String userPhone) {

        this.activity = activity;
        this.userId = userId;
        this.userFullName = userFullName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;

        FirebaseDatabase firebaseDb = FirebaseDatabase.getInstance(
                "CLOUD_PATH"
        );
        usersRef = firebaseDb.getReference("users");
    }

    /**
     * Gets activity.
     *
     * @return the activity
     */
    public Activity getActivity() {
        return activity;
    }

    /**
     * Gets users ref.
     *
     * @return the users ref
     */
    public DatabaseReference getUsersRef() {
        return usersRef;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Gets user full name.
     *
     * @return the user full name
     */
    public String getUserFullName() {
        return userFullName;
    }

    /**
     * Sets user full name.
     *
     * @param userFullName the user full name
     */
    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    /**
     * Gets user email.
     *
     * @return the user email
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Sets user email.
     *
     * @param userEmail the user email
     */
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    /**
     * Gets user phone.
     *
     * @return the user phone
     */
    public String getUserPhone() {
        return userPhone;
    }

    /**
     * Sets user phone.
     *
     * @param userPhone the user phone
     */
    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    /**
     * Gets log.
     *
     * @return the log
     */
    public Long getLog() {
        return log;
    }

    /**
     * Sets log.
     *
     * @param log the log
     */
    public void setLog(Long log) {
        this.log = log;
    }
}