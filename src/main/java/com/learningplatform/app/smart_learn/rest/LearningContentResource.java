package com.learningplatform.app.smart_learn.rest;

import com.learningplatform.app.smart_learn.model.LearningContentDTO;
import com.learningplatform.app.smart_learn.service.LearningContentService;
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
@RequestMapping(value = "/api/learningContents", produces = MediaType.APPLICATION_JSON_VALUE)
public class LearningContentResource {

    private final LearningContentService learningContentService;

    public LearningContentResource(final LearningContentService learningContentService) {
        this.learningContentService = learningContentService;
    }

    @GetMapping
    public ResponseEntity<List<LearningContentDTO>> getAllLearningContents() {
        return ResponseEntity.ok(learningContentService.findAll());
    }

    @GetMapping("/{contentId}")
    public ResponseEntity<LearningContentDTO> getLearningContent(
            @PathVariable(name = "contentId") final Integer contentId) {
        return ResponseEntity.ok(learningContentService.get(contentId));
    }

    @PostMapping
    public ResponseEntity<Integer> createLearningContent(
            @RequestBody @Valid final LearningContentDTO learningContentDTO) {
        final Integer createdContentId = learningContentService.create(learningContentDTO);
        return new ResponseEntity<>(createdContentId, HttpStatus.CREATED);
    }

    @PutMapping("/{contentId}")
    public ResponseEntity<Integer> updateLearningContent(
            @PathVariable(name = "contentId") final Integer contentId,
            @RequestBody @Valid final LearningContentDTO learningContentDTO) {
        learningContentService.update(contentId, learningContentDTO);
        return ResponseEntity.ok(contentId);
    }

    @DeleteMapping("/{contentId}")
    public ResponseEntity<Void> deleteLearningContent(
            @PathVariable(name = "contentId") final Integer contentId) {
        learningContentService.delete(contentId);
        return ResponseEntity.noContent().build();
    }

}
