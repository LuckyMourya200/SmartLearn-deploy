package com.learningplatform.app.smart_learn.service;

import com.learningplatform.app.smart_learn.domain.Course;
import com.learningplatform.app.smart_learn.domain.LearningContent;
import com.learningplatform.app.smart_learn.domain.UserProgress;
import com.learningplatform.app.smart_learn.model.CourseDTO;
import com.learningplatform.app.smart_learn.repos.CourseRepository;
import com.learningplatform.app.smart_learn.repos.LearningContentRepository;
import com.learningplatform.app.smart_learn.repos.UserProgressRepository;
import com.learningplatform.app.smart_learn.util.NotFoundException;
import com.learningplatform.app.smart_learn.util.ReferencedWarning;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final UserProgressRepository userProgressRepository;
    private final LearningContentRepository learningContentRepository;

    public CourseService(final CourseRepository courseRepository,
            final UserProgressRepository userProgressRepository,
            final LearningContentRepository learningContentRepository) {
        this.courseRepository = courseRepository;
        this.userProgressRepository = userProgressRepository;
        this.learningContentRepository = learningContentRepository;
    }

    public List<CourseDTO> findAll() {
        final List<Course> courses = courseRepository.findAll(Sort.by("courseId"));
        return courses.stream()
                .map(course -> mapToDTO(course, new CourseDTO()))
                .toList();
    }

    public CourseDTO get(final Integer courseId) {
        return courseRepository.findById(courseId)
                .map(course -> mapToDTO(course, new CourseDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final CourseDTO courseDTO) {
        final Course course = new Course();
        mapToEntity(courseDTO, course);
        return courseRepository.save(course).getCourseId();
    }

    public void update(final Integer courseId, final CourseDTO courseDTO) {
        final Course course = courseRepository.findById(courseId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(courseDTO, course);
        courseRepository.save(course);
    }

    public void delete(final Integer courseId) {
        courseRepository.deleteById(courseId);
    }

    private CourseDTO mapToDTO(final Course course, final CourseDTO courseDTO) {
        courseDTO.setCourseId(course.getCourseId());
        courseDTO.setCourseTitle(course.getCourseTitle());
        courseDTO.setCourseDescription(course.getCourseDescription());
        courseDTO.setCourseDuration(course.getCourseDuration());
        courseDTO.setCoursePrice(course.getCoursePrice());
        courseDTO.setCourseType(course.getCourseType());
        return courseDTO;
    }

    private Course mapToEntity(final CourseDTO courseDTO, final Course course) {
        course.setCourseTitle(courseDTO.getCourseTitle());
        course.setCourseDuration(courseDTO.getCourseDuration());
        course.setCoursePrice(courseDTO.getCoursePrice());
        course.setCourseDescription(courseDTO.getCourseDescription());
        course.setCourseType(courseDTO.getCourseType());
        return course;
    }

    public ReferencedWarning getReferencedWarning(final Integer courseId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Course course = courseRepository.findById(courseId)
                .orElseThrow(NotFoundException::new);
        final UserProgress courseUserProgress = userProgressRepository.findFirstByCourse(course);
        if (courseUserProgress != null) {
            referencedWarning.setKey("course.userProgress.course.referenced");
            referencedWarning.addParam(courseUserProgress.getProgressId());
            return referencedWarning;
        }
        final LearningContent courseLearningContent = learningContentRepository.findFirstByCourse(course);
        if (courseLearningContent != null) {
            referencedWarning.setKey("course.learningContent.course.referenced");
            referencedWarning.addParam(courseLearningContent.getContentId());
            return referencedWarning;
        }
        return null;
    }

}
