package com.backbase.interview.course.model;

import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

/**
 * Model class representing Course.
 */
@Data
@Entity
@Table(name = "TBL_COURSES")
public class CourseEntity {

    /**
     * Course ID.
     */
    @Id
    @GeneratedValue
    @JsonView(CreateCourseView.class)
    private Long id;

    /**
     * Course Title.
     */
    @Column(name = "title")
    @JsonView(CreateCourseView.class)
    private String title;

    /**
     * Course Start Date.
     */
    @Column(name = "start_date")
    @JsonView(CreateCourseView.class)
    private String startDate;

    /**
     * Course End Date.
     */
    @Column(name = "end_date")
    @JsonView(CreateCourseView.class)
    private String endDate;

    /**
     * Course Capacity.
     */
    @Column(name = "capacity")
    @JsonView(CreateCourseView.class)
    private Long capacity;

    /**
     * Course Remaining Capacity.
     */
    @Column(name = "remaining")
    @JsonView(CreateCourseView.class)
    private Long remaining;

    /**
     * Course Participants.
     */
    @OneToMany(mappedBy = "courseEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ParticipantEntity> participantEntities;

    @Override
    public String toString() {
        return "CourseEntity [id=" + id + ", title=" + title +
                ", startDate=" + startDate + ", endDate=" + endDate + ", capacity=" + capacity + ", remaining=" + remaining + "]";
    }
}
