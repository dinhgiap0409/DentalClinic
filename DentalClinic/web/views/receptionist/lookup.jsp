<%-- 
    Document   : lookup
    Created on : Nov 1, 2025
    Author     : TNO
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Tra cứu lịch hẹn - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    </head>

    <body>
        <div class="dashboard">
            <!-- Sidebar -->
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="content">
                    <div class="header">
                        <h1>Tra cứu lịch hẹn của khách hàng</h1>
                    </div>

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
                    <form action="${pageContext.request.contextPath}/lookUpAppointments" method="get">
                        <input type="text" name="patientName" placeholder="Tên bệnh nhân" value="${param.patientName}">
                        <input type="text" name="phoneNumber" placeholder="SĐT" value="${param.phoneNumber}">
                        <input type="text" name="doctorName" placeholder="Bác sĩ" value="${param.doctorName}">
                        <input type="text" name="serviceName" placeholder="Dịch vụ" value="${param.serviceName}">
                        <select name="status">
                            <option value="">--Trạng thái--</option>
                            <option value="Scheduled" ${param.status == 'Scheduled' ? 'selected' : ''}>Đã đặt lịch</option>
                            <option value="Confirmed" ${param.status == 'Confirmed' ? 'selected' : ''}>Đã xác nhận</option>
                            <option value="Completed" ${param.status == 'Completed' ? 'selected' : ''}>Hoàn thành</option>
                            <option value="Cancelled" ${param.status == 'Cancelled' ? 'selected' : ''}>Đã hủy</option>
                            <option value="Rescheduled" ${param.status == 'Rescheduled' ? 'selected' : ''}>Đã dời lịch</option>
                            <option value="NoShow" ${param.status == 'NoShow' ? 'selected' : ''}>Không đến</option>
                        </select>
                        <input type="date" name="fromDate" value="${param.fromDate}">
                        <input type="date" name="toDate" value="${param.toDate}">
                        <button type="submit">Tìm kiếm</button>
                        <a href="${pageContext.request.contextPath}/lookUpAppointments" class="reset-btn">Bỏ lọc</a>
                    </form>
                </div>

                <!-- Bảng kết quả -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${empty appointments}">
                            <p class="muted text-center" style="padding:15px;">Không có lịch hẹn nào được tìm thấy.</p>
                        </c:when>

                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Mã</th>
                                        <th>Bệnh nhân</th>
                                        <th>SĐT</th>
                                        <th>Bác sĩ</th>
                                        <th>Dịch vụ</th>
                                        <th>Ngày hẹn</th>
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
                                            <td>${a.doctorId.userId.fullName}</td>
                                            <td>${a.serviceId.serviceName}</td>
                                            <td><fmt:formatDate value="${a.appointmentDate}" pattern="dd/MM/yyyy"/></td>
                                            <td>${a.startTime} - ${a.endTime}</td>
                                            <td><span class="status ${a.status}">${a.status}</span></td>
                                            <td class="actions">
                                                <!-- Xác nhận khách hàng đến khám -->
                                                <form action="${pageContext.request.contextPath}/checkin" method="post" onsubmit="confirmCheckIn(this); return false;">
                                                    <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                    <button type="submit" class="btn btn-checkin"
                                                            ${a.status != 'Scheduled' && a.status != 'Rescheduled' ? 'disabled' : ''}>
                                                        Xác nhận
                                                    </button>
                                                </form>

                                                <!-- Xem chi tiết -->
                                                <form action="${pageContext.request.contextPath}/appointmentDetail" method="get">
                                                    <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                    <button type="submit" class="btn btn-detail">Chi tiết</button>
                                                </form>

                                                <!-- In phiếu khám -->
                                                <form action="${pageContext.request.contextPath}/printMedicalExamination" method="get">
                                                    <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                    <button type="submit" class="btn btn-print"
                                                            ${a.status != 'Confirmed' ? 'disabled' : ''}>
                                                        In phiếu khám
                                                    </button>
                                                </form>

                                                <!-- Tạo đơn hàng -->
                                                <form action="${pageContext.request.contextPath}/createOrder" method="get">
                                                    <input type="hidden" name="appointmentId" value="${a.appointmentId}">
                                                    <input type="hidden" name="patientId" value="${a.patientId.patientID}">
                                                    <button type="submit" class="btn btn-order"
                                                            ${a.status != 'Completed' ? 'disabled' : ''}>
                                                        Tạo đơn hàng
                                                    </button>
                                                </form>

                                                <form action="${pageContext.request.contextPath}/printInvoice" method="get" target="_blank">
                                                    <input type="hidden" name="id" value="${a.appointmentId}">
                                                    <button type="submit" class="btn btn-invoice"
                                                            ${a.status != 'Completed' ? 'disabled' : ''}>
                                                        In hóa đơn
                                                    </button>
                                                </form>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>

                    <!-- Phần phân trang -->
                    <jsp:include page="../../common/pagination.jsp">
                        <jsp:param name="baseUrl" value="${baseUrl}" />
                        <jsp:param name="page" value="${page}" />
                        <jsp:param name="size" value="${size}" />
                        <jsp:param name="totalPages" value="${totalPages}" />
                    </jsp:include>

                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
                                                    function confirmCheckIn(form) {
                                                        event.preventDefault();
                                                        Swal.fire({
                                                            title: 'Xác nhận khách hàng đến khám?',
                                                            icon: 'question',
                                                            showCancelButton: true,
                                                            confirmButtonText: 'Xác nhận',
                                                            cancelButtonText: 'Hủy',
                                                            confirmButtonColor: '#28a745'
                                                        }).then((result) => {
                                                            if (result.isConfirmed) {
                                                                form.submit();
                                                            }
                                                        });
                                                    }
        </script>
    </body>
</html>
