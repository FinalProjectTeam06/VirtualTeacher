package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.*;
import com.example.finalprojectvirtualteacher.repositories.contracts.CourseRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class CourseRepositoryImpl implements CourseRepository {
    private final SessionFactory sessionFactory;


    public CourseRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;

    }

    @Override
    public List<Course> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("from Course ", Course.class);
            return query.list();
        }
    }

    @Override
    public List<Course> getAllActiveCoursesNotEnrolled(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("SELECT c FROM Course c " +
                    "WHERE :userId <> ALL(SELECT u.id FROM c.enrolledUsers u) " +
                    "AND c.creator.id <> :userId", Course.class);
            query.setParameter("userId", user.getId());
            return query.list();
        }
    }

    @Override
    public List<Course> getAllPublishedCoursesFromTeacher(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("from Course c where c.creator.id= :userId AND c.isPublished= true", Course.class);
            query.setParameter("userId", user.getId());
            return query.list();
        }
    }

    @Override
    public List<Course> getAllNotPublishedCoursesFromTeacher(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createQuery("from Course c where c.creator.id= :userId AND c.isPublished= false", Course.class);
            query.setParameter("userId", user.getId());
            return query.list();
        }
    }

    @Override
    public Course getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Course course = session.get(Course.class, id);
            if (course == null) {
                throw new EntityNotFoundException("Course", id);
            }
            return course;
        }
    }


    @Override
    public List<Course> getAll(FilterOptions filterOptions) {
        try (
                Session session = sessionFactory.openSession()) {
            List<String> filters = new ArrayList<>();
            Map<String, Object> params = new HashMap<>();

            filterOptions.getTitle().ifPresent(value -> {
                filters.add("title like :title");
                params.put("title", String.format("%%%s%%", value));
            });

            filterOptions.getTopicId().ifPresent(value -> {
                filters.add("topic.id like :topicId");
                params.put("topicId", value);
            });
            filterOptions.getTeacherId().ifPresent(value -> {
                filters.add("creator.id like :teacherId");
                params.put("teacherId", value);
            });
            filterOptions.getRating().ifPresent(value -> {
                    filters.add("rating >= :rating AND rating <= :maxValue");
                    params.put("rating", value);
                    params.put("maxValue", value+0.99);

            });
            StringBuilder queryString = new StringBuilder("FROM Course");
            if (!filters.isEmpty()) {
                queryString.append(" where ")
                        .append(String.join(" and ", filters));
            }
            if (filterOptions.getSortBy().isPresent()) {
                if (!filterOptions.getSortBy().get().equals("")) {
                    queryString.append(generateOrderBy(filterOptions));
                }
            }
            Query<Course> query = session.createQuery(queryString.toString(), Course.class);
            query.setProperties(params);
            return query.list();
            }
        }

    @Override
    public List<Course> getAllByUserCompleted(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createNativeQuery(
                    "select c.* from courses c " +
                            "join enrolled_courses ec on c.course_id = ec.course_id " +
                            "where ec.user_id = :id " +
                            "and ec.isFinished=1", Course.class);
            query.setParameter("id", userId);
            return query.list();
        }
    }

    @Override
    public List<Course> getAllByUserNotCompleted(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createNativeQuery(
                    "select c.* from courses c " +
                            "join enrolled_courses ec on c.course_id = ec.course_id " +
                            "where ec.user_id = :id " +
                            "and ec.isFinished=0", Course.class);
            query.setParameter("id", userId);
            return query.list();
        }
    }

    @Override
    public List<Course> getAllByUserGraduated(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Course> query = session.createNativeQuery(
                    "select c.* from courses c " +
                            "join enrolled_courses ec on c.course_id = ec.course_id " +
                            "where ec.user_id = :id " +
                            "and ec.graduation_status=1", Course.class);
            query.setParameter("id", userId);
            return query.list();
        }
    }



    public String generateOrderBy(FilterOptions filterOptions) {
        if (filterOptions.getSortBy().isEmpty()) {
            return "";
        }
        String orderBy = "";
        switch (filterOptions.getSortBy().get()) {
            case "title":
                orderBy = "title";
                break;
            case "rating":
                orderBy = "rating";
        }
        orderBy = String.format(" order by %s", orderBy);

        if (filterOptions.getSortOrder().isPresent() &&
                filterOptions.getSortOrder().get().equalsIgnoreCase("desc")) {
            orderBy = String.format("%s desc", orderBy);
        }
        return orderBy;
    }


    public Rate getRating(int courseId, int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Rate> query = session.createQuery("from Rate r where r.course.id= :courseId AND r.user.id= :userId", Rate.class);
            query.setParameter("courseId", courseId);
            query.setParameter("userId", userId);
            if (query.list().isEmpty()) {
                throw new EntityNotFoundException("Rate", "courseId", courseId);
            }
            return query.list().get(0);
        }
    }

    @Override
    public Course create(Course course) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(course);
            session.getTransaction().commit();
        }
        return course;
    }

    @Override
    public Course update(Course course) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(course);
            session.getTransaction().commit();
        }
        return course;
    }

    @Override
    public void delete(Course course) {
        try (Session session = sessionFactory.openSession()) {

            session.beginTransaction();
            session.remove(course);
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAllCoursesFromUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            MutationQuery query4 = session.createMutationQuery(
                    "delete from Course c where c.creator.id= :userId");
            query4.setParameter("userId", userId);
            query4.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAllEnrollmentsFromCourse(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            MutationQuery query4 = session.createMutationQuery(
                    "delete from Enrollment e where e.course.id= :courseId");
            query4.setParameter("courseId", courseId);
            query4.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAllRatesFromUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            MutationQuery query = session.createMutationQuery(
                    "DELETE from Rate r where r.user.id =: userId or r.course.creator.id =: userId");
            query.setParameter("userId", userId);
            query.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAllRatesFromCourse(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            MutationQuery query2 = session.createMutationQuery(
                    "delete from Rate r where r.course.id= :courseId");
            query2.setParameter("courseId", courseId);
            query2.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public List<Lecture> getAllByTeacherId(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery(
                    ("from Lecture l where l.teacher.id = :id"), Lecture.class);
            query.setParameter("id", id);
            return query.list();
        }
    }

    @Override
    public List<Rate> getAllUserRates(int userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Rate> query = session.createQuery( "from Rate r where r.user.id = :userId", Rate.class);
            query.setParameter("userId", userId);
            return query.list();
        }
    }

    @Override
    public Course rateCourse(Rate rate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(rate);
            session.getTransaction().commit();
        }
        return rate.getCourse();
    }

    @Override
    public Course updateRating(Rate rate) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(rate);
            session.getTransaction().commit();
        }
        return rate.getCourse();
    }
}
