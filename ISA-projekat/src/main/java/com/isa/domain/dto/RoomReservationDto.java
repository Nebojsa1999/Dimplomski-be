package com.isa.domain.dto;

import com.isa.enums.OperationType;
import jakarta.validation.constraints.NotNull;

public class RoomReservationDto {

    @NotNull
    private Long roomId;

    @NotNull
    private String startTime;

    @NotNull
    private String endTime;

    @NotNull
    private OperationType operationType;

    public Long getRoomId() {
        return roomId;
    }

    public void setRoomId(Long roomId) {
        this.roomId = roomId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public OperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(OperationType operationType) {
        this.operationType = operationType;
    }
}
