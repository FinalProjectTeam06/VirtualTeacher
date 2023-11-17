package com.example.finalprojectvirtualteacher.services.contacts;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Topic;

import java.util.List;

public interface TopicService {
    List<Topic> getAll();
    Topic getById(int id);
}
