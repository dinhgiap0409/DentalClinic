<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Đặt lịch thành công - Dental Clinic</title>
        <link rel="stylesheet" href="/DentalClinic/css/home.css">
        <link rel="stylesheet" href="/DentalClinic/css/success.css">
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="common/header.jsp"></jsp:include>

            <div class="success-container">
                <div class="success-box">
                    <div class="success-icon">✅</div>
                    <h1>Đặt lịch thành công!</h1>

                    <p class="success-message">
                        Cảm ơn bạn đã đặt lịch khám tại <strong>Dental Clinic</strong>.  
                        Lịch hẹn của bạn đã được ghi nhận thành công.
                    </p>

                    <div class="appointment-info">
                        <p><strong>Mã lịch hẹn:</strong> #${appointmentId}</p>
                    <p>Chúng tôi sẽ gửi thông báo khi đến thời gian khám hoặc có thay đổi.</p>
                </div>

                <div class="success-actions">
                    <a href="/DentalClinic/home" class="btn-primary">Về trang chủ</a>
                    <a href="/DentalClinic/medicalHistory" class="btn-secondary">Xem lịch hẹn của tôi</a>
                </div>
            </div>
        </div>

        <!-- Footer -->
        <jsp:include page="common/footer.jsp"></jsp:include>

    </body>
</html>
