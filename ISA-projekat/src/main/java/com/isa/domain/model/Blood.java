package com.isa.domain.model;

import com.isa.enums.BloodType;

import jakarta.persistence.*;

@Entity
public class Blood extends AbstractEntity {

    @Enumerated(EnumType.STRING)
    private BloodType bloodType;
    private double amount;

    @ManyToOne
    @JoinColumn(name = "center_account_id")
    private CenterAccount centerAccount;

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public CenterAccount getCenterAccount() {
        return centerAccount;
    }

    public void setCenterAccount(CenterAccount centerAccount) {
        this.centerAccount = centerAccount;
    }
}
