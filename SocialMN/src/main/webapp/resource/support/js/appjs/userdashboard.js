getData();

function getData() {
    var username = sessionStorage.getItem("username");
    var password = sessionStorage.getItem("password");


    var credentialData = {
        username: username, password: password
    };

    $.ajax({
        type: "POST",
        url: "/SocialMN/user/details",
        contentType: "application/json",
        data: JSON.stringify(credentialData),
        success: function (response) {
            // alert("fetching data successfully");
            $("#usernameSpan").text(response.data.username);
            $("#fullNameSpan").text(response.data.fullname);
            $("#dobSpan").text(response.data.dateOfBirth);
            $("#bioSpan").text(response.data.bio);
            $("#emailSpan").text(response.data.email);
            $("#profilePicture").attr("src", response.data.profilePicture);


        },
        error: function (error) {
            console.error("Error fetching data", error);
            alert("Error fetching data" + error);
        }
    });
}

function logout() {
    $.ajax({
        type: "POST", url: "/SocialMN/user/logout",
        success: function () {
            Swal.fire({
                icon: 'success',
                title: 'Log Out Successful!',
                showConfirmButton: false,
                timer: 2000,
                customClass: {
                    popup: 'swal2-popup-custom',
                    title: 'swal2-title-custom',
                },
                background: '#fff',
            });

            sessionStorage.clear();
            localStorage.clear();
            history.pushState(null, null, window.location.href);
            window.onpopstate = function () {
                history.go(-2);
            };


            window.location.href = "/SocialMN/user/login";

        }, error: function (error) {
            console.error("Error during logout", error);
            alert("Error during logout" + error);
        }
    });
}


$(document).ready(function getSuggestedFriendsList() {
    // console.log("Before AJAX call");


    var loggedUserName = sessionStorage.getItem("username");
    $.ajax({
        type: "GET", url: "/SocialMN/user/suggested-friends", headers: {
            'user-name': loggedUserName
        }, // contentType:'application/json',
        success: function (response) {
            // alert("Fetching suggested friends successfully");
            displayFriends(response);
        }, error: function (error) {
            alert("Error fetching suggested friends" + error);
        }
    });
    // console.log("After AJAX call");

});


function displayFriends(friends) {
    var friendList = $("#friendList");
    friendList.empty(); // Clear the existing list


    friends.forEach(function (friend) {
        var listItem = $("<li>").addClass("list-item");
        listItem.text(friend.username);

        var addButton = $("<button>").addClass("add-button");
        addButton.text("Add Friend");

        addButton.click(function () {
            addFriend(friend.username);

        });

        listItem.append(addButton);
        friendList.append(listItem);
    });
}


function addFriend(friendUsername) {
    var loggedUserName = sessionStorage.getItem("username");


    $.ajax({
        type: "POST", url: "/SocialMN/user/add-friend",
        contentType: "application/json", headers: {
            'userName': loggedUserName, 'friendUserName': friendUsername
        }, success: function () {
            showNotification("Friend added successfully");

            // alert("Friend added successfully");
            setTimeout(function () {
                removeFriendFromUI(friendUsername);
            }, 500);
            $("#searchResults").html("");

        },

        error: function (error) {
            alert("Error adding friend" + error);
        }
    });
}

function removeFriendFromUI(friendUsername) {
    // Remove the friend from the UI
    $("li:contains('" + friendUsername + "')").fadeOut('slow', function () {
        $(this).remove();
    });
}

function showNotification(message, type = 'info') {
    if (Notification.permission === 'granted') {
        var options = {
            body: message, // icon: '/path/to/icon.png',
        };
        var notification = new Notification('User Dashboard', options);
    } else if (Notification.permission !== 'denied') {
        Notification.requestPermission().then(function (permission) {
            if (permission === 'granted') {
                showNotification(message, type);
            }
        });
    }
}

var userFriendsListVisible = false;

