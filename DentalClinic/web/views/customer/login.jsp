<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Dental Clinic</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
        .container { max-width: 300px; margin: 50px auto; background: white; padding: 20px; border: 1px solid #ddd; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input { width: 100%; padding: 8px; border: 1px solid #ddd; }
        .btn { background: #007bff; color: white; padding: 10px; border: none; cursor: pointer; width: 100%; }
        .error { background: #f8d7da; color: #721c24; padding: 10px; margin-bottom: 15px; }
        .success { background: #d4edda; color: #155724; padding: 10px; margin-bottom: 15px; }
        .text-center { text-align: center; }
        a { color: #007bff; text-decoration: none; }
    </style>
</head>
<body>
    <!-- Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>

    <div class="container">
        <h2 style="text-align: center;">Đăng nhập</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="user" method="POST">
            <input type="hidden" name="action" value="login">
            
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label>Mật khẩu:</label>
                <input type="password" name="password" required>
            </div>

            <button type="submit" class="btn">Đăng nhập</button>
        </form>

        <div class="text-center" style="margin-top: 15px;">
            <p>Chưa có tài khoản? <a href="/DentalClinic/user?action=register">Đăng ký</a></p>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="../../common/footer.jsp"></jsp:include>
</body>
</html>
