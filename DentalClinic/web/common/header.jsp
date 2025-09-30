<%-- 
    Document   : header
    Created on : Sep 16, 2025, 11:49:47 PM
    Author     : Nguyen Dinh Giap
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Users"%>
<header style="background: #333; color: white; padding: 15px;">
    <div style="max-width: 1200px; margin: 0 auto; display: flex; justify-content: space-between; align-items: center;">
        <div>
            <a href="/DentalClinic/views/guest/home.jsp" style="color: white; text-decoration: none; font-size: 20px; font-weight: bold;">Dental Clinic</a>
        </div>
        <nav>
            <a href="/DentalClinic/views/guest/home.jsp" style="color: white; text-decoration: none; margin-right: 20px;">Home</a>
            <a href="/DentalClinic/service?action=list" style="color: white; text-decoration: none; margin-right: 20px;">Services</a>
            <%
                Users user = (Users) session.getAttribute("user");
                if (user != null) {
            %>
                <a href="/DentalClinic/user?action=profile" style="color: white; text-decoration: none; margin-right: 20px;">Profile</a>
                <% if ("admin".equals(user.getRole())) { %>
                    <a href="/DentalClinic/views/dashboard/dashboard.jsp" style="color: white; text-decoration: none; margin-right: 20px;">Dashboard</a>
                <% } %>
                <a href="/DentalClinic/user?action=logout" style="color: white; text-decoration: none;">Logout</a>
            <%
                } else {
            %>
                <a href="/DentalClinic/user?action=login" style="color: white; text-decoration: none; margin-right: 20px;">Login</a>
                <a href="/DentalClinic/user?action=register" style="color: white; text-decoration: none;">Register</a>
            <%
                }
            %>
        </nav>
    </div>
</header>
