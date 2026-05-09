package com.isa.domain.dto;

public class DoctorRatingDto {

    private final Long doctorId;
    private final String doctorName;
    private final double averageRating;
    private final long ratingCount;

    public DoctorRatingDto(Long doctorId, String doctorName, double averageRating, long ratingCount) {
        this.doctorId = doctorId;
        this.doctorName = doctorName;
        this.averageRating = averageRating;
        this.ratingCount = ratingCount;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public long getRatingCount() {
        return ratingCount;
    }
}
