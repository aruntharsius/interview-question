package com.example.demo.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Participant repository interface for CRUD operations.
 */
@Repository
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {
    /**
     * @param name
     * @return Participant having the specified name.
     */
    ParticipantEntity findByName(@Param("name") String name);
}
