<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Hóa đơn - ${appointment.patientId.userID.fullName}</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            body {
                background-color: #f3f6fa;
                font-family: "Segoe UI", Roboto, Arial, sans-serif;
                color: #333;
            }
            .content {
                padding: 20px 30px;
            }
            .print-header {
                text-align: center;
                margin-bottom: 20px;
            }
            .print-header h1 {
                margin: 0;
                font-size: 22px;
                color: #004085;
            }
            .print-section {
                margin-bottom: 20px;
            }
            .print-section h2 {
                font-size: 16px;
                margin-bottom: 8px;
                color: #007bff;
            }
            .print-section table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 10px;
            }
            .print-section table td,
            .print-section table th {
                padding: 8px 10px;
                border: 1px solid #e9ecef;
                vertical-align: top;
            }
            .print-section table td.label {
                font-weight: 600;
                width: 30%;
                background-color: #f1f3f5;
            }
            .btn-print-page {
                display: inline-flex;
                align-items: center;
                justify-content: center;
                min-width: 90px;
                height: 36px;
                padding: 0 14px;
                border-radius: 6px;
                font-size: 13px;
                font-weight: 500;
                text-decoration: none;
                border: 1px solid #007bff;
                background-color: #e3f2fd;
                color: #0d6efd;
                cursor: pointer;
                transition: all 0.2s ease;
                margin-bottom: 20px;
            }
            .btn-print-page:hover {
                background-color: #bbdefb;
            }
            @media print {
                .btn-print-page, .sidebar {
                    display: none;
                }
            }
            .signature {
                margin-top: 40px;
                text-align: right;
            }
            .signature p {
                margin-bottom: 80px;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <div class="content">
                    <div class="print-header">
                        <h1>HÓA ĐƠN THANH TOÁN</h1>
                        <p>Ngày: <fmt:formatDate value="${order.orderDate}" pattern="dd/MM/yyyy"/></p>
                </div>

                <!-- Thông tin bệnh nhân -->
                <div class="print-section">
                    <h2>Thông tin bệnh nhân</h2>
                    <table>
                        <tr>
                            <td class="label">Họ và tên</td>
                            <td>${appointment.patientId.userID.fullName}</td>
                        </tr>
                        <tr>
                            <td class="label">SĐT</td>
                            <td>${appointment.patientId.userID.phoneNumber}</td>
                        </tr>
                        <tr>
                            <td class="label">Ngày sinh</td>
                            <td><fmt:formatDate value="${appointment.patientId.userID.dateOfBirth}" pattern="dd/MM/yyyy"/></td>
                        </tr>
                        <tr>
                            <td class="label">Giới tính</td>
                            <td>${appointment.patientId.userID.gender}</td>
                        </tr>
                        <tr>
                            <td class="label">Địa chỉ</td>
                            <td>${appointment.patientId.userID.address}</td>
                        </tr>
                    </table>
                </div>

                <!-- Thông tin khám -->
                <div class="print-section">
                    <h2>Thông tin khám</h2>
                    <table>
                        <tr>
                            <td class="label">Bác sĩ</td>
                            <td>${appointment.doctorId.userId.fullName}</td>
                        </tr>
                        <tr>
                            <td class="label">Chuyên khoa</td>
                            <td>${appointment.doctorId.specialization}</td>
                        </tr>
                        <tr>
                            <td class="label">Dịch vụ</td>
                            <td>${appointment.serviceId.serviceName}</td>
                        </tr>
                        <tr>
                            <td class="label">Giờ khám</td>
                            <td>${appointment.startTime} - ${appointment.endTime}</td>
                        </tr>
                    </table>
                </div>

                <!-- Thuốc / kê đơn -->
                <c:if test="${not empty prescription}">
                    <div class="print-section">
                        <h2>Thuốc / Kê đơn</h2>
                        <table>
                            <tr>
                                <th>Tên thuốc</th>
                                <th>Hướng dẫn sử dụng</th>
                            </tr>
                            <tr>
                                <td>${prescription != null ? prescription.recordId.diagnosis : ""}</td>
                                <td>${prescription != null ? prescription.instructions : ""}</td>
                            </tr>
                        </table>
                    </div>
                </c:if>

                <!-- Hóa đơn -->
                <div class="print-section">
                    <h2>Chi tiết hóa đơn</h2>
                    <table>
                        <tr>
                            <td class="label">Tổng tiền</td>
                            <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="₫"/></td>
                        </tr>
                        <tr>
                            <td class="label">Phương thức thanh toán</td>
                            <td>${order.paymentMethod}</td>
                        </tr>
                        <tr>
                            <td class="label">Trạng thái thanh toán</td>
                            <td>${order.paymentStatus}</td>
                        </tr>
                    </table>
                </div>

                <!-- Chỗ ký bác sĩ -->
                <div class="signature">
                    <p>Bác sĩ khám</p>
                    <p>__________________________</p>
                </div>

                <button class="btn-print-page" onclick="window.print()">In hóa đơn</button>
            </div>
        </div>
    </body>
</html>
