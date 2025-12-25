<%-- 
    Document   : service-list
    Created on : Nov 2, 2025
    Author     : TNO
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý dịch vụ - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .content h1 {
                margin-bottom: 20px;
            }
            .filter-box form {
                display: flex;
                flex-wrap: wrap;
                gap: 10px;
                align-items: center;
                margin-bottom: 20px;
            }
            .filter-box input, .filter-box select {
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 6px;
                font-size: 14px;
            }
            .filter-box button, .filter-box .reset-btn {
                padding: 8px 14px;
                border: none;
                border-radius: 6px;
                background: #007bff;
                color: #fff;
                cursor: pointer;
                transition: 0.2s;
            }
            .filter-box .reset-btn {
                background: #6c757d;
                text-decoration: none;
            }
            .filter-box button:hover, .filter-box .reset-btn:hover {
                opacity: 0.9;
            }
            .table-container {
                background: #fff;
                border-radius: 8px;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
                padding: 15px;
            }
            .data-table {
                width: 100%;
                border-collapse: collapse;
            }
            .data-table th, .data-table td {
                padding: 10px;
                border-bottom: 1px solid #eee;
                text-align: center;
                vertical-align: middle;
            }
            .data-table th {
                background: #f7f7f7;
            }
            .service-img {
                width: 60px;
                height: 60px;
                object-fit: cover;
                border-radius: 8px;
            }
            .actions button {
                margin: 0 4px;
                padding: 5px 10px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                font-size: 13px;
            }
            .btn-edit {
                background: #ffc107;
                color: #fff;
            }
            .btn-delete {
                background: #dc3545;
                color: #fff;
            }
            .btn-add {
                background: #28a745;
                color: #fff;
                padding: 10px 18px;
                border-radius: 6px;
                text-decoration: none;
                display: inline-block;
                margin-bottom: 15px;
            }
            .status-true {
                color: green;
                font-weight: 600;
            }
            .status-false {
                color: #aaa;
                font-style: italic;
            }
        </style>
    </head>

    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <div class="content">
                    <h1>Quản lý dịch vụ</h1>

                    <!-- Thông báo -->
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success">${successMessage}</div>
                    <c:remove var="successMessage" scope="session"/>
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error">${errorMessage}</div>
                    <c:remove var="errorMessage" scope="session"/>
                </c:if>

                <!-- Bộ lọc tìm kiếm -->
                <div class="filter-box">
                    <form action="${pageContext.request.contextPath}/manageService" method="get">
                        <input type="hidden" name="action" value="view">
                        <input type="text" name="name" placeholder="Tên dịch vụ" value="${param.name}">
                        <input type="number" name="priceFrom" placeholder="Giá từ" step="0.01" value="${param.priceFrom}">
                        <input type="number" name="priceTo" placeholder="Giá đến" step="0.01" value="${param.priceTo}">
                        <select name="isActive">
                            <option value="">--Trạng thái--</option>
                            <option value="true" ${param.isActive == 'true' ? 'selected' : ''}>Đang hoạt động</option>
                            <option value="false" ${param.isActive == 'false' ? 'selected' : ''}>Ngừng</option>
                        </select>
                        <button type="submit">Lọc</button>
                        <a href="${pageContext.request.contextPath}/manageService?action=view" class="reset-btn">Bỏ lọc</a>
                    </form>
                </div>

                <!-- Nút thêm mới -->
                <a href="${pageContext.request.contextPath}/views/manage/service-add.jsp" class="btn-add">+ Thêm dịch vụ mới</a>

                <!-- Bảng danh sách -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${empty services}">
                            <p style="padding:15px;text-align:center;">Không có dịch vụ nào được tìm thấy.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Ảnh</th>
                                        <th>Tên dịch vụ</th>
                                        <th>Mô tả</th>
                                        <th>Giá</th>
                                        <th>Thời lượng (phút)</th>
                                        <th>Trạng thái</th>
                                        <th>Người tạo</th>
                                        <th>Ngày tạo</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${services}">
                                        <tr>
                                            <td>${s.serviceId}</td>
                                            <td>
                                                <c:if test="${not empty s.image}">
                                                    <img src="${pageContext.request.contextPath}/uploads/${s.image}" class="service-img">
                                                </c:if>
                                                <c:if test="${empty s.image}">
                                                    <img src="${pageContext.request.contextPath}/images/no-image.png" class="service-img">
                                                </c:if>
                                            </td>
                                            <td>${s.serviceName}</td>
                                            <td style="max-width:250px;text-align:left;">${s.description}</td>
                                            <td><fmt:formatNumber value="${s.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                            <td>${s.duration}</td>
                                            <td class="${s.isActive ? 'status-true' : 'status-false'}">
                                                ${s.isActive ? 'Hoạt động' : 'Ngừng'}
                                            </td>
                                            <td>${s.createdBy.fullName}</td>
                                            <td><fmt:formatDate value="${s.createdDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                                            <td class="actions">
                                                <form action="${pageContext.request.contextPath}/manageService" method="get" style="display:inline;">
                                                    <input type="hidden" name="action" value="detail">
                                                    <input type="hidden" name="id" value="${s.serviceId}">
                                                    <button type="submit" class="btn-edit">Chi tiết</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>

                    <jsp:include page="../../common/pagination.jsp">
                        <jsp:param name="baseUrl" value="${baseUrl}" />
                        <jsp:param name="page" value="${page}" />
                        <jsp:param name="size" value="${size}" />
                        <jsp:param name="totalPages" value="${totalPages}" />
                    </jsp:include>
                </div>
            </div>
        </div>
    </body>
</html>
