<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Service"%>
<%@page import="dto.ServiceDto"%>
<%@page import="model.Users"%>
<!DOCTYPE html>
<html>
<head>
    <title>Services</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 1000px;
            margin: 0 auto;
            padding: 20px;
            background-color: #f9f9f9;
        }
        h1, h3 {
            color: #333;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            background-color: white;
            margin-bottom: 15px;
        }
        th, td {
            padding: 10px;
            text-align: left;
            border: 1px solid #ddd;
        }
        th {
            background-color: #f2f2f2;
            font-weight: bold;
        }
        input, select, button {
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button {
            background-color: #007bff;
            color: white;
            cursor: pointer;
            margin-right: 10px;
        }
        button:hover {
            background-color: #0056b3;
        }
        button:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }
        .service-table {
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .service-header {
            background-color: #007bff;
            color: white;
        }
        .filter-section {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 20px;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
        .navigation {
            text-align: center;
            margin: 20px 0;
        }
        .page-info {
            text-align: center;
            font-weight: bold;
            color: #666;
        }
        hr {
            border: none;
            height: 1px;
            background-color: #ddd;
            margin: 20px 0;
        }
    </style>
</head>
<body>
    <!-- Include Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>
    
    <div style="padding: 20px;">
        <h1>Dental Services</h1>
        
        <!-- Debug: Show current user -->
        <%
            Users currentUser = (Users) session.getAttribute("user");
            if (currentUser != null) {
        %>
            <p style="background: #d4edda; padding: 10px; border-radius: 5px;">
                <strong>Welcome:</strong> <%= currentUser.getFullName() %> 
                (<%= currentUser.getEmail() %>) - Role: <%= currentUser.getRole() %>
            </p>
        <% } else { %>
            <p style="background: #f8d7da; padding: 10px; border-radius: 5px;">
                <strong>Not logged in</strong> - Please login to access full features
            </p>
        <% } %>
    
    <!-- Filter Form -->
    <div class="filter-section">
        <h3>Search Services:</h3>
        <form method="POST" action="service">
            <table>
            <tr>
                <td>Service Name:</td>
                <td><input type="text" name="serviceName"></td>
            </tr>
            <tr>
                <td>Min Price:</td>
                <td><input type="number" name="priceFrom"></td>
            </tr>
            <tr>
                <td>Max Price:</td>
                <td><input type="number" name="priceTo"></td>
            </tr>
            <tr>
                <td>Status:</td>
                <td>
                    <select name="isActive">
                        <option value="">All</option>
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                    </select>
                </td>
            </tr>
        </table>
            <input type="hidden" name="page" value="1">
            <button type="submit">Search</button>
            <button type="button" onclick="location.href='service'">Clear</button>
        </form>
    </div>
    
    <hr>
    
    <!-- Page Info -->
    <div class="page-info">Page ${page} / ${totalPages}</div>
    
    <!-- Services List -->
    <%
        List<ServiceDto> serviceDtos = (List<ServiceDto>) request.getAttribute("list");
        
        if (serviceDtos != null && !serviceDtos.isEmpty()) {
    %>
        <h3>Services Found: <%= serviceDtos.size() %></h3>
        
        <% for (int i = 0; i < serviceDtos.size(); i++) { 
            ServiceDto dto = serviceDtos.get(i);
            Service service = dto.getService();
        %>
            <table class="service-table">
                <tr class="service-header">
                    <th colspan="2">Service #<%= (i+1) %></th>
                </tr>
                <tr>
                    <td><strong>Name:</strong></td>
                    <td><%= service.getServiceName() %></td>
                </tr>
                <tr>
                    <td><strong>Description:</strong></td>
                    <td><%= service.getDescription() %></td>
                </tr>
                <tr>
                    <td><strong>Price:</strong></td>
                    <td><%= service.getPrice() %> VND</td>
                </tr>
                <tr>
                    <td><strong>Duration:</strong></td>
                    <td><%= service.getDuration() %> minutes</td>
                </tr>
                <tr>
                    <td><strong>Status:</strong></td>
                    <td><%= service.isIsActive() ? "Active" : "Inactive" %></td>
                </tr>
                <% if (dto.getCreator() != null) { %>
                <tr>
                    <td><strong>Created by:</strong></td>
                    <td><%= dto.getCreator().getFullName() %></td>
                </tr>
                <% } %>
                <% if (dto.getDoctror() != null && dto.getDoctror().getSpecialization() != null) { %>
                <tr>
                    <td><strong>Doctor Specialization:</strong></td>
                    <td><%= dto.getDoctror().getSpecialization() %></td>
                </tr>
                <% } %>
            </table>
        <% } %>
        
    <% } else { %>
        <h3>No services found</h3>
        <p>Try different search criteria or check if there are services in the database.</p>
    <% } %>
    
    <!-- Pagination -->
    <hr>
    <div class="navigation">
        <h3>Navigation:</h3>
        <form method="POST" action="service" style="display: inline;">
            <input type="hidden" name="page" value="${page-1}">
            <button type="submit" <%= (Integer.valueOf(request.getAttribute("page").toString()) <= 1) ? "disabled" : "" %>>Previous</button>
        </form>
        
        <span> | Page ${page} / ${totalPages} | </span>
        
        <form method="POST" action="service" style="display: inline;">
            <input type="hidden" name="page" value="${page+1}">
            <button type="submit" <%= (Integer.valueOf(request.getAttribute("page").toString()) >= Integer.valueOf(request.getAttribute("totalPages").toString())) ? "disabled" : "" %>>Next</button>
        </form>
    </div>
    
    <hr>
    <p><a href="home">Back to Home</a></p>
    
    </div> <!-- Close padding div -->
</body>
</html>