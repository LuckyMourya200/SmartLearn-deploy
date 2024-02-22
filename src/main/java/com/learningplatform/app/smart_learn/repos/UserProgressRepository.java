package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.domain.User;
import com.learningplatform.app.smart_learn.domain.UserProgress;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserProgressRepository extends MongoRepository<UserProgress, Integer> {

    UserProgress findFirstByUser(User user);

    UserProgress findFirstByCourse(Course course);

    Optional<UserProgress> findByUserAndCourse(User user, Course course);

}
