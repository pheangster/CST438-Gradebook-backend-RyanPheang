package com.cst438.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends CrudRepository <Course, Integer> {
	
//	Course findByCourse_id(@Param("course_id") int course_id);
}
