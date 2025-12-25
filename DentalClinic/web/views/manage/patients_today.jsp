<%-- 
    Document   : patients_today
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
        <title>Bệnh nhân hôm nay - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>

    <body>
        <div class="dashboard">
            <!-- Sidebar -->
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="content">
                    <div class="header">
                        <h1>Danh sách bệnh nhân hôm nay</h1>
                    </div>

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
                    <form action="${pageContext.request.contextPath}/patientsToday" method="get">
                        <input type="text" name="patientName" placeholder="Tên bệnh nhân" value="${param.patientName}">
                        <input type="text" name="phoneNumber" placeholder="SĐT" value="${param.phoneNumber}">
                        <input type="text" name="serviceName" placeholder="Dịch vụ" value="${param.serviceName}">
                        <button type="submit">Tìm kiếm</button>
                        <a href="${pageContext.request.contextPath}/patientsToday" class="reset-btn">Bỏ lọc</a>
                    </form>
                </div>

                <!-- Bảng kết quả -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${empty appointments}">
                            <p class="muted text-center" style="padding:15px;">Không có bệnh nhân nào hôm nay.</p>
                        </c:when>

                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Mã</th>
                                        <th>Bệnh nhân</th>
                                        <th>SĐT</th>
                                        <th>Giới tính</th>
                                        <th>Ngày sinh</th>
                                        <th>Bác sĩ</th>
                                        <th>Dịch vụ</th>
                                        <th>Giờ</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="a" items="${appointments}">
                                        <tr>
                                            <td>${a.appointmentId}</td>
                                            <td>${a.patientId.userID.fullName}</td>
                                            <td>${a.patientId.userID.phoneNumber}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${a.patientId.userID.gender}">Nam</c:when>
                                                    <c:otherwise>Nữ</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td><fmt:formatDate value="${a.patientId.userID.dateOfBirth}" pattern="dd/MM/yyyy"/></td>
                                            <td>${a.doctorId.userId.fullName}</td>
                                            <td>${a.serviceId.serviceName}</td>
                                            <td>${a.startTime} - ${a.endTime}</td>
                                            <td><span class="status ${a.status}">${a.status}</span></td>
                                            <td class="actions">
                                                <!-- Chi tiết lịch hẹn -->
                                                <form action="${pageContext.request.contextPath}/appointmentDetail" method="get" style="display:inline;">
                                                    <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                    <button type="submit" class="btn btn-detail">Chi tiết</button>
                                                </form>

                                                <!-- Hồ sơ bệnh nhân -->
                                                <form action="${pageContext.request.contextPath}/patientRecord" method="get" style="display:inline;">
                                                    <input type="hidden" name="patientId" value="${a.patientId.patientID}">
                                                    <button type="submit" class="btn btn-info">Hồ sơ</button>
                                                </form>

                                                <!-- Hoàn thành (chỉ khi Confirmed) -->
                                                <form action="${pageContext.request.contextPath}/patientsToday" method="post" style="display:inline;"
                                                      onsubmit="return confirm('Xác nhận hoàn thành lịch hẹn?');">
                                                    <input type="hidden" name="action" value="complete"/>
                                                    <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                    <button type="submit" class="btn btn-success"
                                                            ${a.status != 'Confirmed' ? 'disabled' : ''}>
                                                        Hoàn thành
                                                    </button>
                                                </form>

                                                <!-- Kê thuốc (chỉ khi Confirmed) -->
                                                <form action="${pageContext.request.contextPath}/recordMedicalResults" method="get" style="display:inline;">
                                                    <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                    <button type="submit" class="btn btn-warning"
                                                            ${a.status != 'Confirmed' ? 'disabled' : ''}>
                                                        Kê thuốc
                                                    </button>
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
