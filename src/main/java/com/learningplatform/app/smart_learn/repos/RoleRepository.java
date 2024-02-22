package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.Role;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface RoleRepository extends MongoRepository<Role, Integer> {
}
