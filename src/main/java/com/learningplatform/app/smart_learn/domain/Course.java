package com.learningplatform.app.smart_learn.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Course {

    @Id
    private Integer courseId;
    private String courseTitle;
    private String courseType;
    private String courseDescription;

    private String courseDuration;
    private String coursePrice;

    @DocumentReference(lazy = true, lookup = "{ 'course' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<UserProgress> courseUserProgresses;

    @DocumentReference(lazy = true, lookup = "{ 'course' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<LearningContent> courseLearningContents;
}
