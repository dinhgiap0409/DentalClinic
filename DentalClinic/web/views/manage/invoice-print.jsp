<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Hóa đơn thanh toán</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/style.css"/>
        <style>
            body {
                font-family: "Segoe UI", sans-serif;
                background-color: #f8f9fa;
                margin: 20px;
                color: #333;
            }
            .container {
                max-width: 900px;
                margin: auto;
                background: #fff;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }
            h2 {
                color: #2b5797;
                text-align: center;
                margin-bottom: 25px;
            }
            .info-section {
                margin-bottom: 25px;
            }
            .info-section h4 {
                color: #2b5797;
                border-bottom: 2px solid #2b5797;
                padding-bottom: 5px;
                margin-bottom: 10px;
            }
            .info-item {
                margin-bottom: 5px;
            }
            .info-label {
                font-weight: 600;
                min-width: 130px;
                display: inline-block;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 15px;
            }
            th, td {
                border: 1px solid #ddd;
                padding: 10px 12px;
                text-align: left;
            }
            th {
                background-color: #2b5797;
                color: white;
            }
            tr:nth-child(even) {
                background-color: #f4f6f9;
            }
            .total {
                text-align: right;
                font-weight: 700;
                font-size: 1.1rem;
                margin-top: 20px;
            }
            .footer {
                text-align: center;
                color: #888;
                margin-top: 40px;
                font-size: 0.9rem;
            }
            .btn-print {
                display: inline-block;
                background: #2b5797;
                color: #fff;
                padding: 10px 20px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: 600;
                transition: background 0.2s;
            }
            .btn-print:hover {
                background: #1d3f75;
            }
            @media print {
                .btn-print {
                    display: none;
                }
                body {
                    background: #fff;
                    margin: 0;
                }
            }
        </style>
    </head>

    <body>
        <div class="container">
            <h2>HÓA ĐƠN THANH TOÁN</h2>

            <!-- Thông tin bệnh nhân -->
            <div class="info-section">
                <h4>Thông tin bệnh nhân</h4>
                <div class="info-item"><span class="info-label">Họ tên:</span> ${order.patients.userID.fullName}</div>
                <div class="info-item"><span class="info-label">Số điện thoại:</span> ${order.patients.userID.phoneNumber}</div>
                <div class="info-item"><span class="info-label">Địa chỉ:</span> ${order.patients.userID.address}</div>
            </div>

            <!-- Thông tin đơn hàng -->
            <div class="info-section">
                <h4>Thông tin đơn hàng</h4>
                <div class="info-item"><span class="info-label">Mã đơn hàng:</span> ${order.orderId}</div>
                <div class="info-item"><span class="info-label">Ngày lập:</span> 
                    <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy HH:mm" />
                </div>
                <div class="info-item"><span class="info-label">Phương thức thanh toán:</span> ${order.paymentMethod}</div>
                <div class="info-item"><span class="info-label">Trạng thái thanh toán:</span> ${order.paymentStatus}</div>
            </div>

            <!-- Chi tiết thuốc -->
            <h4>Chi tiết đơn thuốc</h4>
            <table>
                <thead>
                    <tr>
                        <th>Tên thuốc</th>
                        <th>Mô tả</th>
                        <th>Giá</th>
                        <th>Số lượng</th>
                        <th>Thành tiền</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="d" items="${orderDetails}">
                        <tr>
                            <td>${d.medicationId.medicationName}</td>
                            <td>${d.medicationId.description}</td>
                            <td><fmt:formatNumber value="${d.medicationId.price}" type="currency" currencySymbol="₫" /></td>
                            <td>${d.quantity}</td>
                            <td>
                                <fmt:formatNumber value="${d.price * d.quantity}" type="currency" currencySymbol="₫" />
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <div class="total">
                Tổng cộng: 
                <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫" />
            </div>

            <div style="text-align:center; margin-top: 30px;">
                <a href="#" class="btn-print" onclick="window.print()">In hóa đơn</a>
            </div>

            <div class="footer">
                Cảm ơn quý khách đã tin tưởng sử dụng dịch vụ của phòng khám.<br/>
                Hóa đơn được tạo tự động — không cần ký tên.
            </div>
        </div>
    </body>
</html>
