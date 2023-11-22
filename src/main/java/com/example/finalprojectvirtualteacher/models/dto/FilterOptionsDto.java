package com.example.finalprojectvirtualteacher.models.dto;

import java.util.Optional;

public class FilterOptionsDto {
    private String title;
    private Integer topicId;
    private Integer teacherId;
    private Double rating;
    private String sortBy;
    private String sortOrder;

    public FilterOptionsDto() {
    }

    public FilterOptionsDto(String title, Integer topicId, Integer teacherId, Double rating, String sortBy, String sortOrder) {
        this.title = title;
        this.topicId = topicId;
        this.teacherId = teacherId;
        this.rating = rating;
        this.sortBy = sortBy;
        this.sortOrder = sortOrder;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getTopicId() {
        return topicId;
    }

    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(String sortOrder) {
        this.sortOrder = sortOrder;
    }
}
