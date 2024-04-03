<%@ taglib prefix="c" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Login</title>
    <script src="https://code.jquery.com/jquery-3.6.4.min.js"></script>
    <!-- Add this in your HTML file to include SweetAlert -->
    <script src="https://cdn.jsdelivr.net/npm/sweetalert2@10"></script>


    <style>

        body {
            font-family: 'Arial', sans-serif;
            margin: 0;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            height: 100vh;
            background: url("https://static.vecteezy.com/system/resources/previews/007/164/537/original/fingerprint-identity-sensor-data-protection-system-podium-hologram-blue-light-and-concept-free-vector.jpg") center/cover no-repeat;

        }


        .container {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            width: 400px;
            text-align: center;
        }

        label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 16px;
            box-sizing: border-box;
        }

        button {
            background-color: #3498db;
            color: #fff;
            padding: 10px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
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
            color: #fff;
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
    <h2>User Login</h2>
    <form id="loginForm" action="/signin" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required>

        <button type="button" onclick="loginUser()">Login</button>
    </form>
</div>

<script src="<c:url value="/resource/support/js/appjs/login.js"/>"></script>
</body>
</html>


