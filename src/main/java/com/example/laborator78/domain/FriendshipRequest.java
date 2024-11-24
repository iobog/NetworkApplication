package com.example.laborator78.domain;

public class FriendshipRequest extends Entity<Long> {


    private Long from;
    private Long to;
    private String status;

    public FriendshipRequest( Long from, Long to, String status) {
        this.from = from;
        this.to = to;
        this.status = status;
    }


    public FriendshipRequest(Long from, Long to) {
        this.from = from;
        this.to = to;
        this.status = "pending";
    }


    public FriendshipRequest() {
        this.status = "pending";
    }



    public Long getFrom() {
        return from;
    }

    public Long getTo() {
        return to;
    }

    public String getStatus() {
        return status;
    }


    public void setFrom(Long from) {
        this.from = from;
    }

    public void setTo(Long to) {
        this.to = to;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "FriendshipRequest{" +
                ", from=" + from +
                ", to=" + to +
                ", status='" + status + '\'' +
                '}';
    }

}
