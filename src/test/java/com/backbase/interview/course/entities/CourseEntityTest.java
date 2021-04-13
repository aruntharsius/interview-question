package com.backbase.interview.course.entities;

import com.backbase.interview.course.model.CourseEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class to test CourseEntity.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class CourseEntityTest {

    /**
     * The entity manager.
     */
    @Autowired
    private TestEntityManager entityManager;

    /**
     * Test CourseEntity object.
     */
    @Test
    void testCourseEntity() {
        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setRemaining(10L);
        courseEntity.setCapacity(10L);
        courseEntity.setTitle("test");
        courseEntity.setStartDate("2021-05-01");
        courseEntity.setEndDate("2021-05-05");

        CourseEntity testCourseEntity = entityManager.persist(courseEntity);
        assertEquals(testCourseEntity.getTitle(), courseEntity.getTitle());
        assertEquals(testCourseEntity.getRemaining(), courseEntity.getRemaining());
        assertEquals(testCourseEntity.getCapacity(), courseEntity.getCapacity());
        assertEquals(testCourseEntity.getEndDate(), courseEntity.getEndDate());
        assertEquals(testCourseEntity.getStartDate(), courseEntity.getStartDate());
        assertEquals(null, testCourseEntity.getParticipantEntities());
        assertNotNull(testCourseEntity.getId());
    }
}
