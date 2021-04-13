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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.backbase.interview.course.util.Constants.DATE_FORMAT_YYYYMMDD;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Class to test the CourseServiceImpl.
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
    CourseServiceImpl courseService;

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
     * Method to test courseService.signUpForCourse()
     */
    @Test
    void signUpForCourseTest() {
        when(participantRepository.save(participantEntity)).thenReturn(participantEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.signUpForCourse(1L, participantEntity));
        assertTrue(persistedCourseEntity.isPresent());
    }

    /**
     * Method to test courseService.cancelUserFromCourse()
     */
    @Test
    void cancelFromCourseTest() {
        when(participantRepository.save(participantEntity)).thenReturn(participantEntity);
        Map<String, Object> participantEntityMap = new HashMap<>();
        participantEntityMap.put("name", participantEntity.getName());
        participantEntityMap.put("cancelDate", "2020-04-27");
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.cancelUserFromCourse(1L, participantEntityMap));
        assertTrue(persistedCourseEntity.isPresent());
    }

    /**
     * Method to test courseService.getCalendarFromDate()
     */
    @Test
    void getCalendarFromDateTest() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
        Calendar calendar = courseService.getCalendarFromDate(dateFormat.parse("2020-05-01"));
        assertEquals(calendar.get(Calendar.MILLISECOND), 0);
    }
}
