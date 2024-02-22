package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class CourseListener extends AbstractMongoEventListener<Course> {

    private final PrimarySequenceService primarySequenceService;

    public CourseListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Course> event) {
        if (event.getSource().getCourseId() == null) {
            event.getSource().setCourseId((int)primarySequenceService.getNextValue());
        }
    }

}
