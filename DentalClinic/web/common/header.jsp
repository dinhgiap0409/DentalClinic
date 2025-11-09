<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header style="background: #333; color: white; padding: 15px;">
    <div style="max-width: 1200px; margin: 0 auto; display: flex; justify-content: space-between; align-items: center;">
        <div>
            <a href="${pageContext.request.contextPath}/home"
               style="color: white; text-decoration: none; font-size: 20px; font-weight: bold;">
                Dental Clinic
            </a>
        </div>

        <nav>
            <a href="${pageContext.request.contextPath}/home"
               style="color: white; text-decoration: none; margin-right: 20px;">Home</a>

            <!-- Nếu user đã đăng nhập -->
            <c:if test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/profile"
                   style="color: white; text-decoration: none; margin-right: 20px;">Profile</a>

                <!-- Nếu user có role = admin -->
                <c:if test="${sessionScope.user.role eq 'admin'}">
                    <a href="${pageContext.request.contextPath}/dashboard"
                       style="color: white; text-decoration: none; margin-right: 20px;">Dashboard</a>
                </c:if>

                <!-- Nếu user có role = Patient -->
                <c:if test="${sessionScope.user.role eq 'Patient'}">
                    <a href="${pageContext.request.contextPath}/medicalHistory"
                       style="color: white; text-decoration: none; margin-right: 20px;">Lịch sử khám</a>
                </c:if>

                <!-- Logout -->
                <a href="${pageContext.request.contextPath}/logout"
                   style="color: white; text-decoration: none;">Logout</a>
            </c:if>

            <!-- Nếu chưa đăng nhập -->
            <c:if test="${empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/login"
                   style="color: white; text-decoration: none; margin-right: 20px;">Login</a>
                <a href="${pageContext.request.contextPath}/register"
                   style="color: white; text-decoration: none;">Register</a>
            </c:if>
        </nav>
    </div>
</header>
