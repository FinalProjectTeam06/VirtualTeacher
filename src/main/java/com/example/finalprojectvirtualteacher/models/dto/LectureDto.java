package com.example.finalprojectvirtualteacher.models.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LectureDto {

    @NotNull(message = "Title can't be empty.")
    @Size(min = 5, max = 50, message = "Title should be between 5 and 50 symbols.")
    private String title;

    @NotNull
    @Size(max = 1000, message = "Description can't be at most 1000 symbols.")
    private String description;

    @NotNull
    private String videoUrl;

    private String assignmentUrl;

    @NotNull
    private int courseId;

    public LectureDto() {

    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getAssignmentUrl() {
        return assignmentUrl;
    }

    public void setAssignmentUrl(String assignmentUrl) {
        this.assignmentUrl = assignmentUrl;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
