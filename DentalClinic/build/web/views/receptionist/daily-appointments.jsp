<%-- Document : daily-appointments Created on : Oct 23, 2025, 10:30:00 AM Author : vuhieu --%>

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
            <%@page contentType="text/html" pageEncoding="UTF-8" %>
                <!DOCTYPE html>
                <html>

                <head>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                    <title>Lịch hẹn trong ngày</title>
                    <style>
                        body {
                            font-family: sans-serif;
                        }

                        table {
                            width: 100%;
                            border-collapse: collapse;
                            margin-top: 20px;
                        }

                        th,
                        td {
                            border: 1px solid #ddd;
                            padding: 8px;
                            text-align: left;
                        }

                        th {
                            background-color: #f2f2f2;
                        }

                        tr:nth-child(even) {
                            background-color: #f9f9f9;
                        }

                        .status-scheduled {
                            color: #007bff;
                            font-weight: bold;
                        }

                        .status-confirmed {
                            color: #28a745;
                            font-weight: bold;
                        }

                        .status-cancelled {
                            color: #dc3545;
                            font-weight: bold;
                        }

                        .status-completed {
                            color: #6c757d;
                            font-weight: bold;
                        }

                        .back-link {
                            display: inline-block;
                            margin-top: 20px;
                            padding: 10px 15px;
                            background-color: #555;
                            color: white;
                            text-decoration: none;
                            border-radius: 4px;
                        }
                    </style>
                </head>

                <body>
                    <h1>
                        Danh sách lịch hẹn ngày:
                        <fmt:setLocale value="vi_VN" />
                        <fmt:formatDate value="${viewDate}" pattern="dd/MM/yyyy" />
                    </h1>

                    <c:if test="${not empty dailyAppointmentsList}">
                        <table>
                            <thead>
                                <tr>
                                    <th>Giờ hẹn</th>
                                    <th>Bệnh nhân</th>
                                    <th>Số điện thoại</th>
                                    <th>Bác sĩ</th>
                                    <th>Dịch vụ</th>
                                    <th>Trạng thái</th>
                                    <th>Hành động</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="app" items="${dailyAppointmentsList}">
                                    <tr>
                                        <td>
                                            <fmt:formatDate value="${app.startTime}" pattern="HH:mm" />
                                        </td>
                                        <td>${app.patientName}</td>
                                        <td>${app.patientPhone}</td>
                                        <td>${app.doctorName}</td>
                                        <td>${app.serviceName}</td>
                                        <td>
                                            <span class="status-${app.status.toLowerCase()}">${app.status}</span>
                                        </td>
                                        <td>
                                            <%-- Chỉ hiển thị nút khi trạng thái là 'Scheduled' --%>
                                                <c:if test="${app.status == 'Scheduled'}">
                                                    <%-- Lấy timestamp của ngày hẹn và ngày hiện tại để so sánh --%>
                                                        <c:set var="appointmentTime"
                                                            value="${app.appointmentDate.time}" />
                                                        <c:set var="currentTime" value="${currentDate.time}" />

                                                        <%-- Nếu lịch hẹn là ngày hôm nay --%>
                                                            <c:if test="${appointmentTime == currentTime}">
                                                                <a href="${pageContext.request.contextPath}/update-appointment-status?id=${app.appointmentId}&status=Confirmed&source=daily"
                                                                    class="action-btn btn-confirm">Check-in</a>
                                                                <a href="${pageContext.request.contextPath}/update-appointment-status?id=${app.appointmentId}&status=Cancelled&source=daily"
                                                                    class="action-btn btn-cancel">Hủy lịch</a>
                                                            </c:if>

                                                            <%-- Nếu lịch hẹn là ở tương lai (logic này hữu ích nếu bạn
                                                                mở rộng chức năng xem ngày khác) --%>
                                                                <c:if test="${appointmentTime > currentTime}">
                                                                    <a href="${pageContext.request.contextPath}/update-appointment-status?id=${app.appointmentId}&status=Cancelled&source=daily"
                                                                        class="action-btn btn-cancel">Hủy lịch</a>
                                                                </c:if>
                                                </c:if>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </c:if>

                    <c:if test="${empty dailyAppointmentsList}">
                        <p>Không có lịch hẹn nào trong ngày hôm nay.</p>
                    </c:if>

                    <br>
                    <a href="${pageContext.request.contextPath}/receptionist/dashboard" class="back-link">Quay lại
                        Dashboard</a>

                </body>

                </html>