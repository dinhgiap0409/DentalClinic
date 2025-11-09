<%-- 
    Document   : manageOrderDetail
    Created on : Nov 3, 2025
    Author     : TNO
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết đơn hàng</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .content {
                padding: 30px;
                background-color: #f8fafc;
                min-height: 100vh;
            }
            .card {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.05);
                padding: 24px;
                margin-bottom: 25px;
            }
            .card h2 {
                font-size: 20px;
                color: #2563eb;
                margin-bottom: 16px;
                border-bottom: 2px solid #e2e8f0;
                padding-bottom: 6px;
            }
            table.info-table th {
                width: 200px;
                text-align: left;
                color: #334155;
                padding: 8px 0;
            }
            table.info-table td {
                padding: 8px 0;
                color: #1e293b;
            }
            .status {
                padding: 4px 10px;
                border-radius: 8px;
                font-size: 13px;
                font-weight: 500;
            }
            .status.Pending {
                background: #fef9c3;
                color: #854d0e;
            }
            .status.Processing {
                background: #dbeafe;
                color: #1e40af;
            }
            .status.Completed {
                background: #dcfce7;
                color: #166534;
            }
            .status.Cancelled {
                background: #fee2e2;
                color: #991b1b;
            }
            .status.Paid {
                background: #dcfce7;
                color: #166534;
            }
            .status.Unpaid {
                background: #fef9c3;
                color: #854d0e;
            }
            .btn {
                background: #2563eb;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                transition: 0.2s;
                text-decoration: none;
                font-size: 14px;
            }
            .btn:hover {
                background: #1d4ed8;
            }
            .btn-back {
                background: #e2e8f0;
                color: #1e293b;
            }
            .btn-back:hover {
                background: #cbd5e1;
            }
            table.data-table {
                width: 100%;
                border-collapse: collapse;
            }
            table.data-table th, table.data-table td {
                border-bottom: 1px solid #e2e8f0;
                padding: 10px;
                text-align: left;
            }
            table.data-table th {
                background-color: #f1f5f9;
            }
            .form-update {
                margin-top: 20px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            select {
                padding: 6px 10px;
                border-radius: 6px;
                border: 1px solid #cbd5e1;
                width: 100%;
            }
            .form-actions {
                text-align: right;
            }
        </style>
    </head>

    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <div class="content">
                    <div class="header">
                        <h1>Chi tiết đơn hàng</h1>
                        <a href="${pageContext.request.contextPath}/manageOrder" class="btn btn-back">← Quay lại</a>
                </div>

                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <div class="card">
                    <h2>Thông tin đơn hàng</h2>
                    <form action="${pageContext.request.contextPath}/manageOrder" method="post" class="form-update">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="orderId" value="${order.orderId}">
                        <table class="info-table">
                            <tr>
                                <th>Mã đơn hàng:</th>
                                <td>${order.orderId}</td>
                            </tr>
                            <tr>
                                <th>Ngày tạo:</th>
                                <td><fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></td>
                            </tr>
                            <tr>
                                <th>Trạng thái đơn hàng:</th>
                                <td>
                                    <select name="status">
                                        <c:forEach var="s" items="${['Pending','Processing','Completed','Cancelled']}">
                                            <option value="${s}" <c:if test="${s eq order.status}">selected</c:if>>${s}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <th>Trạng thái thanh toán:</th>
                                <td>
                                    <select name="paymentStatus">
                                        <c:forEach var="p" items="${['Unpaid','Paid','Refunded']}">
                                            <option value="${p}" <c:if test="${p eq order.paymentStatus}">selected</c:if>>${p}</option>
                                        </c:forEach>
                                    </select>
                                </td>
                            </tr>
                            <tr>
                                <th>Phương thức thanh toán:</th>
                                <td>${order.paymentMethod}</td>
                            </tr>
                            <tr>
                                <th>Tổng tiền:</th>
                                <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/></td>
                            </tr>
                        </table>
                        <div class="form-actions">
                            <button type="submit" class="btn">Cập nhật</button>
                        </div>
                    </form>

                </div>

                <div class="card">
                    <h2>Thông tin bệnh nhân</h2>
                    <table class="info-table">
                        <tr>
                            <th>Họ và tên:</th>
                            <td>${order.patients.userID.fullName}</td>
                        </tr>
                        <tr>
                            <th>Số điện thoại:</th>
                            <td>${order.patients.userID.phoneNumber}</td>
                        </tr>
                    </table>
                </div>

                <div class="card">
                    <h2>Chi tiết thuốc trong đơn</h2>
                    <c:choose>
                        <c:when test="${empty details}">
                            <p class="muted">Không có thuốc nào trong đơn hàng này.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Mã thuốc</th>
                                        <th>Tên thuốc</th>
                                        <th>Số lượng</th>
                                        <th>Đơn giá</th>
                                        <th>Thành tiền</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="d" items="${details}">
                                        <tr>
                                            <td>${d.medicationId.medicationId}</td>
                                            <td>${d.medicationId.medicationName}</td>
                                            <td>${d.quantity}</td>
                                            <td><fmt:formatNumber value="${d.price}" type="currency" currencySymbol="₫"/></td>
                                            <td><fmt:formatNumber value="${d.price * d.quantity}" type="currency" currencySymbol="₫"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </body>
</html>
