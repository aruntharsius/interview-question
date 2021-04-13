package com.backbase.interview.course.service;

import com.backbase.interview.course.model.CourseEntity;
import com.backbase.interview.course.model.ParticipantEntity;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * Service interface that provides various methods to work on the HTTP request objects from REST calls.
 */
public interface CourseService {


    /**
     * Given a valid CourseEntity object a course is created in the database.
     * @param entity - CourseEntity object.
     * @return CourseEntity object if a course is created successfully.
     * Return error messages when course already exists or when invalid date is provided.
     */
    Object createCourse(CourseEntity entity);

    /**
     * Returns a course corresponding to the title provided.
     * @param title - String object.
     * @return CourseEntity object if a course having the specified title is available. Else return null.
     */
     Object getCourseByTitle(String title);

    /**
     * Returns a course corresponding to the ID provided.
     * @param id - Long object.
     * @return CourseEntity object if a course having the specified id is available. Else return null.
     */
     Object getCourseById(Long id);

    /**
     * Assigns the participant to the course corresponding to the ID provided.
     * @param courseId - Long object.
     * @param participantEntity - ParticipantEntity object.
     * @return CourseEntity object if a participant is created successfully under the course with the specified id.
     * If the course with the specified id is not available or if the request body is invalid, return error message.
     */
     Object signUpForCourse(Long courseId, ParticipantEntity participantEntity);

    /**
     * Returns a calendar object after trimming the hour, minute, seconds and milliseconds.
     * @param date - Calendar Object
     * @return Calendar object with hour, minute, seconds and milliseconds trimmed from date object.
     */
     Calendar getCalendarFromDate(Date date);

    /**
     *  Removes a participant from the course corresponding to the ID provided.
     * @param courseId - Long object.
     * @param jsonObject - HashMap object.
     * @return CourseEntity object if a participant is removed successfully from the course with the specified id.
     * If the course with the specified id is not available or the request body is invalid, return error message.
     */
     Object cancelUserFromCourse(Long courseId, Map<String, Object> jsonObject);

}
