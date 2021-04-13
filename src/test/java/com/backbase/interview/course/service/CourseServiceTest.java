package com.backbase.interview.course.service;

import com.backbase.interview.course.model.CourseEntity;
import com.backbase.interview.course.model.ParticipantEntity;
import com.backbase.interview.course.repository.CourseRepository;
import com.backbase.interview.course.repository.ParticipantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

/**
 * Class to test the CourseService.
 */
@ExtendWith(SpringExtension.class)
public class CourseServiceTest {
    /**
     * The course repository.
     */
    @Mock
    CourseRepository courseRepository;

    /**
     * The participant repository.
     */
    @Mock
    ParticipantRepository participantRepository;

    /**
     * The course service.
     */
    @InjectMocks
    CourseService courseService;

    /**
     * The CourseEntity.
     */
    CourseEntity courseEntity = new CourseEntity();

    /**
     * The ParticipantEntity.
     */
    ParticipantEntity participantEntity = new ParticipantEntity();

    /**
     * Init method.
     */
    @BeforeEach
    public void setup() {
        courseEntity.setTitle("Course A");
        courseEntity.setCapacity(10L);
        courseEntity.setRemaining(10L);
        courseEntity.setStartDate("2020-05-01");
        courseEntity.setEndDate("2020-05-05");

        participantEntity.setName("Daniel");
        participantEntity.setRegistrationDate("2020-04-27");
        participantEntity.setCourseEntity(courseEntity);
    }


    /**
     * Method to test courseService.createCourse()
     */
    @Test
    void createCourseTest() {
        when(courseRepository.save(courseEntity)).thenReturn(courseEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.createCourse(courseEntity));
        assertTrue(persistedCourseEntity.isPresent());
    }

    /**
     * Method to test courseService.getCourseById()
     */
    @Test
    void getCourseByIdTest() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        Optional<Object> testCourseEntity = Optional.ofNullable(courseService.getCourseById(1L));
        assertTrue(testCourseEntity.isPresent());
    }

    /**
     * Method to test courseService.getCourseByTitle()
     */
    @Test
    void getCoursesByTitleTest() {
        when(courseRepository.findByTitle("test")).thenReturn(courseEntity);
        Optional<Object> testCourseEntity = Optional.of(courseService.getCourseByTitle("test"));
        assertTrue(testCourseEntity.isPresent());
    }

    /**
     * Method to test courseService.signUpForCourse
     */
    @Test
    void signUpForCourseTest() {
        when(participantRepository.save(participantEntity)).thenReturn(participantEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.signUpForCourse(1L, participantEntity));
        assertTrue(persistedCourseEntity.isPresent());
    }
}
