<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Dental Clinic</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
        .container { max-width: 400px; margin: 20px auto; background: white; padding: 20px; border: 1px solid #ddd; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input, select, textarea { width: 100%; padding: 8px; border: 1px solid #ddd; }
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
        <h2 style="text-align: center;">Đăng ký</h2>

        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <form action="user" method="POST">
            <input type="hidden" name="action" value="register">
            
            <div class="form-group">
                <label>Tên đăng nhập:</label>
                <input type="text" name="username" required>
            </div>
            
            <div class="form-group">
                <label>Email:</label>
                <input type="email" name="email" required>
            </div>
            
            <div class="form-group">
                <label>Mật khẩu:</label>
                <input type="password" name="password" required>
            </div>
            
            <div class="form-group">
                <label>Họ và tên:</label>
                <input type="text" name="fullname" required>
            </div>
            
            <div class="form-group">
                <label>Số điện thoại:</label>
                <input type="tel" name="phone" required>
            </div>
            
            <div class="form-group">
                <label>Giới tính:</label>
                <select name="gender" required>
                    <option value="">Chọn giới tính</option>
                    <option value="Nam">Nam</option>
                    <option value="Nữ">Nữ</option>
                </select>
            </div>

            <button type="submit" class="btn">Đăng ký</button>
        </form>

        <div class="text-center" style="margin-top: 15px;">
            <p>Đã có tài khoản? <a href="/DentalClinic/user?action=login">Đăng nhập</a></p>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="../../common/footer.jsp"></jsp:include>
</body>
</html>
