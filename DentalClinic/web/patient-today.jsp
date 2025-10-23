<%-- 
    Document   : patient-today
    Created on : Oct 23, 2025, 11:47:36 AM
    Author     : ADMIN
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h2>Danh sách bệnh nhân hôm nay</h2>
        <table border="1">
            <tr><th>Tên</th><th>Giờ hẹn</th><th>Thao tác</th></tr>
            <c:forEach var="p" items="${patients}">
                <tr>
                    <td>${p.patientName}</td>
                    <td>${p.appointmentTime}</td>
                    <td>
                        <a href="doctor?action=record&patientId=${p.patientId}">Xem hồ sơ</a>
                    </td>
                </tr>
            </c:forEach>
        </table>

    </body>
</html>
