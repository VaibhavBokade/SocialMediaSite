function loginUser() {
    var username = document.getElementById('username').value;
    var password = document.getElementById('password').value;

    sessionStorage.setItem("username", username);
    sessionStorage.setItem("password", password);


    var userData = {
        "username": username,
        "password": password
    };

    $.ajax({
        type: "POST",
        url: "/SocialMN/user/login",
        contentType: "application/json",
        data: JSON.stringify(userData),
        success: function () {
            // alert("Login successfully!");
            Swal.fire({
                icon: 'success',
                title: 'Login Successful!',
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
            }, 800);

            // window.location.href = "/SocialMN/user/userdashboard";
        },
        error: function () {
            // alert("Login failed. Please check your username and password.");
            Swal.fire({
                icon: 'error',
                title: 'Login Failed',
                text: 'Please check your username and password.',
                showConfirmButton: true,
                customClass: {
                    popup: 'swal2-popup-custom',
                    title: 'swal2-title-custom',
                    content: 'swal2-content-custom',
                },
                background: '#fff',
            });
        }
    });
}