package org.example.socialMN.dto;

import org.example.socialMN.model.User;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class UserDTO {


    private String username;

    private String password;

    private String fullname;

    private String dateOfBirth;

    private String gender;
    private Set<FriendDTO> friends = new HashSet<>();//take FriendDTO here

    private String profilePicture;
    private String bio;
    private String email;

    public UserDTO() {
    }

    public UserDTO(String username,  String fullname, Date dateOfBirth, String gender, Set<FriendDTO> friends, String profilePicture, String bio, String email) {
        this.username = username;
        this.fullname = fullname;
        this.dateOfBirth = String.valueOf(dateOfBirth);
        this.gender = gender;
        this.friends = friends;
        this.profilePicture = profilePicture;
        this.bio = bio;
        this.email = email;
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", fullname='" + fullname + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", gender='" + gender + '\'' +
                ", friends=" + friends +
                ", profilePicture='" + profilePicture + '\'' +
                ", bio='" + bio + '\'' +
                ", email='" + email + '\'' +
                '}';
    }



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Set<FriendDTO> getFriends() {
        return friends;
    }

    public void setFriends(Set<FriendDTO> friends) {
        this.friends = friends;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
