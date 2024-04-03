package org.example.socialMN.service;

import org.example.socialMN.exceptions.*;
import org.example.socialMN.model.User;

import java.util.List;

public interface IService {

    void addUser(User user);

    boolean getValidateUser(String username, String password);

    User getUserData(String username, String password) throws UserDataRetrievalException;

    List<User> getSuggestedFriends(String loggedUserName) throws SuggestedFriendsException;

    void addFriend(User user, User friend) throws AddFriendException;

    User getByUsername(String username) throws UserDataRetrievalException;

    List<User> getUserFriends(String loggedUserName) throws UserFriendsException;

    void removeFriend(String loggedUserName, String friendUserName) throws RemoveFriendException;

    boolean isFriends(User user, User friend) throws AreFriendsException;

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    void updateUserProfile(User updatedUser, String loggedinUser) throws UserDataRetrievalException;

    User searchSuggestedFriend(String loggedUserName, String friendUsername) throws SearchfriendException;

    User searchExistingFriend(String loggedUserName, String friendUsername);

    void sendMail(String to, String subject, String text);


}
