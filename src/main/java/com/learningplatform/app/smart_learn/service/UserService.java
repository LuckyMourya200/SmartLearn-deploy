package com.learningplatform.app.smart_learn.service;

import com.learningplatform.app.smart_learn.domain.Role;
import com.learningplatform.app.smart_learn.domain.User;
import com.learningplatform.app.smart_learn.domain.UserProgress;
import com.learningplatform.app.smart_learn.model.UserDTO;
import com.learningplatform.app.smart_learn.repos.RoleRepository;
import com.learningplatform.app.smart_learn.repos.UserProgressRepository;
import com.learningplatform.app.smart_learn.repos.UserRepository;
import com.learningplatform.app.smart_learn.util.NotFoundException;
import com.learningplatform.app.smart_learn.util.ReferencedWarning;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserProgressRepository userProgressRepository;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository,

            final UserProgressRepository userProgressRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userProgressRepository = userProgressRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("userId"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final Integer userId) {
        return userRepository.findById(userId)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getUserId();
    }

    public void update(final Integer userId, final UserDTO userDTO) {
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final Integer userId) {
        userRepository.deleteById(userId);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setUserId(user.getUserId());
        userDTO.setUsername(user.getUsername());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        userDTO.setRoleId(user.getRoleId().stream()
                .map(role -> role.getRoleId())
                .toList());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        final List<Role> roleId = iterableToList(roleRepository.findAllById(
                userDTO.getRoleId() == null ? Collections.emptyList() : userDTO.getRoleId()));
        if (roleId.size() != (userDTO.getRoleId() == null ? 0 : userDTO.getRoleId().size())) {
            throw new NotFoundException("one of roleId not found");
        }
        user.setRoleId(new HashSet<>(roleId));
        return user;
    }

    private <T> List<T> iterableToList(final Iterable<T> iterable) {
        final List<T> list = new ArrayList<T>();
        iterable.forEach(item -> list.add(item));
        return list;
    }

    public ReferencedWarning getReferencedWarning(final Integer userId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(userId)
                .orElseThrow(NotFoundException::new);

        final UserProgress userUserProgress = userProgressRepository.findFirstByUser(user);
        if (userUserProgress != null) {
            referencedWarning.setKey("user.userProgress.user.referenced");
            referencedWarning.addParam(userUserProgress.getProgressId());
            return referencedWarning;
        }
        return null;
    }

}
