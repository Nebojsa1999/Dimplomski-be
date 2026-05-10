package com.isa.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class DepartmentName extends AbstractEntity {

    @Column(unique = true, nullable = false)
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
