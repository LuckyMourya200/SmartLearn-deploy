package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.UserProgress;
import com.learningplatform.app.smart_learn.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class UserProgressListener extends AbstractMongoEventListener<UserProgress> {

    private final PrimarySequenceService primarySequenceService;

    public UserProgressListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<UserProgress> event) {
        if (event.getSource().getProgressId() == null) {
            event.getSource().setProgressId((int)primarySequenceService.getNextValue());
        }
    }

}
