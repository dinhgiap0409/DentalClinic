<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thông tin cá nhân</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                background-color: #f9fafb;
                font-family: "Segoe UI", sans-serif;
            }
            .profile-card {
                background: #fff;
                border-radius: 16px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.08);
                overflow: hidden;
            }
            .profile-header {
                background: linear-gradient(135deg, #007bff, #00c6ff);
                color: white;
                padding: 2rem;
                text-align: center;
            }
            .profile-header img {
                width: 120px;
                height: 120px;
                border-radius: 50%;
                border: 4px solid #fff;
                object-fit: cover;
                margin-bottom: 1rem;
            }
            .profile-body {
                padding: 2rem;
            }
            .info-group {
                margin-bottom: 1rem;
            }
            .info-group label {
                font-weight: 600;
                color: #555;
            }
            .info-group p, .info-group input, .info-group textarea {
                margin: 0;
                color: #333;
            }
            .info-group input, .info-group textarea {
                width: 100%;
                border: 1px solid #ccc;
                border-radius: 8px;
                padding: 8px 10px;
                background-color: #fdfdfd;
            }
            .btn-save {
                background-color: #007bff;
                color: white;
                border: none;
                border-radius: 8px;
                padding: 10px 20px;
                font-weight: 600;
                transition: 0.3s;
            }
            .btn-save:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <jsp:include page="../../common/header.jsp" />

        <div class="container my-5">
            <div class="profile-card mx-auto" style="max-width: 800px;">
                <div class="profile-header">
                    <c:choose>
                        <c:when test="${not empty user.image}">
                            <img src="${pageContext.request.contextPath}/uploads/${user.image}" alt="Avatar">
                        </c:when>
                        <c:otherwise>
                            <img src="https://cdn-icons-png.flaticon.com/512/3135/3135715.png" alt="Avatar mặc định">
                        </c:otherwise>
                    </c:choose>
                    <h3>${user.fullName}</h3>
                    <p class="mb-0 text-light text-capitalize">${user.role}</p>
                </div>

                <div class="profile-body">
                    <h5 class="mb-3 border-bottom pb-2">Thông tin tài khoản</h5>
                    <div class="row">
                        <div class="col-md-6 info-group">
                            <label>Email:</label>
                            <p>${user.email}</p>
                        </div>
                        <div class="col-md-6 info-group">
                            <label>Số điện thoại:</label>
                            <p>${user.phoneNumber}</p>
                        </div>
                        <div class="col-md-6 info-group">
                            <label>Ngày sinh:</label>
                            <p><fmt:formatDate value="${user.dateOfBirth}" pattern="dd/MM/yyyy"/></p>
                        </div>
                        <div class="col-md-6 info-group">
                            <label>Giới tính:</label>
                            <p>${user.gender}</p>
                        </div>
                        <div class="col-md-12 info-group">
                            <label>Địa chỉ:</label>
                            <p>${user.address}</p>
                        </div>
                    </div>

                    <!-- THÔNG TIN BÁC SĨ -->
                    <c:if test="${user.role == 'Doctor'}">
                        <h5 class="mt-4 mb-3 border-bottom pb-2">Thông tin bác sĩ</h5>
                        <div class="row">
                            <div class="col-md-6 info-group">
                                <label>Chuyên khoa:</label>
                                <p>${doctor.specialization}</p>
                            </div>
                            <div class="col-md-6 info-group">
                                <label>Số giấy phép:</label>
                                <p>${doctor.licenseNumber}</p>
                            </div>
                            <div class="col-md-6 info-group">
                                <label>Kinh nghiệm:</label>
                                <p>${doctor.yearsOfExperience} năm</p>
                            </div>
                            <div class="col-md-6 info-group">
                                <label>Học vấn:</label>
                                <p>${doctor.education}</p>
                            </div>
                            <div class="col-md-12 info-group">
                                <label>Tiểu sử:</label>
                                <p>${doctor.biography}</p>
                            </div>
                        </div>
                    </c:if>

                    <!-- THÔNG TIN BỆNH NHÂN -->
                    <c:if test="${user.role == 'Patient'}">
                        <h5 class="mt-4 mb-3 border-bottom pb-2">Cập nhật thông tin bệnh nhân</h5>
                        <form action="${pageContext.request.contextPath}/profile" method="post">
                            <div class="row">
                                <div class="col-md-6 info-group">
                                    <label>Nhóm máu:</label>
                                    <input type="text" name="bloodType" value="${patient.bloodType}" placeholder="VD: O, A, B, AB">
                                </div>
                                <div class="col-md-6 info-group">
                                    <label>Dị ứng:</label>
                                    <input type="text" name="allergies" value="${patient.allergies}" placeholder="VD: Phấn hoa, thuốc, thực phẩm...">
                                </div>
                                <div class="col-md-6 info-group">
                                    <label>Bảo hiểm:</label>
                                    <input type="text" name="insuranceInfo" value="${patient.insuranceInfo}" placeholder="VD: BHYT-12345">
                                </div>
                                <div class="col-md-6 info-group">
                                    <label>Tiền sử bệnh:</label>
                                    <input type="text" name="medicalHistory" value="${patient.medicalHistory}" placeholder="VD: Cao huyết áp, tiểu đường...">
                                </div>
                                <div class="col-md-6 info-group">
                                    <label>Người liên hệ khẩn cấp:</label>
                                    <input type="text" name="emergencyContactName" value="${patient.emergencyContactName}" placeholder="Họ tên người liên hệ">
                                </div>
                                <div class="col-md-6 info-group">
                                    <label>Số điện thoại khẩn cấp:</label>
                                    <input type="text" name="emergencyContactPhone" value="${patient.emergencyContactPhone}" placeholder="VD: 090xxxxxxx">
                                </div>
                            </div>
                            <div class="text-center mt-4">
                                <button type="submit" class="btn-save">Lưu thay đổi</button>
                            </div>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>

        <jsp:include page="../../common/footer.jsp" />
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
