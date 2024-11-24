package com.example.laborator78.domain;

import java.time.LocalDateTime;

public class FriendshipRequest extends Entity<Long> {

    private Long user_from;
    private Long user_to;
    private String status;
    private LocalDateTime created_at;

    public FriendshipRequest( Long user_from, Long user_to, String status, LocalDateTime created_at) {
        this.user_from = user_from;
        this.user_to = user_to;
        this.status = status;
        this.created_at = created_at;
    }


    public Long getUser_from() {
        return user_from;
    }

    public void setUser_from(Long user_from) {
        this.user_from = user_from;
    }

    public Long getUser_to() {
        return user_to;
    }

    public void setUser_to(Long user_to) {
        this.user_to = user_to;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }
}
