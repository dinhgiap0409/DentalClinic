<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thông tin cá nhân</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-4">
        <div class="row justify-content-center">
            <div class="col-md-8">
                <h2 class="mb-4">Cập nhật thông tin y tế</h2>

                <!-- Alert messages -->
                <c:if test="${not empty success}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${success}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${error}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Profile Form -->
                <div class="card">
                    <div class="card-body">
                        <form action="/profile" method="POST" id="profileForm">
                            
                            <div class="mb-3">
                                <label class="form-label">Họ và tên</label>
                                <input type="text" class="form-control" value="${patient.userID.fullName}" readonly>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Email</label>
                                <input type="email" class="form-control" value="${patient.userID.email}" readonly>
                            </div>

                            <hr class="my-4">
                            <h5 class="mb-3">Thông tin y tế</h5>

                            <div class="mb-3">
                                <label class="form-label">Nhóm máu <span class="text-danger">*</span></label>
                                <select class="form-select" name="bloodType" required>
                                    <option value="">Chọn nhóm máu</option>
                                    <option value="A+" ${patient.bloodType == 'A+' ? 'selected' : ''}>A+</option>
                                    <option value="A-" ${patient.bloodType == 'A-' ? 'selected' : ''}>A-</option>
                                    <option value="B+" ${patient.bloodType == 'B+' ? 'selected' : ''}>B+</option>
                                    <option value="B-" ${patient.bloodType == 'B-' ? 'selected' : ''}>B-</option>
                                    <option value="AB+" ${patient.bloodType == 'AB+' ? 'selected' : ''}>AB+</option>
                                    <option value="AB-" ${patient.bloodType == 'AB-' ? 'selected' : ''}>AB-</option>
                                    <option value="O+" ${patient.bloodType == 'O+' ? 'selected' : ''}>O+</option>
                                    <option value="O-" ${patient.bloodType == 'O-' ? 'selected' : ''}>O-</option>
                                </select>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Dị ứng <span class="text-danger">*</span></label>
                                <input type="text" class="form-control" name="allergies" 
                                       value="${patient.allergies}" 
                                       placeholder="Ví dụ: Penicillin, Shellfish..." required>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Tiền sử bệnh <span class="text-danger">*</span></label>
                                <textarea class="form-control" name="medicalHistory" rows="3" required>${patient.medicalHistory}</textarea>
                            </div>

                            <div class="mb-3">
                                <label class="form-label">Thông tin bảo hiểm</label>
                                <input type="text" class="form-control" name="insuranceInfo" 
                                       value="${patient.insuranceInfo}" 
                                       placeholder="Số thẻ bảo hiểm...">
                            </div>

                            <hr class="my-4">
                            <h5 class="mb-3">Liên hệ khẩn cấp</h5>

                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">Tên người liên hệ <span class="text-danger">*</span></label>
                                    <input type="text" class="form-control" name="emergencyContactName" 
                                           value="${patient.emergencyContactName}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label class="form-label">Số điện thoại <span class="text-danger">*</span></label>
                                    <input type="tel" class="form-control" name="emergencyContactPhone" 
                                           value="${patient.emergencyContactPhone}" required>
                                </div>
                            </div>

                            <div class="d-flex justify-content-end gap-2">
                                <button type="button" class="btn btn-outline-secondary" onclick="resetForm()">
                                    Đặt lại
                                </button>
                                <button type="submit" class="btn btn-primary">
                                    Cập nhật thông tin
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function resetForm() {
            document.getElementById('profileForm').reset();
        }
        
        // Auto-hide alerts after 5 seconds
        setTimeout(function() {
            const alerts = document.querySelectorAll('.alert');
            alerts.forEach(function(alert) {
                const bsAlert = new bootstrap.Alert(alert);
                bsAlert.close();
            });
        }, 5000);
    </script>
</body>
</html>
