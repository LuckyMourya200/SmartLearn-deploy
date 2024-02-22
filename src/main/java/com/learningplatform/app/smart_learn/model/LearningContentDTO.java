package com.learningplatform.app.smart_learn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LearningContentDTO {

    private Integer contentId;
    private String contentTitle;
    private String contentDescription;
    private boolean hasImage;
    private String unit;
    private Integer course;

}
