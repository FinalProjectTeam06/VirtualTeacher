package com.example.finalprojectvirtualteacher.repositories;

import com.example.finalprojectvirtualteacher.exceptions.EntityNotFoundException;
import com.example.finalprojectvirtualteacher.models.Assignment;
import com.example.finalprojectvirtualteacher.models.Lecture;
import com.example.finalprojectvirtualteacher.models.Note;
import com.example.finalprojectvirtualteacher.repositories.contracts.LectureRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.MutationQuery;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LectureRepositoryImpl implements LectureRepository {

    private final SessionFactory sessionFactory;


    @Autowired
    public LectureRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    @Override
    public List<Lecture> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery("from Lecture", Lecture.class);
            return query.list();
        }
    }

    @Override
    public Lecture getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Lecture lecture = session.get(Lecture.class, id);
            if (lecture == null) {
                throw new EntityNotFoundException("Lecture", id);
            }
            return lecture;
        }
    }

    @Override
    public List<Lecture> lecturesByCourseId(int id) {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery
                    ("from Lecture l where l.course.id= :id", Lecture.class);
            query.setParameter("id", id);
            return query.getResultList();
        }
    }


    @Override
    public Lecture getByTitle(String title) {
        try (Session session = sessionFactory.openSession()) {
            Query<Lecture> query = session.createQuery
                    ("from Lecture where lower(title) = lower(:title)", Lecture.class);
            query.setParameter("title", title);
            List<Lecture> lectures = query.getResultList();
            if (lectures.isEmpty()) {
                throw new EntityNotFoundException("Lecture", "title", title);
            }
            return lectures.get(0);
        }
    }




    @Override
    public Lecture create(Lecture lecture) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(lecture);
            session.getTransaction().commit();
        }
        return lecture;
    }

    @Override
    public Lecture update(Lecture updatedLecture) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(updatedLecture);
            session.getTransaction().commit();
        }

        return updatedLecture;
    }


    @Override
    public void delete(int id) {
        Lecture lecture = getById(id);
        try (Session session = sessionFactory.openSession()) {
      //      deleteLecture(id);
            session.beginTransaction();
            session.remove(lecture);
            session.getTransaction().commit();
        }
    }

//    private void deleteLecture(int id) {
//        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
//            Query<?> query = session.createNativeQuery(
//                    "delete from virtual_teacher.course_lectures where lecture_id=:id", Lecture.class);
//            query.setParameter("id", id);
//            query.executeUpdate();
//            session.getTransaction().commit();
//
//        }
//    }

    @Override
    public void deleteAllLecturesByUser(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            MutationQuery query5 = session.createMutationQuery(
                    "delete from Lecture l where l.teacher.id= :userId");
            query5.setParameter("userId", userId);
            query5.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public void deleteAllLecturesFromCourse(int courseId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            MutationQuery query3 = session.createMutationQuery(
                    "delete from Lecture l where l.course.id= :courseId");
            query3.setParameter("courseId", courseId);
            query3.executeUpdate();
            session.getTransaction().commit();
        }
    }

    @Override
    public Lecture submitAssignment(Assignment assignment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(assignment);
            session.getTransaction().commit();
        }
        return assignment.getLecture();
    }
}

