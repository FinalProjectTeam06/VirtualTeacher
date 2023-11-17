package com.example.finalprojectvirtualteacher.models;

import jakarta.persistence.*;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @Column(name = "rate_value")
    private int rateValue;

    @Column(name = "rate_count")
    private int count;

    public Rate() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getRateValue() {
        return rateValue;
    }

    public void setRateValue(int rateValue) {
        this.rateValue = rateValue;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
