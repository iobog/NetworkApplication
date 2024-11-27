package com.example.laborator78.service;

import com.example.laborator78.domain.*;
import com.example.laborator78.domain.validators.ValidationException;
import com.example.laborator78.repository.database.MessageDataBaseRepository;
import com.example.laborator78.repository.database.RequestDataBaseRepository;
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
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.StreamSupport;

public class Network implements Observable {
    private final UserDataBaseRepository repositoryUser;
    private final FriendshipDataBaseRepository repositoryFriendship;
    private final RequestDataBaseRepository repositoryFriendshipRequest;
    private final MessageDataBaseRepository repositoryMessage;

    private final List<Observer> observers = new ArrayList<>();

    public Network(UserDataBaseRepository userDataBaseRepository, FriendshipDataBaseRepository friendshipDataBaseRepository, RequestDataBaseRepository requestDataBaseRepository, MessageDataBaseRepository repositoryMessage) {
        this.repositoryUser = userDataBaseRepository;
        this.repositoryFriendship = friendshipDataBaseRepository;
        this.repositoryFriendshipRequest=requestDataBaseRepository;
        this.repositoryMessage = repositoryMessage;
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


    public List<User> getRecommendedFriends(User currentUser) {
        List<User>nonFriends = new ArrayList<>();

        var friends = this.getListFriends(currentUser).stream()
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(User::getId)
                .toList();

        var sentRequests = getSentFriendshipRequests(currentUser).stream()
                .map(UserRequestDTO::getUserId)
                .toList();

        getUsers().forEach(user -> {
            if (!user.equals(currentUser)) {
                boolean containsValue = friends.contains(user.getId());

                boolean requested = sentRequests.contains(user.getId());

                if (!containsValue && !requested) {
                    nonFriends.add(user);
                }
            }
        });
        return nonFriends;
    }

    public List<UserRequestDTO> getFriendshipRequests(User currentUser) {
        var requests = repositoryFriendshipRequest.findAll();
        return StreamSupport.stream(requests.spliterator(), false)
            .filter(request -> request.getUser_to().equals(currentUser.getId()) && request.getStatus().equals("pending"))
            .map(request -> {
                var user = findUser(request.getUser_from()).orElse(null);
                UserRequestDTO userRequestDTO= new UserRequestDTO(user.getFirstName(), user.getLastName(), request.getUser_from(), request.getCreated_at(),request.getStatus());
                userRequestDTO.setId(request.getId());
                return userRequestDTO;
            })
            .toList();

    }

    public List<UserRequestDTO> getSentFriendshipRequests(User currentUser) {
        var requests = repositoryFriendshipRequest.findAll();
        return StreamSupport.stream(requests.spliterator(), false)
                .filter(request -> request.getUser_from().equals(currentUser.getId()) && request.getStatus().equals("pending"))
                .map(request -> {
                    var user = findUser(request.getUser_to()).orElse(null);
                    UserRequestDTO userRequestDTO= new UserRequestDTO(user.getFirstName(), user.getLastName(), request.getUser_to(), request.getCreated_at(),request.getStatus());
                    userRequestDTO.setId(request.getId());
                    return userRequestDTO;
                })
                .toList();

    }


    public void acceptFriendshipRequest(User currentUser, UserRequestDTO userRequestDTO) {
        Friendship friendship = new Friendship(currentUser.getId(), userRequestDTO.getUserId());
        addFriendship(friendship);
        FriendshipRequest friendshipRequest = new FriendshipRequest(userRequestDTO.getUserId(),currentUser.getId(),"accepted", userRequestDTO.getCreated_at());
        friendshipRequest.setId(userRequestDTO.getId());
        repositoryFriendshipRequest.update(friendshipRequest);

    }

    public void rejectFriendshipRequest(User currentUser, UserRequestDTO userRequestDTO) {
        Friendship friendship = new Friendship(currentUser.getId(), userRequestDTO.getUserId());
       // addFriendship(friendship);
        FriendshipRequest friendshipRequest = new FriendshipRequest(userRequestDTO.getUserId(),currentUser.getId(),"rejected", userRequestDTO.getCreated_at());
        friendshipRequest.setId(userRequestDTO.getId());
        repositoryFriendshipRequest.update(friendshipRequest);
    }

    public void sendFriendshipRequest(User user, User user1) {
        FriendshipRequest friendshipRequest = new FriendshipRequest(user.getId(), user1.getId(), "pending", java.time.LocalDateTime.now());
        repositoryFriendshipRequest.save(friendshipRequest);
    }

    public void deleteFriendshipRequest(UserRequestDTO userRequestDTO) {
        repositoryFriendshipRequest.delete(userRequestDTO.getId());
    }


    //send message,
    // get my messages
//
//    public void sendMessage(User from, User to, String message, Optional<Long> reply_message_id) {
//        Message message1 = new Message(from.getId(), to.getId(), message, java.time.LocalDateTime.now(), reply_message_id);
//        repositoryMessage.save(message1);
//    }
//
//
//
//    public List<MessageDTO> listMessages(User user1,User user2) {
//        List<MessageDTO>messages = new ArrayList<>();
//        repositoryMessage.findAll().forEach(message -> {
//            if (message.getTo_id().equals(user1.getId()) && message.getFrom_id().equals(user2.getId())
//            || (message.getTo_id().equals(user2.getId()) && message.getFrom_id().equals(user1.getId()))) {
//
//                Optional<String> strReplayMessage = Optional.empty();
//                if (message.getReply_massage_id().isPresent()) {
//                    Optional<Message> replyMessage = repositoryMessage.findOne(message.getReply_massage_id().get());
//                    if (replyMessage.isPresent()) {
//                        strReplayMessage = Optional.of(replyMessage.get().getMessage());
//                    }
//                }
//
//                MessageDTO messageDTO = new MessageDTO(message.getId(), message.getMessage(), message.getCreated_at(), strReplayMessage, message.getFrom_id(), message.getTo_id());
//                messages.add(messageDTO);
//
//            }
//        });
//        messages.sort((m1,m2)->m1.getCreated_at().compareTo(m2.getCreated_at()));
//        return messages;
//    }

    public void sendMessage(User from, User to, String message, Optional<Long> reply_message_id) {
        Message message1 = new Message(from.getId(), to.getId(), message, java.time.LocalDateTime.now(), reply_message_id);
        repositoryMessage.save(message1);

        // Notify observers about the new message
        notifyObservers("message_sent", new MessageDTO(
                message1.getId(),
                message1.getMessage(),
                message1.getCreated_at(),
                reply_message_id.map(id -> repositoryMessage.findOne(id).orElse(new Message(0L, 0L, "", null, Optional.empty())).getMessage()),
                from.getId(),
                to.getId()
        ));
    }

    public List<MessageDTO> listMessages(User user1, User user2) {
        List<MessageDTO> messages = new ArrayList<>();
        repositoryMessage.findAll().forEach(message -> {
            if (message.getTo_id().equals(user1.getId()) && message.getFrom_id().equals(user2.getId())
                    || (message.getTo_id().equals(user2.getId()) && message.getFrom_id().equals(user1.getId()))) {

                AtomicReference<Optional<String>> strReplyMessage = new AtomicReference<>(Optional.empty());
                if (message.getReply_massage_id().isPresent()) {
                    Optional<Message> replyMessage = repositoryMessage.findOne(message.getReply_massage_id().get());
                    replyMessage.ifPresent(value -> strReplyMessage.set(Optional.of(value.getMessage())));
                }

                MessageDTO messageDTO = new MessageDTO(
                        message.getId(),
                        message.getMessage(),
                        message.getCreated_at(),
                        strReplyMessage.get(),
                        message.getFrom_id(),
                        message.getTo_id()
                );
                messages.add(messageDTO);
            }
        });
        messages.sort((m1, m2) -> m1.getCreated_at().compareTo(m2.getCreated_at()));
        return messages;
    }


}
