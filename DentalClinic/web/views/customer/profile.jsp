<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Users"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin cá nhân - Dental Clinic</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
        .container { max-width: 500px; margin: 20px auto; background: white; padding: 20px; border: 1px solid #ddd; }
        .form-group { margin-bottom: 15px; }
        label { display: block; margin-bottom: 5px; }
        input, select, textarea { width: 100%; padding: 8px; border: 1px solid #ddd; }
        input[readonly] { background: #f5f5f5; }
        .btn { background: #007bff; color: white; padding: 10px; border: none; cursor: pointer; margin-right: 10px; }
        .btn-danger { background: #dc3545; }
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
        <h1>Thông tin cá nhân</h1>

        <% if (request.getAttribute("success") != null) { %>
            <div class="success"><%= request.getAttribute("success") %></div>
        <% } %>
        <% if (request.getAttribute("error") != null) { %>
            <div class="error"><%= request.getAttribute("error") %></div>
        <% } %>

        <% Users user = (Users) request.getAttribute("user"); %>
        <% if (user != null) { %>
            <form action="user" method="POST">
                <input type="hidden" name="action" value="update">
                
                <div class="form-group">
                    <label>Tên đăng nhập</label>
                    <input type="text" name="username" value="${user.userName}" readonly>
                </div>
                
                <div class="form-group">
                    <label>Email:</label>
                    <input type="email" name="email" value="${user.email}" required>
                </div>
                
                <div class="form-group">
                    <label>Họ và tên:</label>
                    <input type="text" name="fullname" value="${user.fullName}" required>
                </div>
                
                <div class="form-group">
                    <label>Số điện thoại:</label>
                    <input type="tel" name="phone" value="${user.phoneNumber}" required>
                </div>
                
                <div class="form-group">
                    <label>Giới tính:</label>
                    <select name="gender" required>
                        <option value="Nam" ${user.gender.equals("Nam") ? "selected" : ""}>Nam</option>
                        <option value="Nữ" ${user.gender.equals("Nữ") ? "selected" : ""}>Nữ</option>
                    </select>
                </div>
                
                <div class="form-group">
                    <label>Vai trò</label>
                    <input type="text" value="${user.role}" readonly>
                </div>

                <button type="submit" class="btn">Cập nhật</button>
                <a href="/DentalClinic/user?action=logout" class="btn btn-danger">Đăng xuất</a>
            </form>
        <% } else { %>
            <div class="text-center">
                <p>Không tìm thấy thông tin.</p>
                <a href="/DentalClinic/user?action=login">Đăng nhập</a>
            </div>
        <% } %>
    </div>

    <!-- Footer -->
    <jsp:include page="../../common/footer.jsp"></jsp:include>
</body>
</html>
