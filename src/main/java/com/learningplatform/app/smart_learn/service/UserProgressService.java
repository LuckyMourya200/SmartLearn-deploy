package com.learningplatform.app.smart_learn.service;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.domain.User;
import com.learningplatform.app.smart_learn.domain.UserProgress;
import com.learningplatform.app.smart_learn.model.UserProgressDTO;
import com.learningplatform.app.smart_learn.repos.CourseRepository;
import com.learningplatform.app.smart_learn.repos.UserProgressRepository;
import com.learningplatform.app.smart_learn.repos.UserRepository;
import com.learningplatform.app.smart_learn.util.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserProgressService {

    private final UserProgressRepository userProgressRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    public UserProgressService(final UserProgressRepository userProgressRepository,
            final UserRepository userRepository, final CourseRepository courseRepository) {
        this.userProgressRepository = userProgressRepository;
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    public List<UserProgressDTO> findAll() {
        final List<UserProgress> userProgresses = userProgressRepository.findAll(Sort.by("progressId"));
        return userProgresses.stream()
                .map(userProgress -> mapToDTO(userProgress, new UserProgressDTO()))
                .toList();
    }

    public UserProgressDTO get(final Integer progressId) {
        return userProgressRepository.findById(progressId)
                .map(userProgress -> mapToDTO(userProgress, new UserProgressDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final UserProgressDTO userProgressDTO) {
        final UserProgress userProgress = new UserProgress();
        mapToEntity(userProgressDTO, userProgress);
        return userProgressRepository.save(userProgress).getProgressId();
    }

    public void update(final Integer progressId, final UserProgressDTO userProgressDTO) {
        final UserProgress userProgress = userProgressRepository.findById(progressId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userProgressDTO, userProgress);
        userProgressRepository.save(userProgress);
    }

    public void delete(final Integer progressId) {
        userProgressRepository.deleteById(progressId);
    }

    private UserProgressDTO mapToDTO(final UserProgress userProgress,
            final UserProgressDTO userProgressDTO) {
        userProgressDTO.setProgressId(userProgress.getProgressId());
        userProgressDTO.setCompletionStatus(userProgress.getCompletionStatus());
        userProgressDTO.setUser(userProgress.getUser() == null ? null : userProgress.getUser().getUserId());
        userProgressDTO.setCourse(userProgress.getCourse() == null ? null : userProgress.getCourse().getCourseId());
        return userProgressDTO;
    }

    private UserProgress mapToEntity(final UserProgressDTO userProgressDTO,
            final UserProgress userProgress) {
        userProgress.setCompletionStatus(userProgressDTO.getCompletionStatus());
        final User user = userProgressDTO.getUser() == null ? null : userRepository.findById(userProgressDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        userProgress.setUser(user);
        final Course course = userProgressDTO.getCourse() == null ? null : courseRepository.findById(userProgressDTO.getCourse())
                .orElseThrow(() -> new NotFoundException("course not found"));
        userProgress.setCourse(course);
        return userProgress;
    }

}
