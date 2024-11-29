package com.example.laborator78.repository.database;


import com.example.laborator78.domain.User;
import com.example.laborator78.domain.validators.Validator;
import com.example.laborator78.repository.Repository;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class UserDataBaseRepository implements Repository<Long, User> {
    private final String url;
    private final String username;
    private final String password;
    private final Validator<User> validator;

    public UserDataBaseRepository(String url, String username, String password, Validator<User> validator) {
        this.url = url;
        this.username = username;
        this.password = password;
        this.validator = validator;
    }
//    @Override
//    public Optional<Utilizator> findOne(Long aLong) {
//        return Optional.empty();
//    }

    @Override

    public Optional<User> findOne(Long id) {
        String query = "SELECT * FROM users WHERE id_user = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setLong(1, id); // Set the ID parameter in the query
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) { // Check if a result was found
                Long userId = resultSet.getLong("id_user");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User user = new User(firstName, lastName, email, password);
                user.setId(userId);
                return Optional.of(user); // Return the found user
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty(); // Return empty if user is not found or error occurred
    }


    @Override
    public Iterable<User> findAll() {
        Set<User> users = new HashSet<>();
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("SELECT * from users");
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Long id = resultSet.getLong("id_user");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String email = resultSet.getString("email");
                String password = resultSet.getString("password");

                User utilizator = new User(firstName, lastName, email, password);
                utilizator.setId(id);
                users.add(utilizator);
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public Optional<User> save(User entity) {
        int rez = -1;
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users (first_name, last_name,email,password) VALUES (?,?,?, ?)");
        ) {

            //statement.setString(0, String.valueOf(entity.getId()));
            statement.setString(1, entity.getFirstName());
            statement.setString(2, entity.getLastName());
            statement.setString(3, entity.getEmail());
            statement.setString(4, entity.getPassword());
            rez = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (rez > 0)
            return null;
        else
            return Optional.ofNullable(entity);
    }

    @Override
    public Optional<User> delete(Long id) {
        Optional<User> userToDelete = findOne(id); // Check if user exists

        if (userToDelete.isPresent()) {
            try (Connection connection = DriverManager.getConnection(url, username, password);
                 PreparedStatement statement = connection.prepareStatement("DELETE FROM users WHERE id_user = ?")) {

                statement.setLong(1, id);
                statement.executeUpdate();

                return userToDelete; // Return the deleted user
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return Optional.empty(); // User not found or delete failed
    }


    @Override
    public Optional<User> update(User user) {
        if( user== null)
            throw new IllegalArgumentException("entity must be not null!");
        validator.validate(user);
        String sql = "update users set first_name = ?, last_name = ?,email = ?, password = ?, where id_user = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1,user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setString(3, user.getEmail());
            ps.setString(4, user.getPassword());
            ps.setLong(5, user.getId());
            if( ps.executeUpdate() > 0 )
                return Optional.empty();
            return Optional.ofNullable(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public Optional<User> findByEmailAndPassword(String email, String passwordUser) {
        if (email == null || passwordUser == null)
            throw new IllegalArgumentException("Email and password must not be null!");

        String sql = "SELECT * FROM users WHERE email = ? AND password = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the query parameters
            statement.setString(1, email);
            statement.setString(2, passwordUser);

            ResultSet resultSet = statement.executeQuery();

            // If a user is found, create a User object
            if (resultSet.next()) {
                Long userId = resultSet.getLong("id_user");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String emailResult = resultSet.getString("email");
                String passwordResult = resultSet.getString("password");

                User user = new User(firstName, lastName, emailResult, passwordResult);
                user.setId(userId);

                return Optional.of(user); // Return the found user
            }
        } catch (SQLException e) {
            System.err.println("Database error during findByEmailAndPassword: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty(); // Return empty if no user is found
    }

    public Optional<User> findByEmail(String email) {
        if (email == null)
            throw new IllegalArgumentException("Email must not be null!");

        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the query parameters
            statement.setString(1, email);

            ResultSet resultSet = statement.executeQuery();

            // If a user is found, create a User object
            if (resultSet.next()) {
                Long userId = resultSet.getLong("id_user");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String emailResult = resultSet.getString("email");
                String passwordResult = resultSet.getString("password");

                User user = new User(firstName, lastName, emailResult, passwordResult);
                user.setId(userId);

                return Optional.of(user); // Return the found user
            }
        } catch (SQLException e) {
            System.err.println("Database error during findByEmail: " + e.getMessage());
            e.printStackTrace();
        }

        return Optional.empty(); // Return empty if no user is found
    }





}
