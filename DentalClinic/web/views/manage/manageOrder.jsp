<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý đơn hàng - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>

    <body>
        <div class="dashboard">
            <!-- Sidebar -->
            <jsp:include page="../../common/sidebar.jsp" />

            <!-- Main Content -->
            <div class="content">
                <div class="header">
                    <h1>Quản lý đơn hàng</h1>
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

                <!-- Bộ lọc -->
                <div class="filter-box">
                    <form action="${pageContext.request.contextPath}/manageOrder" method="get">
                        <input type="hidden" name="action" value="viewList" />
                        <input type="text" name="patientName" placeholder="Tên bệnh nhân" value="${param.patientName}" />
                        <input type="date" name="orderDateFrom" value="${param.orderDateFrom}" />
                        <input type="date" name="orderDateTo" value="${param.orderDateTo}" />

                        <select name="status">
                            <option value="">--Trạng thái đơn--</option>
                            <option value="Pending" ${param.status == 'Pending' ? 'selected' : ''}>Chờ xử lý</option>
                            <option value="Processing" ${param.status == 'Processing' ? 'selected' : ''}>Đang xử lý</option>
                            <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                            <option value="Cancelled" ${param.status == 'Cancelled' ? 'selected' : ''}>Đã hủy</option>
                        </select>

                        <select name="paymentStatus">
                            <option value="">--Thanh toán--</option>
                            <option value="Unpaid" ${param.paymentStatus == 'Unpaid' ? 'selected' : ''}>Chưa thanh toán</option>
                            <option value="Paid" ${param.paymentStatus == 'Paid' ? 'selected' : ''}>Đã thanh toán</option>
                            <option value="Refunded" ${param.paymentStatus == 'Refunded' ? 'selected' : ''}>Hoàn tiền</option>
                        </select>

                        <button type="submit">Tìm kiếm</button>
                        <a href="${pageContext.request.contextPath}/manageOrder?action=viewList" class="reset-btn">Bỏ lọc</a>
                    </form>
                </div>

                <!-- Bảng dữ liệu -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${empty orders}">
                            <p class="muted text-center" style="padding:15px;">Không có đơn hàng nào được tìm thấy.</p>
                        </c:when>

                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Mã đơn</th>
                                        <th>Bệnh nhân</th>
                                        <th>Ngày đặt</th>
                                        <th>Tổng tiền</th>
                                        <th>Trạng thái</th>
                                        <th>Thanh toán</th>
                                        <th>Phương thức</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="o" items="${orders}">
                                        <tr>
                                            <td>${o.orderId}</td>
                                            <td>${o.patients.userID.fullName}</td>
                                            <td><fmt:formatDate value="${o.orderDate}" pattern="dd/MM/yyyy HH:mm" /></td>
                                            <td><fmt:formatNumber value="${o.totalAmount}" type="currency" currencySymbol="₫" /></td>
                                            <td><span class="status ${o.status}">${o.status}</span></td>
                                            <td><span class="status ${o.paymentStatus}">${o.paymentStatus}</span></td>
                                            <td>${o.paymentMethod}</td>
                                            <td>
                                                <form action="${pageContext.request.contextPath}/manageOrder" method="get" style="display:inline;">
                                                    <input type="hidden" name="action" value="viewDetail" />
                                                    <input type="hidden" name="orderId" value="${o.orderId}" />
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

        <style>
            .btn-detail {
                background-color: #0d6efd;
                color: white;
                border: none;
                padding: 6px 12px;
                border-radius: 6px;
                cursor: pointer;
            }
            .btn-detail:hover {
                background-color: #0b5ed7;
            }
            .status.Pending {
                color: orange;
            }
            .status.Processing {
                color: #17a2b8;
            }
            .status.Completed {
                color: green;
            }
            .status.Cancelled {
                color: red;
            }
            .status.Unpaid {
                color: #dc3545;
            }
            .status.Paid {
                color: #28a745;
            }
            .status.Refunded {
                color: #6c757d;
            }
        </style>
    </body>
</html>
