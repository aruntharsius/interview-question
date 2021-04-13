package com.backbase.interview.course.repository;

import com.backbase.interview.course.model.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Participant repository interface for CRUD operations.
 */
@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
    /**
     * Returns the participant corresponding to the name provided.
     * @param name - String object.
     * @return Participant having the specified name.
     */
    ParticipantEntity findByName(@Param("name") String name);
}
