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
import java.util.*;
import java.util.stream.Collectors;

import static com.backbase.interview.course.util.Constants.*;
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
        courseEntity.setId(1L);
        courseEntity.setTitle("Course A");
        courseEntity.setCapacity(10L);
        courseEntity.setRemaining(10L);
        courseEntity.setStartDate("2020-05-01");
        courseEntity.setEndDate("2020-05-05");

        participantEntity.setId(2L);
        participantEntity.setName("Daniel");
        participantEntity.setRegistrationDate("2020-04-27");
        participantEntity.setCourseEntity(courseEntity);
        courseEntity.setParticipantEntities(new ArrayList<>());
    }


    /**
     * Method to test courseService.createCourse()
     */
    @Test
    void createCourseTest() throws ParseException {
        CourseEntity newEntity = new CourseEntity();
        newEntity.setTitle(courseEntity.getTitle());
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
        newEntity.setStartDate(dateFormat.format(dateFormat.parse(courseEntity.getStartDate())));
        newEntity.setEndDate(dateFormat.format(dateFormat.parse(courseEntity.getEndDate())));
        newEntity.setCapacity(courseEntity.getCapacity());
        newEntity.setRemaining(courseEntity.getCapacity());
        when(courseRepository.save(newEntity)).thenReturn(courseEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.createCourse(courseEntity));
        assertTrue(persistedCourseEntity.isPresent());
    }

    /**
     * Method to test courseService.createCourse() with invalid date
     */
    @Test
    void createCourseInvalidDateTest() {
        courseEntity.setStartDate("2020-SS-04");
        when(courseRepository.save(courseEntity)).thenReturn(courseEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.createCourse(courseEntity));
        persistedCourseEntity.ifPresent(o -> assertEquals(o, INVALID_DATE_FORMAT));
    }

    /**
     * Method to test courseService.createCourse() if a course name already exists
     */
    @Test
    void createCourseNameAlreadyExistsTest() {
        when(courseRepository.findByTitle("Course A")).thenReturn(courseEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.createCourse(courseEntity));
        persistedCourseEntity.ifPresent(o -> assertEquals(o, COURSE_ALREADY_EXISTS));
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
     * Method to test courseService.getCourseById() if the provided id does not exist
     */
    @Test
    void getCourseByIdNotFoundTest() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        Optional<Object> testCourseEntity = Optional.ofNullable(courseService.getCourseById(2L));
        assertFalse(testCourseEntity.isPresent());
    }

    /**
     * Method to test courseService.getCourseByTitle()
     */
    @Test
    void getCoursesByTitleTest() {
        when(courseRepository.findByTitle("Course A")).thenReturn(courseEntity);
        Optional<Object> testCourseEntity = Optional.of(courseService.getCourseByTitle("Course A"));
        assertTrue(testCourseEntity.get() instanceof CourseEntity);
    }


    /**
     * Method to test courseService.getCourseByTitle() if the title does not exist
     */
    @Test
    void getCoursesByTitleNotFoundTest() {
        when(courseRepository.findByTitle("Course B")).thenReturn(null);
        Optional<Object> testCourseEntity = Optional.ofNullable(courseService.getCourseByTitle("Course B"));
        assertFalse(testCourseEntity.isPresent());
    }

    /**
     * Method to test courseService.signUpForCourse()
     */
    @Test
    void signUpForCourseTest() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.signUpForCourse(1L, participantEntity));
        if (persistedCourseEntity.isPresent() && persistedCourseEntity.get() instanceof CourseEntity) {
            assertTrue(((CourseEntity) persistedCourseEntity.get()).getParticipantEntities().size() > 0);
        }
    }

    /**
     * Method to test courseService.signUpForCourse() if the course capacity is full
     */
    @Test
    void signUpForCourseCapacityTest() {
        courseEntity.setRemaining(0L);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.signUpForCourse(1L, participantEntity));
        if (persistedCourseEntity.isPresent() && persistedCourseEntity.get() instanceof String) {
            assertEquals(persistedCourseEntity.get(), COURSE_FULL);
        }
    }

    /**
     * Method to test courseService.signUpForCourse() if the registration date is invalid
     */
    @Test
    void signUpForCourseInvalidRegistrationDateTest() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        participantEntity.setRegistrationDate("2020-04-29");
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.signUpForCourse(1L, participantEntity));
        persistedCourseEntity.ifPresent(o -> assertEquals(o, INVALID_REG_DATE));
    }

    /**
     * Method to test courseService.signUpForCourse() if the registration date format is invalid
     */
    @Test
    void signUpForCourseInvalidRegistrationDateFormatTest() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        participantEntity.setRegistrationDate("2020-SS-29");
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.signUpForCourse(1L, participantEntity));
        persistedCourseEntity.ifPresent(o -> assertEquals(o, INVALID_DATE_FORMAT));
    }

    /**
     * Method to test courseService.signUpForCourse() if the user is already enrolled in the project
     */
    @Test
    void signUpForCourseUserAlreadyEnrolledTest() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        courseService.signUpForCourse(1L, participantEntity);
        Optional<Object> persistedCourseEntity1 = Optional.ofNullable(courseService.signUpForCourse(1L, participantEntity));
        persistedCourseEntity1.ifPresent(o -> assertEquals(o, NAME_ALREADY_ENROLLED));
    }

    /**
     * Method to test courseService.signUpForCourse() if the course is not found
     */
    @Test
    void signUpForCourseNotFoundTest() {
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.signUpForCourse(2L, participantEntity));
        persistedCourseEntity.ifPresent(o -> assertEquals(o, COURSE_NOT_FOUND));
    }

    /**
     * Method to test courseService.cancelUserFromCourse()
     */
    @Test
    void cancelFromCourseTest() {
        Map<String, Object> participantEntityMap = new HashMap<>();
        participantEntityMap.put("name", "Daniel");
        participantEntityMap.put("cancelDate", "2020-04-27");
        when(courseService.cancelUserFromCourse(1L, participantEntityMap)).thenReturn(Optional.ofNullable(courseEntity));
        when(participantRepository.findByName("Daniel")).thenReturn(participantEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.cancelUserFromCourse(1L, participantEntityMap));
        if (persistedCourseEntity.isPresent() && persistedCourseEntity.get() instanceof CourseEntity) {
            CourseEntity courseEntity1 = (CourseEntity) persistedCourseEntity.get();
            List<ParticipantEntity> participantEntities = courseEntity1.getParticipantEntities().stream().filter((a) -> a.getName().equals("Daniel")).collect(Collectors.toList());
            assertEquals(participantEntities.size(), 0);
        }
    }

    /**
     * Method to test courseService.cancelUserFromCourse() if the course is not found
     */
    @Test
    void cancelFromCourseNotFoundTest() {
        Map<String, Object> participantEntityMap = new HashMap<>();
        participantEntityMap.put("name", "Daniel");
        participantEntityMap.put("cancelDate", "2020-04-30");
        when(participantRepository.findByName("Daniel")).thenReturn(participantEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.cancelUserFromCourse(2L, participantEntityMap));
        if (persistedCourseEntity.isPresent() && persistedCourseEntity.get() instanceof String) {
            assertEquals(persistedCourseEntity.get(), COURSE_NOT_FOUND);
        }
    }

    /**
     * Method to test courseService.cancelUserFromCourse() if the user is not enrolled
     */
    @Test
    void cancelFromCourseUserNotEnrolledTest() {
        Map<String, Object> participantEntityMap = new HashMap<>();
        participantEntityMap.put("name", "Craig");
        participantEntityMap.put("cancelDate", "2020-04-30");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        when(participantRepository.findByName("Daniel")).thenReturn(participantEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.cancelUserFromCourse(1L, participantEntityMap));
        if (persistedCourseEntity.isPresent() && persistedCourseEntity.get() instanceof String) {
            assertEquals(persistedCourseEntity.get(), USER_NOT_ENROLLED);
        }
    }

    /**
     * Method to test courseService.cancelUserFromCourse() if the cancel date is invalid
     */
    @Test
    void cancelFromCourseInvalidCancelDateTest() {
        Map<String, Object> participantEntityMap = new HashMap<>();
        participantEntityMap.put("name", "Daniel");
        participantEntityMap.put("cancelDate", "2020-04-30");
        when(courseService.cancelUserFromCourse(1L, participantEntityMap)).thenReturn(Optional.ofNullable(courseEntity));
        when(participantRepository.findByName("Daniel")).thenReturn(participantEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.cancelUserFromCourse(1L, participantEntityMap));
        if (persistedCourseEntity.isPresent() && persistedCourseEntity.get() instanceof CourseEntity) {
            CourseEntity courseEntity1 = (CourseEntity) persistedCourseEntity.get();
            List<ParticipantEntity> participantEntities = courseEntity1.getParticipantEntities().stream().filter((a) -> a.getName().equals("Daniel")).collect(Collectors.toList());
            assertEquals(participantEntities.size(), 0);
        } else if (persistedCourseEntity.isPresent() && persistedCourseEntity.get() instanceof String) {
            assertEquals(persistedCourseEntity.get(), INVALID_CANCEL_DATE);
        }
    }

    /**
     * Method to test courseService.cancelUserFromCourse() if the cancel date format is invalid
     */
    @Test
    void cancelFromCourseInvalidCancelDateFormatTest() {
        Map<String, Object> participantEntityMap = new HashMap<>();
        participantEntityMap.put("name", "Daniel");
        participantEntityMap.put("cancelDate", "2020-ST-30");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(courseEntity));
        when(participantRepository.findByName("Daniel")).thenReturn(participantEntity);
        Optional<Object> persistedCourseEntity = Optional.ofNullable(courseService.cancelUserFromCourse(1L, participantEntityMap));
        if (persistedCourseEntity.isPresent() && persistedCourseEntity.get() instanceof String) {
            assertEquals(persistedCourseEntity.get(), INVALID_DATE_FORMAT);
        }
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
