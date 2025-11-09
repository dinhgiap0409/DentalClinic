<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Chi tiết lịch hẹn</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/appointment-detail.css">
</head>

<body>
    <div class="dashboard">
        <jsp:include page="../../common/sidebar.jsp"></jsp:include>

        <div class="content">
            <div class="detail-container">
                <div class="detail-header">
                    <h2>Chi tiết lịch hẹn #${appointment.appointmentId}</h2>
                </div>

                <div class="info-group">
                    <strong>Tên bệnh nhân:</strong>
                    <span>${appointment.patientId.userID.fullName}</span>
                </div>
                <div class="info-group">
                    <strong>Số điện thoại:</strong>
                    <span>${appointment.patientId.userID.phoneNumber}</span>
                </div>
                <div class="info-group">
                    <strong>Bác sĩ phụ trách:</strong>
                    <span>${appointment.doctorId.userId.fullName}</span>
                </div>
                <div class="info-group">
                    <strong>Dịch vụ khám:</strong>
                    <span>${appointment.serviceId.serviceName}</span>
                </div>
                <div class="info-group">
                    <strong>Ngày hẹn:</strong>
                    <span><fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy"/></span>
                </div>
                <div class="info-group">
                    <strong>Giờ:</strong>
                    <span>${appointment.startTime} - ${appointment.endTime}</span>
                </div>
                <div class="info-group">
                    <strong>Trạng thái:</strong>
                    <span class="statuss">${appointment.status}</span>
                </div>
                <div class="info-group">
                    <strong>Ghi chú:</strong>
                    <span><c:out value="${appointment.notes}" default="Không có"/></span>
                </div>
                <div class="info-group">
                    <strong>Ngày tạo:</strong>
                    <span><fmt:formatDate value="${appointment.createdDate}" pattern="dd/MM/yyyy HH:mm"/></span>
                </div>
                <div class="info-group">
                    <strong>Ngày cập nhật:</strong>
                    <span><fmt:formatDate value="${appointment.updatedDate}" pattern="dd/MM/yyyy HH:mm"/></span>
                </div>

                <div class="back-container">
                    <a href="${pageContext.request.contextPath}/lookUpAppointments" class="btn-back">← Quay lại</a>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
