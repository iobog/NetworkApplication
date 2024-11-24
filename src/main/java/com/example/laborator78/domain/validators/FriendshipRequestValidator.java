package com.example.laborator78.domain.validators;

import com.example.laborator78.domain.FriendshipRequest;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) {
        if (entity.getUser_from() == null || entity.getUser_to() == null) {
            throw new ValidationException("Invalid friendship request");
        }
    }

}
