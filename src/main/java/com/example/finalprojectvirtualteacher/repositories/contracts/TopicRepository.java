package com.example.finalprojectvirtualteacher.repositories.contracts;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Topic;

import java.util.List;

public interface TopicRepository {
    List<Topic> getAll();

    Topic getById(int id);
}
