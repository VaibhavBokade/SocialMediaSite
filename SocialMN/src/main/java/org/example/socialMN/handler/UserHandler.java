package org.example.socialMN.handler;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.example.socialMN.dto.FriendDTO;
import org.example.socialMN.dto.UserDTO;
import org.example.socialMN.exceptions.*;
import org.example.socialMN.model.Friendship;
import org.example.socialMN.model.User;
import org.example.socialMN.service.IService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class UserHandler {
    private static final Logger logger = LogManager.getLogger(IService.class);

    @Autowired
    private IService iService;


    /**
     * Handles the request to retrieve user data based on provided credentials.
     * userCredentials The user credentials containing username and password.
     * ResponseEntity with user data or an error message.
     *
     * @throws UserDataRetrievalException If an error occurs while retrieving user data.
     */
    public ResponseEntity<?> handleUserDataRequest(User userCredentials) throws UserDataRetrievalException {
        try {
            Map<String, Object> result = new HashMap<>();
            String username = userCredentials.getUsername();
            String password = userCredentials.getPassword();
            logger.info("Received request for user credentials - ," + username + "," + password);
            User user = iService.getUserData(username, password);
            UserDTO userDTO = mapUserToDTO(user);
            result.put("data", userDTO);

            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (UserDataRetrievalException e) {

            logger.error("Error retrieving user data", e);
            throw new UserDataRetrievalException("Error retrieving user data");
        }
    }

    public UserDTO mapUserToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        if (user.getDateOfBirth() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String formattedDateOfBirth = dateFormat.format(user.getDateOfBirth());
            userDTO.setDateOfBirth(formattedDateOfBirth);
        }
        logger.debug("Mapped user data to DTO successfully");
        return userDTO;
    }

    public FriendDTO mapToFriendDTO(User user) {
        FriendDTO friendDTO = new FriendDTO();
        BeanUtils.copyProperties(user, friendDTO);
        return friendDTO;
    }

    /**
     * Retrieves a list of suggested friends for the given user.
     * loggedUserName The username of the logged-in user.
     * List of FriendDTO representing suggested friends.
     *
     * @throws SuggestedFriendsException If an error occurs while fetching suggested friends.
     */
    public List<FriendDTO> handleSuggestedFriends(String loggedUserName) throws SuggestedFriendsException {
        logger.info("Received request to get suggested friends for user: " + loggedUserName);
        if (iService.existsByUsername(loggedUserName)) {

            try {

                List<User> allUsers = iService.getSuggestedFriends(loggedUserName);
                List<FriendDTO> list = new ArrayList<>();
                for (User user : allUsers) {
                    FriendDTO friendDTO = mapToFriendDTO(user);
                    list.add(friendDTO);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                throw new SuggestedFriendsException("error for listing the suggested friend list");
            }
        } else {
            throw new SuggestedFriendsException("Error occurred to fetching suggested friends");
        }
    }


    public List<UserDTO> handleGetUserFriends(String loggedUserName) throws UserFriendsException {
        if (iService.existsByUsername(loggedUserName)) {
            try {
                List<User> userFriends = iService.getUserFriends(loggedUserName);
                List<UserDTO> list = new ArrayList<>();
                for (User user : userFriends) {
                    UserDTO userDTO = mapUserToDTO(user);
                    list.add(userDTO);
                }
                return list;
            } catch (Exception e) {
                e.printStackTrace();
                throw new UserFriendsException("Error retrieving user friends: " + e.getMessage());
            }
        } else {
            throw new UserFriendsException("User does not exist");
        }
    }

    /**
     * Handles the removal of a friend relationship between two users.
     * loggedUserName The username of the logged-in user initiating the removal.
     * friendUserName The username of the friend to be removed.
     *
     * @throws RemoveFriendException If an error occurs during the friend removal process.
     */
    public void handleRemoveFriend(String loggedUserName, String friendUserName) throws RemoveFriendException {
        if (iService.existsByUsername(loggedUserName) && iService.existsByUsername(friendUserName)) {
            iService.removeFriend(loggedUserName, friendUserName);
        } else {
            throw new RemoveFriendException("logged in username or friend username does not exist");
        }
    }


    public void handleAddFriend(String userName, String friendUserName) throws AddFriendException, UserDataRetrievalException {
        logger.info("Received request to add friend - User: {}, Friend: {}" + userName + "," + friendUserName);
        try {
            User user = iService.getByUsername(userName);
            User friend = iService.getByUsername(friendUserName);
            if (user != null && friend != null) {
                iService.addFriend(user, friend);
                iService.addFriend(friend, user);
                ResponseEntity.ok("Added Successfully");
            } else {
                throw new AddFriendException("User or friend not found");
            }
        } catch (AddFriendException e) {
            e.printStackTrace();
            throw new AddFriendException("error occurred for adding friends");
        }
    }

    /**
     * Retrieves the list of mutual friends between two users.
     *
     * @throws MutualFriendsException If an error occurs during the retrieval of mutual friends.
     */
    public List<String> getMutualFriends(String loggedUserName, String friendUserName) throws MutualFriendsException {
        if (iService.existsByUsername(loggedUserName) && iService.existsByUsername(friendUserName)) {
            try {
                List<String> loggedUserFriends = getUserFriends(loggedUserName);

                List<String> friendsOfFriend = getUserFriends(friendUserName);
                loggedUserFriends.retainAll(friendsOfFriend);
                return loggedUserFriends;
            } catch (Exception e) {
                throw new MutualFriendsException("Error retrieving mutual friends: " + e.getMessage());
            }
        } else {
            throw new MutualFriendsException("logged in user or friend user does not exist");
        }
    }

    /**
     * Retrieves the list of friends for a given user.
     *
     * @throws UserDataRetrievalException If an error occurs during the retrieval of user friends.
     */
    public List<String> getUserFriends(String username) throws UserDataRetrievalException {
        User user = iService.getByUsername(username);
        if (user != null) {
            List<Friendship> friendships = user.getFriendList();
            List<String> list = new ArrayList<>();
            //u can apply java 8 feature here
            for (Friendship friendship : friendships) {
                String s = friendship.getFriend().getUsername();
                list.add(s);
            }
            return list;
        }
        return Collections.emptyList();
    }

    /**
     * Retrieves the list of friends for a given user.
     *
     * @throws UserDataRetrievalException If an error occurs during the retrieval of user friends.
     */
    public boolean areFriends(String loggedUserName, String username) throws AreFriendsException, UserDataRetrievalException {
        if (iService.existsByUsername(loggedUserName) && iService.existsByUsername(username)) {
            try {
                User user1 = iService.getByUsername(loggedUserName);
                User user2 = iService.getByUsername(username);
                if (user1 != null && user2 != null) {
                    return iService.isFriends(user1, user2);
                }
                return false;
            } catch (AreFriendsException e) {
                e.printStackTrace();
                throw new AreFriendsException("AreFriendship exception occurred");
            }
        } else {
            throw new AreFriendsException("logged in user or other checking(for friendship relation) does not exist");
        }
    }

    /**
     * Retrieves a user by their username.
     *
     * @throws UserDataRetrievalException If the user does not exist.
     */
    public User getByUsername(String username) throws UserDataRetrievalException {
        if (iService.existsByUsername(username)) {
            return iService.getByUsername(username);
        } else {
            throw new UserDataRetrievalException("user does not exist");
        }
    }

    /**
     * Handles the update of a user's profile information.
     *
     * @throws UserDataRetrievalException If an error occurs during the retrieval of user data.
     */
    public void handleUpdateUserProfile(User updatedUser, String loggedinUser) throws UserDataRetrievalException {
        iService.updateUserProfile(updatedUser, loggedinUser);
    }


    public User handleSearchFriend(String loggedUserName, String friendUsername) throws SearchfriendException {
        return iService.searchSuggestedFriend(loggedUserName, friendUsername);
    }

    public User handleSearchExistingFriend(String loggedUserName, String friendUsername) throws SearchfriendException {
        return iService.searchExistingFriend(loggedUserName, friendUsername);

    }
}