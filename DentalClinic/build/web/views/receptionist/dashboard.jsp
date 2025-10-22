<%-- Document : newjsp Created on : 22 thg 10, 2025, 11:31:12 Author : vuhieu --%>

    <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
        <%@page contentType="text/html" pageEncoding="UTF-8" %>
            <!DOCTYPE html>
            <html>

            <head>
                <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
                <title>Receptionist Dashboard</title>
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
                </style>
            </head>

            <body>
                <%-- Hiển thị thông báo từ session (nếu có) --%>
                    <c:if test="${not empty sessionScope.successMessage}">
                        <p style="color: green;">${sessionScope.successMessage}</p>
                        <%-- Xóa message sau khi hiển thị --%>
                            <c:remove var="successMessage" scope="session" />
                    </c:if>
                    <c:if test="${not empty sessionScope.errorMessage}">
                        <p style="color: red;">${sessionScope.errorMessage}</p>
                        <%-- Xóa message sau khi hiển thị --%>
                            <c:remove var="errorMessage" scope="session" />
                    </c:if>

                    <h1>Receptionist Dashboard</h1>


                    <form action="${pageContext.request.contextPath}/receptionist-searchandcheckin" method="POST">
                        <c:if test="${not empty error}">
                            <p style="color: red">${error}</p>
                        </c:if>
                        <table>
                            <tr>
                                <td>Tìm cuộc hẹn theo SĐT:</td>
                                <td><input type="text" name="phoneNumber" value="${phoneNumber}"
                                        placeholder="Nhập số điện thoại bệnh nhân" /></td>
                                <td><input type="submit" value="Tìm kiếm" /></td>
                            </tr>
                        </table>
                    </form>

                    <div style="margin-top: 20px;">
                        <a href="${pageContext.request.contextPath}/receptionist/daily-appointments" class="action-btn"
                            style="background-color: #007bff;">Xem tất cả lịch hẹn trong ngày</a>
                    </div>

                    <hr>

                    <%-- Hiển thị danh sách bệnh nhân đã check-in trong ngày --%>
                        <h2>Danh sách bệnh nhân đã Check-in hôm nay</h2>
                        <c:if test="${not empty checkedInList}">
                            <table>
                                <thead>
                                    <tr>
                                        <th>Giờ hẹn</th>
                                        <th>Bệnh nhân</th>
                                        <th>Số điện thoại</th>
                                        <th>Bác sĩ</th>
                                        <th>Dịch vụ</th>
                                        <th>Trạng thái</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="app" items="${checkedInList}">
                                        <tr>
                                            <td>${app.startTime}</td>
                                            <td>${app.patientName}</td>
                                            <td>${app.patientPhone}</td>
                                            <td>${app.doctorName}</td>
                                            <td>${app.serviceName}</td>
                                            <td><span style="color: green; font-weight: bold;">${app.status}</span></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                        <c:if test="${empty checkedInList}">
                            <p>Chưa có bệnh nhân nào check-in trong ngày hôm nay.</p>
                        </c:if>

            </body>

            </html>