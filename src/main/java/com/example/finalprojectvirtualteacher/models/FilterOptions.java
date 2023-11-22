package com.example.finalprojectvirtualteacher.models;

import java.util.Optional;

public class FilterOptions {
    private Optional<String> title;

    private Optional<Integer> topicId;
    private Optional<Integer> teacherId;
    private Optional<Double> rating;

    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions(String title,
                         Integer topicId,
                         Integer teacherId,
                         Double rating,
                         String sortBy,
                         String sortOrder) {

        this.title = Optional.ofNullable(title);
        this.topicId = Optional.ofNullable(topicId);
        this.teacherId = Optional.ofNullable(teacherId);
        this.rating = Optional.ofNullable(rating);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<Integer> getTopicId() {
        return topicId;
    }

    public Optional<Integer> getTeacherId() {
        return teacherId;
    }

    public Optional<Double> getRating() {
        return rating;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
