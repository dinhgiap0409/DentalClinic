<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="model.Service"%>
<%@page import="java.text.NumberFormat"%>
<%@page import="java.util.Locale"%>

<%
    List<Service> services = (List<Service>) request.getAttribute("services");
    NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    
    if (services != null && !services.isEmpty()) {
%>
    <div class="services">
        <% 
            String[] images = {"dept-1.jpg", "dept-2.jpg", "dept-3.jpg", "dept-4.jpg", "dept-5.jpg", "dept-6.jpg"};
            int currentPage = (Integer) request.getAttribute("currentPage");
            int size = (Integer) request.getAttribute("size");
            int startImageIndex = (currentPage - 1) * size;
        %>
        <% for (int i = 0; i < services.size(); i++) { 
               Service s = services.get(i);
        %>
            <div class="service">
                <img src="/DentalClinic/img/<%= images[(startImageIndex + i) % images.length] %>" 
                     alt="<%= s.getServiceName() %>">

                <h3><%= s.getServiceName() %></h3>
                <p><%= s.getDescription() %></p>
                <div class="service-price"><%= currencyFormat.format(s.getPrice()) %></div>
                <div class="service-duration">Thời lượng: <%= s.getDuration() %> phút</div>

                <!-- Nút đặt lịch -->
                <a href="/DentalClinic/appropriateSpecialist?serviceId=<%= s.getServiceId() %>" 
                   class="btn-book">
                   Đặt lịch ngay
                </a>
            </div>
        <% } %>
    </div>
<% } else { %>
    <p style="text-align: center; color: #666;">Không có dịch vụ nào</p>
<% } %>
