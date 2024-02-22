package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.LearningContent;
import com.learningplatform.app.smart_learn.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class LearningContentListener extends AbstractMongoEventListener<LearningContent> {

    private final PrimarySequenceService primarySequenceService;

    public LearningContentListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<LearningContent> event) {
        if (event.getSource().getContentId() == null) {
            event.getSource().setContentId((int)primarySequenceService.getNextValue());
        }
    }

}
