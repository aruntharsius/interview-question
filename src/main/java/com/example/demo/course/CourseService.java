package com.example.demo.course;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.course.Constants.*;

/**
 * Service class that provides various methods to work on the HTTP request objects from REST calls.
 */
@Service
public class CourseService {

    /**
     * Injected course repository bean.
     */
    @Autowired
    CourseRepository courseRepository;

    /**
     * Injected participant repository bean.
     */
    @Autowired
    ParticipantRepository participantRepository;

    /**
     * @param entity
     * @return CourseEntity object if a course is created successfully.
     * Return error messages when course already exists or when invalid date is provided.
     */
    public Object createCourse(CourseEntity entity) {
        Optional<CourseEntity> course = Optional.ofNullable(courseRepository.findByTitle(entity.getTitle()));
        if (course.isPresent()) {
            return COURSE_ALREADY_EXISTS;
        } else {
            try {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
                CourseEntity newEntity = new CourseEntity();
                newEntity.setTitle(entity.getTitle());
                newEntity.setStartDate(dateFormat.format(dateFormat.parse(entity.getStartDate())));
                newEntity.setEndDate(dateFormat.format(dateFormat.parse(entity.getEndDate())));
                newEntity.setCapacity(entity.getCapacity());
                newEntity.setRemaining(entity.getCapacity());
                newEntity = courseRepository.save(newEntity);
                return newEntity;
            } catch (ParseException e) {
                return INVALID_DATE_FORMAT;
            }
        }
    }

    /**
     * @param title
     * @return CourseEntity object if a course having the specified title is available. Else return null.
     */
    public Object getCourseByTitle(String title) {
        return Optional.ofNullable(courseRepository.findByTitle(title)).orElse(null);
    }

    /**
     * @param id
     * @return CourseEntity object if a course having the specified id is available. Else return null.
     */
    public Object getCourseById(Long id) {
        return courseRepository.findById(id).orElse(null);
    }

    /**
     * @param courseId
     * @param participantEntity
     * @return CourseEntity object if a participant is created successfully under the course with the specified id.
     * If the course with the specified id is not available or if the request body is invalid, return error message.
     */
    public Object signUpForCourse(Long courseId, ParticipantEntity participantEntity) {
        return courseRepository.findById(courseId).map(course -> {
            if (course.getRemaining() == 0) {
                return COURSE_FULL;
            }
            try {
                DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
                Calendar participantCalendar = getCalendarFromDate(dateFormat.parse(participantEntity.getRegistrationDate()));
                Calendar courseCalendar = getCalendarFromDate(dateFormat.parse(course.getStartDate()));
                courseCalendar.add(Calendar.DATE, -3);

                boolean isInvalidDate = participantCalendar.equals(courseCalendar) || participantCalendar.after(courseCalendar);
                if (isInvalidDate) {
                    return INVALID_REG_DATE;
                }
            } catch (ParseException e) {
                return INVALID_DATE_FORMAT;
            }
            Optional<ParticipantEntity> participantEntity1 = course.getParticipantEntities().stream().filter(p -> p.getName().equals(participantEntity.getName())).findAny();
            if (participantEntity1.isPresent()) {
                return NAME_ALREADY_ENROLLED;
            } else {
                participantEntity.setCourseEntity(course);
                participantRepository.save(participantEntity);
                course.setRemaining(course.getRemaining() - 1);
                course.getParticipantEntities().add(participantEntity);
                courseRepository.save(course);
                return course;
            }
        }).orElse(COURSE_NOT_FOUND);
    }

    /**
     * @param date
     * @return Calendar object with hour, minute, seconds and milliseconds trimmed from date object.
     */
    public Calendar getCalendarFromDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar;
    }

    /**
     * @param courseId
     * @param jsonObject
     * @return CourseEntity object if a participant is removed successfully from the course with the specified id.
     * If the course with the specified id is not available or the request body is invalid, return error message.
     */
    public Object cancelUserFromCourse(Long courseId, Map<String, Object> jsonObject) {
        return courseRepository.findById(courseId).map(course -> {
            ParticipantEntity participantEntity = participantRepository.findByName((String) jsonObject.get("name"));
            if (null != participantEntity) {
                try {
                    DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
                    Date cancelDate = dateFormat.parse((String) jsonObject.get("cancelDate"));
                    Calendar cancelCalendar = getCalendarFromDate(cancelDate);
                    Calendar courseCalendar = getCalendarFromDate(dateFormat.parse(course.getStartDate()));
                    courseCalendar.add(Calendar.DATE, -3);

                    boolean isInvalidDate = cancelCalendar.equals(courseCalendar) || cancelCalendar.after(courseCalendar);
                    if (isInvalidDate) {
                        return INVALID_CANCEL_DATE;
                    }
                } catch (ParseException e) {
                    return INVALID_DATE_FORMAT;
                }
                participantRepository.deleteById(participantEntity.getId());
                course.setRemaining(course.getRemaining() + 1);
                course.getParticipantEntities().remove(participantEntity);
                courseRepository.save(course);
            } else {
                return USER_NOT_ENROLLED;
            }
            return course;
        }).orElse(COURSE_NOT_FOUND);
    }

}
