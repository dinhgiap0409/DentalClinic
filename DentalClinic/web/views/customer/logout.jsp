<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Users"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng xuất - Dental Clinic</title>
</head>
<body>
    <!-- Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>

    <div>
        <h2>Logout successful!</h2>
        <p>Thank for using service</p>
        
        <div style="margin-top: 20px;">
            <a href="/DentalClinic/user?action=login" class="btn">Đăng nhập lại</a>
            <a href="/DentalClinic/views/guest/home.jsp" class="btn btn-secondary">Về trang chủ</a>
        </div>
        
        <p style="margin-top: 15px; font-size: 14px; color: #666;">
            Tự động chuyển về trang chủ sau <span id="countdown">5</span> giây...
        </p>
    </div>

    <script>
        let countdown = 5;
        const countdownSpan = document.getElementById('countdown');
        const interval = setInterval(function() {
            countdown--;
            countdownSpan.textContent = countdown;
            if (countdown <= 0) {
                clearInterval(interval);
                window.location.href = '/DentalClinic/views/guest/home.jsp';
            }
        }, 1000);
    </script>

    <!-- Footer -->
    <jsp:include page="../../common/footer.jsp"></jsp:include>
</body>
</html>
