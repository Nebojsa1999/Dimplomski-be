package com.isa.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Poll extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "blood_id")
    private Blood blood;

    private boolean weight;

    private boolean sickness;

    private boolean infection;

    private boolean pressure;

    private boolean therapy;

    private boolean cycle;

    private boolean dentalIntervention;

    private boolean piercing;

    public Blood getBlood() {
        return blood;
    }

    public void setBlood(Blood blood) {
        this.blood = blood;
    }

    public boolean isPiercing() {
        return piercing;
    }

    public void setPiercing(boolean piercing) {
        this.piercing = piercing;
    }

    public boolean isWeight() {
        return weight;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public boolean isSickness() {
        return sickness;
    }

    public void setSickness(boolean sickness) {
        this.sickness = sickness;
    }

    public boolean isInfection() {
        return infection;
    }

    public void setInfection(boolean infection) {
        this.infection = infection;
    }

    public boolean isPressure() {
        return pressure;
    }

    public void setPressure(boolean pressure) {
        this.pressure = pressure;
    }

    public boolean isTherapy() {
        return therapy;
    }

    public void setTherapy(boolean therapy) {
        this.therapy = therapy;
    }

    public boolean isCycle() {
        return cycle;
    }

    public void setCycle(boolean cycle) {
        this.cycle = cycle;
    }

    public boolean isDentalIntervention() {
        return dentalIntervention;
    }

    public void setDentalIntervention(boolean dentalIntervention) {
        this.dentalIntervention = dentalIntervention;
    }
}
