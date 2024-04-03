package org.example.socialMN.service;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.example.socialMN.dao.IDao;
import org.example.socialMN.exceptions.*;
import org.example.socialMN.model.Friendship;
import org.example.socialMN.model.User;
import org.jasypt.util.password.BasicPasswordEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ServiceImpl implements IService {
    private static final Logger logger = LogManager.getLogger(IService.class);

    @Autowired
    private IDao dao;

    @Autowired
    private IService service;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void addUser(User user) {
        BasicPasswordEncryptor passwordEncryptor = new BasicPasswordEncryptor();

        String hashedPassword = passwordEncryptor.encryptPassword(user.getPassword());
        user.setPassword(hashedPassword);

        logger.info("Adding a new user: " + user.getUsername());
        dao.save(user);
        logger.info("User added successfully: " + user.getUsername());
    }

    /**
     * Validates a user in the system based on the provided username and password.
     * This method checks if a user with the given username and password combination exists.
     * return true if a user with the specified username and password exists, false otherwise.
     */
    @Override
    public boolean getValidateUser(String username, String password) throws AuthenticationException {
        logger.info("Validating user with username: " + username);


        // HQL to count users with the given username and password
        String hql = "SELECT COUNT(*) FROM User WHERE username = :username AND password = :password";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);
        logger.info("User validation result for " + username + ": " + dao.executeQueryForValidation(hql, parameters));

        // Delegate the validation to the DAO layer by executing the HQL query for validation
        boolean validationResult = dao.executeQueryForValidation(hql, parameters);

        if (!validationResult) {
            throw new AuthenticationException("User validation failed for username: " + username);
        }

        logger.info("User validation result for " + username + ": " + validationResult);
        return validationResult;
    }

    /**
     * Retrieves user data based on the provided username and password.
     * This method retrieves a user entity from the system using the specified username and password.
     * return The User entity corresponding to the given username and password
     */
    @Override
    public User getUserData(String username, String password) throws UserDataRetrievalException {
        logger.info("Retrieving user data for username: " + username + "ANd password");
        String hql = "FROM " + User.class.getName() + " WHERE username = :username AND password = :password";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("username", username);
        parameters.put("password", password);


        logger.info("User data retrieved successfully for " + username);


        // Delegate the query execution to the DAO layer, expecting a single result
        User user = dao.executeHqlQuerySingleResult(hql, User.class, parameters);

        if (user == null) {
            logger.error("User not found for the provided credentials: " + username);
            throw new UserDataRetrievalException("User not found ");
        }

        logger.info("User data retrieved successfully for " + username);
        return user;
    }

    /**
     * Retrieves a list of suggested friends for the user with the given username.
     * This method queries the system to obtain a list of users who are suggested friends
     * for the user with the specified username, excluding the user themselves.
     * return A list of User entities representing suggested friends for the logged-in user.
     */

    @Override
    public List<User> getSuggestedFriends(String loggedUserName) throws SuggestedFriendsException {
        try {
            logger.info("Retrieving suggested friends for user: " + loggedUserName);

            List<User> userFriends = getUserFriends(loggedUserName);
            // If the user has no friends, return all suggested friends
            if (userFriends.isEmpty()) {
                // select all users except the logged-in user
                String hql = "FROM " + User.class.getName() + " WHERE username != :userName";
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("userName", loggedUserName);
                List<User> suggestedFriends = dao.executeHqlQuery(hql, User.class, parameters);
                logger.info("Suggested friends retrieved successfully for " + loggedUserName);
                return suggestedFriends;
            } else {
                // If the user has friends, exclude them from the list of suggested friends
                List<String> friendUsernames = new ArrayList<>();
                for (User userFriend : userFriends) {
                    String username = userFriend.getUsername();
                    friendUsernames.add(username);
                }
                //to select suggested friends who are not already friends
                String hql = "FROM " + User.class.getName() + " WHERE username != :userName AND username NOT IN :friendUsernames";
                Map<String, Object> parameters = new HashMap<>();
                parameters.put("userName", loggedUserName);
                parameters.put("friendUsernames", friendUsernames);
                List<User> suggestedFriends = dao.executeHqlQuery(hql, User.class, parameters);
                logger.info("Suggested friends retrieved successfully for " + loggedUserName);
                return suggestedFriends;
            }

        } catch (Exception e) {
            throw new SuggestedFriendsException("Error retrieving suggested friends: " + e.getMessage());
        }
    }


    /**
     * Adds a friendship connection between the given user and friend.
     * the user and the specified friend, and then saves this relationship using the DAO layer.
     */
    @Override
    public void addFriend(User user, User friend) throws AddFriendException {


        logger.info("Adding friend connection between " + user.getUsername() + " and " + friend.getUsername());


        // Create a new Friendship entity to represent the connection
        Friendship friends = new Friendship();
        friends.setUser(user);
        friends.setFriend(friend);
        try {
            dao.save(friends);
            logger.info("Friend connection added successfully");
        } catch (Exception e) {
            // If an exception occurs during the friend addition process, throw AddFriendException
            throw new AddFriendException("Error adding friend: " + e.getMessage());
        }
    }

    /**
     * Retrieves a user by their username using Hibernate Query Language (HQL).
     * return The User object corresponding to the provided username
     */
    @Override
    public User getByUsername(String username) throws UserDataRetrievalException {
        try {
            logger.info("Retrieving user by username: " + username);
            // HQL query to select a user based on the provided username
            String hql = "FROM User WHERE username = :username";
            // Set the parameters for the HQL query
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("username", username);
            logger.info("User retrieved successfully for " + username);
            // Execute the HQL query and retrieve a single result (or null)
            return dao.executeHqlQuerySingleResult(hql, User.class, parameters);
        } catch (Exception e) {
            // Log the exception
            logger.error("Error retrieving user by username: " + username, e);
            // Throw a custom exception to indicate the problem
            throw new UserDataRetrievalException("Error retrieving user by username: " + username);
        }
    }


    /**
     * Retrieves the list of friends for a given user.
     * <p>
     * loggedUserName The username of the user for whom friends are to be retrieved.
     * return A List of User objects representing the friends of the specified user.
     *
     * @throws UserFriendsException If an error occurs while retrieving user friends.
     */
    @Override
    public List<User> getUserFriends(String loggedUserName) throws UserFriendsException {
        try {
            logger.info("Retrieving friends for user: " + loggedUserName);
            //select friends for the logged-in user
            String hql = "SELECT f.friend FROM " + Friendship.class.getName() + " f WHERE f.user.username = :userName";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("userName", loggedUserName);
            List<User> friends = dao.executeHqlQuery(hql, User.class, parameters);
            logger.info("Friends retrieved successfully for " + loggedUserName);
            return friends;
        } catch (Exception e) {
            e.printStackTrace();
            throw new UserFriendsException("Error retrieving user friends: " + e.getMessage());
        }
    }

    /**
     * Removes the friendship connection between two users.
     *
     * @throws RemoveFriendException If an error occurs while removing the friend connection.
     */
    @Override
    public void removeFriend(String loggedUserName, String friendUserName) throws RemoveFriendException {
        try {
            String sql = "DELETE FROM user_friends " + "WHERE " + "(user_id IN (SELECT id FROM User WHERE username = :loggedUserName) AND friend_id IN (SELECT id FROM User WHERE username = :friendUserName)) " + "OR " + "(user_id IN (SELECT id FROM User WHERE username = :friendUserName) AND friend_id IN (SELECT id FROM User WHERE username = :loggedUserName))";

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("loggedUserName", loggedUserName);
            parameters.put("friendUserName", friendUserName);
            dao.executeHqlUpdate(sql, Friendship.class, parameters);
        } catch (Exception e) {
            throw new RemoveFriendException("Error removing friend: " + e.getMessage());
        }
    }

    @Override
    public User searchSuggestedFriend(String loggedUserName, String friendUsername) throws SearchfriendException {

        String hql = "SELECT u FROM User u WHERE u.username = :friendUsername AND u.id <> (SELECT f.id FROM User f WHERE f.username = :loggedUserName)";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("loggedUserName", loggedUserName);
        parameters.put("friendUsername", friendUsername);


        User user = dao.executeHqlQuerySingleResult(hql, User.class, parameters);
        if (user != null) {
            return user;
        } else {
            throw new SearchfriendException(friendUsername + "is does not exist");
        }
    }

    @Override
    public User searchExistingFriend(String loggedUserName, String friendUsername) {
        String hql = "SELECT u " +
                "FROM User u " +
                "WHERE EXISTS (" +
                "    SELECT 1 " +
                "    FROM Friendship f " +
                "    WHERE (" +
                "        (f.user.username = :loggedUserName AND f.friend.username = :friendUsername) " +
                "        OR " +
                "        (f.user.username = :friendUsername AND f.friend.username = :loggedUserName)" +
                "    )" +
                ")";
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("loggedUserName", loggedUserName);
        parameters.put("friendUsername", friendUsername);


        return dao.executeHqlQuerySingleResult(hql, User.class, parameters);


    }


    /**
     * Checks if two users are friends by querying the Friendship records.
     *
     * @return True if the users are friends, false otherwise.
     * @throws AreFriendsException If an error occurs while checking the friendship status.
     */
    @Override
    public boolean isFriends(User user, User friend) throws AreFriendsException {
        try {
            String hql = "SELECT COUNT(f) FROM Friendship f " + "WHERE (f.user.username = :user AND f.friend.username = :friend) " + "OR (f.user.username = :friend AND f.friend.username = :user)";
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("user", user.getUsername());
            parameters.put("friend", friend.getUsername());

            Long count = dao.executeHqlQuerySingleResult(hql, Long.class, parameters);

            return count > 0;
        } catch (Exception e) {
            throw new AreFriendsException("Error checking friendship: " + e.getMessage());
        }
    }

    @Override
    public boolean existsByUsername(String username) {

        return dao.existsByField(User.class, "username", username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return dao.existsByField(User.class, "email", email);
    }

    /**
     * Updates the user profile with the provided information.
     * updatedUser   The user entity containing updated information.
     * loggedinUser  The username of the logged-in user whose profile is being updated.
     *
     * @throws UserDataRetrievalException If an error occurs while retrieving user data.
     */
    @Override
    public void updateUserProfile(User updatedUser, String loggedinUser) throws UserDataRetrievalException {
        // Fetch the existing user entity from the database
//        User existingUser = dao.getById(getUserIdByUsername(loggedinUser));

        User existingUser = service.getByUsername(loggedinUser);
        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setPassword(updatedUser.getPassword());

        existingUser.setFullname(updatedUser.getFullname());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        existingUser.setBio(updatedUser.getBio());
        existingUser.setEmail(updatedUser.getEmail());

        // If a new profile picture is provided, update it
        if (updatedUser.getProfilePicture() != null) {
            existingUser.setProfilePicture(updatedUser.getProfilePicture());
        }
        dao.merge(existingUser);
    }

    @Override
    public void sendMail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        javaMailSender.send(message);

    }


}

