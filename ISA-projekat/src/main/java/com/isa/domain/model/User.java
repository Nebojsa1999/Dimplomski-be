package com.isa.domain.model;

import com.isa.enums.Gender;
import com.isa.enums.Role;
import org.apache.commons.lang3.builder.ToStringBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


/**
 * The user entity representing all users of the system.
 */
@Entity
public class User extends AbstractEntity {

    @NotNull
    private String password;

    @NotNull
    @Column(unique = true)
    private String email;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    private String address;

    @NotNull
    private String city;

    @NotNull
    private String country;

    private double latitude;

    private double longitude;

    @NotNull
    private String phone;

    @NotNull
    private boolean firstLogin;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String personalId;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String occupation;

    private String occupationInfo;

    @ManyToOne
    private Hospital hospital;

    private double points;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(boolean firstLogin) {
        this.firstLogin = firstLogin;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPersonalId() {
        return personalId;
    }

    public void setPersonalId(String personalId) {
        this.personalId = personalId;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOccupationInfo() {
        return occupationInfo;
    }

    public void setOccupationInfo(String occupationInfo) {
        this.occupationInfo = occupationInfo;
    }

    public Hospital getHospital() {
        return hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("password", password).append("email", email).append("firstName", firstName).append("lastName", lastName).append("address", address).append("city", city).append("country", country).append("latitude", latitude).append("longitude", longitude).append("phone", phone).append("firstLogin", firstLogin).append("role", role).append("personalId", personalId).append("gender", gender).append("occupation", occupation).append("occupationInfo", occupationInfo).append("hospital", hospital).append("points", points).toString();
    }
}