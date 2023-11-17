package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.Topic;
import com.example.finalprojectvirtualteacher.repositories.contracts.TopicRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TopicRepositoryImpl implements TopicRepository {
    private final SessionFactory sessionFactory;

    public TopicRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Topic> getAll() {
        try (Session session= sessionFactory.openSession()){
            Query<Topic> query=session.createQuery("from Topic ", Topic.class);
            return query.list();
        }
    }

    @Override
    public Topic getById(int id) {
        try (Session session= sessionFactory.openSession()){
            Topic topic=session.get(Topic.class, id);
            if (topic == null) {
                throw new EntityNotFoundException("Topic", id);
            }
            return topic;
        }
    }
}