function loadUserFriends() {
    var loggedUserName = sessionStorage.getItem("username");

    $.ajax({
        type: "GET", url: "/SocialMN/user/user-friends", headers: {
            'user-name': loggedUserName
        }, success: function (response) {
            displayUserFriends(response);
        }, error: function (error) {
            alert("Error fetching user friends: " + error);
        }
    });
    var userFriendsList = $("#userFriendsList");
    if (userFriendsListVisible) {
        userFriendsList.hide();
        userFriendsListVisible = false;
    } else {
        userFriendsList.show();
        userFriendsListVisible = true;
    }
}


function displayUserFriends(userFriends) {

    var username = sessionStorage.getItem("username");

    var userFriendsList = $("#userFriendsList");
    userFriendsList.empty(); // Clear the existing list

    userFriends.forEach(function (friend) {
        var listItem = $("<li>").addClass("list-item");
        listItem.text(friend.username);

        var removeButton = $("<button>").addClass("remove-button").text("Remove");
        removeButton.click(function () {
            removeFriend(friend.username);
        });

        var mutualButton = $("<button>").addClass("mutual-button").text("Mutual Friends");
        mutualButton.click(function () {
            getMutualFriends(friend.username);
        });

        var friendsButton = $("<button>").addClass("view-friendsButton").text("Friends");
        friendsButton.click(function () {
            viewFriends(friend.username);
        });

        var viewProfileButton = $("<button>").addClass("view-profile-button").text("View Profile");
        viewProfileButton.click(function () {
            viewProfile(friend.username);
        });


        listItem.append(removeButton);
        listItem.append(mutualButton);
        listItem.append(friendsButton);
        listItem.append(viewProfileButton);


        $("#userFriendsList").append(listItem);
    });
}

function removeFriend(friendUsername) {
    var loggedUserName = sessionStorage.getItem("username");

    $.ajax({
        type: "DELETE", url: "/SocialMN/user/remove-friend", contentType: "application/json", headers: {
            'loggedUserName': loggedUserName, 'friendUserName': friendUsername
        }, success: function () {
            showNotification("Friend removed successfully");
            setTimeout(function () {
                removeFriendFromUI(friendUsername);
            }, 500);
        }, error: function (error) {
            alert("Error removing friend: " + error);
        }
    });
}


function getMutualFriends(friendUsername) {
    var loggedUserName = sessionStorage.getItem("username");

    $.ajax({
        type: "GET", url: "/SocialMN/user/mutual-friends", headers: {
            'loggedUserName': loggedUserName, 'friendUserName': friendUsername
        }, success: function (response) {
            displayMutualFriends(response);
        }, error: function (error) {
            alert("Error fetching mutual friends: " + error);
        }
    });
}

function displayMutualFriends(mutualFriends) {
    // if (mutualFriends.length > 0) {
    //     var message = "Mutual Friends:\n" + mutualFriends.join("\n");
    //     alert(message);
    // } else {
    //     alert("No mutual friends found.");
    // }
    var mutualFriendsContent = document.getElementById('mutualFriendsContent');

    if (mutualFriends.length > 0) {
        var usernames = mutualFriends.map(function (friend) {
            return `<li>${friend}</li>`;
        });

        var mutualFriendsHTML = `<div><h1>Mutual Friends</h1><ul>${usernames.join('')}</ul></div>`;
        mutualFriendsContent.innerHTML = mutualFriendsHTML;
    } else {
        mutualFriendsContent.innerHTML = "<p>No mutual friends found.</p>";
    }
    openPopup('mutualFriendsPopup');
}

function viewFriends(username) {

    $.ajax({
        type: "GET", url: "/SocialMN/user/user-friends", headers: {
            'user-name': username
        }, success: function (response) {
            view(response);
        }, error: function (error) {
            alert("Error fetching user friends: " + error);
        }
    });
}


