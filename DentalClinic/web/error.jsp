<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <head>
        <title>Lỗi hệ thống</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f9f9f9;
                color: #333;
                text-align: center;
                padding-top: 80px;
            }
            .error-container {
                background-color: #fff;
                border: 1px solid #ddd;
                display: inline-block;
                padding: 40px 60px;
                border-radius: 12px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.1);
            }
            h1 {
                color: #d9534f;
                font-size: 28px;
            }
            p {
                font-size: 16px;
                margin: 10px 0 25px 0;
            }
            button {
                background-color: #0275d8;
                color: #fff;
                border: none;
                padding: 10px 18px;
                font-size: 15px;
                border-radius: 6px;
                cursor: pointer;
            }
            button:hover {
                background-color: #025aa5;
            }
            .details {
                color: #777;
                font-size: 14px;
                margin-top: 20px;
                text-align: left;
                max-width: 400px;
                margin-left: auto;
                margin-right: auto;
            }
        </style>
    </head>
    <body>
        <div class="error-container">
            <h1>⚠️ Đã xảy ra lỗi!</h1>
            <p><strong><c:out value="${errorMessage}" default="Đã xảy ra lỗi trong quá trình xử lý yêu cầu." /></strong></p>

            <button onclick="window.history.back()">⬅️ Quay lại</button>

            <div class="details">
                <c:if test="${not empty exception}">
                    <p><strong>Chi tiết lỗi:</strong></p>
                    <pre>${exception.message}</pre>
                </c:if>
            </div>
        </div>
    </body>
</html>
