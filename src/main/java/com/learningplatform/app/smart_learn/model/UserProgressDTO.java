package com.learningplatform.app.smart_learn.model;


public class UserProgressDTO {

    private Integer progressId;
    private Boolean completionStatus;
    private Integer user;
    private Integer course;

    public Integer getProgressId() {
        return progressId;
    }

    public void setProgressId(final Integer progressId) {
        this.progressId = progressId;
    }

    public Boolean getCompletionStatus() {
        return completionStatus;
    }

    public void setCompletionStatus(final Boolean completionStatus) {
        this.completionStatus = completionStatus;
    }

    public Integer getUser() {
        return user;
    }

    public void setUser(final Integer user) {
        this.user = user;
    }

    public Integer getCourse() {
        return course;
    }

    public void setCourse(final Integer course) {
        this.course = course;
    }

}
