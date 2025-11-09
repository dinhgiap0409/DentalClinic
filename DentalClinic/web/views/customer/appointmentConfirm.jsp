<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Xác nhận đặt lịch - Dental Clinic</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <!-- CSS có sẵn -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/appointment-confirm.css">
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../../common/header.jsp"></jsp:include>

            <div class="container">
                <h1>Xác nhận đặt lịch khám</h1>

                <!-- Nút quay lại trang lịch làm việc -->
                <div style="margin: 12px 0 20px;">
                    <a class="btn btn-ghost"
                       href="${pageContext.request.contextPath}/doctorWorkSchedule?doctorId=${doctor.doctorID}&serviceId=${service.serviceId}">
                    ⬅ Quay lại chọn khung giờ
                </a>
            </div>

            <!-- Nếu chưa đăng nhập/không có patient -->
            <c:if test="${patient == null}">
                <div class="confirm-card" style="border-color:#ffe0e0;">
                    <p style="color:#b30000;">
                        Bạn cần đăng nhập và cập nhật hồ sơ khám.
                    </p>
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Đăng nhập</a>
                </div>
            </c:if>

            <c:if test="${patient != null}">
                <div class="confirm-card">
                    <!-- THÔNG TIN CHUNG -->
                    <div class="row">
                        <!-- Bệnh nhân -->
                        <div class="col">
                            <div class="section-title">Thông tin bệnh nhân</div>
                            <div class="field">
                                <span class="label">Họ tên</span>
                                <span class="value">${patient.userID.fullName}</span>
                            </div>
                            <div class="field">
                                <span class="label">SĐT</span>
                                <span class="value">${patient.userID.phoneNumber}</span>
                            </div>
                            <div class="field">
                                <span class="label">Email</span>
                                <span class="value">${patient.userID.email}</span>
                            </div>
                        </div>

                        <!-- Bác sĩ -->
                        <div class="col">
                            <div class="section-title">Bác sĩ phụ trách</div>
                            <div class="field">
                                <span class="label">Họ tên</span>
                                <span class="value">${doctor.userId.fullName}</span>
                            </div>
                            <div class="field">
                                <span class="label">Chuyên khoa</span>
                                <span class="value">${doctor.specialization}</span>
                            </div>
                            <div class="field">
                                <span class="label">Kinh nghiệm</span>
                                <span class="value">${doctor.yearsOfExperience} năm</span>
                            </div>
                            <div class="field">
                                <span class="label">Phí tư vấn</span>
                                <span class="value">
                                    <fmt:formatNumber value="${doctor.consultationFee}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                </span>
                            </div>
                        </div>

                        <!-- Dịch vụ -->
                        <div class="col">
                            <div class="section-title">Dịch vụ</div>
                            <div class="field">
                                <span class="label">Tên dịch vụ</span>
                                <span class="value">${service.serviceName}</span>
                            </div>
                            <div class="field">
                                <span class="label">Giá</span>
                                <span class="value">
                                    <fmt:formatNumber value="${service.price}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                </span>
                            </div>
                            <div class="field">
                                <span class="label">Thời lượng</span>
                                <span class="value">${service.duration} phút</span>
                            </div>
                        </div>
                    </div>

                    <hr style="margin:16px 0;"/>

                    <!-- NGÀY/GIỜ -->
                    <div class="row">
                        <div class="col">
                            <div class="section-title">Thời gian khám</div>
                            <div class="field">
                                <span class="label">Ngày</span>
                                <span class="value">${date}</span>
                            </div>
                            <div class="field">
                                <span class="label">Khung giờ</span>
                                <span class="value">${start} - ${end}</span>
                            </div>
                        </div>

                        <!-- TỔNG QUAN CHI PHÍ (tuỳ chọn) -->
                        <div class="col">
                            <div class="section-title">Tạm tính</div>
                            <div class="field">
                                <span class="label">Phí tư vấn</span>
                                <span class="value">
                                    <fmt:formatNumber value="${doctor.consultationFee}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                </span>
                            </div>
                            <div class="field">
                                <span class="label">Giá dịch vụ</span>
                                <span class="value">
                                    <fmt:formatNumber value="${service.price}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                </span>
                            </div>
                            <div class="field">
                                <span class="label">Tổng (ước tính)</span>
                                <span class="value">
                                    <!-- Nếu muốn cộng hai BigDecimal ở server rồi setAttribute("total") thì thay dòng dưới bằng ${total} -->
                                    <fmt:formatNumber value="${service.price + doctor.consultationFee}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                </span>
                            </div>
                        </div>
                    </div>

                    <!-- FORM XÁC NHẬN -->
                    <form action="${pageContext.request.contextPath}/CustomersBookSchedules" method="post" style="margin-top:12px;">
                        <!-- Không cần patientId nếu bạn lấy từ session trong Controller; 
                             nếu vẫn tạm thời dùng cứng 3, có thể bỏ hẳn input này. -->
                        <!-- <input type="hidden" name="patientId" value="${patient.patientID}"> -->

                        <input type="hidden" name="doctorId"       value="${doctor.doctorID}">
                        <input type="hidden" name="serviceId"      value="${service.serviceId}">
                        <input type="hidden" name="appointmentDate" value="${date}">
                        <!-- Ghép slot theo đúng định dạng mà controller đang split -->
                        <input type="hidden" name="slot"           value="${start} - ${end}">

                        <!-- Ghi chú (tuỳ chọn) -->
                        <div class="field" style="margin-top:10px;">
                            <label for="notes" class="label">Ghi chú cho bác sĩ (không bắt buộc)</label>
                            <textarea id="notes" name="notes" rows="3" style="width:100%; padding:8px; border:1px solid #ddd; border-radius:8px;"></textarea>
                        </div>

                        <div class="actions">
                            <a class="btn btn-ghost"
                               href="${pageContext.request.contextPath}/doctorWorkSchedule?doctorId=${doctor.doctorID}&serviceId=${service.serviceId}">
                                ⬅ Chọn lại khung giờ
                            </a>
                            <button type="submit" class="btn btn-primary">
                                Xác nhận đặt lịch
                            </button>
                        </div>
                    </form>

                </div>
            </c:if>
        </div>

        <!-- Footer -->
        <jsp:include page="../../common/footer.jsp"></jsp:include>

    </body>
</html>
