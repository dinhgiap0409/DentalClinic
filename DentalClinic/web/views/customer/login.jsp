<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
</head>
<body class="auth-body">
    <!-- Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>

    <main class="auth-container">
        <section class="auth-card">
            <div class="auth-card__illustration">
                <div class="auth-card__illustration-content">
                    <h1>Chào mừng trở lại!</h1>
                    <p>Quản lý lịch hẹn, hồ sơ bệnh nhân và dịch vụ của phòng khám chỉ bằng một lần đăng nhập.</p>
                </div>
                <div class="auth-highlights">
                    <div class="auth-highlight">
                        <span>1</span>
                        Theo dõi lịch khám theo thời gian thực
                    </div>
                    <div class="auth-highlight">
                        <span>2</span>
                        Quản lý hồ sơ và thông tin bệnh nhân
                    </div>
                    <div class="auth-highlight">
                        <span>3</span>
                        Nhận thông báo nhanh cho các thay đổi
                    </div>
                </div>
            </div>

            <div class="auth-card__form">
                <div class="auth-card__form-header">
                    <h2>Đăng nhập</h2>
                    <p>Nhập thông tin tài khoản của bạn để tiếp tục.</p>
                </div>

                <% if (request.getAttribute("error") != null) { %>
                    <div class="auth-alert auth-alert--error">
                        <%= request.getAttribute("error") %>
                    </div>
                <% } %>

                <form class="auth-form" action="${pageContext.request.contextPath}/login" method="POST">
                    <div class="auth-form__group">
                        <label class="auth-form__label" for="email">Email</label>
                        <input class="auth-form__input" type="email" id="email" name="email" placeholder="name@example.com" required>
                    </div>

                    <div class="auth-form__group">
                        <label class="auth-form__label" for="password">Mật khẩu</label>
                        <input class="auth-form__input" type="password" id="password" name="password" placeholder="Nhập mật khẩu" required>
                    </div>

                    <div class="auth-actions">
                        <a class="auth-link" href="${pageContext.request.contextPath}/register">Đăng ký tài khoản mới</a>
                    </div>

                    <button type="submit" class="auth-submit">
                        Đăng nhập
                    </button>
                </form>

                <p class="auth-footer-text">
                    Cần hỗ trợ? <a href="mailto:contact@dentalclinic.com">Liên hệ với chúng tôi</a>
                </p>
            </div>
        </section>
    </main>
</body>
</html>
