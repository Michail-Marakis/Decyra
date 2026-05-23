package com.example.decyra.backend.domain;

/**
 * The type Forum review.
 */
public class ForumReview {
    /**
     * The Id.
     */
    public String id;
    /**
     * The User id.
     */
    public String user_id;
    /**
     * The User name.
     */
    public String user_name;
    /**
     * The Type.
     */
    public String type; //erasmus,master
    /**
     * The University.
     */
    public String university;
    /**
     * The Country.
     */
    public String country;
    /**
     * The Text.
     */
    public String text;
    /**
     * The Rating.
     */
    public float rating; //1-5
    /**
     * The Likes.
     */
    public int likes = 0;
    /**
     * The Timestamp.
     */
    public String timestamp;
    /**
     * The Comments.
     */
    public int comments=0;
    /**
     * The Views.
     */
    public int views;


    /**
     * Instantiates a new Forum review.
     */
    public ForumReview() {}

    /**
     * Instantiates a new Forum review.
     *
     * @param id         the id
     * @param user_id    the user id
     * @param user_name  the user name
     * @param type       the type
     * @param university the university
     * @param country    the country
     * @param text       the text
     * @param rating     the rating
     * @param likes      the likes
     * @param timestamp  the timestamp
     * @param views      the views
     */
    public ForumReview(String id,
                       String user_id,
                       String user_name,
                       String type,
                       String university,
                       String country,
                       String text,
                       float rating,
                       long likes,
                       String timestamp,
                       int views) {

        this.id = id;
        this.user_id = user_id;
        this.user_name = user_name;
        this.type = type;
        this.university = university;
        this.country = country;
        this.text = text;
        this.rating = rating;
        this.likes = Math.toIntExact(likes);
        this.timestamp = timestamp;
        this.views = views;
    }

    /**
     * Get views int.
     *
     * @return the int
     */
    public int getViews(){
        return this.views;
    }

    /**
     * Set views.
     *
     * @param v the v
     */
    public void setViews(int v){
        this.views = v;
    }

    /**
     * Add view.
     */
    public void addView(){
        this.views++;
    }

    /**
     * Gets likes.
     *
     * @return the likes
     */
    public int getLikes() {
        return likes;
    }

    /**
     * Sets likes.
     *
     * @param likes the likes
     */
    public void setLikes(int likes) {
        this.likes = likes;
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

    /**
     * Gets user name.
     *
     * @return the user name
     */
    public String getUser_name() {
        return user_name;
    }

    /**
     * Sets user name.
     *
     * @param user_name the user name
     */
    public void setUser_name(String user_name) {
        this.user_name = user_name;
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
     * Gets university.
     *
     * @return the university
     */
    public String getUniversity() {
        return university;
    }

    /**
     * Sets university.
     *
     * @param university the university
     */
    public void setUniversity(String university) {
        this.university = university;
    }

    /**
     * Gets country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Gets text.
     *
     * @return the text
     */
    public String getText() {
        return text;
    }

    /**
     * Sets text.
     *
     * @param text the text
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * Gets rating.
     *
     * @return the rating
     */
    public float getRating() {
        return rating;
    }

    /**
     * Sets rating.
     *
     * @param rating the rating
     */
    public void setRating(float rating) {
        this.rating = rating;
    }

    /**
     * Gets timestamp.
     *
     * @return the timestamp
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * Sets timestamp.
     *
     * @param timestamp the timestamp
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