function view(response) {

    var friendListContent = document.getElementById('friendListContent');

    if (response.length > 0) {
        var usernames = "";
        for (var i = 0; i < response.length; i++) {
            var friendUsername = response[i].username;
            usernames += `<li>${friendUsername}</li>`;
        }

        var friendListHTML = `<h1>Friends</h1><ul>${usernames}</ul>`;
        friendListContent.innerHTML = friendListHTML;

    } else {
        friendListContent.innerHTML = "<h1>Profile</h1>\n<p>No friends available.</p>";
    }

    openPopup('friendListPopup');

}

function openPopup(popupId) {
    var popup = document.getElementById(popupId);
    popup.style.display = "block";
}

// Function to close a popup
function closePopup(popupId) {
    var popup = document.getElementById(popupId);
    popup.style.display = "none";
}

// Function to display user profile details in a popup
function displayProfile(response) {

    var profileContent = document.getElementById('profileContent');
    profileContent.innerHTML = `
                <h1>Profile</h1>
                <img src="${response.profilePicture}" alt="Profile Picture" style="width: 100px; height: 100px;">
                <p>Username: <span>${response.username}</span></p>
                <p>Full Name: <span>${response.fullname}</span></p>
                <p>Date of Birth: <span>${response.dateOfBirth}</span></p>
                <p>Bio: <span>${response.bio}</span></p>
                <p>Email: <span>${response.email}</span></p>`;

    openPopup('profilePopup');
}


function viewProfile(username) {

    $.ajax({
        type: "GET", url: "/SocialMN/user/view-profile",
        contentType: "application/json",
        headers: {
            'user-name': username
        }, success: function (response) {
            // alert("fetching data successfully");
            displayProfile(response);
        },
        error: function (error) {
            alert("Error fetching user profile: " + error);
        }
    });
}

function fetchExistingUserData() {
    var loggedInUsername = sessionStorage.getItem("username");

    $.ajax({
        type: "GET",
        url: "/SocialMN/user/get-user-data",
        contentType: "application/json",
        data: {username: loggedInUsername},
        success: function (existingUser) {
            // Populate the input fields with existing user data
            $("#username").val(existingUser.username);
            $("#password").val(existingUser.password);
            $("#fullname").val(existingUser.fullname);
            const formattedDate = new Date(existingUser.dateOfBirth).toISOString().split('T')[0];
            $("#dateOfBirth").val(formattedDate);

            // $("#gender").val(existingUser.gender);
            $("#bio").val(existingUser.bio);
            $("#email").val(existingUser.email);

            // const formProps=Object.fromEntries(existingUser.profilePicture);
            // $("#updateProfilePicture").val(formProps);


            if (existingUser.profilePicture) {
                $("#updateProfilePicture").attr("src", "data:image/jpeg;base64," + existingUser.profilePicture);
            }

        },
        error: function (error) {
            console.error("Error fetching existing user data: ", error);
        }
    });

}

$(document).ready(function () {
    // Add the click event listener for the updateProfileButton
    document.getElementById("editProfileButton").addEventListener("click", function () {
        openPopup('updateProfileForm');

        fetchExistingUserData();
    });
});


