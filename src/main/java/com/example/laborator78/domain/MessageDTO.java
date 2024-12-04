package com.example.laborator78.domain;

import java.time.LocalDateTime;
import java.util.Optional;

public class MessageDTO extends Entity<Long>{

    private Long id;
    private Long from_id;;
    private Long to_id;
    private String message;
    private LocalDateTime created_at;

    private Optional<String> replyMessage;


    public MessageDTO(Long id, String message, LocalDateTime created_at,Optional<String> replyMessage,Long from_id,Long to_id) {
        this.id = id;
        this.message = message;
        this.created_at = created_at;
        this.replyMessage = replyMessage;
        this.from_id= from_id;
        this.to_id= to_id;
    }

    public Long getFrom_id() {
        return from_id;
    }

    public Long getTo_id() {
        return to_id;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
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

    public Optional<String> getReplyMessage() {
        return replyMessage;
    }

    public void setReplyMessage(Optional<String> replyMessage) {
        this.replyMessage = replyMessage;
    }
}
