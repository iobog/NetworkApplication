package com.example.laborator78.domain;

import javax.xml.crypto.Data;
import java.time.LocalDateTime;

public class UserRequestDTO extends Entity<Long> {

    private String firstName;
    private String lastName;
    private String status;
    private LocalDateTime created_at;

    private long userId;

    public UserRequestDTO(String firstName, String lastName, long userId, LocalDateTime data, String status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.userId = userId;
        this.created_at = data;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

}
