package com.learningplatform.app.smart_learn.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LearningContent {

    @Id
    private Integer contentId;

    private String unit;
    private String contentTitle;
    private String contentDescription;
    private byte[] postImage;

    @DocumentReference(lazy = true)
    private Course course;

}
