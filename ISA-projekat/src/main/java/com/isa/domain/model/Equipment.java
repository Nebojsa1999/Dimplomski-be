package com.isa.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Equipment extends AbstractEntity {

    private String name;

    private double amount;

    @ManyToOne()
    @JoinColumn(name = "hospital_id")
    private Hospital hospital;

    public Hospital getHospital() {
        return hospital;
    }

    public String getName() {
        return name;
    }

    public void setName(String equipmentType) {
        this.name = equipmentType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
