package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, Integer> {

}
