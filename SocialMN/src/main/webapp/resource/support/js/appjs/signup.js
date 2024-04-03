function signupUser() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;
    var fullname = document.getElementById('fullname').value;
    var dateOfBirth = document.getElementById('dateOfBirth').value;
    var gender = document.getElementById('gender').value;
    // var profilePicture = document.getElementById('profilePicture').value;
    // var profilePicture = document.getElementById("profilePicture").src = base64Image;
    var bio = document.getElementById('bio').value;
    var email = document.getElementById('email').value;


    var fileInput = document.getElementById('profilePicture');
    var profilePicture = '';

    if (fileInput.files.length > 0) {
        var file = fileInput.files[0];

        // Use FileReader to convert the image to base64
        var reader = new FileReader();
        reader.onloadend = function () {
            // Set the base64 representation of the image in the userData
            profilePicture = reader.result;

            // Now you can proceed with the rest of your code
            var userData = {
                "username": username,
                "password": password,
                "fullname": fullname,
                "dateOfBirth": dateOfBirth,
                "gender": gender,
                "profilePicture": profilePicture,
                "bio": bio,
                "email": email
            };
            var passwordRegex = /.*(.*[A-Z].*|\d.*\d|.*[!@#$%^&*()-_+=].*).*/;
            if (!passwordRegex.test(userData.password)) {
                alert("Password must contain one capital, two digit and one symbol");
                return;
            }

            var emailRegex = /^[^\s@]+@gmail\.com$/;
            if (!emailRegex.test(userData.email)) {
                alert("Email should be in a valid format.");
                return;
            }

            $.ajax({
                type: "POST",
                url: "/SocialMN/user/signup",
                contentType: "application/json",
                data: JSON.stringify(userData),
                success: function () {
                    // alert("Sign Up successfully..!");

                    Swal.fire({
                        icon: 'success',
                        title: 'Sign Up Successfully',
                        showConfirmButton: false,
                        timer: 2000,
                        customClass: {
                            popup: 'swal2-popup-custom',
                            title: 'swal2-title-custom',
                        },
                        background: '#fff',
                    });

                    setTimeout(function () {
                        window.location.href = "/SocialMN/user/userdashboard";
                    }, 10000);
                    window.location.href = "/SocialMN/user/login";
                },
                error: function () {
                    alert("sign up failed ");
                }
            });
        };
        reader.readAsDataURL(file);
    } else {
        // Handle the case where no file is selected
        alert("Please select a profile picture.");
    }
}

function checkUsernameExistence() {
    var username = document.getElementById('username').value;


    //  API to check username existence
    $.ajax({
        type: "GET",
        url: "/SocialMN/user/check-username",
        headers: {
            "username": username
        },
        success: function (exists) {
            if (exists) {
                alert("Username already exists. Please choose a different one.");
            }
        },
        error: function () {
            alert("Failed to check username existence");

        }
    });
}

function checkEmailExistence() {
    var email = document.getElementById('email').value;

    // Call API to check email existence
    $.ajax({
        type: "GET",
        url: "/SocialMN/user/check-email",
        headers: {
            "email": email
        },
        success: function (exists) {
            if (exists) {
                alert("Email already exists. Please use a different email.");
            }
        },
        error: function () {
            alert("Failed to check email existence");

        }
    });
}


