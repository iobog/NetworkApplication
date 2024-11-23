package com.example.laborator78.service;

import com.example.laborator78.controller.UserRequestDTO;
import com.example.laborator78.domain.Friendship;
import com.example.laborator78.domain.User;
import com.example.laborator78.domain.validators.ValidationException;
import com.example.laborator78.utils.events.ChangeEventType;
import com.example.laborator78.utils.events.Event;
import com.example.laborator78.utils.events.UserEntityChangeEvent;
import com.example.laborator78.utils.observer.Observable;
import com.example.laborator78.utils.observer.Observer;
import com.example.laborator78.repository.database.FriendshipDataBaseRepository;
import com.example.laborator78.repository.database.UserDataBaseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

public class Network implements Observable {
    private final UserDataBaseRepository repositoryUser;
    private final FriendshipDataBaseRepository repositoryFriendship;
    private final List<Observer> observers = new ArrayList<>();

    public Network(UserDataBaseRepository userDataBaseRepository, FriendshipDataBaseRepository friendshipDataBaseRepository) {
        this.repositoryUser = userDataBaseRepository;
        this.repositoryFriendship = friendshipDataBaseRepository;
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Event t) {

    }

    @Override
    public void notifyObservers(String eventType, Object data) {
        for (Observer observer : observers) {
            observer.update(eventType, data);
        }
    }

    public Iterable<User> getUsers() {
        return repositoryUser.findAll();
    }

    public Optional<User> findUser(Long id) {
        return repositoryUser.findOne(id);
    }

    public User addUser(User user) {
        repositoryUser.save(user);
        notifyObservers("user_added", user);
        return user;
    }

    public Iterable<Friendship> getFriendships() {
        return repositoryFriendship.findAll();
    }

    public User removeUser(Long id) {
        try {
            Optional<User> u = repositoryUser.findOne(id);
            if (u.isEmpty()) {
                throw new IllegalArgumentException("The user doesn't exist!");
            }
            Vector<Long> toDelete = new Vector<>();
            for (Friendship friendship : getFriendships()) {
                if (friendship.getIdUser2().equals(id) || friendship.getIdUser1().equals(id)) {
                    toDelete.add(friendship.getId());
                }
            }
            for (Long idToDelete : toDelete) {
                repositoryFriendship.delete(idToDelete);
            }
            Optional<User> user = repositoryUser.delete(id);
            for (User friend : u.get().getFriends()) {
                friend.removeFriend(u.orElse(null));
            }
            notifyObservers("user_removed", u.orElse(null));
            return user.orElse(null);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid user!");
        }
        return null;
    }

    public void addFriendship(Friendship friendship) {
        Optional<User> user1 = repositoryUser.findOne(friendship.getIdUser1());
        Optional<User> user2 = repositoryUser.findOne(friendship.getIdUser2());
        if (getFriendships() != null) {
            for (Friendship f : getFriendships()) {
                if (f.getIdUser1().equals(friendship.getIdUser1()) && f.getIdUser2().equals(friendship.getIdUser2())) {
                    throw new ValidationException("The friendship already exists!");
                }
            }
            if (repositoryUser.findOne(friendship.getIdUser1()) == null || repositoryUser.findOne(friendship.getIdUser2()) == null) {
                throw new ValidationException("User doesn't exist!");
            }
            if (friendship.getIdUser1().equals(friendship.getIdUser2()))
                throw new ValidationException("IDs can't be the same!!!");
        }
        repositoryFriendship.save(friendship);

        user1.get().addFriend(user2.orElse(null));
        user2.get().addFriend(user1.orElse(null));

        notifyObservers("friendship_added", friendship);
    }

    public void removeFriendship(Long id1, Long id2) {
        Optional<User> user1 = repositoryUser.findOne(id1);
        Optional<User> user2 = repositoryUser.findOne(id2);

        Long id = 0L;
        for (Friendship f : repositoryFriendship.findAll()) {
            if ((f.getIdUser1().equals(id1) && f.getIdUser2().equals(id2)) || (f.getIdUser1().equals(id2) && f.getIdUser2().equals(id1)))
                id = f.getId();
        }
        if (id == 0L)
            throw new IllegalArgumentException("The friendship doesn't exist!");
        repositoryFriendship.delete(id);

        user1.get().removeFriend(user2.orElse(null));
        user2.get().removeFriend(user1.orElse(null));

        notifyObservers("friendship_removed", id);
    }

    public User updateUtilizator(User u) {
        Optional<User> oldUser=repositoryUser.findOne(u.getId());
        if(oldUser.isPresent()) {
            Optional<User> newUser=repositoryUser.update(u);
            if (newUser.isEmpty())
                notifyObservers(new UserEntityChangeEvent(ChangeEventType.UPDATE, u, oldUser.get()));
            return newUser.orElse(null);
        }
        return oldUser.orElse(null);
    }
    public List<Optional<User>> getListFriends(User user) {
        List<Optional<User>> friends = new ArrayList<>();
        getFriendships().forEach(friendship -> {
            if (friendship.getIdUser1().equals(user.getId())) {
                friends.add(findUser(friendship.getIdUser2()));
            } else if (friendship.getIdUser2().equals(user.getId())) {
                friends.add(findUser(friendship.getIdUser1()));
            }
        });
        return friends;
    }

    public User findUserByEmailAndPassword(String email, String password) {
        Optional<User> user = repositoryUser.findByEmailAndPassword(email, password);

        return user.orElse(null);
    }

    public boolean findUserByEmail(String email) {
        Optional<User> user = repositoryUser.findByEmail(email);

        return user.isPresent();
    }

    /// this function is not implemented good
    public List<UserRequestDTO> getUserRequests() {
        ///
        List<UserRequestDTO> userRequests = new ArrayList<>();
        for (User user : getUsers()) {
            if (user.getFriends().size() == 0) {
                userRequests.add(new UserRequestDTO(user.getFirstName(), user.getLastName(), user.getId()));
            }
        }
        return userRequests;
    }
}
