package com.example.decyra.backend.domain;

import java.util.List;

/**
 * The type User face embedding.
 */
public class User_Face_Embedding {

    private String id;
    private String userId;
    /**
     * The User.
     */
    User user;
    private List<List<Double>> faceEmbeddings;

    /**
     * Instantiates a new User face embedding.
     */
    public User_Face_Embedding() {
        // Required empty constructor for Firebase
    }

    /**
     * Instantiates a new User face embedding.
     *
     * @param id             the id
     * @param userId         the user id
     * @param faceEmbeddings the face embeddings
     * @param user           the user
     */
    public User_Face_Embedding(String id, String userId, List<List<Double>> faceEmbeddings, User user) {
        this.id = id;
        this.userId = userId;
        this.faceEmbeddings = faceEmbeddings;
        this.user = user;
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
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Gets face embeddings.
     *
     * @return the face embeddings
     */
    public List<List<Double>> getFaceEmbeddings() {
        return faceEmbeddings;
    }

    /**
     * Sets face embeddings.
     *
     * @param faceEmbeddings the face embeddings
     */
    public void setFaceEmbeddings(List<List<Double>> faceEmbeddings) {
        this.faceEmbeddings = faceEmbeddings;
    }
}