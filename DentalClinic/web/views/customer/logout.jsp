<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="model.Users"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng xuất - Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body class="auth-body">
    <!-- Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>

    <main class="auth-container">
        <section class="auth-card" style="max-width: 680px; grid-template-columns: 1fr;">
            <div class="logout-content">
                <h1 class="logout-title">Bạn đã đăng xuất thành công</h1>
                <p class="logout-message">Cảm ơn bạn đã sử dụng dịch vụ của Dental Clinic. Hẹn gặp lại bạn vào lần tới!</p>

                <div class="logout-actions">
                    <a class="button button--primary" href="${pageContext.request.contextPath}/login">Đăng nhập lại</a>
                    <a class="button button--ghost" href="${pageContext.request.contextPath}/home">Về trang chủ</a>
                </div>

                <p class="logout-countdown">
                    Tự động chuyển về trang chủ sau <span id="logoutCountdown">5</span> giây.
                </p>
            </div>
        </section>
    </main>

    <script>
        (function () {
            let countdown = 5;
            const countdownSpan = document.getElementById('logoutCountdown');

            const interval = setInterval(function () {
                countdown--;
                countdownSpan.textContent = countdown;
                if (countdown <= 0) {
                    clearInterval(interval);
                    window.location.href = '${pageContext.request.contextPath}/home';
                }
            }, 1000);
        })();
    </script>
</body>
</html>
