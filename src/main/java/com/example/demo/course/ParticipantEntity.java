package com.example.demo.course;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;

/**
 * Model class representing Participant.
 */
@Data
@Entity
@Table(name = "TBL_PARTICIPANT")
public class ParticipantEntity {

    /**
     * Participant ID.
     */
    @Id
    @GeneratedValue
    @JsonIgnore
    private Long id;

    /**
     * Participant Name.
     */
    @Column(name = "name")
    private String name;

    /**
     * Participant Registration Date.
     */
    @Column(name = "registration_date")
    private String registrationDate;

    /**
     * Courses enrolled by the participant.
     */
    @ManyToOne()
    @JoinColumn(name = "course_id")
    @JsonIgnore
    private CourseEntity courseEntity;

    @Override
    public String toString() {
        return "ParticipantEntity [id=" + id + ", name=" + name + ", registrationDate="
                + registrationDate + "]";
    }
}
