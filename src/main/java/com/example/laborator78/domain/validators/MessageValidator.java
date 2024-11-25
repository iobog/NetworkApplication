package com.example.laborator78.domain.validators;

import com.example.laborator78.domain.Message;

public class MessageValidator implements Validator<Message> {

    @Override
    public void validate(Message entity) throws ValidationException {

        if (entity.getFrom_id() == null || entity.getTo_id() == null) {
            throw new ValidationException("Invalid message");
        }
        if("".contains(entity.getMessage())){
            throw new ValidationException("Invalid message");
        }
    }
}
