<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Service"%>
<%@page import="model.Users"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dental Clinic - Home</title>
    <link rel="stylesheet" href="/DentalClinic/css/home.css">
</head>
<body>
    <!-- Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>

    <div class="container">
        <!-- Hero Section -->
        <div class="hero">
            <h1>Dental Clinic</h1>
            <p>Dịch vụ nha khoa chuyên nghiệp</p>
            <%
                Users currentUser = (Users) session.getAttribute("user");
                if (currentUser != null) {
            %>
                <p class="welcome-message">Chào mừng, <strong><%= currentUser.getFullName() %></strong>!</p>
                <a href="/DentalClinic/service" class="btn">Xem dịch vụ</a>
                <% if ("admin".equals(currentUser.getRole())) { %>
                    <a href="/DentalClinic/dashboard" class="btn">Quản lý</a>
                <% } %>
            <%
                } else {
            %>
                <a href="/DentalClinic/login" class="btn">Đăng nhập</a>
                <a href="/DentalClinic/register" class="btn">Đăng ký</a>
            <%
                }
            %>
        </div>

        <!-- Services Section -->
        <h2>Dental Services</h2>
        
        <!-- Services Component -->
        <jsp:include page="services-section.jsp"></jsp:include>

        <!-- Pagination Component -->
        <jsp:include page="pagination-section.jsp"></jsp:include>

        <!-- Doctors Section -->
        <h2>Bác sĩ</h2>
        <div class="services">
            <div class="service">
                <img src="/DentalClinic/img/dept-5.jpg" alt="Bác sĩ Nguyễn Văn A" style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                <h3>Bác sĩ Nguyễn Văn A</h3>
                <p>Chuyên khoa: Niềng răng</p>
                <p>Kinh nghiệm: 10 năm</p>
            </div>
            <div class="service">
                <img src="/DentalClinic/img/dept-6.jpg" alt="Bác sĩ Trần Thị B" style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                <h3>Bác sĩ Trần Thị B</h3>
                <p>Chuyên khoa: Phẫu thuật răng</p>
                <p>Kinh nghiệm: 8 năm</p>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="../../common/footer.jsp"></jsp:include>

    <script src="/DentalClinic/js/home.js"></script>
</body>
</html>