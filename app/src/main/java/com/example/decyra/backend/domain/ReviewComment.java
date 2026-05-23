package com.example.decyra.backend.domain;

/**
 * The type Review comment.
 */
public class ReviewComment {

    /**
     * The Id.
     */
    public String id;
    /**
     * The Review id.
     */
    public String review_id;
    /**
     * The User id.
     */
    public String user_id;
    /**
     * The User name.
     */
    public String user_name;
    /**
     * The Academic profile.
     */
    public String academic_profile;
    /**
     * The Comment text.
     */
    public String comment_text;
    /**
     * The Created at.
     */
    public String created_at;

    /**
     * Instantiates a new Review comment.
     */
    public ReviewComment() {
    }

    /**
     * Instantiates a new Review comment.
     *
     * @param id           the id
     * @param review_id    the review id
     * @param user_id      the user id
     * @param user_name    the user name
     * @param comment_text the comment text
     * @param created_at   the created at
     */
    public ReviewComment(String id, String review_id, String user_id, String user_name, String comment_text, String created_at) {
        this.id = id;
        this.review_id = review_id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.comment_text = comment_text;
        this.created_at = created_at;
    }
}
