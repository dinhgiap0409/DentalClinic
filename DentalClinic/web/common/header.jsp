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
            <a href="/DentalClinic/home" style="color: white; text-decoration: none; font-size: 20px; font-weight: bold;">Dental Clinic</a>
        </div>
        <nav>
            <a href="/DentalClinic/home" style="color: white; text-decoration: none; margin-right: 20px;">Home</a>
            <a href="/DentalClinic/service" style="color: white; text-decoration: none; margin-right: 20px;">Services</a>
            <%
                Users user = (Users) session.getAttribute("user");
                if (user != null) {
            %>
                <a href="/DentalClinic/profile" style="color: white; text-decoration: none; margin-right: 20px;">Profile</a>
                <% if ("admin".equals(user.getRole())) { %>
                    <a href="/DentalClinic/dashboard" style="color: white; text-decoration: none; margin-right: 20px;">Dashboard</a>
                <% } %>
                <a href="/DentalClinic/logout" style="color: white; text-decoration: none;">Logout</a>
            <%
                } else {
            %>
                <a href="/DentalClinic/login" style="color: white; text-decoration: none; margin-right: 20px;">Login</a>
                <a href="/DentalClinic/register" style="color: white; text-decoration: none;">Register</a>
            <%
                }
            %>
        </nav>
    </div>
</header>
