package com.example.decyra.backend.domain;

/**
 * The type User.
 */
public class User {

    private String id;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;

    /**
     * Is remember long.
     *
     * @return the long
     */
    public Long isRemember() {
        return remember;
    }

    /**
     * Sets remember.
     *
     * @param remember the remember
     */
    public void setRemember(Long remember) {
        this.remember = remember;
    }

    /**
     * Gets fcm token.
     *
     * @return the fcm token
     */
    public String getFcmToken() {
        return fcmToken;
    }

    /**
     * Sets fcm token.
     *
     * @param fcmToken the fcm token
     */
    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    private Long remember;
    private String fcmToken;

//    private List<Double> faceEmbedding;

    private String profileImageUrl;

    /**
     * Instantiates a new User.
     */
    public User(){
    }

    /**
     * Instantiates a new User.
     *
     * @param id          the id
     * @param fullName    the full name
     * @param email       the email
     * @param password    the password
     * @param phoneNumber the phone number
     */
    public User(String id, String fullName, String email, String password, String phoneNumber) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    private boolean gray = false;

    /**
     * Is gray boolean.
     *
     * @return the boolean
     */
    public boolean isGray() {
        return gray;
    }

    /**
     * Sets gray.
     *
     * @param gray the gray
     */
    public void setGray(boolean gray) {
        this.gray = gray;
    }

    /**
     * Gets profile image url.
     *
     * @return the profile image url
     */
    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    /**
     * Sets profile image url.
     *
     * @param profileImageUrl the profile image url
     */
    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets full name.
     *
     * @return the full name
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Sets full name.
     *
     * @param fullName the full name
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets phone number.
     *
     * @return the phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets phone number.
     *
     * @param phoneNumber the phone number
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}