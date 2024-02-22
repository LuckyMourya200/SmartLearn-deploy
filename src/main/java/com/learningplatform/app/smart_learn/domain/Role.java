package com.learningplatform.app.smart_learn.domain;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;


@Document
public class Role {

    @Id
    private Integer roleId;

    @NotNull
    @Size(max = 50)
    private String name;

    @DocumentReference(lazy = true, lookup = "{ 'roleId' : ?#{#self._id} }")
    @ReadOnlyProperty
    private Set<User> userId;

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Set<User> getUserId() {
        return userId;
    }

    public void setUserId(final Set<User> userId) {
        this.userId = userId;
    }

}
