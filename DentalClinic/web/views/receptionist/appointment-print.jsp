<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Phiếu khám - ${appointment.patientId.userID.fullName}</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            /* ===== Chỉnh cho in phiếu ===== */
            body {
                background-color: #f8f9fa;
                font-family: "Segoe UI", Roboto, Arial, sans-serif;
                color: #212529;
                margin: 0;
            }

            .content {
                max-width: 800px;
                margin: 20px auto;
                padding: 30px;
                background-color: #fff;
                box-shadow: 0 0 15px rgba(0,0,0,0.1);
                border-radius: 6px;
                border: 1px solid #dee2e6;
            }

            .print-header {
                text-align: center;
                margin-bottom: 25px;
            }

            .print-header img.logo {
                max-height: 60px;
                margin-bottom: 10px;
            }

            .print-header h1 {
                margin: 0;
                font-size: 24px;
                color: #004085;
            }
            .print-header p {
                margin: 2px 0;
                font-size: 14px;
                color: #495057;
            }

            .print-section {
                margin-bottom: 25px;
            }
            .print-section h2 {
                font-size: 16px;
                margin-bottom: 10px;
                color: #007bff;
                border-bottom: 1px solid #dee2e6;
                padding-bottom: 5px;
            }

            .print-section table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 10px;
            }

            .print-section table td {
                padding: 8px 10px;
                vertical-align: top;
            }

            .print-section table td.label {
                font-weight: 600;
                width: 30%;
                background-color: #f1f3f5;
                border: 1px solid #dee2e6;
            }

            .print-section table td.value {
                border: 1px solid #dee2e6;
            }

            .signature-section {
                margin-top: 40px;
                display: flex;
                justify-content: space-between;
            }

            .signature {
                text-align: center;
            }

            .signature p {
                margin-top: 60px;
                border-top: 1px dashed #000;
                width: 200px;
                margin-left: auto;
                margin-right: auto;
                padding-top: 5px;
                font-size: 13px;
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
                .btn-print-page {
                    display: none;
                }
                .sidebar {
                    display: none;
                }
                body {
                    background-color: #fff;
                }
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <!-- Sidebar giống lookup -->
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <!-- Nội dung chính -->
                <div class="content">
                    <div class="print-header">
                        <!-- Logo phòng khám nếu có -->
                        <img class="logo" src="${pageContext.request.contextPath}/images/logo.png" alt="Logo Phòng khám">
                    <h1>PHIẾU KHÁM BỆNH</h1>
                    <p>Ngày hẹn: <fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy"/></p>
                </div>

                <div class="print-section">
                    <h2>Thông tin bệnh nhân</h2>
                    <table>
                        <tr>
                            <td class="label">Họ và tên</td>
                            <td class="value">${appointment.patientId.userID.fullName}</td>
                        </tr>
                        <tr>
                            <td class="label">SĐT</td>
                            <td class="value">${appointment.patientId.userID.phoneNumber}</td>
                        </tr>
                        <tr>
                            <td class="label">Ngày sinh</td>
                            <td class="value"><fmt:formatDate value="${appointment.patientId.userID.dateOfBirth}" pattern="dd/MM/yyyy"/></td>
                        </tr>
                        <tr>
                            <td class="label">Giới tính</td>
                            <td class="value">${appointment.patientId.userID.gender}</td>
                        </tr>
                        <tr>
                            <td class="label">Địa chỉ</td>
                            <td class="value">${appointment.patientId.userID.address}</td>
                        </tr>
                    </table>
                </div>

                <div class="print-section">
                    <h2>Thông tin khám</h2>
                    <table>
                        <tr>
                            <td class="label">Bác sĩ</td>
                            <td class="value">${appointment.doctorId.userId.fullName}</td>
                        </tr>
                        <tr>
                            <td class="label">Chuyên khoa</td>
                            <td class="value">${appointment.doctorId.specialization}</td>
                        </tr>
                        <tr>
                            <td class="label">Dịch vụ</td>
                            <td class="value">${appointment.serviceId.serviceName}</td>
                        </tr>
                        <tr>
                            <td class="label">Giờ khám</td>
                            <td class="value">${appointment.startTime} - ${appointment.endTime}</td>
                        </tr>
                        <tr>
                            <td class="label">Ghi chú</td>
                            <td class="value">${appointment.notes}</td>
                        </tr>
                    </table>
                </div>

                <div class="print-section">
                    <h2>Ghi chú</h2>
                    <table>
                        <tr>
                            <td class="value" colspan="2" style="border:1px solid #dee2e6; padding:10px; min-height:80px;">
                                ${appointment.notes != null ? appointment.notes : "Không có ghi chú"}
                            </td>
                        </tr>
                    </table>
                </div>

                <div class="signature-section">
                    <div class="signature">
                        <p>Bác sĩ ký</p>
                    </div>
                    <div class="signature">
                        <p>Người nhận</p>
                    </div>
                </div>

                <button class="btn-print-page" onclick="window.print()">In phiếu</button>
            </div>
        </div>
    </body>
</html>
