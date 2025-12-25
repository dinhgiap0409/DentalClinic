<%-- 
    Document   : patient-record
    Created on : Nov 4, 2025
    Author     : TNO
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Hồ sơ bệnh nhân - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>
    <body>
        <div class="dashboard">
            <!-- Sidebar -->
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="content">
                    <div class="header">
                        <h1>Hồ sơ bệnh nhân: ${patient.userID.fullName}</h1>
                </div>

                <!-- Thông tin bệnh nhân -->
                <div class="patient-info">
                    <h2>Thông tin cá nhân</h2>
                    <p><strong>Họ tên:</strong> ${patient.userID.fullName}</p>
                    <p><strong>SĐT:</strong> ${patient.userID.phoneNumber}</p>
                    <p><strong>Ngày sinh:</strong> <fmt:formatDate value="${patient.userID.dateOfBirth}" pattern="dd/MM/yyyy"/></p>
                    <p><strong>Giới tính:</strong> ${patient.userID.gender}</p>
                    <p><strong>Địa chỉ:</strong> ${patient.userID.address}</p>
                    <p><strong>Nhóm máu:</strong> ${patient.bloodType}</p>
                    <p><strong>Dị ứng:</strong> ${patient.allergies}</p>
                    <p><strong>Tiền sử bệnh:</strong> ${patient.medicalHistory}</p>
                    <p><strong>Thông tin bảo hiểm:</strong> ${patient.insuranceInfo}</p>
                    <p><strong>Người liên hệ khẩn cấp:</strong> ${patient.emergencyContactName} - ${patient.emergencyContactPhone}</p>
                </div>

                <!-- Hồ sơ y tế -->
                <div class="medical-records">
                    <h2>Hồ sơ y tế</h2>
                    <c:if test="${empty records}">
                        <p>Chưa có hồ sơ y tế nào.</p>
                    </c:if>
                    <c:forEach var="r" items="${records}">
                        <div class="record">
                            <h3>Ngày tạo: <fmt:formatDate value="${r.createdDate}" pattern="dd/MM/yyyy"/></h3>
                            <p><strong>Chẩn đoán:</strong> ${r.diagnosis}</p>
                            <p><strong>Triệu chứng:</strong> ${r.symptoms}</p>
                            <p><strong>Kế hoạch điều trị:</strong> ${r.treatmentPlan}</p>
                            <p><strong>Ngày tái khám:</strong> <fmt:formatDate value="${r.followUpDate}" pattern="dd/MM/yyyy"/></p>

                            <c:set var="presList" value="${prescriptionsMap[r.recordId]}"/>
                            <c:if test="${not empty presList}">
                                <h4>Đơn thuốc</h4>
                                <table class="data-table">
                                    <thead>
                                        <tr>
                                            <th>Ngày kê</th>
                                            <th>Bác sĩ</th>
                                            <th>Hướng dẫn</th>
                                            <th>Đơn hàng</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="p" items="${presList}">
                                            <tr>
                                                <td><fmt:formatDate value="${p.issueDate}" pattern="dd/MM/yyyy"/></td>
                                                <td>${p.doctorId.userId.fullName}</td>
                                                <td>${p.instructions}</td>
                                                <td>
                                                    <c:set var="orders" value="${ordersByPrescription[p.prescriptionId]}"/>
                                                    <c:if test="${not empty orders}">
                                                        <ul>
                                                            <c:forEach var="o" items="${orders}">
                                                                <li>${o.orderDate} - ${o.totalAmount} - ${o.status}</li>
                                                                </c:forEach>
                                                        </ul>
                                                    </c:if>
                                                    <c:if test="${empty orders}">Chưa có đơn hàng</c:if>
                                                    </td>
                                                </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </body>
</html>
