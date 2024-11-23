package com.example.laborator78.domain.validators;

import com.example.laborator78.domain.Friendship;
import com.example.laborator78.domain.User;
import com.example.laborator78.repository.Repository;
import com.example.laborator78.repository.database.UserDataBaseRepository;

import java.util.Optional;

public class FriendshipValidator implements Validator<Friendship> {

    private Repository<Long, User> repo;

    public FriendshipValidator(UserDataBaseRepository repo) {
        this.repo = repo;
    }

    public FriendshipValidator() {

    }

    @Override
    public void validate(Friendship entity) throws ValidationException {

        Optional<User> u1 = repo.findOne(entity.getIdUser1());
        Optional<User> u2 = repo.findOne(entity.getIdUser2());

        if (entity.getIdUser1() == null || entity.getIdUser2() == null)
            throw new ValidationException("The id can't be null! ");
        if (u1.isEmpty() || u2.isEmpty())
            throw new ValidationException("The id doesn't exist! ");
    }
}