package com.example.decyra.backend.domain;

/**
 * The type Note.
 */
public class Note {
    private String id;
    private String title;
    private String description;
    private long createdTime;
    private String user_id;
    private boolean pinned;

    /**
     * Instantiates a new Note.
     */
    public Note() {
    }

    /**
     * Instantiates a new Note.
     *
     * @param id          the id
     * @param title       the title
     * @param description the description
     * @param createdTime the created time
     * @param user_id     the user id
     * @param pinned      the pinned
     */
    public Note(String id, String title, String description, long createdTime, String user_id, boolean pinned) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdTime = createdTime;
        this.user_id = user_id;
        this.pinned = pinned;
    }

    /**
     * Is pinned boolean.
     *
     * @return the boolean
     */
    public boolean isPinned() {
        return pinned;
    }

    /**
     * Sets pinned.
     *
     * @param pinned the pinned
     */
    public void setPinned(boolean pinned) {
        this.pinned = pinned;
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
     * Gets title.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets title.
     *
     * @param title the title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Gets description.
     *
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets description.
     *
     * @param description the description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gets created time.
     *
     * @return the created time
     */
    public long getCreatedTime() {
        return createdTime;
    }

    /**
     * Sets created time.
     *
     * @param createdTime the created time
     */
    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUser_id() {
        return user_id;
    }

    /**
     * Sets user id.
     *
     * @param user_id the user id
     */
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}