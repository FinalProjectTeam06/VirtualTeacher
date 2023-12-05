package com.example.finalprojectvirtualteacher.models;

import jakarta.persistence.*;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "enrolled_courses")
@Immutable
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "isFinished")
    private boolean isFinished;

    @Column(name = "graduation_status")
    private boolean isGraduated;

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Course getCourse() {
        return course;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public boolean isGraduated() {
        return isGraduated;
    }
}


