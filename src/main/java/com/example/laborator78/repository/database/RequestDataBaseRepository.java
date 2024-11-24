package com.example.laborator78.repository.database;

import com.example.laborator78.domain.FriendshipRequest;
import com.example.laborator78.domain.validators.FriendshipRequestValidator;
import com.example.laborator78.domain.validators.Validator;
import com.example.laborator78.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class RequestDataBaseRepository implements Repository<Long, FriendshipRequest> {

    private String url;
    private String username;
    private String password;
    private Validator<FriendshipRequest>validator;

    public RequestDataBaseRepository(String url, String username, String password,FriendshipRequestValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Optional<FriendshipRequest> findOne(Long id) {
        String query = "SELECT * FROM friendship_requests WHERE req_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id); // Set the ID parameter in the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Check if a result was found
                Long requestId = resultSet.getLong("req_id");
                Long fromId = resultSet.getLong("user_from");
                Long toId = resultSet.getLong("user_to");
                String status = resultSet.getString("status");
                Timestamp date = resultSet.getTimestamp("created_at");
                LocalDateTime createdAt = new java.sql.Timestamp(date.getTime()).toLocalDateTime();

                FriendshipRequest request = new FriendshipRequest(fromId, toId, status,createdAt);
                request.setId(requestId);
                return Optional.of(request); // Return the found request
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty(); // Return empty if request is not found or error occurred
    }

    @Override
    public Iterable<FriendshipRequest> findAll() {
        Set<FriendshipRequest> requests = new HashSet<>();
        String query = "SELECT * FROM friendship_requests";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long requestId = resultSet.getLong("req_id");
                Long fromId = resultSet.getLong("user_from");
                Long toId = resultSet.getLong("user_to");
                String status = resultSet.getString("status");
                Timestamp date = resultSet.getTimestamp("created_at");
                LocalDateTime createdAt = new java.sql.Timestamp(date.getTime()).toLocalDateTime();

                FriendshipRequest request = new FriendshipRequest(fromId, toId, status,createdAt);
                request.setId(requestId);
                requests.add(request);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }
    @Override
    public Optional<FriendshipRequest> save(FriendshipRequest entity) {
        validator.validate(entity); // Validate the entity before saving

        String query = "INSERT INTO friendship_requests (user_from, user_to, status) VALUES (?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, entity.getUser_from());
            statement.setLong(2, entity.getUser_to());
            statement.setString(3, entity.getStatus());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                ResultSet generatedKeys = statement.getGeneratedKeys();
                if (generatedKeys.next()) {
                    entity.setId(generatedKeys.getLong(1));
                }
                return Optional.empty(); // Return empty if save was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity); // Return the entity if save failed
    }

    @Override
    public Optional<FriendshipRequest> delete(Long id) {
        Optional<FriendshipRequest> requestToDelete = findOne(id); // Check if request exists

        if (requestToDelete.isPresent()) {
            String query = "DELETE FROM friendship_requests WHERE req_id = ?";
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement(query)) {

                statement.setLong(1, id);
                statement.executeUpdate();

                return requestToDelete; // Return the deleted request
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty(); // Request not found or delete failed
    }
    @Override
    public Optional<FriendshipRequest> update(FriendshipRequest entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        validator.validate(entity); // Validate the entity before updating

        String query = "UPDATE friendship_requests SET  status = ? WHERE req_id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getStatus());
            statement.setLong(2, entity.getId());

            int affectedRows = statement.executeUpdate();
            if (affectedRows > 0) {
                return Optional.empty(); // Return empty if update was successful
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.of(entity); // Return the entity if update failed
    }

    @Override
    public Optional<FriendshipRequest> findByEmailAndPassword(String email, String password) {
        return Optional.empty();
    }
}
