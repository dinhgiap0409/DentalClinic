<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Register - Dental Clinic</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/auth.css">
    </head>
    <body class="auth-body">
        <jsp:include page="../../common/header.jsp"></jsp:include>

        <main class="auth-container">
            <section class="auth-card">
                <div class="auth-card__illustration">
                    <div class="auth-card__illustration-content">
                        <h1>Trở thành hội viên Dental Clinic</h1>
                        <p>Đặt lịch nhanh chóng, theo dõi quá trình điều trị và nhận ưu đãi dành riêng cho bạn.</p>
                    </div>
                    <div class="auth-highlights">
                        <div class="auth-highlight">
                            <span>1</span>
                            Chủ động đặt lịch khám và theo dõi lịch sử điều trị
                        </div>
                        <div class="auth-highlight">
                            <span>2</span>
                            Nhận nhắc lịch tự động qua email và điện thoại
                        </div>
                        <div class="auth-highlight">
                            <span>3</span>
                            Tận hưởng quyền lợi thành viên và ưu đãi đặc biệt
                        </div>
                    </div>
                </div>

                <div class="auth-card__form">
                    <%
                        String usernameVal = request.getParameter("username") != null ? request.getParameter("username") : "";
                        String emailVal = request.getParameter("email") != null ? request.getParameter("email") : "";
                        String fullNameVal = request.getParameter("fullname") != null ? request.getParameter("fullname") : "";
                        String phoneVal = request.getParameter("phone") != null ? request.getParameter("phone") : "";
                        String genderVal = request.getParameter("gender") != null ? request.getParameter("gender") : "";
                        String addressVal = request.getParameter("address") != null ? request.getParameter("address") : "";
                    %>

                    <div class="auth-card__form-header">
                        <h2>Tạo tài khoản mới</h2>
                        <p>Chỉ mất vài bước để bắt đầu sử dụng các dịch vụ của Dental Clinic.</p>
                    </div>

                    <% if (request.getAttribute("error") != null) { %>
                        <div class="auth-alert auth-alert--error">
                            <%= request.getAttribute("error") %>
                        </div>
                    <% } %>

                    <% if (request.getAttribute("success") != null) { %>
                        <div class="auth-alert auth-alert--success">
                            <%= request.getAttribute("success") %>
                        </div>
                    <% } %>

                    <form class="auth-form auth-form--two-column" action="${pageContext.request.contextPath}/register" method="POST">
                        <input type="hidden" name="action" value="register">

                        <div class="auth-form__group">
                            <label class="auth-form__label" for="username">Tên đăng nhập</label>
                            <input class="auth-form__input" type="text" id="username" name="username" value="<%= usernameVal %>" placeholder="Tên đăng nhập của bạn" required>
                        </div>

                        <div class="auth-form__group">
                            <label class="auth-form__label" for="email">Email</label>
                            <input class="auth-form__input" type="email" id="email" name="email" value="<%= emailVal %>" placeholder="name@example.com" required>
                        </div>

                        <div class="auth-form__group">
                            <label class="auth-form__label" for="password">Mật khẩu</label>
                            <input class="auth-form__input" type="password" id="password" name="password" placeholder="Tạo mật khẩu" required minlength="6">
                        </div>

                        <div class="auth-form__group">
                            <label class="auth-form__label" for="fullname">Họ và tên</label>
                            <input class="auth-form__input" type="text" id="fullname" name="fullname" value="<%= fullNameVal %>" placeholder="Nguyễn Văn A" required>
                        </div>

                        <div class="auth-form__group">
                            <label class="auth-form__label" for="phone">Số điện thoại</label>
                            <input class="auth-form__input" type="tel" id="phone" name="phone" value="<%= phoneVal %>" placeholder="0123 456 789" required>
                        </div>

                        <div class="auth-form__group">
                            <label class="auth-form__label" for="gender">Giới tính</label>
                            <select class="auth-form__select" id="gender" name="gender" required>
                                <option value="" <%= genderVal.isEmpty() ? "selected" : "" %>>Chọn giới tính</option>
                                <option value="Nam" <%= "Nam".equalsIgnoreCase(genderVal) ? "selected" : "" %>>Nam</option>
                                <option value="Nữ" <%= "Nữ".equalsIgnoreCase(genderVal) ? "selected" : "" %>>Nữ</option>
                                <option value="Khác" <%= "Khác".equalsIgnoreCase(genderVal) ? "selected" : "" %>>Khác</option>
                            </select>
                        </div>

                        <div class="auth-form__group auth-form__group--full">
                            <label class="auth-form__label" for="address">Địa chỉ (không bắt buộc)</label>
                            <textarea class="auth-form__textarea" id="address" name="address" placeholder="Số nhà, đường, quận/huyện, tỉnh/thành phố"><%= addressVal %></textarea>
                        </div>

                        <button type="submit" class="auth-submit auth-submit--full">
                            Đăng ký tài khoản
                        </button>
                    </form>

                    <p class="auth-footer-text">
                        Đã có tài khoản? <a href="${pageContext.request.contextPath}/login">Đăng nhập ngay</a>
                    </p>
                </div>
            </section>
        </main>
    </body>
</html>
