package com.backbase.interview.course.repository;

import com.backbase.interview.course.model.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Course repository interface for CRUD operations.
 */
@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {

    /**
     * Returns a course corresponding to the title provided.
     * @param title - String object.
     * @return Course having the specified title.
     */
    CourseEntity findByTitle(@Param("title") String title);
}
