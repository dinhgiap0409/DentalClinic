<%-- 
    Document   : prescribe
    Created on : Oct 23, 2025, 11:49:23 AM
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
            <input type="hidden" name="action" value="savePrescription"/>
            <input type="hidden" name="patientId" value="${patientId}"/>
            <c:forEach var="s" items="${services}">
                <input type="checkbox" name="serviceIds" value="${s.id}"/> ${s.name}<br/>
            </c:forEach>
            <button type="submit">Lưu</button>
        </form>

    </body>
</html>
