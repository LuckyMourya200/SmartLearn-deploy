package com.learningplatform.app.smart_learn.rest;

import com.learningplatform.app.smart_learn.model.UserProgressDTO;
import com.learningplatform.app.smart_learn.service.UserProgressService;
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
@RequestMapping(value = "/api/userProgresses", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserProgressResource {

    private final UserProgressService userProgressService;

    public UserProgressResource(final UserProgressService userProgressService) {
        this.userProgressService = userProgressService;
    }

    @GetMapping
    public ResponseEntity<List<UserProgressDTO>> getAllUserProgresses() {
        return ResponseEntity.ok(userProgressService.findAll());
    }

    @GetMapping("/{progressId}")
    public ResponseEntity<UserProgressDTO> getUserProgress(
            @PathVariable(name = "progressId") final Integer progressId) {
        return ResponseEntity.ok(userProgressService.get(progressId));
    }

    @PostMapping
    public ResponseEntity<Integer> createUserProgress(
            @RequestBody @Valid final UserProgressDTO userProgressDTO) {
        final Integer createdProgressId = userProgressService.create(userProgressDTO);
        return new ResponseEntity<>(createdProgressId, HttpStatus.CREATED);
    }

    @PutMapping("/{progressId}")
    public ResponseEntity<Integer> updateUserProgress(
            @PathVariable(name = "progressId") final Integer progressId,
            @RequestBody @Valid final UserProgressDTO userProgressDTO) {
        userProgressService.update(progressId, userProgressDTO);
        return ResponseEntity.ok(progressId);
    }

    @DeleteMapping("/{progressId}")
    public ResponseEntity<Void> deleteUserProgress(
            @PathVariable(name = "progressId") final Integer progressId) {
        userProgressService.delete(progressId);
        return ResponseEntity.noContent().build();
    }

}
