package org.example.socialMN.controller;


import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.example.socialMN.dto.FriendDTO;
import org.example.socialMN.dto.UserDTO;
import org.example.socialMN.exceptions.*;
import org.example.socialMN.handler.UserHandler;
import org.example.socialMN.model.User;
import org.example.socialMN.service.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final Logger logger = LogManager.getLogger(IService.class);

    @Autowired
    private UserHandler userHandler;


    /**
     * Handles a POST request to retrieve user data based on provided credentials.
     * userCredentials The user credentials sent in the request body.
     */
    @PostMapping(value = "/details", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUserData(@RequestBody User userCredentials) throws UserDataRetrievalException {
        
        return userHandler.handleUserDataRequest(userCredentials);

    }

    /**
     * Handles a GET request to retrieve suggested friends for a user.
     * loggedUserName The username of the logged-in user (provided in the request header).
     * return ResponseEntity containing a list of suggested friends for the logged-in user.
     */
    @GetMapping(value = "/suggested-friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FriendDTO>> getFriendsSuggestions(@RequestHeader(value = "user-name") String loggedUserName)
            throws SuggestedFriendsException {

        List<FriendDTO> userList = userHandler.handleSuggestedFriends(loggedUserName);
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    /**
     * Handles a POST request to add a friend for the specified user.
     * userName        The username of the user initiating the friend request (provided in the request header).
     * friendUserName  The username of the friend to be added (provided in the request header).
     * return ResponseEntity containing the result that added or not
     */

    @PostMapping(value = "/add-friend", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addFriends(@RequestHeader String userName, @RequestHeader String friendUserName)
            throws AddFriendException, UserDataRetrievalException, AreFriendsException {
        logger.info("Received add friend request - User: " + userName + ", Friend:" + friendUserName);


        userHandler.handleAddFriend(userName, friendUserName);
        return ResponseEntity.ok("Added Successfully");
    }

    /**
     * Retrieves the friends of a user based on the provided username.
     * loggedUserName The username of the logged-in user.
     * ResponseEntity containing a list of UserDTO representing the user's friends.
     */
    @GetMapping(value = "/user-friends", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getUserFriends(@RequestHeader(value = "user-name")
                                                        String loggedUserName)
            throws UserFriendsException {
        logger.info("Fetching friends for user: " + loggedUserName);

        List<UserDTO> userFriends = userHandler.handleGetUserFriends(loggedUserName);
        logger.info("Retrieved  friends for user: " + loggedUserName);

        return new ResponseEntity<>(userFriends, HttpStatus.OK);
    }

    /**
     * Handles a DELETE request to remove a friend for the specified user.
     * userName        The username of the user initiating the friend removal (provided in the request header).
     * friendUserName  The username of the friend to be removed (provided in the request header).
     * return ResponseEntity containing the result that friend removed or not.
     */
    @DeleteMapping("/remove-friend")
    public ResponseEntity<String> removeFriend(@RequestHeader String loggedUserName, @RequestHeader String friendUserName)
            throws RemoveFriendException {

        logger.info("Received remove friend request - User: " + loggedUserName + ", Friend: " + friendUserName);
        userHandler.handleRemoveFriend(loggedUserName, friendUserName);
        return ResponseEntity.ok("Friend removed successfully");

    }

    /**
     * Retrieves the mutual friends between a user and their friend.
     * loggedUserName The username of the logged-in user.
     * friendUserName The username of the friend.
     * ResponseEntity containing a list of usernames representing mutual friends.
     *
     * @throws MutualFriendsException If an error occurs while fetching mutual friends.
     */
    @GetMapping("/mutual-friends")
    public ResponseEntity<List<String>> getMutualFriends(@RequestHeader String loggedUserName, @RequestHeader String friendUserName) throws MutualFriendsException {
        logger.info("Received request for mutual friends - User: " + loggedUserName + ", Friend: " + friendUserName);
        List<String> mutualFriends = userHandler.getMutualFriends(loggedUserName, friendUserName);
        logger.info("Retrieved mutual friends for User: " + loggedUserName + ", Friend: " + friendUserName);

        return ResponseEntity.ok(mutualFriends);
    }


    /**
     * Checks if two users are friends.
     * <p>
     * loggedUserName The username of the logged-in user.
     * friendUserName The username of the friend to check.
     * ResponseEntity containing a Boolean indicating whether the users are friends or not.
     *
     * @throws AreFriendsException        If an error occurs during the process of checking friendship status.
     * @throws UserDataRetrievalException If an error occurs while retrieving user data.
     */
    @GetMapping("/are-friends")
    public ResponseEntity<Boolean> areFriends(@RequestParam("user1") String loggedUserName, @RequestParam("user2") String friendUserName)
            throws AreFriendsException, UserDataRetrievalException {
        logger.info("Received request to check friendship status between User1: " + loggedUserName +
                " and User2: " + friendUserName);

        boolean areFriends = userHandler.areFriends(loggedUserName, friendUserName);

        return ResponseEntity.ok(areFriends);
    }

    /**
     * Retrieves the profile information of a user based on the provided username.
     * username The username of the user whose profile is to be viewed.
     * ResponseEntity containing a UserDTO representing the user's profile.
     *
     * @throws UserDataRetrievalException If an error occurs while retrieving user data.
     */
    @GetMapping("/view-profile")
    public ResponseEntity<UserDTO> viewProfile(@RequestHeader(value = "user-name") String username)
            throws UserDataRetrievalException {
        logger.info("Received request to view profile for user: " + username);

        User user = userHandler.getByUsername(username);
        UserDTO userDTO = userHandler.mapUserToDTO(user);
        logger.info("Retrieved profile for user: " + username);

        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }


    /**
     * Updates the profile of a user.
     * <p>
     * updatedUser   The updated user profile data received in the request body.
     * loggedinUser  The username of the logged-in user initiating the update.
     * ResponseEntity indicating the success of the profile update.
     *
     * @throws UserDataRetrievalException If an error occurs while retrieving user data.
     */
    @PostMapping("/update-profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedUser, @RequestHeader("loggedUsername") String loggedinUser) throws UserDataRetrievalException {
        logger.info("Received request to update profile for user: " + loggedinUser);

        userHandler.handleUpdateUserProfile(updatedUser, loggedinUser);
        logger.info("Profile updated successfully for user: " + loggedinUser);

        return ResponseEntity.ok("Profile updated successfully!");
    }

    /**
     * Retrieves user data based on the provided username.
     * <p>
     * username The username for which user data needs to be retrieved.
     * ResponseEntity containing the user data if found.
     *
     * @throws UserDataRetrievalException If an error occurs while retrieving user data.
     */
    @GetMapping("/get-user-data")
    public ResponseEntity<User> getUserData(@RequestParam("username") String username)
            throws UserDataRetrievalException {
        logger.info("Received request to retrieve user data for username: " + username);

        User existingUser = userHandler.getByUsername(username);
        logger.info("User data retrieved successfully for username: " + username);
        return ResponseEntity.ok(existingUser);
    }

//    @GetMapping("/error")
//    public String exceptionHandle() {
//        return "error";
//    }


    /**
     * @param loggedUserName The username of the logged-in user.
     * @param friendUsername The username of the friend to search for.
     * @return ResponseEntity with the found User as the response body.
     * @throws SearchfriendException If an error occurs during the friend search operation.
     */
    @GetMapping("/search-suggested-friend")
    public ResponseEntity<User> searchSuggestedFriend(
            @RequestHeader(value = "user-name") String loggedUserName,
            @RequestParam("friend-username") String friendUsername
    ) throws SearchfriendException {

        User friend = userHandler.handleSearchFriend(loggedUserName, friendUsername);
        return ResponseEntity.ok(friend);
    }

    /**
     * @param loggedUserName The username of the logged-in user.
     * @param friendUsername The username of the friend to search for.
     * @return ResponseEntity with the found User as the response body.
     * @throws SearchfriendException If an error occurs during the friend search operation.
     */
    @GetMapping("/search-existing-friend")
    public ResponseEntity<User> searchExistingFriend(
            @RequestHeader(value = "user-name") String loggedUserName,
            @RequestParam("friend-username") String friendUsername
    ) throws SearchfriendException {
        logger.info("Received request to search existing friend for user " + loggedUserName + "with friend username: " + friendUsername);

        User friend = userHandler.handleSearchExistingFriend(loggedUserName, friendUsername);
        return new ResponseEntity<>(friend, HttpStatus.OK);
    }
}



