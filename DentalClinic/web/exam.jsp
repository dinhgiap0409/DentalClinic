<%-- 
    Document   : exam
    Created on : Oct 23, 2025, 11:48:56 AM
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
        <form method="post" action="doctor">
            <input type="hidden" name="action" value="saveExam"/>
            <input type="hidden" name="patientId" value="${patientId}"/>
            <textarea name="result" rows="6" cols="60" placeholder="Nhập kết quả khám..."></textarea><br>
            <button type="submit">Lưu</button>
        </form>

    </body>
</html>
