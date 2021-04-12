package com.example.demo.course;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Class to unit test all methods used in CourseController class.
 * IMPORTANT: Uncomment the lines within methods to run the tests separately.
 */
@SpringBootTest
@AutoConfigureMockMvc
public class CourseControllerTests {

    /**
     * Injected MockMvc bean to perform tests on API calls.
     */
    @Autowired
    private MockMvc mockMvc;

    private final String CREATE_COURSE_JSON = "{   \"title\": \"Course A\",   \"startDate\": \"2021-05-01\",   \"endDate\": \"2021-05-05\",   \"capacity\": 10 }";

    private final String SIGN_UP_FOR_COURSE = "{   \"courseId\": 1,   \"registrationDate\": \"2021-4-27\",   \"name\": \"Daniel\" }";

    private final String SIGN_UP_FOR_COURSE_INVALID_DATE = "{   \"courseId\": 1,   \"registrationDate\": \"2021-4-30\",   \"name\": \"Craig\" }";

    private final String CANCEL_USER_FROM_COURSE = "{   \"courseId\": 1,   \"cancelDate\": \"2021-04-27\",   \"name\": \"Daniel\" }";

    private final String CANCEL_USER_FROM_COURSE_INVALID_DATE = "{   \"courseId\": 1,   \"cancelDate\": \"2021-04-30\",   \"name\": \"Daniel\" }";

    public final String COURSE_NOT_FOUND = "Course not found!";

    public final String INVALID_CANCEL_DATE = "Invalid cancellation date!";

    public final String INVALID_REG_DATE = "Invalid registration date!";

    /**
     * @throws Exception
     * Method to test the /courses API which creates courses.
     */
    @Test
    public void createCourse() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                //.andExpect(status().isCreated())
                //.andExpect(jsonPath("$.title").value("Course A"))
                .andDo(MockMvcResultHandlers.print());
    }

    /**
     * @throws Exception
     * Method to test the /courses/{id}/add API which signs participants to the courses.
     */
    @Test
    public void signUpForCourse() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(post("/courses/1/add").content(SIGN_UP_FOR_COURSE)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                        //.andExpect(status().isOk())
                        //.andExpect(jsonPath("$.remaining").value("9"))
                        .andDo(MockMvcResultHandlers.print()));
    }

    /**
     * @throws Exception
     * Method to test the /courses/{id}/add API for negative scenario.
     */
    @Test
    public void signUpForInvalidCourse() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(post("/courses/2/add").content(SIGN_UP_FOR_COURSE)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.Reason").value(COURSE_NOT_FOUND)).andDo(MockMvcResultHandlers.print()));
    }

    /**
     * @throws Exception
     * Method to test the /courses/{id}/add API which signs participants to the courses.
     */
    @Test
    public void signUpForCourseWithInvalidRegistrationDate() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(post("/courses/1/add").content(SIGN_UP_FOR_COURSE_INVALID_DATE)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.Reason").value(INVALID_REG_DATE)).andDo(MockMvcResultHandlers.print()));
    }

    /**
     * @throws Exception
     * Method to test the /courses/{id} API which returns the course for specific ID.
     */
    @Test
    public void getCourseById() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(get("/courses/1")).andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value("1")));
    }

    /**
     * @throws Exception
     * Method to test the /courses/{id} API for the negative scenario.
     */
    @Test
    public void getCourseByInvalidId() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(get("/courses/10")).andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.Reason").value(COURSE_NOT_FOUND)));
    }

    /**
     * @throws Exception
     * Method to test the /courses?q={title} API which returns the course with the specific title.
     */
    @Test
    public void getCourseByTitle() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(get("/courses?q=Course A")).andExpect(status().isOk())
                        .andExpect(jsonPath("$.title").value("Course A")));
    }

    /**
     * @throws Exception
     * Method to test the /courses?q={title} API for the negative scenario.
     */
    @Test
    public void getCourseByInvalidTitle() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(get("/courses?q=Course AA")).andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.Reason").value(COURSE_NOT_FOUND)));
    }

    /**
     * @throws Exception
     * Method to test the /courses/{id}/remove API which removes the participants from the course in the specified ID.
     */
    @Test
    public void cancelUserFromCourse() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(post("/courses/1/add").content(SIGN_UP_FOR_COURSE)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                        //.andExpect(status().isOk())
                        //.andExpect(jsonPath("$.remaining").value("9"))
                        .andDo(
                                result1 -> this.mockMvc.perform(post("/courses/1/remove").content(CANCEL_USER_FROM_COURSE)
                                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isOk())
                                        .andExpect(jsonPath("$.remaining").value("10")).andDo(MockMvcResultHandlers.print())));
    }

    /**
     * @throws Exception
     * Method to test the /courses/{id}/remove for the negative scenario.
     */
    @Test
    public void cancelUserFromInvalidCourse() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(post("/courses/1/add").content(SIGN_UP_FOR_COURSE)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                        //.andExpect(status().isOk())
                        //.andExpect(jsonPath("$.remaining").value("9"))
                        .andDo(
                                result1 -> this.mockMvc.perform(post("/courses/10/remove").content(CANCEL_USER_FROM_COURSE)
                                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isNotFound())
                                        .andExpect(jsonPath("$.Reason").value(COURSE_NOT_FOUND)).andDo(MockMvcResultHandlers.print())));
    }

    /**
     * @throws Exception
     * Method to test the /courses/{id}/remove API with an invalid cancellation date.
     */
    @Test
    public void cancelUserFromCourseWithInvalidCancelDate() throws Exception {
        this.mockMvc.perform(post("/courses").content(CREATE_COURSE_JSON)
                .contentType(MediaType.APPLICATION_JSON))
                //.andExpect(status().isCreated())
                .andDo(result -> this.mockMvc.perform(post("/courses/1/add").content(SIGN_UP_FOR_COURSE)
                        .contentType(MediaType.APPLICATION_JSON)).andDo(print())
                        //.andExpect(status().isOk())
                        //.andExpect(jsonPath("$.remaining").value("9"))
                        .andDo(
                                result1 -> this.mockMvc.perform(post("/courses/1/remove").content(CANCEL_USER_FROM_COURSE_INVALID_DATE)
                                        .contentType(MediaType.APPLICATION_JSON)).andDo(print()).andExpect(status().isBadRequest())
                                        .andExpect(jsonPath("$.Reason").value(INVALID_CANCEL_DATE)).andDo(MockMvcResultHandlers.print())));
    }

}
