package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.domain.LearningContent;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface LearningContentRepository extends MongoRepository<LearningContent, Integer> {

    LearningContent findFirstByCourse(Course course);

    List<LearningContent> findByCourse(Course course);
}
