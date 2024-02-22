package com.learningplatform.app.smart_learn.service;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.domain.LearningContent;
import com.learningplatform.app.smart_learn.model.LearningContentDTO;
import com.learningplatform.app.smart_learn.repos.CourseRepository;
import com.learningplatform.app.smart_learn.repos.LearningContentRepository;
import com.learningplatform.app.smart_learn.util.NotFoundException;

import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class LearningContentService {

    private final LearningContentRepository learningContentRepository;
    private final CourseRepository courseRepository;

    public LearningContentService(final LearningContentRepository learningContentRepository,
            final CourseRepository courseRepository) {
        this.learningContentRepository = learningContentRepository;
        this.courseRepository = courseRepository;
    }

    public List<LearningContentDTO> findAll() {
        final List<LearningContent> learningContents = learningContentRepository.findAll(Sort.by("contentId"));
        return learningContents.stream()
                .map(learningContent -> mapToDTO(learningContent, new LearningContentDTO()))
                .toList();
    }

    public LearningContentDTO get(final Integer contentId) {
        return learningContentRepository.findById(contentId)
                .map(learningContent -> mapToDTO(learningContent, new LearningContentDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final LearningContentDTO learningContentDTO) {
        final LearningContent learningContent = new LearningContent();
        mapToEntity(learningContentDTO, learningContent);
        return learningContentRepository.save(learningContent).getContentId();
    }

    public void update(final Integer contentId, final LearningContentDTO learningContentDTO) {
        final LearningContent learningContent = learningContentRepository.findById(contentId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(learningContentDTO, learningContent);
        learningContentRepository.save(learningContent);
    }

    public void delete(final Integer contentId) {
        learningContentRepository.deleteById(contentId);
    }

    public LearningContentDTO mapToDTO(final LearningContent learningContent,
            final LearningContentDTO learningContentDTO) {
        learningContentDTO.setContentId(learningContent.getContentId());
        learningContentDTO.setContentTitle(learningContent.getContentTitle());
        learningContentDTO.setContentDescription(learningContent.getContentDescription());
        learningContentDTO.setHasImage(
                (learningContent.getPostImage() != null) && (learningContent.getPostImage().length > 0) ? true : false);
        learningContentDTO.setUnit(learningContent.getUnit());
        learningContentDTO
                .setCourse(learningContent.getCourse() == null ? null : learningContent.getCourse().getCourseId());
        return learningContentDTO;
    }

    public LearningContent mapToEntity(final LearningContentDTO learningContentDTO,
            final LearningContent learningContent) {
        learningContent.setContentTitle(learningContentDTO.getContentTitle());
        learningContent.setContentDescription(learningContentDTO.getContentDescription());
        final Course course = learningContentDTO.getCourse() == null ? null
                : courseRepository.findById(learningContentDTO.getCourse())
                        .orElseThrow(() -> new NotFoundException("course not found"));
        learningContent.setCourse(course);
        return learningContent;
    }

}
