package com.example.laborator78.repository.database;

import com.example.laborator78.domain.FriendshipRequest;
import com.example.laborator78.domain.Message;
import com.example.laborator78.domain.validators.MessageValidator;
import com.example.laborator78.repository.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class MessageDataBaseRepository implements Repository<Long, Message> {
    private final String url;
    private final String username;
    private final String password;
    private MessageValidator validator;

    public MessageDataBaseRepository(String url, String username, String password, MessageValidator validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }


    @Override
    public Optional<Message> findOne(Long id) {
        String query = "SELECT * FROM messages WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id); // Set the ID parameter in the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Check if a result was found
                Long messageId = resultSet.getLong("id");
                Long fromId = resultSet.getLong("from_id");
                Long toId = resultSet.getLong("to_id");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("created_at");
                LocalDateTime createdAt = new java.sql.Timestamp(date.getTime()).toLocalDateTime();

                Optional<Long> ReplyMessageId = Optional.of(resultSet.getLong("reply_message_id"));

                Message messageEntity = new Message(fromId, toId, message,createdAt,ReplyMessageId);
                messageEntity.setId(messageId);
                return Optional.of(messageEntity); // Return the found request
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty(); // Return empty if request is not found or error occurred
    }

    @Override
    public Iterable<Message> findAll() {
        Set<Message> requests = new HashSet<>();
        String query = "SELECT * FROM messages";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long fromId = resultSet.getLong("from_id");
                Long toId = resultSet.getLong("to_id");
                String message = resultSet.getString("message");
                Timestamp date = resultSet.getTimestamp("created_at");
                LocalDateTime createdAt = new java.sql.Timestamp(date.getTime()).toLocalDateTime();

                Optional<Long> ReplyMessageId = Optional.of(resultSet.getLong("reply_message_id"));

                Message entityMessage = new Message(fromId, toId, message,createdAt,ReplyMessageId);
                entityMessage.setId(id);
                requests.add(entityMessage);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return requests;
    }

    @Override
    public Optional<Message> save(Message entity) {
        validator.validate(entity); // Validate the entity before saving

        String query = "INSERT INTO messages (from_id,to_id,message,reply_message_id) VALUES (?,?, ?, ?)";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, entity.getFrom_id());
            statement.setLong(2, entity.getTo_id());
            statement.setString(3, entity.getMessage());
            if(entity.getReply_massage_id().isPresent())
                statement.setLong(4, entity.getReply_massage_id().get());
            else
                statement.setNull(4, Types.BIGINT);

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
    public Optional<Message> delete(Long id) {
        Optional<Message> requestToDelete = findOne(id); // Check if request exists

        if (requestToDelete.isPresent()) {
            String query = "DELETE FROM messages WHERE id = ?";
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
    public Optional<Message> update(Message entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entity must not be null!");
        }
        validator.validate(entity); // Validate the entity before updating

        String query = "UPDATE messages SET  message = ? WHERE id = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.getMessage());
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
    public Optional<Message> findByEmailAndPassword(String email, String password) {
        return Optional.empty();
    }
}
