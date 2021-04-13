package com.backbase.interview.course.entities;

import com.backbase.interview.course.model.CourseEntity;
import com.backbase.interview.course.model.ParticipantEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Class to test ParticipantEntity.
 */
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class ParticipantEntityTest {

    /**
     * The entity manager.
     */
    @Autowired
    private TestEntityManager entityManager;

    /**
     * Test ParticipantEntity object.
     */
    @Test
    public void testParticipantEntity() {
        ParticipantEntity participantEntity = new ParticipantEntity();
        participantEntity.setName("Daniel");
        participantEntity.setRegistrationDate("2021-04-27");

        CourseEntity courseEntity = new CourseEntity();
        courseEntity.setRemaining(10L);
        courseEntity.setCapacity(10L);
        courseEntity.setTitle("test");
        courseEntity.setStartDate("2021-05-01");
        courseEntity.setEndDate("2021-05-05");
        courseEntity.setParticipantEntities(new ArrayList<>());
        courseEntity.getParticipantEntities().add(participantEntity);

        entityManager.persist(courseEntity);
        ParticipantEntity testParticipantEntity = entityManager.persist(participantEntity);
        assertEquals(testParticipantEntity.getName(), testParticipantEntity.getName());
        assertEquals(testParticipantEntity.getRegistrationDate(), testParticipantEntity.getRegistrationDate());
        assertNotNull(testParticipantEntity.getId());
        assertEquals(courseEntity.getParticipantEntities().get(0), participantEntity);
    }
}
