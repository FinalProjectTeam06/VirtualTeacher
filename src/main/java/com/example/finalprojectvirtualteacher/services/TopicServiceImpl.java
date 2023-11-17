package com.example.finalprojectvirtualteacher.services;

import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Topic;
import com.example.finalprojectvirtualteacher.repositories.contracts.TopicRepository;
import com.example.finalprojectvirtualteacher.services.contacts.TopicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    @Override
    public List<Topic> getAll() {
        return topicRepository.getAll();
    }

    @Override
    public Topic getById(int id) {
        return topicRepository.getById(id);
    }
}
