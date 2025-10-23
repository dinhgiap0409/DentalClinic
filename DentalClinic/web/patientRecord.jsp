<%-- 
    Document   : patientRecord
    Created on : Oct 23, 2025, 11:48:04 AM
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
        <h2>Hồ sơ bệnh án của ${record.patientName}</h2>
        <p>Tuổi: ${record.age}</p>
        <p>Tiền sử bệnh: ${record.history}</p>
        <p>Ghi chú: ${record.note}</p>
        <a href="doctor?action=exam&patientId=${record.patientId}">Ghi kết quả</a> |
        <a href="doctor?action=prescribe&patientId=${record.patientId}">Kê dịch vụ</a>

    </body>
</html>
