package com.isa.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Entity
public class Equipment extends AbstractEntity {

    private String name;

    private double amount;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("name", name)
                .append("amount", amount)
                .append("room", room)
                .toString();
    }
}
