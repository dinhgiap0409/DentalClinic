<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Lịch làm việc bác sĩ - Dental Clinic</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/doctor-schedule.css">
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../../common/header.jsp"></jsp:include>

            <div class="container">

                <!-- 🔙 Nút quay lại -->
                <div style="margin: 16px 0;">
                    <a href="${pageContext.request.contextPath}/appropriateSpecialist?serviceId=${serviceId}" 
                   class="btn" 
                   style="background-color:#4CAF50; color:white; padding:8px 16px; border-radius:6px; text-decoration:none;">
                    ⬅ Quay lại danh sách bác sĩ
                </a>
            </div>

            <div class="page-header">
                <h1>Lịch làm việc của bác sĩ</h1>
                <p>
                    Tuần: <strong>${monday}</strong> – <strong>${sunday}</strong>
                </p>

                <div class="week-nav" style="display:flex; gap:8px; margin:8px 0;">
                    <a class="btn"
                       href="${pageContext.request.contextPath}/doctorWorkSchedule?doctorId=${doctorId}&serviceId=${serviceId}&weekOffset=0">
                        Tuần này
                    </a>
                    <a class="btn"
                       href="${pageContext.request.contextPath}/doctorWorkSchedule?doctorId=${doctorId}&serviceId=${serviceId}&weekOffset=${weekOffset + 1}">
                        Tuần sau ⟶
                    </a>
                </div>
            </div>

            <hr/>

            <!-- Nếu toàn tuần trống -->
            <c:if test="${empty availableByDate}">
                <p style="text-align:center; color:gray;">Chưa có dữ liệu lịch làm việc trong tuần.</p>
            </c:if>

            <!-- Lưới các ngày trong tuần -->
            <div class="schedule-week" style="display:grid; grid-template-columns: repeat(1, minmax(0, 1fr)); gap:16px;">
                <c:forEach var="entry" items="${availableByDate}">
                    <c:set var="date" value="${entry.key}" />
                    <c:set var="slots" value="${entry.value}" />

                    <div class="day-card" style="border:1px solid #eee; border-radius:12px; padding:16px;">
                        <div class="day-header" style="display:flex; justify-content:space-between; align-items:center;">
                            <h3 style="margin:0;">${date}</h3>
                            <span class="slot-count" style="color:#666;">
                                <c:choose>
                                    <c:when test="${empty slots}">Không có khung giờ trống</c:when>
                                    <c:otherwise>${fn:length(slots)} khung giờ</c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <div class="slot-list" style="margin-top:12px;">
                            <c:choose>
                                <c:when test="${empty slots}">
                                    <p style="color:gray; margin:8px 0 0;">—</p>
                                </c:when>
                                <c:otherwise>
                                    <ul style="list-style:none; padding:0; margin:0; display:flex; flex-wrap:wrap; gap:8px;">
                                        <c:forEach var="s" items="${slots}">
                                            <li class="slot-item"
                                                style="display:flex; align-items:center; gap:8px; border:1px solid #ddd; border-radius:10px; padding:8px 12px;">
                                                <span class="time" style="font-weight:600;">
                                                    ${s.startTime} - ${s.endTime}
                                                </span>
                                                <a class="btn slot-btn"
                                                   data-date="${date}"
                                                   data-start="${s.startTime}"
                                                   href="${pageContext.request.contextPath}/appointmentCreate?doctorId=${doctorId}&serviceId=${serviceId}&date=${date}&start=${s.startTime}&end=${s.endTime}">
                                                    Chọn khung giờ này
                                                </a>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>

        </div>

        <!-- Footer -->
        <jsp:include page="../../common/footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
        <script>
            document.addEventListener("DOMContentLoaded", function () {
                const now = new Date();

                // Lặp qua tất cả nút chọn khung giờ
                document.querySelectorAll(".slot-btn").forEach(function (btn) {
                    btn.addEventListener("click", function (e) {
                        const dateStr = btn.getAttribute("data-date");   // "2025-11-04"
                        const startTime = btn.getAttribute("data-start"); // "08:02:00"

                        // Ghép lại thành ISO datetime
                        const slotDateTime = new Date(dateStr + "T" + startTime);

                        if (slotDateTime < now) {
                            e.preventDefault(); // Ngăn chuyển trang
                            Swal.fire({
                                icon: "warning",
                                title: "Không thể chọn khung giờ này",
                                text: "Khung giờ bạn chọn đã qua. Vui lòng chọn khung giờ khác.",
                                confirmButtonColor: "#3085d6",
                                confirmButtonText: "Đã hiểu"
                            });
                        }
                    });
                });
            });
        </script>

    </body>
</html>
