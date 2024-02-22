package com.learningplatform.app.smart_learn.rest;

import com.learningplatform.app.smart_learn.model.RoleDTO;
import com.learningplatform.app.smart_learn.service.RoleService;
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
@RequestMapping(value = "/api/roles", produces = MediaType.APPLICATION_JSON_VALUE)
public class RoleResource {

    private final RoleService roleService;

    public RoleResource(final RoleService roleService) {
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{roleId}")
    public ResponseEntity<RoleDTO> getRole(@PathVariable(name = "roleId") final Integer roleId) {
        return ResponseEntity.ok(roleService.get(roleId));
    }

    @PostMapping
    public ResponseEntity<Integer> createRole(@RequestBody @Valid final RoleDTO roleDTO) {
        final Integer createdRoleId = roleService.create(roleDTO);
        return new ResponseEntity<>(createdRoleId, HttpStatus.CREATED);
    }

    @PutMapping("/{roleId}")
    public ResponseEntity<Integer> updateRole(@PathVariable(name = "roleId") final Integer roleId,
            @RequestBody @Valid final RoleDTO roleDTO) {
        roleService.update(roleId, roleDTO);
        return ResponseEntity.ok(roleId);
    }

    @DeleteMapping("/{roleId}")
    public ResponseEntity<Void> deleteRole(@PathVariable(name = "roleId") final Integer roleId) {
        roleService.delete(roleId);
        return ResponseEntity.noContent().build();
    }

}
