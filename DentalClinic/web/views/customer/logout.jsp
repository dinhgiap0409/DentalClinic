<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Users"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng xuất - Dental Clinic</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #f5f5f5; }
        .container { max-width: 300px; margin: 100px auto; background: white; padding: 20px; border: 1px solid #ddd; text-align: center; }
        .btn { background: #007bff; color: white; padding: 10px; text-decoration: none; margin: 5px; display: inline-block; }
        .btn-secondary { background: #6c757d; }
    </style>
</head>
<body>
    <!-- Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>

    <div class="container">
        <h2>Đăng xuất thành công!</h2>
        <p>Cảm ơn bạn đã sử dụng dịch vụ</p>
        
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
