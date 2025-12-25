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

    </div>

    <!-- Footer -->
    <jsp:include page="../../common/footer.jsp"></jsp:include>

    <script src="/DentalClinic/js/home.js"></script>
</body>
</html>