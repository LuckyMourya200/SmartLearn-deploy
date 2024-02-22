package com.learningplatform.app.smart_learn.rest;

import com.learningplatform.app.smart_learn.model.CourseDTO;
import com.learningplatform.app.smart_learn.service.CourseService;
import com.learningplatform.app.smart_learn.util.ReferencedException;
import com.learningplatform.app.smart_learn.util.ReferencedWarning;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/courses", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseResource {

    private final CourseService courseService;

    public CourseResource(final CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        return ResponseEntity.ok(courseService.findAll());
    }

    @GetMapping("/{courseId}")
    public ResponseEntity<CourseDTO> getCourse(
            @PathVariable(name = "courseId") final Integer courseId) {
        return ResponseEntity.ok(courseService.get(courseId));
    }

    @PostMapping
    public ResponseEntity<Integer> createCourse(@RequestBody @Valid final CourseDTO courseDTO) {
        final Integer createdCourseId = courseService.create(courseDTO);
        return new ResponseEntity<>(createdCourseId, HttpStatus.CREATED);
    }

    @PutMapping("/{courseId}")
    public ResponseEntity<Integer> updateCourse(
            @PathVariable(name = "courseId") final Integer courseId,
            @RequestBody @Valid final CourseDTO courseDTO) {
        courseService.update(courseId, courseDTO);
        return ResponseEntity.ok(courseId);
    }

    @DeleteMapping("/{courseId}")
    public ResponseEntity<Void> deleteCourse(
            @PathVariable(name = "courseId") final Integer courseId) {
        final ReferencedWarning referencedWarning = courseService.getReferencedWarning(courseId);
        if (referencedWarning != null) {
            throw new ReferencedException(referencedWarning);
        }
        courseService.delete(courseId);
        return ResponseEntity.noContent().build();
    }

}
