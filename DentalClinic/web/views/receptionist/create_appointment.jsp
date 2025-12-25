<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Đặt lịch trực tiếp - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/create_appointment.css">
    </head>
    <body>
        <div class="dashboard">
            <!-- Sidebar -->
            <jsp:include page="../../common/sidebar.jsp" />

            <!-- Content -->
            <div class="content">
                <div class="header">
                    <h1>Đặt lịch trực tiếp cho bệnh nhân</h1>
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

                <!-- Form đặt lịch -->
                <div class="form-container">
                    <form id="bookingForm" action="bookAppointmentsDirectly" method="post">

                        <!-- Bệnh nhân -->
                        <div class="form-group">
                            <label for="patientId">Bệnh nhân</label>
                            <select name="patientId" id="patientId" required>
                                <option value="">-- Chọn bệnh nhân --</option>
                                <c:forEach var="p" items="${patients}">
                                    <option value="${p.patientID}"
                                            <c:if test="${param.patientId == p.patientID}">selected</c:if>>
                                        ${p.userID != null ? p.userID.fullName : ''} - ${p.userID != null ? p.userID.phoneNumber : ''}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Dịch vụ -->
                        <div class="form-group">
                            <label for="serviceId">Dịch vụ</label>
                            <select name="serviceId" id="serviceId" onchange="loadDoctors()" required>
                                <option value="">-- Chọn dịch vụ --</option>
                                <c:forEach var="s" items="${services}">
                                    <option value="${s.serviceId}"
                                            <c:if test="${selectedServiceId == s.serviceId}">selected</c:if>>
                                        ${s.serviceName} - ${s.price} VNĐ (${s.duration} phút)
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Bác sĩ -->
                        <c:if test="${not empty doctors}">
                            <div class="form-group">
                                <label for="doctorId">Bác sĩ</label>
                                <select name="doctorId" id="doctorId" onchange="loadSchedule()" required>
                                    <option value="">-- Chọn bác sĩ --</option>
                                    <c:forEach var="d" items="${doctors}">
                                        <option value="${d.doctorID}"
                                                <c:if test="${selectedDoctorId == d.doctorID}">selected</c:if>>
                                            ${d.userId != null ? d.userId.fullName : d.doctorID} (${d.specialization})
                                        </option>
                                    </c:forEach>
                                </select>
                            </div>
                        </c:if>

                        <!-- Lịch sẵn có -->
                        <c:forEach var="entry" items="${availableByDate}">
                            <c:set var="date" value="${entry.key}" />
                            <h4 class="schedule-date">Ngày: <fmt:formatDate value="${date}" pattern="dd/MM/yyyy"/></h4>

                            <c:choose>
                                <c:when test="${not empty entry.value}">
                                    <table class="schedule-table">
                                        <thead>
                                            <tr>
                                                <th>Chọn</th>
                                                <th>Bác sĩ</th>
                                                <th>Bắt đầu</th>
                                                <th>Kết thúc</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="s" items="${entry.value}">
                                                <tr>
                                                    <td class="schedule-radio">
                                                        <input type="radio" name="selectedSlot"
                                                               value="${date},${s.startTime},${s.endTime}" required />
                                                    </td>
                                                    <td class="schedule-doctor">
                                                        ${s.doctorId.userId != null ? s.doctorId.userId.fullName : s.doctorId.doctorID}
                                                    </td>
                                                    <td class="schedule-time"><fmt:formatDate value="${s.startTime}" pattern="HH:mm"/></td>
                                                    <td class="schedule-time"><fmt:formatDate value="${s.endTime}" pattern="HH:mm"/></td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </c:when>
                                <c:otherwise>
                                    <p>Không có slot nào.</p>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>

                        <!-- Nút đặt lịch -->
                        <c:if test="${not empty availableByDate}">
                            <div class="form-actions mt-3">
                                <button type="submit" class="btn btn-primary">Đặt lịch</button>
                                <a href="${pageContext.request.contextPath}/lookUpAppointments" class="btn btn-secondary">← Quay lại</a>
                            </div>
                        </c:if>

                    </form>
                </div>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

        <script>
                                    function loadDoctors() {
                                        const serviceId = document.getElementById("serviceId").value;
                                        const patientId = document.getElementById("patientId").value;
                                        if (serviceId) {
                                            window.location.href = "bookAppointmentsDirectly?patientId=" + patientId + "&serviceId=" + serviceId;
                                        }
                                    }

                                    function loadSchedule() {
                                        const serviceId = document.getElementById("serviceId").value;
                                        const patientId = document.getElementById("patientId").value;
                                        const doctorId = document.getElementById("doctorId").value;
                                        if (serviceId && doctorId) {
                                            window.location.href = "bookAppointmentsDirectly?patientId=" + patientId
                                                    + "&serviceId=" + serviceId
                                                    + "&doctorId=" + doctorId;
                                        }
                                    }
        </script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const now = new Date();

                // Khi chọn radio
                document.querySelectorAll("input[name='selectedSlot']").forEach(radio => {
                    radio.addEventListener("change", function (e) {
                        const [dateStr, startTime] = e.target.value.split(",");
                        const slotDateTime = new Date(dateStr + "T" + startTime);

                        if (slotDateTime < now) {
                            e.target.checked = false;
                            Swal.fire({
                                icon: "warning",
                                title: "Không thể chọn khung giờ này",
                                text: "Khung giờ bạn chọn đã qua. Vui lòng chọn khung giờ khác.",
                                confirmButtonColor: "#3085d6"
                            });
                        }
                    });
                });

                // Khi submit form
                const form = document.getElementById("bookingForm");
                form.addEventListener("submit", function (e) {
                    const checked = document.querySelector("input[name='selectedSlot']:checked");
                    if (checked) {
                        const [dateStr, startTime] = checked.value.split(",");
                        const slotDateTime = new Date(dateStr + "T" + startTime);
                        if (slotDateTime < now) {
                            e.preventDefault();
                            Swal.fire({
                                icon: "error",
                                title: "Khung giờ đã qua",
                                text: "Vui lòng chọn khung giờ trong tương lai.",
                                confirmButtonColor: "#3085d6"
                            });
                        }
                    }
                });
            });
        </script>


    </body>
</html>
