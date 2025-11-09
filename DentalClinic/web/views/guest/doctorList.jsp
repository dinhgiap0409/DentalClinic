<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Bác sĩ phù hợp - Dental Clinic</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/doctor-list.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/doctor-schedule.css">
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
                                <img src="${doc.image}" alt="${doc.fullName}">
                                <h3>${doc.fullName}</h3>
                                <p><strong>Chuyên khoa:</strong> ${doc.specialization}</p>
                                <p><strong>Kinh nghiệm:</strong> ${doc.yearsOfExperience} năm</p>
                                <p><strong>Email:</strong> ${doc.email}</p>
                                <p><strong>SĐT:</strong> ${doc.phoneNumber}</p>
                                <p><strong>Phí tư vấn:</strong>
                                    <fmt:formatNumber value="${doc.consultationFee}" type="currency" currencySymbol="₫" groupingUsed="true"/>
                                </p>

                                <!-- Link chuyển trang, kèm doctorId & serviceId -->
                                <a class="btn-book"
                                   href="${pageContext.request.contextPath}/doctorWorkSchedule?doctorId=${doc.doctorId}&serviceId=${service.serviceId}">
                                    Xem lịch làm việc & đặt lịch
                                </a>
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
