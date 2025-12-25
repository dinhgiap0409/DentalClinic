<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thanh toán - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            table.data-table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }
            table.data-table th, table.data-table td {
                border: 1px solid #ddd;
                padding: 8px;
            }
            .total {
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp" />

            <div class="content">
                <div class="header">
                    <h1>Thanh toán đơn thuốc</h1>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <h3>Thông tin đơn hàng</h3>
                <p>Mã đơn hàng: ${order.orderId}</p>
                <p>Bệnh nhân: ${order.patients.userID.fullName} - ${order.patients.userID.phoneNumber}</p>
                <p>Bác sĩ: ${order.prescriptions.doctorId.userId.fullName}</p>
                <p>Ngày tạo: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm"/></p>

                <h3>Chi tiết thuốc</h3>
                <table class="data-table">
                    <thead>
                        <tr>
                            <th>STT</th>
                            <th>Tên thuốc</th>
                            <th>Giá</th>
                            <th>Số lượng</th>
                            <th>Thành tiền</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="od" items="${orderDetailsList}" varStatus="status">
                            <tr>
                                <td>${status.index + 1}</td>
                                <td>${od.medicationId.medicationName}</td>
                                <td><fmt:formatNumber value="${od.price}" type="currency" currencySymbol="₫"/></td>
                                <td>${od.quantity}</td>
                                <td>
                                    <fmt:formatNumber value="${od.price.multiply(od.quantity)}" type="currency" currencySymbol="₫"/>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <p class="total">Tổng tiền thuốc: <fmt:formatNumber value="${totalMedAmount}" type="currency" currencySymbol="₫"/></p>
                <p class="total">Phí khám bác sĩ: <fmt:formatNumber value="${consultationFee}" type="currency" currencySymbol="₫"/></p>
                <p class="total">Tổng thanh toán: <fmt:formatNumber value="${grandTotal}" type="currency" currencySymbol="₫"/></p>

                <form action="sendZaloPage" method="post">
                    <input type="hidden" name="orderId" value="${order.orderId}"/>
                    <input type="hidden" name="amount" value="${grandTotal}"/>
                    <input type="hidden" name="phone" value="${order.patients.userID.phoneNumber}"/>
                    <button type="submit">Thanh toán</button>
                </form>

            </div>
        </div>
    </body>
</html>
