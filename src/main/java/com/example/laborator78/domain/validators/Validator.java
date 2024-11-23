package com.example.laborator78.domain.validators;

public interface Validator <T>{
    void validate(T entity) throws ValidationException;
}
