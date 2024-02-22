package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.Role;
import com.learningplatform.app.smart_learn.service.PrimarySequenceService;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;


@Component
public class RoleListener extends AbstractMongoEventListener<Role> {

    private final PrimarySequenceService primarySequenceService;

    public RoleListener(final PrimarySequenceService primarySequenceService) {
        this.primarySequenceService = primarySequenceService;
    }

    @Override
    public void onBeforeConvert(final BeforeConvertEvent<Role> event) {
        if (event.getSource().getRoleId() == null) {
            event.getSource().setRoleId((int)primarySequenceService.getNextValue());
        }
    }

}
