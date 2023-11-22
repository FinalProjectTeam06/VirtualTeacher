package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Course;
import com.example.finalprojectvirtualteacher.models.UserFilterOptions;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import com.example.finalprojectvirtualteacher.models.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<User> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            return query.list();
        }
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public List<User> getAllTeachers() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where role.id= :roleId", User.class);
            query.setParameter("roleId", 2);

            return query.list();
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email= :email", User.class);
            query.setParameter("email", email);

            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }

    @Override
    public User create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(user);
            session.getTransaction().commit();
        }
        return user;
    }

    @Override
    public User updateUser(User updatedUser) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(updatedUser);
            session.getTransaction().commit();
        }
        return updatedUser;
    }

    @Override
    public void deleteUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        }
    }


    @Override
    public List<User> getAll(UserFilterOptions userFilterOptions) {
        try (
                Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            userFilterOptions.getFirstName().ifPresent(value -> {
                filters.add("firstName like :firstName ");
                params.put("firstName", String.format("%%%s%%", value));
            });
            userFilterOptions.getLastName().ifPresent(value -> {
                filters.add("lastName like :lastName ");
                params.put("lastName", String.format("%%%s%%", value));
            });
            userFilterOptions.getEmail().ifPresent(value -> {
                filters.add("email like :email ");
                params.put("email", String.format("%%%s%%", value));
            });
            StringBuilder queryString = new StringBuilder("from User ");
            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }

            Query<User> query = session.createQuery(queryString.toString(), User.class);
            query.setProperties(params);
            List<User> users = query.list();
            return query.list();
        }
    }

    private void deleteCoursesFromUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<?> query = session.createNativeQuery(
                    "delete from virtual_teacher.enrolled_courses where user_id= :userId", User.class);
            query.setParameter("userId", userId);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
