package com.example.laborator78.domain.validators;

import com.example.laborator78.domain.FriendshipRequest;
import com.example.laborator78.repository.database.FriendshipDataBaseRepository;
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
//
//public class FriendshipRequestValidator implements Validator<FriendshipRequest> {
//
//    private FriendshipDataBaseRepository repo;
//
//    public FriendshipRequestValidator(FriendshipDataBaseRepository repo) {
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
//            if((x.getIdUser1().equals(entity.getUser_from()) && x.getIdUser2().equals(entity.getUser_to()))
//                    ||
//                   ( x.getIdUser2().equals(entity.getUser_to()) && x.getIdUser1().equals(entity.getUser_from())))
//                throw new ValidationException("The friendship request already exists! ");
//        });
//    }
//}
//
