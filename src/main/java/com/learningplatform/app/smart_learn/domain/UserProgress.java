package com.learningplatform.app.smart_learn.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Document
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserProgress {

    @Id
    private Integer progressId;

    private Integer progress;

    private Boolean completionStatus;

    @DocumentReference(lazy = true)
    private User user;

    @DocumentReference(lazy = true)
    private Course course;

}
