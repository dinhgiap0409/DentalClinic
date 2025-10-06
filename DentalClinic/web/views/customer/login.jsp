<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Dental Clinic</title>
</head>
<body>
    <!-- Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>

    <div>
        <h2 style="color: #0056b3; 
            font-size: 40px;
            text-align: center">LOGIN</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div style="color: red; text-align: center; margin-bottom: 15px;">
                <%= request.getAttribute("error") %>
            </div>
        <% } %>

        <form action="login" method="POST">
            <table>
                <tbody>
                    <tr>
                        <td><label>Email:</label></td>
                        <td><input type="email" name="email" required></td>
                    </tr>
                    <tr>
                        <td><label>PassWord:</label></td>
                        <td><input type="password" name="password" required></td>
                    </tr>
                    <tr>
                        <td><button type="submit" class="btn">Login</button></td>
                    </tr>
                </tbody>
            </table>
        </form>
        <div class="text-center" style="margin-top: 15px;">
            <p>Do you have an account? <a href="/DentalClinic/user?action=register">Resigter</a></p>
        </div>
    </div>
</body>
</html>
