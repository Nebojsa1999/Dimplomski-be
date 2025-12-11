package com.isa.domain.dto;


import jakarta.validation.constraints.NotNull;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class LoginDTO {

    @NotNull
    private String username;

    @NotNull
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("username", username)
                .append("password", password)
                .toString();
    }
}
