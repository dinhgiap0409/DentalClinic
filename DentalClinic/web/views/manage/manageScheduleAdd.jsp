<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thêm lịch làm việc - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .form-container {
                max-width: 600px;
                margin: 20px auto;
            }
            label {
                display:block;
                margin:10px 0 5px;
                font-weight:bold;
            }
            input, select {
                width:100%;
                padding:6px;
                box-sizing:border-box;
            }
            button {
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
                    <h1>Thêm lịch làm việc mới</h1>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <form class="form-container" action="${pageContext.request.contextPath}/manageSchedule" method="post">
                    <input type="hidden" name="action" value="add"/>

                    <label for="doctorId">Bác sĩ</label>
                    <select name="doctorId" id="doctorId" required>
                        <c:forEach var="doc" items="${doctors}">
                            <option value="${doc.doctorID}">${doc.userId.fullName}</option>
                        </c:forEach>
                    </select>

                    <label for="dayOfWeek">Ngày trong tuần</label>
                    <select name="dayOfWeek" id="dayOfWeek" required>
                        <option value="1">Thứ 2</option>
                        <option value="2">Thứ 3</option>
                        <option value="3">Thứ 4</option>
                        <option value="4">Thứ 5</option>
                        <option value="5">Thứ 6</option>
                        <option value="6">Thứ 7</option>
                        <option value="7">Chủ nhật</option>
                    </select>

                    <label for="startTime">Giờ bắt đầu</label>
                    <input type="time" name="startTime" id="startTime" required/>

                    <label for="endTime">Giờ kết thúc</label>
                    <input type="time" name="endTime" id="endTime" required/>

                    <label for="isAvailable">Trạng thái</label>
                    <select name="isAvailable" id="isAvailable" required>
                        <option value="true">Có lịch</option>
                        <option value="false">Không có lịch</option>
                    </select>

                    <label for="maxAppointments">Số lượt khám tối đa</label>
                    <input type="number" name="maxAppointments" id="maxAppointments" min="1" value="1"/>

                    <label for="requiresApproval">Yêu cầu phê duyệt</label>
                    <select name="requiresApproval" id="requiresApproval">
                        <option value="true">Có</option>
                        <option value="false">Không</option>
                    </select>

                    <label for="isApproved">Đã phê duyệt</label>
                    <select name="isApproved" id="isApproved">
                        <option value="true">Có</option>
                        <option value="false">Chưa</option>
                    </select>

                    <label for="validFrom">Ngày bắt đầu hiệu lực</label>
                    <input type="date" name="validFrom" id="validFrom"/>

                    <label for="validTo">Ngày kết thúc hiệu lực</label>
                    <input type="date" name="validTo" id="validTo"/>

                    <button type="submit">Thêm lịch</button>
                    <a href="${pageContext.request.contextPath}/manageSchedule" class="btn btn-cancel">Quay lại</a>
                </form>
            </div>
        </div>
    </body>
</html>
