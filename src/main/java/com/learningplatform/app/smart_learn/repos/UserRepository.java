package com.learningplatform.app.smart_learn.repos;

import com.learningplatform.app.smart_learn.domain.Role;
import com.learningplatform.app.smart_learn.domain.User;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {

    User findFirstByRoleId(Role role);

    List<User> findAllByRoleId(Role role);

    Optional<User> findByEmail(String email);

}
