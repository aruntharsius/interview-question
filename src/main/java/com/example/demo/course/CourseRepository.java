package com.example.demo.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Course repository interface for CRUD operations.
 */
@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    /**
     * @param title
     * @return Course having the specified title.
     */
    CourseEntity findByTitle(@Param("title") String title);
}
