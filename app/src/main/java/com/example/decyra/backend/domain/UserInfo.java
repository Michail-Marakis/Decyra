package com.example.decyra.backend.domain;

/**
 * The type User info.
 */
public class UserInfo {
    private String userId;
    private String university;
    private String academicLevel;
    private String languages;
    private String gpa;
    private String field;
    private Double budgetPerYear;
    private Integer yearOfStudies;
    private String advisorType;
    private String advisorImage;

    /**
     * Instantiates a new User info.
     */
    public UserInfo() {
    }

    /**
     * Instantiates a new User info.
     *
     * @param userId        the user id
     * @param university    the university
     * @param academicLevel the academic level
     * @param languages     the languages
     * @param gpa           the gpa
     * @param field         the field
     * @param budgetPerYear the budget per year
     * @param yearOfStudies the year of studies
     * @param advisorType   the advisor type
     * @param advisorImage  the advisor image
     */
    public UserInfo(String userId,
                    String university,
                    String academicLevel,
                    String languages,
                    String gpa,
                    String field,
                    Double budgetPerYear,
                    Integer yearOfStudies,
                    String advisorType,
                    String advisorImage) {
        this.userId = userId;
        this.university = university;
        this.academicLevel = academicLevel;
        this.languages = languages;
        this.gpa = gpa;
        this.field = field;
        this.budgetPerYear = budgetPerYear;
        this.yearOfStudies = yearOfStudies;
        this.advisorType = advisorType;
        this.advisorImage = advisorImage;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public String getUserId() { return userId; }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(String userId) { this.userId = userId; }

    /**
     * Gets university.
     *
     * @return the university
     */
    public String getUniversity() { return university; }

    /**
     * Sets university.
     *
     * @param university the university
     */
    public void setUniversity(String university) { this.university = university; }

    /**
     * Gets academic level.
     *
     * @return the academic level
     */
    public String getAcademicLevel() { return academicLevel; }

    /**
     * Sets academic level.
     *
     * @param academicLevel the academic level
     */
    public void setAcademicLevel(String academicLevel) { this.academicLevel = academicLevel; }

    /**
     * Gets languages.
     *
     * @return the languages
     */
    public String getLanguages() { return languages; }

    /**
     * Sets languages.
     *
     * @param languages the languages
     */
    public void setLanguages(String languages) { this.languages = languages; }

    /**
     * Gets gpa.
     *
     * @return the gpa
     */
    public String getGpa() { return gpa; }

    /**
     * Sets gpa.
     *
     * @param gpa the gpa
     */
    public void setGpa(String gpa) { this.gpa = gpa; }

    /**
     * Gets field.
     *
     * @return the field
     */
    public String getField() { return field; }

    /**
     * Sets field.
     *
     * @param field the field
     */
    public void setField(String field) { this.field = field; }

    /**
     * Gets budget per year.
     *
     * @return the budget per year
     */
    public Double getBudgetPerYear() { return budgetPerYear; }

    /**
     * Sets budget per year.
     *
     * @param budgetPerYear the budget per year
     */
    public void setBudgetPerYear(Double budgetPerYear) { this.budgetPerYear = budgetPerYear; }

    /**
     * Gets year of studies.
     *
     * @return the year of studies
     */
    public Integer getYearOfStudies() { return yearOfStudies; }

    /**
     * Sets year of studies.
     *
     * @param yearOfStudies the year of studies
     */
    public void setYearOfStudies(Integer yearOfStudies) { this.yearOfStudies = yearOfStudies; }

    /**
     * Gets advisor type.
     *
     * @return the advisor type
     */
    public String getAdvisorType() { return advisorType; }

    /**
     * Sets advisor type.
     *
     * @param advisorType the advisor type
     */
    public void setAdvisorType(String advisorType) { this.advisorType = advisorType; }

    /**
     * Gets advisor image.
     *
     * @return the advisor image
     */
    public String getAdvisorImage() { return advisorImage; }

    /**
     * Sets advisor image.
     *
     * @param advisorImage the advisor image
     */
    public void setAdvisorImage(String advisorImage) { this.advisorImage = advisorImage; }
}