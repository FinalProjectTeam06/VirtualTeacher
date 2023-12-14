package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.*;
import com.example.finalprojectvirtualteacher.repositories.contracts.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    public List<User> getAllStudents() {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where role.id= :roleId", User.class);
            query.setParameter("roleId", 3);

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

            MutationQuery query = session.createMutationQuery(
                    "delete from Assignment a where a.user.id= :userId or a.lecture.teacher.id= :userId");
            query.setParameter("userId", user.getId());
            query.executeUpdate();
            session.getTransaction().commit();

            session.beginTransaction();
            MutationQuery query1 = session.createMutationQuery(
                    "delete from Comment c where c.creator.id= :userId or c.lecture.teacher.id= :userId");
            query1.setParameter("userId", user.getId());
            query1.executeUpdate();
            session.getTransaction().commit();

            session.beginTransaction();
            MutationQuery query2 = session.createMutationQuery(
                    "delete from Note n where n.userId= :userId or n.lecture.teacher.id= :userId");
            query2.setParameter("userId", user.getId());
            query2.executeUpdate();
            session.getTransaction().commit();

            session.beginTransaction();
            MutationQuery query3 = session.createMutationQuery(
                    "delete from Rate r where r.user.id= :userId or r.course.creator.id= :userId");
            query3.setParameter("userId", user.getId());
            query3.executeUpdate();
            session.getTransaction().commit();

            session.beginTransaction();
            MutationQuery query5 = session.createMutationQuery(
                    "delete from Lecture l where l.teacher.id= :userId");
            query5.setParameter("userId", user.getId());
            query5.executeUpdate();
            session.getTransaction().commit();

            session.beginTransaction();
            MutationQuery query4 = session.createMutationQuery(
                    "delete from Course c where c.creator.id= :userId");
            query4.setParameter("userId", user.getId());
            query4.executeUpdate();
            session.getTransaction().commit();

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

    @Override
    public void setCourseEnrollmentStatusToFinished(int userId, int courseId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Course> query = session.createNativeQuery("UPDATE enrolled_courses e set e.isFinished = 1" +
                    " where e.user_id = :userId and e.course_id = :courseId", Course.class);
            query.setParameter("userId", userId);
            query.setParameter("courseId", courseId);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void setEnrollmentCourseStatusToGraduated(int userId, int courseId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query<Course> query = session.createNativeQuery("UPDATE enrolled_courses e set e.graduation_status = 1" +
                    " where e.user_id = :userId and e.course_id = :courseId", Course.class);
            query.setParameter("userId", userId);
            query.setParameter("courseId", courseId);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }
}
