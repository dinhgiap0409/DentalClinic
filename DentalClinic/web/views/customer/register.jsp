<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Resigter - Dental Clinic</title>
    </head>
    <body>
        <!-- Header -->
        <jsp:include page="../../common/header.jsp"></jsp:include>

        <div>
            <h2 style="color: #0056b3; font-size: 40px; text-align: center"">Resigter</h2>
            
            <% if (request.getAttribute("error") != null) { %>
                <div style="color: red; text-align: center; margin-bottom: 15px;">
                    <%= request.getAttribute("error") %>
                </div>
            <% } %>
            
            <% if (request.getAttribute("success") != null) { %>
                <div style="color: green; text-align: center; margin-bottom: 15px;">
                    <%= request.getAttribute("success") %>
                </div>
            <% } %>

            <form action="register" method="POST">
                <input type="hidden" name="action" value="register">
                <table>
                    <tbody>
                        <tr>
                            <td><label>UserName</label></td>
                            <td><input type="text" name="username" required></td>
                        </tr>
                        <tr>
                            <td><label>Email:</label></td>
                            <td><input type="email" name="email" required></td>
                        </tr>
                        <tr>
                            <td><label>PassWord</label></td>
                            <td><input type="password" name="password" required></td>
                        </tr>
                        <tr>
                            <td><label>Full Name:</label></td>
                            <td><input type="text" name="fullname" required></td>
                        </tr>
                        <tr>
                            <td><label>Phone Number:</label></td>
                            <td><input type="tel" name="phone" required></td>
                        </tr>
                        <tr>
                            <td><label>Gender:</label></td>
                            <td><select name="gender" required>
                                    <option value="">Choose gender</option>
                                    <option value="Nam">Male</option>
                                    <option value="Nữ">Female</option>
                                </select></td>
                        </tr>
                        <tr>
                            <td><button type="submit" class="btn">Register</button></td>
                        </tr>
                    </tbody>
                </table>
            </form>

            <div style="margin-top: 15px;">
                <p>Already have an account ?<a href="/DentalClinic/login">Login</a></p>
            </div>
        </div>

    </body>
</html>
