package com.virtualteacher.models.dtos;

import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class CourseDto {
    @Size(min = 5, max = 50, message = "Title should be between 5 and 50 characters long.")
    private String title;

    @Size(max = 1000, message = "Description can be be at most 1000 symbols in length.")
    private String description;
    private int topicId;
    private LocalDate startDate;

    public CourseDto(String title, String description, int topicId, LocalDate startDate) {
        this.title = title;
        this.description = description;
        this.topicId = topicId;
        this.startDate = startDate;
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

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
}
