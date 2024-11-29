package com.example.laborator78.repository.database;


import com.example.laborator78.domain.Friendship;
import com.example.laborator78.domain.pagining.Page;
import com.example.laborator78.domain.pagining.Pageable;
import com.example.laborator78.domain.validators.FriendshipValidator;
import com.example.laborator78.repository.PagingRepository;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

public class FriendshipDataBaseRepository implements PagingRepository<Long, Friendship> {
    private String url;
    private String username;
    private String password;
    private FriendshipValidator validator;


    public FriendshipDataBaseRepository(String url, String username, String pasword, FriendshipValidator friendshipValidator) {
        this.username = username;
        this.password = pasword;
        this.url = url;
        this.validator=friendshipValidator;
    }


    @Override
    public Optional<Friendship> findOne(Long aLong) {
        String query = "SELECT * FROM friendships WHERE \"id_friendship\"= ?";
        Friendship friendship = null;
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement statement = connection.prepareStatement(query);) {

            statement.setLong(1, aLong);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idFriend1 = resultSet.getLong("user_id1");
                Long idFriend2 = resultSet.getLong("user_id2");
                Timestamp date = resultSet.getTimestamp("friendship_date");
                LocalDateTime friendsFrom = new java.sql.Timestamp(date.getTime()).toLocalDateTime();
                friendship = new Friendship(idFriend1, idFriend2, friendsFrom);
                friendship.setId(aLong);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.ofNullable(friendship);
    }

    @Override
    public Iterable<Friendship> findAll() {
        Map<Long, Friendship> friendships = new HashMap<>();
        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM friendships");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id_friendship");
                Long idFriend1 = resultSet.getLong("user_id1");
                Long idFriend2 = resultSet.getLong("user_id2");
                Timestamp date = resultSet.getTimestamp("friendship_date");
                LocalDateTime friendsFrom = LocalDateTime.ofInstant(date.toInstant(), ZoneOffset.ofHours(0));
                Friendship friendship = new Friendship(idFriend1, idFriend2, friendsFrom);
                friendship.setId(id);
                friendships.put(friendship.getId(), friendship);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return friendships.values();
    }

    @Override
    public Optional<Friendship> save(Friendship entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Friendship can't be null!");
        }
        String query = "INSERT INTO friendships( \"user_id1\", \"user_id2\", \"friendship_date\") VALUES (?,?,?)";

        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement statement = connection.prepareStatement(query);) {
            //statement.setLong(1, entity.getId());
            statement.setLong(1, entity.getIdUser1());
            statement.setLong(2, entity.getIdUser2());
            statement.setTimestamp(3, java.sql.Timestamp.valueOf(entity.getDate()));
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return Optional.of(entity);
    }
    @Override
    public Optional<Friendship> delete(Long aLong) {
        String query = "DELETE FROM friendships WHERE \"id_friendship\" = ?";

        try (Connection connection = DriverManager.getConnection(url,username,password);
             PreparedStatement statement = connection.prepareStatement(query);) {
            statement.setLong(1, aLong);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Friendship friendshipToDelete = null;
        for (Friendship friendship : findAll()) {
            if (Objects.equals(friendship.getId(), aLong)) {
                friendshipToDelete = friendship;
            }
        }
        return Optional.ofNullable(friendshipToDelete);
    }

    @Override
    public Optional<Friendship> update(Friendship entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Friendship> findByEmailAndPassword(String email, String password) {
        return Optional.empty();
    }



    private int count(Long  id) throws SQLException {
        String query = "SELECT COUNT(*) FROM friendships WHERE user_id1 = ? OR user_id2 = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getInt(1);
        }
    }

    private List<Friendship> findAllOnPagePrivate(Long id, Pageable pageable) throws SQLException {
        String query = "SELECT * FROM friendships WHERE user_id1 = ? OR user_id2 = ? LIMIT ? OFFSET ?";
        List<Friendship> friendships = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            statement.setLong(2, id);
            statement.setInt(3, pageable.getPageSize());
            statement.setInt(4, pageable.getPageSize() * pageable.getPageNumber());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Long idFriend1 = resultSet.getLong("user_id1");
                Long idFriend2 = resultSet.getLong("user_id2");
                Timestamp date = resultSet.getTimestamp("friendship_date");
                LocalDateTime friendsFrom = new java.sql.Timestamp(date.getTime()).toLocalDateTime();
                Friendship friendship = new Friendship(idFriend1, idFriend2, friendsFrom);
                friendship.setId(resultSet.getLong("id_friendship"));
                friendships.add(friendship);
            }
        }
        return friendships;
    }



    @Override
    public Page<Friendship> findAllOnPage(Long id, Pageable pageable) {
        try{
            List<Friendship> friendshipsUsers;
            int totalElements = count(id);
            if(totalElements == 0) {
                friendshipsUsers = new ArrayList<>();
            }else{
                friendshipsUsers = findAllOnPagePrivate(id, pageable);
            }
            return new Page<>(friendshipsUsers, totalElements);
        }catch (SQLException e){
            throw new RuntimeException("Eroare la baza de date la paginare");
        }
    }
}
