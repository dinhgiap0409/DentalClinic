<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thay đổi lịch hẹn - Dental Clinic</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- CSS chung (tùy dự án của bạn) -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/doctor-schedule.css">

        <style>
            .container {
                max-width:1100px;
                margin:24px auto;
                padding:0 16px;
            }
            .page-header h1 {
                margin:0 0 6px;
            }
            .info-card {
                background:#fff;
                border:1px solid #eee;
                border-radius:12px;
                padding:16px;
                margin:12px 0;
                box-shadow:0 2px 10px rgba(0,0,0,.05);
            }
            .row {
                display:flex;
                gap:16px;
                flex-wrap:wrap
            }
            .col {
                flex:1 1 260px
            }
            .label {
                color:#666;
                font-size:13px;
                display:block
            }
            .value {
                font-weight:600
            }

            .week-nav{
                display:flex;
                gap:8px;
                margin:12px 0
            }
            .btn{
                display:inline-block;
                padding:10px 14px;
                border-radius:8px;
                text-decoration:none;
                font-weight:600;
                border:2px solid transparent
            }
            .btn-primary{
                background:#0d6efd;
                color:#fff;
                border-color:#0d6efd
            }
            .btn-ghost{
                background:#fff;
                color:#0d6efd;
                border-color:#0d6efd
            }
            .btn-ghost:hover{
                background:#e9f2ff
            }

            .schedule-week{
                display:grid;
                grid-template-columns:repeat(1,minmax(0,1fr));
                gap:16px
            }
            .day-card{
                border:1px solid #eee;
                border-radius:12px;
                padding:16px;
                background:#fff
            }
            .day-header{
                display:flex;
                justify-content:space-between;
                align-items:center
            }
            .slot-list ul{
                list-style:none;
                padding:0;
                margin:0;
                display:flex;
                flex-wrap:wrap;
                gap:8px
            }
            .slot-item{
                display:flex;
                align-items:center;
                gap:10px;
                border:1px solid #ddd;
                border-radius:10px;
                padding:8px 12px
            }
            .slot-time{
                font-weight:700
            }
            .slot-pick{
                margin-left:auto
            }
            .back-row{
                display:flex;
                justify-content:space-between;
                align-items:center;
                margin:12px 0
            }
            .muted{
                color:#666
            }
        </style>
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../../common/header.jsp"></jsp:include>

            <div class="container">

                <div class="back-row">
                    <a class="btn btn-ghost" href="${pageContext.request.contextPath}/medicalHistory">⬅ Về lịch sử khám</a>
                <div class="muted">Tuần: <strong>${monday}</strong> – <strong>${sunday}</strong></div>
            </div>

            <div class="page-header">
                <h1>Thay đổi lịch hẹn</h1>
                <div class="week-nav">
                    <a class="btn btn-ghost"
                       href="${pageContext.request.contextPath}/loadRescheduleAppointment?appointmentId=${appointment.appointmentId}&weekOffset=0">
                        Tuần hiện tại
                    </a>
                    <a class="btn btn-ghost"
                       href="${pageContext.request.contextPath}/loadRescheduleAppointment?appointmentId=${appointment.appointmentId}&weekOffset=${weekOffset + 1}">
                        Tuần sau ⟶
                    </a>
                </div>
            </div>

            <!-- Thông tin lịch hẹn hiện tại -->
            <div class="info-card">
                <div class="row">
                    <div class="col">
                        <span class="label">Mã lịch hẹn</span>
                        <div class="value">#${appointment.appointmentId}</div>
                    </div>
                    <div class="col">
                        <span class="label">Bác sĩ</span>
                        <div class="value">
                            <c:choose>
                                <c:when test="${not empty appointment.doctorId and not empty appointment.doctorId.userId}">
                                    ${appointment.doctorId.userId.fullName}
                                </c:when>
                                <c:otherwise>
                                    Bác sĩ #${appointment.doctorId.doctorID}
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                    <div class="col">
                        <span class="label">Ngày hiện tại</span>
                        <div class="value"><fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy"/></div>
                    </div>
                    <div class="col">
                        <span class="label">Giờ hiện tại</span>
                        <div class="value">${appointment.startTime} – ${appointment.endTime}</div>
                    </div>
                    <div class="col">
                        <span class="label">Trạng thái</span>
                        <div class="value">${appointment.status}</div>
                    </div>
                </div>
            </div>

            <hr/>

            <!-- Nếu không có dữ liệu -->
            <c:if test="${empty availableByDate}">
                <p class="muted">Chưa có dữ liệu lịch làm việc trong tuần này.</p>
            </c:if>

            <!-- Lưới ngày trong tuần -->
            <div class="schedule-week">
                <c:forEach var="entry" items="${availableByDate}">
                    <c:set var="date"  value="${entry.key}" />
                    <c:set var="slots" value="${entry.value}" />

                    <div class="day-card">
                        <div class="day-header">
                            <h3 style="margin:0;">${date}</h3>
                            <span class="muted">
                                <c:choose>
                                    <c:when test="${empty slots}">Không có khung giờ trống</c:when>
                                    <c:otherwise>${slots.size()} khung giờ</c:otherwise>
                                </c:choose>
                            </span>
                        </div>

                        <div class="slot-list" style="margin-top:12px;">
                            <c:choose>
                                <c:when test="${empty slots}">
                                    <p class="muted" style="margin:8px 0 0;">—</p>
                                </c:when>
                                <c:otherwise>
                                    <ul>
                                        <c:forEach var="s" items="${slots}">
                                            <li class="slot-item">
                                                <span class="slot-time">${s.startTime} - ${s.endTime}</span>
                                                <button type="button"
                                                        class="btn btn-primary slot-pick"
                                                        data-date="${date}"
                                                        data-start="${s.startTime}"
                                                        data-end="${s.endTime}">
                                                    Chọn khung giờ này
                                                </button>
                                            </li>
                                        </c:forEach>
                                    </ul>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Form submit reschedule (ẩn) -->
            <form id="rescheduleForm" method="post" action="${pageContext.request.contextPath}/cancelOrChangeAppointment" style="display:none;">
                <input type="hidden" name="action"        value="reschedule"/>
                <input type="hidden" name="appointmentId" value="${appointment.appointmentId}"/>
                <input type="hidden" name="newDate"       id="f-newDate"/>
                <input type="hidden" name="newStart"      id="f-newStart"/>
                <input type="hidden" name="newEnd"        id="f-newEnd"/>
            </form>

        </div>

        <!-- Footer -->
        <jsp:include page="../../common/footer.jsp"></jsp:include>

        <script>
            // Bắt sự kiện chọn khung giờ -> fill form ẩn -> submit
            document.addEventListener('DOMContentLoaded', function () {
                var buttons = document.querySelectorAll('.slot-pick');
                var fDate = document.getElementById('f-newDate');
                var fStart = document.getElementById('f-newStart');
                var fEnd = document.getElementById('f-newEnd');
                var form = document.getElementById('rescheduleForm');

                buttons.forEach(function (btn) {
                    btn.addEventListener('click', function () {
                        var date = this.getAttribute('data-date');   // yyyy-MM-dd (LocalDate toString)
                        var start = this.getAttribute('data-start');  // HH:mm:ss (java.sql.Time toString)
                        var end = this.getAttribute('data-end');    // HH:mm:ss

                        // Nếu backend muốn HH:mm thì cắt bớt giây:
                        // start = start.substring(0,5);
                        // end   = end.substring(0,5);

                        fDate.value = date;
                        fStart.value = start;
                        fEnd.value = end;

                        form.submit();
                    });
                });
            });
        </script>

    </body>
</html>
