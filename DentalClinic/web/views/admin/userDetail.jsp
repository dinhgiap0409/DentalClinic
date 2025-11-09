<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết người dùng - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .user-detail {
                background: #fff;
                border-radius: 10px;
                padding: 20px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.1);
                margin-top: 20px;
            }

            .user-detail h2 {
                margin-bottom: 15px;
                color: #007bff;
            }

            .info-group {
                margin-bottom: 10px;
            }

            .info-label {
                font-weight: 600;
                width: 180px;
                display: inline-block;
                color: #333;
            }

            .status.active {
                background-color: #007bff;
                color: #fff;
                padding: 3px 8px;
                border-radius: 4px;
            }

            .status.inactive {
                background-color: #6c757d;
                color: #fff;
                padding: 3px 8px;
                border-radius: 4px;
            }

            .section-title {
                margin-top: 25px;
                font-size: 18px;
                font-weight: 600;
                border-bottom: 2px solid #007bff;
                padding-bottom: 5px;
                color: #007bff;
            }

            .back-btn {
                margin-top: 20px;
                display: inline-block;
                background-color: #6c757d;
                color: #fff;
                padding: 8px 14px;
                border-radius: 6px;
                text-decoration: none;
            }

            .back-btn:hover {
                background-color: #5a6268;
            }

            .status-form {
                margin-top: 10px;
            }

            .status-form button {
                padding: 6px 12px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
            }

            .btn-active {
                background-color: #28a745;
                color: #fff;
            }

            .btn-lock {
                background-color: #dc3545;
                color: #fff;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <div class="content">
                    <div class="header">
                        <h1>Chi tiết người dùng</h1>
                    </div>

                    <div class="user-detail">
                        <h2>Thông tin cơ bản</h2>
                        <div class="info-group"><span class="info-label">Họ tên:</span> ${user.fullName}</div>
                    <div class="info-group"><span class="info-label">Email:</span> ${user.email}</div>
                    <div class="info-group"><span class="info-label">Số điện thoại:</span> ${user.phoneNumber}</div>
                    <div class="info-group"><span class="info-label">Ngày sinh:</span> 
                        <fmt:formatDate value="${user.dateOfBirth}" pattern="dd/MM/yyyy" />
                    </div>
                    <div class="info-group"><span class="info-label">Giới tính:</span> ${user.gender}</div>
                    <div class="info-group"><span class="info-label">Địa chỉ:</span> ${user.address}</div>
                    <div class="info-group"><span class="info-label">Vai trò:</span> ${user.role}</div>
                    <div class="info-group">
                        <span class="info-label">Trạng thái:</span>
                        <span class="status ${user.isActive ? 'active' : 'inactive'}">
                            ${user.isActive ? 'Hoạt động' : 'Bị khóa'}
                        </span>
                    </div>

                    <!-- Nút khóa / mở khóa -->
                    <form action="${pageContext.request.contextPath}/manageUser" method="post" class="status-form">
                        <input type="hidden" name="action" value="updateStatus">
                        <input type="hidden" name="userId" value="${user.userId}">
                        <input type="hidden" name="isActive" value="${not user.isActive}">
                        <button type="submit" class="${user.isActive ? 'btn-lock' : 'btn-active'}">
                            ${user.isActive ? 'Khóa tài khoản' : 'Mở khóa tài khoản'}
                        </button>
                    </form>

                    <!-- Nếu là bệnh nhân -->
                    <c:if test="${not empty patient}">
                        <div class="section-title">Thông tin bệnh nhân</div>
                        <div class="info-group"><span class="info-label">Nhóm máu:</span> ${patient.bloodType}</div>
                        <div class="info-group"><span class="info-label">Dị ứng:</span> ${patient.allergies}</div>
                        <div class="info-group"><span class="info-label">Tiền sử bệnh:</span> ${patient.medicalHistory}</div>
                        <div class="info-group"><span class="info-label">Thông tin bảo hiểm:</span> ${patient.insuranceInfo}</div>
                        <div class="info-group"><span class="info-label">Người liên hệ khẩn cấp:</span> ${patient.emergencyContactName}</div>
                        <div class="info-group"><span class="info-label">SĐT liên hệ:</span> ${patient.emergencyContactPhone}</div>
                    </c:if>

                    <!-- Nếu là bác sĩ -->
                    <c:if test="${not empty doctor}">
                        <div class="section-title">Thông tin bác sĩ</div>
                        <div class="info-group"><span class="info-label">Chuyên khoa:</span> ${doctor.specialization}</div>
                        <div class="info-group"><span class="info-label">Số giấy phép:</span> ${doctor.licenseNumber}</div>
                        <div class="info-group"><span class="info-label">Kinh nghiệm:</span> ${doctor.yearsOfExperience} năm</div>
                        <div class="info-group"><span class="info-label">Học vấn:</span> ${doctor.education}</div>
                        <div class="info-group"><span class="info-label">Tiểu sử:</span> ${doctor.biography}</div>
                        <div class="info-group"><span class="info-label">Phí tư vấn:</span> 
                            <fmt:formatNumber value="${doctor.consultationFee}" type="currency" currencySymbol="₫"/>
                        </div>
                    </c:if>

                    <a href="${pageContext.request.contextPath}/manageUser?action=list" class="back-btn">← Quay lại danh sách</a>
                </div>
            </div>
        </div>
    </body>
</html>
