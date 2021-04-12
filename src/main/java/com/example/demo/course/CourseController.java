package com.example.demo.course;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static com.example.demo.course.Constants.*;


/**
 * Controller class with different methods to execute during different REST API calls.
 */
@RestController
@RequestMapping("/courses")
public class CourseController {

    /**
     * Injected service bean having methods to work on the HTTP request objects.
     */
    @Autowired
    CourseService courseService;

    /**
     * @param courseEntity
     * @return HTTP status code 201 with CourseEntity object as body if a course is created successfully.
     * Else return HTTP status code 400.
     */
    @PostMapping
    @JsonView(CreateCourseView.class)
    public ResponseEntity<Object> createCourse(@RequestBody CourseEntity courseEntity) {
        Object response = courseService.createCourse(courseEntity);
        if (COURSE_ALREADY_EXISTS.equals(response) || INVALID_DATE_FORMAT.equals(response)) {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("Reason", response);
            jsonObject.put("Status", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(jsonObject, new HttpHeaders(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.CREATED);
    }

    /**
     * @param title
     * @return HTTP status code 200 with CourseEntity object as body if a course having the specified title is available.
     * Else return HTTP status 404.
     */
    @GetMapping()
    @JsonView(CreateCourseView.class)
    public ResponseEntity<Object> getCourseByTitle(@RequestParam(name = "q") String title) {
        Object response = courseService.getCourseByTitle(title);
        if (response == null) {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("Reason", COURSE_NOT_FOUND);
            jsonObject.put("Status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(jsonObject, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * @param id
     * @return HTTP status code 200 with CourseEntity object as body if a course having the specified id is available.
     * Else return HTTP status 404.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Object> getCourseById(@PathVariable("id") Long id) {
        Object response = courseService.getCourseById(id);
        if (response == null) {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("Reason", COURSE_NOT_FOUND);
            jsonObject.put("Status", HttpStatus.NOT_FOUND.value());
            return new ResponseEntity<>(jsonObject, new HttpHeaders(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * @param id
     * @param participantEntity
     * @return HTTP status code 200 with CourseEntity object as body if a participant is created successfully under the course with the specified id.
     * If the course with the specified id is not available return HTTP status code 404.
     * HTTP status code 400 is returned if the request body is invalid.
     */
    @PostMapping("/{id}/add")
    public ResponseEntity<Object> signUpForCourse(@PathVariable("id") Long id, @RequestBody ParticipantEntity participantEntity) {
        Object response = courseService.signUpForCourse(id, participantEntity);
        if (response instanceof String) {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("Reason", response);
            if (COURSE_NOT_FOUND.equals(response)) {
                jsonObject.put("Status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(jsonObject, new HttpHeaders(), HttpStatus.NOT_FOUND);
            } else if (NAME_ALREADY_ENROLLED.equals(response) || INVALID_REG_DATE.equals(response) || COURSE_FULL.equals(response)) {
                jsonObject.put("Status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(jsonObject, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    /**
     * @param id
     * @param participantEntity
     * @return HTTP status code 200 with CourseEntity object as body if a participant is removed successfully from the course with the specified id.
     * If the course with the specified id is not available return HTTP status code 404.
     * HTTP status code 400 is returned if the request body is invalid.
     */
    @PostMapping("/{id}/remove")
    public ResponseEntity<Object> cancelUserFromCourse(@PathVariable("id") Long id, @RequestBody Map<String, Object> participantEntity) {
        Object response = courseService.cancelUserFromCourse(id, participantEntity);
        if (response instanceof String) {
            Map<String, Object> jsonObject = new HashMap<>();
            jsonObject.put("Reason", response);
            if (COURSE_NOT_FOUND.equals(response) || USER_NOT_ENROLLED.equals(response)) {
                jsonObject.put("Status", HttpStatus.NOT_FOUND.value());
                return new ResponseEntity<>(jsonObject, new HttpHeaders(), HttpStatus.NOT_FOUND);
            } else if (INVALID_CANCEL_DATE.equals(response) || INVALID_DATE_FORMAT.equals(response)) {
                jsonObject.put("Status", HttpStatus.BAD_REQUEST.value());
                return new ResponseEntity<>(jsonObject, new HttpHeaders(), HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }
}
