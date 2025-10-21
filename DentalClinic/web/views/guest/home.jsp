<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Service"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
                Users user = (Users) session.getAttribute("user");
                if (user != null) {
            %>
                <p>Chào mừng, <strong><%= user.getFullName() %></strong>!</p>
                <a href="/DentalClinic/service" class="btn">Xem dịch vụ</a>
                <% if ("admin".equals(user.getRole())) { %>
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
        <div class="services"> <%-- Giữ lại class 'services' để tái sử dụng CSS --%>
            <c:forEach var="doctor" items="${doctors}">
                <div class="service"> <%-- Giữ lại class 'service' --%>
                    <%-- Sử dụng ảnh placeholder hoặc ảnh từ DB nếu có --%>
                    <img src="${pageContext.request.contextPath}/img/doctor_placeholder.png" 
                         alt="Bác sĩ ${doctor.userId.fullName}" 
                         style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                    
                    <h3>${doctor.userId.fullName}</h3>
                    <p>Chuyên khoa: ${doctor.specialization}</p>
                    <p>Kinh nghiệm: ${doctor.yearsOfExperience} năm</p>
                    <%-- Thêm nút xem lịch nếu cần --%>
                    <a href="${pageContext.request.contextPath}/doctorWorkSchedule?doctorId=${doctor.doctorID}" class="btn-book">Xem lịch</a>
                </div>
            </c:forEach>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="../../common/footer.jsp"></jsp:include>

    <script src="/DentalClinic/js/home.js"></script>
</body>
</html>