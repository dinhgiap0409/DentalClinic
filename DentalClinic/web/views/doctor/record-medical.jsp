<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Ghi kết quả khám - ${appointment.patientId.userID.fullName}</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .content {
                padding: 20px 30px;
            }
            .section {
                margin-bottom: 20px;
                border: 1px solid #e1e1e1;
                padding: 15px;
                border-radius: 6px;
                background: #fff;
            }
            .section h2 {
                margin-top: 0;
                color: #007bff;
            }
            .section table {
                width: 100%;
                border-collapse: collapse;
            }
            .section table td {
                padding: 8px;
                vertical-align: top;
            }
            .section table td.label {
                font-weight: 600;
                width: 25%;
                background-color: #f7f7f7;
            }
            .medications-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 10px;
            }
            .medications-table th, .medications-table td {
                border: 1px solid #ccc;
                padding: 6px;
                text-align: left;
            }
            .medications-table th {
                background-color: #f1f1f1;
            }
            .btn-submit {
                padding: 8px 16px;
                background-color: #28a745;
                color: #fff;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }
            .btn-submit:hover {
                background-color: #218838;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <!-- Sidebar -->
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <div class="content">
                    <h1>Ghi kết quả khám - ${appointment.patientId.userID.fullName}</h1>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="alert alert-success">${success}</div>
                </c:if>

                <!-- Thông tin bệnh nhân -->
                <div class="section">
                    <h2>Thông tin bệnh nhân</h2>
                    <table>
                        <tr><td class="label">Họ và tên</td><td>${appointment.patientId.userID.fullName}</td></tr>
                        <tr><td class="label">SĐT</td><td>${appointment.patientId.userID.phoneNumber}</td></tr>
                        <tr><td class="label">Ngày sinh</td><td><fmt:formatDate value="${appointment.patientId.userID.dateOfBirth}" pattern="dd/MM/yyyy"/></td></tr>
                        <tr><td class="label">Giới tính</td><td>${appointment.patientId.userID.gender}</td></tr>
                        <tr><td class="label">Địa chỉ</td><td>${appointment.patientId.userID.address}</td></tr>
                    </table>
                </div>

                <!-- Thông tin khám -->
                <div class="section">
                    <h2>Thông tin khám</h2>
                    <table>
                        <tr><td class="label">Bác sĩ</td><td>${appointment.doctorId.userId.fullName}</td></tr>
                        <tr><td class="label">Chuyên khoa</td><td>${appointment.doctorId.specialization}</td></tr>
                        <tr><td class="label">Dịch vụ</td><td>${appointment.serviceId.serviceName}</td></tr>
                        <tr><td class="label">Ngày hẹn</td><td><fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy"/></td></tr>
                        <tr><td class="label">Giờ khám</td><td>${appointment.startTime} - ${appointment.endTime}</td></tr>
                    </table>
                </div>

                <!-- Form tìm thuốc (GET) -->
                <div class="section">
                    <h2>Tìm thuốc</h2>
                    <form action="recordMedicalResults" method="get">
                        <input type="hidden" name="appointmentId" value="${appointment.appointmentId}" />
                        <input type="text" name="medName" placeholder="Tên thuốc" value="${param.medName}" />
                        <input type="text" name="manu" placeholder="Nhà sản xuất" value="${param.manu}" />
                        <button type="submit">Tìm kiếm</button>
                        <a href="recordMedicalResults?appointmentId=${appointment.appointmentId}">Bỏ lọc</a>
                    </form>
                </div>

                <!-- Form ghi kết quả khám & kê đơn (POST) -->
                <form action="${pageContext.request.contextPath}/recordMedicalResults" method="post">
                    <input type="hidden" name="appointmentId" value="${appointment.appointmentId}"/>

                    <div class="section">
                        <h2>Kết quả khám</h2>
                        <table>
                            <tr><td class="label">Chẩn đoán</td><td><textarea name="diagnosis" rows="3" style="width:100%;"></textarea></td></tr>
                            <tr><td class="label">Triệu chứng</td><td><textarea name="symptoms" rows="3" style="width:100%;"></textarea></td></tr>
                            <tr><td class="label">Kế hoạch điều trị</td><td><textarea name="treatmentPlan" rows="3" style="width:100%;"></textarea></td></tr>
                            <tr><td class="label">Ngày tái khám</td><td><input type="date" name="followUpDate"/></td></tr>
                        </table>
                    </div>

                    <!-- Bảng kê đơn thuốc -->
                    <div class="section">
                        <h2>Kê đơn thuốc</h2>
                        <table class="medications-table">
                            <thead>
                                <tr>
                                    <th>Thuốc</th>
                                    <th>Hãng SX</th>
                                    <th>Liều lượng</th>
                                    <th>Số lượng</th>
                                    <th>Thời gian sử dụng</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="m" items="${medications}">
                                    <tr>
                                        <td>${m.medicationName}</td>
                                        <td>${m.manufacturer}</td>
                                        <td><input type="text" name="dosage" /></td>
                                        <td><input type="number" name="quantity" min="0" /></td>
                                        <td><input type="text" name="duration" placeholder="VD: 7 ngày"/></td>
                                <input type="hidden" name="medicationId" value="${m.medicationId}"/>
                                </tr>
                            </c:forEach>
                            </tbody>
                        </table>
                        <div style="margin-top:10px;">
                            <label>Hướng dẫn sử dụng chung:</label>
                            <textarea name="instructions" rows="3" style="width:100%;"></textarea>
                        </div>
                    </div>
                    <!-- Kê thêm dịch vụ (tùy chọn) -->
                    <div class="section">
                        <h2>Kê thêm dịch vụ</h2>
                        <c:forEach var="s" items="${services}">
                            <div>
                                <input type="checkbox" name="extraServiceIds" value="${s.serviceId}" />
                                ${s.serviceName} - ${s.price} VNĐ
                            </div>
                        </c:forEach>
                        <label>Ghi chú chung cho các dịch vụ:</label>
                        <textarea name="extraServiceNotes" rows="2" style="width:100%;"></textarea>
                    </div>

                    <button type="submit" class="btn-submit">Lưu kết quả khám & kê đơn</button>
                </form>
            </div>
        </div>
    </body>
</html>
