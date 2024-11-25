package com.example.laborator78.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public class Message extends Entity<Long>{

    private Long from_id;
    private Long to_id;
    private String message;
    private LocalDateTime created_at;
    private Optional<Long> reply_massage_id;

    public Message(Long from_id, Long to_id, String message, LocalDateTime created_at, Optional<Long> reply_massage_id) {
        this.from_id = from_id;
        this.to_id = to_id;
        this.message = message;
        this.created_at = created_at;
        this.reply_massage_id = reply_massage_id;
    }



    public Long getFrom_id() {
        return from_id;
    }

    public void setFrom_id(Long from_id) {
        this.from_id = from_id;
    }

    public Long getTo_id() {
        return to_id;
    }

    public void setTo_id(Long to_id) {
        this.to_id = to_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public Optional<Long> getReply_massage_id() {
        return reply_massage_id;
    }

    public void setReply_massage_id(Optional<Long> reply_massage_id) {
        this.reply_massage_id = reply_massage_id;
    }
}
