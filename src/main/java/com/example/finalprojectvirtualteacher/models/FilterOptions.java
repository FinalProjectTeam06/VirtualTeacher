package com.example.finalprojectvirtualteacher.models;

import java.util.Optional;

public class FilterOptions {
    private Optional<String> title;

    private Optional<String> topic;
    private Optional<Integer> teacherId;
    private Optional<Double> rating;

    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterOptions(String title,
                         String topic,
                         Integer teacherId,
                         Double rating,
                         String sortBy,
                         String sortOrder) {

        this.title = Optional.ofNullable(title);
        this.topic = Optional.ofNullable(topic);
        this.teacherId = Optional.ofNullable(teacherId);
        this.rating = Optional.ofNullable(rating);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getTitle() {
        return title;
    }

    public Optional<String> getTopic() {
        return topic;
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
