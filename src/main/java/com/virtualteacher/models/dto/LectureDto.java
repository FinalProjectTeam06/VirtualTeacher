package com.virtualteacher.models.dto;

import com.virtualteacher.models.Course;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class LectureDto {

    @NotNull(message = "Title can't be empty.")
    @Size(min = 5, max = 50, message = "Title should be between 5 and 50 symbols.")
    private String title;


    @Size(max = 1000, message = "Description can't be at most 1000 symbols.")
    private String description;

    @NotNull
    private String videoUrl;

    @NotNull
    private String assignment;

    @NotNull
    private int courseId;

public LectureDto(){

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

    public String getAssignment() {
        return assignment;
    }

    public void setAssignment(String assignment) {
        this.assignment = assignment;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}
