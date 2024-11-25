package com.example.laborator78.domain.validators;

import com.example.laborator78.domain.FriendshipRequest;
import com.example.laborator78.repository.database.RequestDataBaseRepository;

public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
    @Override
    public void validate(FriendshipRequest entity) {
        if (entity.getUser_from() == null || entity.getUser_to() == null) {
            throw new ValidationException("Invalid friendship request");
        }

    }

}


//
//public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
//
//    private Freinds repo;
//
//    public FriendshipRequestValidator(Friendip repo) {
//        this.repo = repo;
//    }
//
//    @Override
//    public void validate(FriendshipRequest entity) throws ValidationException {
//
//        if (entity.getUser_from() == null || entity.getUser_to() == null)
//            throw new ValidationException("The id can't be null! ");
//        if(repo.findOne(entity.getId()).isPresent())
//            throw new ValidationException("The id already exists! ");
//        if(repo.findOne(entity.getId()).isPresent())
//            throw new ValidationException("The id already exists! ");
//        repo.findAll().forEach(x->{
//            if((x.getUser_from().equals(entity.getUser_from()) && x.getUser_to().equals(entity.getUser_to()))
//                    ||
//                   ( x.getUser_from().equals(entity.getUser_to()) && x.getUser_to().equals(entity.getUser_from())))
//                throw new ValidationException("The friendship request already exists! ");
//        });
//    }
//}

