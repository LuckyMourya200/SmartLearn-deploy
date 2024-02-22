package com.learningplatform.app.smart_learn.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDTO {

    private Integer courseId;
    private String courseTitle;
    private String courseDescription;
    private String courseType;
    private String courseDuration;
    private String coursePrice;

}
