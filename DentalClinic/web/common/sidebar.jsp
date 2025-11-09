<%-- 
    Document   : sidebar
    Created on : Nov 1, 2025, 7:56:01 PM
    Author     : TNO
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<div class="sidebar">
    <h2>Quản lý</h2>
    <a href="${pageContext.request.contextPath}/lookUpAppointments" class="active">Tra cứu lịch hẹn</a>
    <a href="${pageContext.request.contextPath}/patientsToday">Bệnh nhân hôm nay</a>
    <a href="${pageContext.request.contextPath}/bookAppointmentsDirectly">Đặt lịch trực tiếp</a>
    <a href="${pageContext.request.contextPath}/manageSchedule">Quản lý lịch bác sĩ</a>
    <a href="${pageContext.request.contextPath}/manageUser">Quản lý người dùng</a>
    <a href="${pageContext.request.contextPath}/manageService">Quản lý dịch vụ</a>
    <a href="${pageContext.request.contextPath}/manageMedications">Quản lý thuốc</a>
    <a href="${pageContext.request.contextPath}/manageOrder">Quản lý đơn hàng</a>
    <a href="${pageContext.request.contextPath}/report">Thống kê</a>

    <a href="${pageContext.request.contextPath}/logout">Đăng xuất</a>
</div>

<script>
    const links = document.querySelectorAll('.sidebar a');
    const currentPath = window.location.pathname;


    links.forEach(link => {
        link.classList.remove('active');
        if (currentPath.includes(link.getAttribute('href'))) {
            link.classList.add('active');
        }
    });


    links.forEach(link => {
        link.addEventListener('click', function () {
            // Xóa active cũ
            links.forEach(l => l.classList.remove('active'));
            // Gán active cho link hiện tại
            this.classList.add('active');
        });
    });
</script>

<style>
    .sidebar a {
        display: block;
        padding: 10px 15px;
        color: #bdc3c7;
        text-decoration: none;
        margin-bottom: 8px;
        border-radius: 4px;
        transition: background 0.2s, color 0.2s;
    }

    .sidebar a:hover {
        background: #34495e;
        color: #fff;
    }

    .sidebar a.active {
        background: #1abc9c;
        color: white;
        font-weight: bold;
    }
</style>