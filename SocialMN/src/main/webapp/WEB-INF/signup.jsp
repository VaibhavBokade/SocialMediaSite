<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Registration</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>

    <style>
        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background: linear-gradient(rgba(0, 0, 0, 0.7), rgba(0, 0, 0, 0.7)), url("https://cdn.pixabay.com/photo/2017/03/19/03/40/avatar-2155431_1280.png") center/cover no-repeat;
            background-blend-mode: overlay;
            color: #fff;
        }

        .container {
            text-align: center;
            max-width: 800px;
            margin: 50px auto; /* Increased margin for better spacing */
            padding: 40px; /* Increased padding for a larger container */
            border: 1px solid #ccc;
            border-radius: 8px;
            background-color: rgba(255, 255, 255, 0.9);
            opacity: 0;
            animation: fadeIn 1s forwards;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }

        h2 {
            color: #333;
        }

        label {
            display: block;
            margin-bottom: 8px;
            color: #333;
        }

        input, select, textarea {
            width: 100%;
            padding: 10px;
            margin-bottom: 16px;
            box-sizing: border-box;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            background-color: #4caf50;
            color: #fff;
            padding: 12px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            transition: background-color 0.3s ease-in-out;
        }

        button:hover {
            background-color: #45a049;
        }

        #top-left-container {
            position: absolute;
            top: 0;
            left: 0;
            padding: 10px;
            display: flex;
            align-items: center;
        }

        #logo {
            width: 40px; /* Adjust the width of the logo as needed */
            margin-right: 10px;
        }

        #home-link {
            text-decoration: none;
            color: #333;
            font-weight: bold;
            font-size: 18px;
        }

        #home-link:hover {
            color: #45a049;
        }


    </style>
</head>
<body>

<div id="top-left-container">
    <img id="logo"
         src="https://png.pngtree.com/png-vector/20190223/ourmid/pngtree-vector-house-icon-png-image_695369.jpg"
         alt="Logo">
    <a id="home-link" href="<c:url value="/user/index"/>">Home</a>
</div>
<div class="container">
    <h2>User SignUp</h2>
    <form action="/signup" id="signupform" method="post" enctype="multipart/form-data">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" placeholder="username" required
               onchange="checkUsernameExistence()" class="username-input">

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" placeholder="Abc%1234 length must be > 8" required>

        <label for="fullname">Full Name:</label>
        <input type="text" id="fullname" name="fullname" placeholder="full Name" required autocomplete="on">

        <label for="dateOfBirth">Date of Birth:</label>
        <input type="date" id="dateOfBirth" name="dateOfBirth" required>

        <label for="gender">Gender:</label>
        <select id="gender" name="gender" required>
            <option value="male">Male</option>
            <option value="female">Female</option>
        </select>

        <%--        <label for="profilePicture">Profile Picture:</label>--%>
        <%--        <input type="text" id="profilePicture" name="profilePicture">--%>

        <label for="profilePicture">Profile Picture:</label>
        <input type="file" id="profilePicture" name="profilePicture" accept="image/*">

        <label for="bio">Bio:</label>
        <textarea id="bio" name="bio" rows="4"></textarea>

        <label for="email">Email:</label>
        <input type="text" id="email" name="email" placeholder="abc@gmail.com" required
               onchange="checkEmailExistence()">


        <button id="submitdata" type="button" onclick="signupUser()">Register</button>
    </form>
</div>
<script src="<c:url value="/resource/support/js/appjs/signup.js"/>"></script>
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>


</body>
</html>
