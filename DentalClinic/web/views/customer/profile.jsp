<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Hồ sơ y tế</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/profile.css"/>
    </head>
    <body>
        <!-- Header -->
        <jsp:include page="/common/header.jsp" />

        <div class="container mt-4 mb-5">
            <h3 class="mb-3 text-center">Cập nhật hồ sơ y tế</h3>

            <!-- Thông báo -->
            <c:if test="${not empty success}">
                <div class="alert alert-success text-center">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">${error}</div>
            </c:if>

            <!-- Form -->
            <form action="${pageContext.request.contextPath}/profile" method="post">
                <div class="card p-4 shadow-sm">
                    <div class="mb-3">
                        <label for="fullName" class="form-label">Họ tên</label>
                        <input type="text" id="fullName" class="form-control" value="${patient.user.fullName}" readonly>
                    </div>
                    <div class="mb-3">
                        <label for="email" class="form-label">Email</label>
                        <input type="email" id="email" class="form-control" value="${patient.user.email}" readonly>
                    </div>
                    <hr>

                    <div class="mb-3">
                        <label for="bloodType" class="form-label">Nhóm máu</label>
                        <select class="form-select" id="bloodType" name="bloodType">
                            <option value="">-- Chọn --</option>
                            <c:set var="bt" value="${patient.bloodType}" />
                            <c:forEach var="type" items="${bloodTypes}">
                                <option value="${type}" <c:if test="${bt == type}">selected</c:if>>${type}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="mb-3">
                        <label for="allergies" class="form-label">Dị ứng</label>
                        <textarea class="form-control" id="allergies" name="allergies" rows="2">${patient.allergies}</textarea>
                    </div>

                    <div class="mb-3">
                        <label for="medicalHistory" class="form-label">Tiền sử bệnh</label>
                        <textarea class="form-control" id="medicalHistory" name="medicalHistory" rows="3">${patient.medicalHistory}</textarea>
                    </div>

                    <div class="mb-3">
                        <label for="insuranceInfo" class="form-label">Thông tin bảo hiểm</label>
                        <input class="form-control" type="text" id="insuranceInfo" name="insuranceInfo" value="${patient.insuranceInfo}">
                    </div>

                    <div class="mb-3">
                        <label for="emergencyContactName" class="form-label">Người liên hệ khẩn cấp</label>
                        <input class="form-control" type="text" id="emergencyContactName" name="emergencyContactName" value="${patient.emergencyContactName}">
                    </div>

                    <div class="mb-3">
                        <label for="emergencyContactPhone" class="form-label">SĐT liên hệ khẩn cấp</label>
                        <input class="form-control" type="text" id="emergencyContactPhone" name="emergencyContactPhone" value="${patient.emergencyContactPhone}">
                    </div>

                    <div class="text-center">
                        <button type="submit" class="btn btn-primary">Lưu thay đổi</button>
                        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Hủy</a>
                    </div>
                </div>
            </form>
        </div>

        <jsp:include page="/common/footer.jsp" />

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Ẩn thông báo sau 5 giây
            setTimeout(() => {
                document.querySelectorAll('.alert').forEach(el => el.style.display = 'none');
            }, 5000);
        </script>
    </body>
</html>
