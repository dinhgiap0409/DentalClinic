<%-- Document : search Created on : Oct 23, 2025, 10:00:00 AM Author : vuhieu --%>

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Kết quả tìm kiếm lịch hẹn</title>
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

                    .action-btn {
                        padding: 5px 10px;
                        border: none;
                        border-radius: 4px;
                        color: white;
                        cursor: pointer;
                        text-decoration: none;
                        display: inline-block;
                        margin-right: 5px;
                    }

                    .btn-confirm {
                        background-color: #4CAF50;
                        /* Green */
                    }

                    .btn-cancel {
                        background-color: #f44336;
                        /* Red */
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
                <h1>Kết quả tìm kiếm lịch hẹn</h1>

                <%-- Hiển thị thông báo từ session (nếu có) --%>
                    <c:if test="${not empty sessionScope.successMessage}">
                        <p style="color: green;">${sessionScope.successMessage}</p>
                        <c:remove var="successMessage" scope="session" />
                    </c:if>
                    <c:if test="${not empty sessionScope.errorMessage}">
                        <p style="color: red;">${sessionScope.errorMessage}</p>
                        <c:remove var="errorMessage" scope="session" />
                    </c:if>

                    <%-- Hiển thị lỗi validation nếu có --%>
                        <c:if test="${not empty error}">
                            <p style="color: red">Lỗi: ${error}</p>
                        </c:if>

                        <%-- Phần hiển thị kết quả tìm kiếm --%>
                            <c:if test="${not empty appointmentList}">
                                <h2>Kết quả tìm kiếm cho SĐT: ${phoneNumber}</h2>
                                <table>
                                    <thead>
                                        <tr>
                                            <th>Mã Lịch hẹn</th>
                                            <th>Bệnh nhân</th>
                                            <th>Ngày hẹn</th>
                                            <th>Giờ hẹn</th>
                                            <th>Bác sĩ</th>
                                            <th>Dịch vụ</th>
                                            <th>Trạng thái</th>
                                            <th>Hành động</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="app" items="${appointmentList}">
                                            <tr>
                                                <td>${app.appointmentId}</td>
                                                <td>${app.patientName}</td>
                                                <td>${app.appointmentDate}</td>
                                                <td>${app.startTime}</td>
                                                <td>${app.doctorName}</td>
                                                <td>${app.serviceName}</td>
                                                <td>${app.status}</td>
                                                <td>
                                                    <%-- Chỉ hiển thị nút khi trạng thái là 'Scheduled' --%>
                                                        <c:if test="${app.status == 'Scheduled'}">
                                                            <%-- Lấy timestamp của ngày hẹn và ngày hiện tại để so sánh
                                                                --%>
                                                                <c:set var="appointmentTime"
                                                                    value="${app.appointmentDate.time}" />
                                                                <c:set var="currentTime" value="${currentDate.time}" />

                                                                <%-- Nếu lịch hẹn là ngày hôm nay --%>
                                                                    <c:if test="${appointmentTime == currentTime}">
                                                                        <a href="${pageContext.request.contextPath}/update-appointment-status?id=${app.appointmentId}&status=Confirmed&phone=${phoneNumber}"
                                                                            class="action-btn btn-confirm">Check-in</a>
                                                                        <a href="${pageContext.request.contextPath}/update-appointment-status?id=${app.appointmentId}&status=Cancelled&phone=${phoneNumber}"
                                                                            class="action-btn btn-cancel">Hủy lịch</a>
                                                                    </c:if>

                                                                    <%-- Nếu lịch hẹn là ở tương lai --%>
                                                                        <c:if test="${appointmentTime > currentTime}">
                                                                            <a href="${pageContext.request.contextPath}/update-appointment-status?id=${app.appointmentId}&status=Cancelled&phone=${phoneNumber}"
                                                                                class="action-btn btn-cancel">Hủy
                                                                                lịch</a>
                                                                        </c:if>
                                                        </c:if>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </c:if>

                            <c:if test="${empty appointmentList && not empty phoneNumber && empty error}">
                                <p>Không tìm thấy lịch hẹn nào cho số điện thoại: ${phoneNumber}</p>
                            </c:if>

                            <br>
                            <a href="${pageContext.request.contextPath}/views/receptionist/dashboard.jsp"
                                class="back-link">Quay lại Dashboard</a>

            </body>

            </html>