function updateProfile() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var fullname = document.getElementById('fullname').value;
    var dateOfBirth = document.getElementById('dateOfBirth').value;
    // var gender = document.getElementById('gender').value;
    var bio = document.getElementById('bio').value;
    var email = document.getElementById('email').value;
    var fileInput = document.getElementById('updateProfilePicture');
    var profilePicture = '';

    if (fileInput.files.length > 0) {
        var file = fileInput.files[0];

        // Use FileReader to convert the image to base64
        var reader = new FileReader();
        reader.onloadend = function () {
            // Set the base64 representation of the image in the userData
            profilePicture = reader.result;

            var updatedUser = {
                "username": username,
                "password": password,
                "fullname": fullname,
                "dateOfBirth": dateOfBirth,
                // "gender": gender,
                "profilePicture": profilePicture,
                "bio": bio,
                "email": email
            };

            var loggedInUsername = sessionStorage.getItem("username");

            var passwordRegex = /.*(.*[A-Z].*|\d.*\d|.*[!@#$%^&*()-_+=].*).*/;
            if (!passwordRegex.test(updatedUser.password)) {
                alert("Password must contain one capital, two digit and one symbol");
                return;
            }


            var emailRegex = /^[^\s@]+@gmail\.com$/;
            if (!emailRegex.test(updatedUser.email)) {
                alert("Email should be in a valid format.");
                return;
            }

            $.ajax({
                type: "POST",
                url: "/SocialMN/user/update-profile",
                contentType: "application/json",
                data: JSON.stringify(updatedUser),
                headers: {
                    'loggedUsername': loggedInUsername
                },
                success: function (response) {
                    showNotification("Profile updated successfully", 'success');
                    closePopup('updateProfileForm'); // Close the popup after successful update
                },
                error: function (error) {
                    showNotification("Error updating profile: " + error.responseJSON, 'error');
                }
            });
        };
        reader.readAsDataURL(file);
    } else {
        // Handle the case where no file is selected
        alert("Please select a profile picture.");
    }
}

function searchSuggestedFriend() {
    var loggedUserName = sessionStorage.getItem("username");
    var friendUsername = $("#friendSearchInput").val();

    $.ajax({
        type: "GET",
        url: "/SocialMN/user/search-suggested-friend",
        contentType: "application/json",

        headers: {
            'user-name': loggedUserName
        },
        data: {
            'friend-username': friendUsername
        },
        success: function (response) {
            displayResult(response);
            // removeUserFromResults();
            // setTimeout(removeUserFromResults, 500);
        },
        error: function (error) {
            console.error("Error searching suggested friend", error);
            $("#searchResults").html("User not Found");
        }
    });
}

function searchExistingFriend() {
    var loggedUserName = sessionStorage.getItem("username");
    var friendUsername = $("#friendSearchInput").val();

    $.ajax({
        type: "GET",
        url: "/SocialMN/user/search-existing-friend",
        contentType: "application/json",

        headers: {
            'user-name': loggedUserName
        },
        data: {
            'friend-username': friendUsername
        },
        success: function (response) {
            // TODO print the existing friend
            if (response && response.username) {
                // Display user information in the searchResults2 div
                var displayHtml = `<p>Username: ${response.username}</p>
                                   <button onclick="addFriend('${response.username}')">Add Friend</button>`;

                $("#searchResults2").html(displayHtml);

                // Display user's friends if available
                if (response.friends && response.friends.length > 0) {
                    displaySearchedFriend(response.friends);
                } else {
                    $("#searchResults2").append("<p>User has no friends.</p>");
                }
            } else {
                $("#searchResults2").html("<p>User not found.</p>");
            }


        },
        error: function (error) {
            console.error("Error searching existing friend", error);
            $("#searchResults").html("Error searching existing friend");
        }
    });
}

function displayResult(result) {
    // Customize this function to display the result in your desired way

    if (typeof result === 'object') {
        var user = result;
    } else {
        try {
            // Parse the result as JSON
            var user = JSON.parse(result);
        } catch (error) {
            console.error("Error parsing JSON:", error);
            $("#searchResults").html("<p>Error parsing result</p>");
            return;  // Exit the function if parsing fails
        }
    }

    // Display user information in the searchResults div
    var displayHtml = ` <p>Username: ${user.username}</p>
        <button onclick="addFriend('${user.username}')">Add Friend</button>`;


    $("#searchResults").html(displayHtml);
    // setTimeout(function () {
    //     $("#searchResults").html("");
    // }, 1000);
}

function displaySearchedFriend(friends) {
    // Display user's friends
    var friendsListHtml = "<h2>User's Friends</h2><ul>";
    friends.forEach(function (friend) {
        friendsListHtml += `<li>${friend.username}</li>`;
    });
    friendsListHtml += "</ul>";

    $("#userFriendsList").html(friendsListHtml);
}


