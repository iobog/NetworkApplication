package com.example.laborator78.domain.validators;

import com.example.laborator78.domain.FriendshipRequest;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) {
        if (entity.getFrom() == null || entity.getTo() == null) {
            throw new ValidationException("Invalid friendship request");
        }
    }

}
