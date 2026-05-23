package com.example.decyra.backend.domain;

/**
 * The type User expectation.
 */
public class UserExpectation {
    private String id;
    private String userId;
    private String type;         //erasmus,master,general_advisor
    private String expectations;

    /**
     * Instantiates a new User expectation.
     */
    public UserExpectation(){
    }

    /**
     * Instantiates a new User expectation.
     *
     * @param id           the id
     * @param userId       the user id
     * @param type         the type
     * @param expectations the expectations
     */
    public UserExpectation(String id, String userId, String type, String expectations) {
        this.id = id;
        this.userId = userId;
        this.type = type;
        this.expectations = expectations;
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
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets expectations.
     *
     * @return the expectations
     */
    public String getExpectations() {
        return expectations;
    }

    /**
     * Sets expectations.
     *
     * @param expectations the expectations
     */
    public void setExpectations(String expectations) {
        this.expectations = expectations;
    }
}
