package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Role;
import com.example.finalprojectvirtualteacher.models.Topic;
import com.example.finalprojectvirtualteacher.repositories.contracts.RoleRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class RoleRepositoryImpl implements RoleRepository {
    private final SessionFactory sessionFactory;

    public RoleRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Role> getAll() {
        try (Session session= sessionFactory.openSession()){
            Query<Role> query=session.createQuery("from Role ", Role.class);
            return query.list();
        }
    }

    @Override
    public Role getById(int id) {
        try (Session session= sessionFactory.openSession()){
            Role role=session.get(Role.class, id);
            if (role == null) {
                throw new EntityNotFoundException("Role", id);
            }
            return role;
        }
    }
}
