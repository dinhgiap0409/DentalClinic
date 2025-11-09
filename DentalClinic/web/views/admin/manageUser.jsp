<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý người dùng - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            /* ====== Thêm 2 trạng thái ====== */
            .status.active {
                background-color: #007bff; /* xanh dương */
                color: #fff;
                padding: 4px 10px;
                border-radius: 5px;
                font-size: 13px;
                font-weight: 600;
                display: inline-block;
                text-transform: capitalize;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            }

            .status.inactive {
                background-color: #6c757d; /* xám */
                color: #fff;
                padding: 4px 10px;
                border-radius: 5px;
                font-size: 13px;
                font-weight: 600;
                display: inline-block;
                text-transform: capitalize;
                box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
            }

            /* (Tuỳ chọn) Hiệu ứng hover */
            .status.active:hover {
                background-color: #0056b3;
            }

            .status.inactive:hover {
                background-color: #5a6268;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <!-- Sidebar -->
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="content">
                    <div class="header">
                        <h1>Quản lý người dùng</h1>
                    </div>

                    <!-- Thông báo -->
                <c:if test="${not empty successMessage}">
                    <div class="alert alert-success">${successMessage}</div>
                    <c:remove var="successMessage" scope="session" />
                </c:if>
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-error">${errorMessage}</div>
                    <c:remove var="errorMessage" scope="session" />
                </c:if>

                <!-- Bộ lọc tìm kiếm -->
                <div class="filter-box">
                    <form action="${pageContext.request.contextPath}/manageUser" method="get">
                        <input type="hidden" name="action" value="list">

                        <input type="text" name="fullName" placeholder="Họ tên" value="${param.fullName}">
                        <input type="text" name="phoneNumber" placeholder="Số điện thoại" value="${param.phoneNumber}">
                        <input type="text" name="address" placeholder="Địa chỉ" value="${param.address}">

                        <select name="isActive">
                            <option value="">-- Trạng thái --</option>
                            <option value="true" ${param.isActive == 'true' ? 'selected' : ''}>Hoạt động</option>
                            <option value="false" ${param.isActive == 'false' ? 'selected' : ''}>Bị khóa</option>
                        </select>

                        <button type="submit">Tìm kiếm</button>
                        <a href="${pageContext.request.contextPath}/manageUser?action=list" class="reset-btn">Bỏ lọc</a>
                    </form>
                </div>

                <!-- Bảng kết quả -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${empty users}">
                            <p class="muted text-center" style="padding:15px;">Không có người dùng nào được tìm thấy.</p>
                        </c:when>

                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Họ tên</th>
                                        <th>Email</th>
                                        <th>Số điện thoại</th>
                                        <th>Địa chỉ</th>
                                        <th>Vai trò</th>
                                        <th>Trạng thái</th>
                                        <th>Ngày tạo</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="u" items="${users}">
                                        <tr>
                                            <td>${u.userId}</td>
                                            <td>${u.fullName}</td>
                                            <td>${u.email}</td>
                                            <td>${u.phoneNumber}</td>
                                            <td>${u.address}</td>
                                            <td>${u.role}</td>
                                            <td>
                                                <span class="status ${u.isActive ? 'active' : 'inactive'}">
                                                    ${u.isActive ? 'Hoạt động' : 'Bị khóa'}
                                                </span>
                                            </td>
                                            <td><fmt:formatDate value="${u.createDate}" pattern="dd/MM/yyyy"/></td>
                                            <td class="actions">
                                                <form action="${pageContext.request.contextPath}/manageUser" method="get" style="display:inline;">
                                                    <input type="hidden" name="action" value="view">
                                                    <input type="hidden" name="userId" value="${u.userId}">
                                                    <button type="submit" class="btn btn-detail">Xem chi tiết</button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>

                    <!-- Phân trang -->
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
