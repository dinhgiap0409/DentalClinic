<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chi tiết lịch làm việc - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .detail-form {
                max-width: 600px;
                margin: 20px auto;
            }
            .detail-form label {
                display: block;
                margin: 10px 0 5px;
                font-weight: bold;
            }
            .detail-form input, .detail-form select {
                width: 100%;
                padding: 6px;
                box-sizing: border-box;
            }
            .detail-form button {
                margin-top: 15px;
                padding: 8px 15px;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp" />

            <div class="content">
                <div class="header">
                    <h1>Chi tiết lịch làm việc</h1>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <c:if test="${not empty schedule}">
                    <form class="detail-form" action="${pageContext.request.contextPath}/manageSchedule" method="post">
                        <input type="hidden" name="action" value="update"/>
                        <input type="hidden" name="scheduleId" value="${schedule.scheduleId}"/>

                        <label>Bác sĩ</label>
                        <input type="text" value="${schedule.doctorId.userId.fullName}" readonly/>

                        <label for="dayOfWeek">Ngày trong tuần</label>
                        <select name="dayOfWeek" id="dayOfWeek" required>
                            <option value="1" ${schedule.dayOfWeek == 1 ? 'selected' : ''}>Thứ 2</option>
                            <option value="2" ${schedule.dayOfWeek == 2 ? 'selected' : ''}>Thứ 3</option>
                            <option value="3" ${schedule.dayOfWeek == 3 ? 'selected' : ''}>Thứ 4</option>
                            <option value="4" ${schedule.dayOfWeek == 4 ? 'selected' : ''}>Thứ 5</option>
                            <option value="5" ${schedule.dayOfWeek == 5 ? 'selected' : ''}>Thứ 6</option>
                            <option value="6" ${schedule.dayOfWeek == 6 ? 'selected' : ''}>Thứ 7</option>
                            <option value="7" ${schedule.dayOfWeek == 7 ? 'selected' : ''}>Chủ nhật</option>
                        </select>

                        <label for="startTime">Giờ bắt đầu</label>
                        <input type="time" name="startTime" id="startTime" value="${schedule.startTime}" required/>

                        <label for="endTime">Giờ kết thúc</label>
                        <input type="time" name="endTime" id="endTime" value="${schedule.endTime}" required/>

                        <label for="isAvailable">Trạng thái</label>
                        <select name="isAvailable" id="isAvailable" required>
                            <option value="true" ${schedule.isAvailable ? 'selected' : ''}>Có lịch</option>
                            <option value="false" ${!schedule.isAvailable ? 'selected' : ''}>Không có lịch</option>
                        </select>

                        <label for="maxAppointments">Số lượt khám tối đa</label>
                        <input type="number" name="maxAppointments" id="maxAppointments" value="${schedule.maxAppointments}" min="1"/>

                        <label for="isApproved">Đã phê duyệt</label>
                        <select name="isApproved" id="isApproved">
                            <option value="true" ${schedule.isApproved ? 'selected' : ''}>Có</option>
                            <option value="false" ${!schedule.isApproved ? 'selected' : ''}>Chưa</option>
                        </select>

                        <label for="validFrom">Ngày bắt đầu hiệu lực</label>
                        <input type="date" name="validFrom" id="validFrom" value="<fmt:formatDate value='${schedule.validFrom}' pattern='yyyy-MM-dd'/>"/>

                        <label for="validTo">Ngày kết thúc hiệu lực</label>
                        <input type="date" name="validTo" id="validTo" value="<fmt:formatDate value='${schedule.validTo}' pattern='yyyy-MM-dd'/>"/>

                        <button type="submit">Cập nhật lịch</button>
                        <a href="${pageContext.request.contextPath}/manageSchedule" class="btn btn-cancel">Quay lại</a>
                    </form>
                </c:if>
            </div>
        </div>
    </body>
</html>
