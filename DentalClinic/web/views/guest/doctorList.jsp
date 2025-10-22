<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bác sĩ phù hợp - Dental Clinic</title>
        <link rel="stylesheet" href="/DentalClinic/css/home.css">
        <link rel="stylesheet" href="/DentalClinic/css/doctor-list.css">
        <link rel="stylesheet" href="/DentalClinic/css/doctor-schedule.css">
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../../common/header.jsp"></jsp:include>

            <div class="container">
                <!-- Thông tin dịch vụ -->
                <div class="service-detail">
                    <h1>${service.serviceName}</h1>
                <p>${service.description}</p>
                <p><strong>Giá:</strong> 
                    <fmt:formatNumber value="${service.price}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                </p>
                <p><strong>Thời lượng:</strong> ${service.duration} phút</p>
            </div>

            <hr>

            <!-- Danh sách bác sĩ -->
            <h2>Bác sĩ phù hợp</h2>

            <c:choose>
                <c:when test="${not empty doctors}">
                    <div class="doctor-list">
                        <c:forEach var="doc" items="${doctors}">
                            <div class="doctor-card" id="doctor-${doc.doctorId}">
                                <img src="/DentalClinic/img/doctor-default.jpg" alt="${doc.fullName}">
                                <h3>${doc.fullName}</h3>
                                <p><strong>Chuyên khoa:</strong> ${doc.specialization}</p>
                                <p><strong>Kinh nghiệm:</strong> ${doc.yearsOfExperience} năm</p>
                                <p><strong>Email:</strong> ${doc.email}</p>
                                <p><strong>SĐT:</strong> ${doc.phoneNumber}</p>
                                <p><strong>Phí tư vấn:</strong>
                                    <fmt:formatNumber value="${doc.consultationFee}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                </p>

                                <button class="btn-book"
                                        data-doctor-id="${doc.doctorId}"
                                        data-service-id="${service.serviceId}">
                                    Đặt lịch với bác sĩ này
                                </button>

                                <!-- Khu vực form chọn ngày và lịch -->
                                <div class="booking-form-container" id="booking-form-${doc.doctorId}" style="display:none;">
                                    <div class="booking-box">
                                        <form class="date-form">
                                            <label for="date-${doc.doctorId}" class="form-label">Chọn ngày:</label>
                                            <input id="date-${doc.doctorId}" type="date" class="date-picker" name="date" required>
                                            <button type="button" class="btn-view-schedule">Xem lịch làm việc</button>
                                        </form>

                                        <div class="schedule-container"></div>
                                    </div>
                                </div>
                            </div>

                        </c:forEach>

                    </div>
                </c:when>

                <c:otherwise>
                    <p style="text-align: center; color: gray;">Không có bác sĩ phù hợp cho dịch vụ này.</p>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- Footer -->
        <jsp:include page="../../common/footer.jsp"></jsp:include>



    </body>
</html>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const buttons = document.querySelectorAll(".btn-book");

        buttons.forEach(function (btn) {
            btn.addEventListener("click", function () {
                const doctorId = this.getAttribute("data-doctor-id");
                const serviceId = this.getAttribute("data-service-id"); // ✅ lấy serviceId từ button
                const container = document.getElementById("booking-form-" + doctorId);

                // Toggle hiển thị form đặt lịch
                if (container.style.display === "none" || container.style.display === "") {
                    container.style.display = "block";
                } else {
                    container.style.display = "none";
                    return;
                }

                const dateInput = container.querySelector(".date-picker");
                const viewBtn = container.querySelector(".btn-view-schedule");
                const scheduleBox = container.querySelector(".schedule-container");

                // Bắt sự kiện xem lịch làm việc
                viewBtn.onclick = function () {
                    const selectedDate = dateInput.value;
                    if (!selectedDate) {
                        alert("Vui lòng chọn ngày trước khi xem lịch.");
                        return;
                    }

                    scheduleBox.innerHTML = "<div class='loading'>Đang tải lịch làm việc...</div>";

                    const url = "/DentalClinic/doctorWorkSchedule?doctorId=" + doctorId
                            + "&serviceId=" + serviceId
                            + "&date=" + selectedDate;

                    fetch(url)
                            .then(function (response) {
                                return response.text();
                            })
                            .then(function (html) {
                                scheduleBox.innerHTML = html;
                            })
                            .catch(function (err) {
                                console.error(err);
                                scheduleBox.innerHTML = "<p style='color:red;'>Không thể tải lịch làm việc.</p>";
                            });
                };
            });
        });
    });
</script>


