<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý lịch làm việc - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            table.data-table {
                width: 100%;
                border-collapse: collapse;
                margin-bottom: 20px;
            }
            table.data-table th, table.data-table td {
                border: 1px solid #ddd;
                padding: 8px;
            }
            .status-available {
                color: green;
                font-weight: bold;
            }
            .status-unavailable {
                color: red;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp" />

            <div class="content">
                <div class="header" style="display:flex; justify-content: space-between; align-items:center;">
                    <h1>Quản lý lịch làm việc</h1>
                    <a href="${pageContext.request.contextPath}/manageSchedule?action=add" class="btn-add">Thêm lịch</a>
                </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>


                <!-- Bộ lọc -->
                <div class="filter-box">
                    <form action="${pageContext.request.contextPath}/manageSchedule" method="get">
                        <input type="text" name="doctorName" placeholder="Tên bác sĩ" value="${param.doctorName}" />
                        <select name="dayOfWeek">
                            <option value="">--Ngày trong tuần--</option>
                            <option value="1" ${param.dayOfWeek == '1' ? 'selected' : ''}>Thứ 2</option>
                            <option value="2" ${param.dayOfWeek == '2' ? 'selected' : ''}>Thứ 3</option>
                            <option value="3" ${param.dayOfWeek == '3' ? 'selected' : ''}>Thứ 4</option>
                            <option value="4" ${param.dayOfWeek == '4' ? 'selected' : ''}>Thứ 5</option>
                            <option value="5" ${param.dayOfWeek == '5' ? 'selected' : ''}>Thứ 6</option>
                            <option value="6" ${param.dayOfWeek == '6' ? 'selected' : ''}>Thứ 7</option>
                            <option value="7" ${param.dayOfWeek == '7' ? 'selected' : ''}>Chủ nhật</option>
                        </select>
                        <select name="isAvailable">
                            <option value="">--Trạng thái--</option>
                            <option value="true" ${param.isAvailable == 'true' ? 'selected' : ''}>Có lịch</option>
                            <option value="false" ${param.isAvailable == 'false' ? 'selected' : ''}>Không có lịch</option>
                        </select>
                        <button type="submit">Tìm kiếm</button>
                        <a href="${pageContext.request.contextPath}/manageSchedule" class="reset-btn">Bỏ lọc</a>
                    </form>
                </div>

                <div class="table-container">

                    <c:choose>
                        <c:when test="${empty schedules}">
                            <p class="muted text-center" style="padding:15px;">Không có lịch làm việc nào.</p>
                        </c:when>
                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>ID</th>
                                        <th>Bác sĩ</th>
                                        <th>Ngày</th>
                                        <th>Giờ bắt đầu</th>
                                        <th>Giờ kết thúc</th>
                                        <th>Trạng thái</th>
                                        <th>Số lượt khám tối đa</th>
                                        <th>Phê duyệt</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${schedules}">
                                        <tr>
                                            <td>${s.scheduleId}</td>
                                            <td>${s.doctorId.userId.fullName}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${s.dayOfWeek == 1}">Thứ 2</c:when>
                                                    <c:when test="${s.dayOfWeek == 2}">Thứ 3</c:when>
                                                    <c:when test="${s.dayOfWeek == 3}">Thứ 4</c:when>
                                                    <c:when test="${s.dayOfWeek == 4}">Thứ 5</c:when>
                                                    <c:when test="${s.dayOfWeek == 5}">Thứ 6</c:when>
                                                    <c:when test="${s.dayOfWeek == 6}">Thứ 7</c:when>
                                                    <c:when test="${s.dayOfWeek == 7}">Chủ nhật</c:when>
                                                </c:choose>
                                            </td>
                                            <td>${s.startTime}</td>
                                            <td>${s.endTime}</td>
                                            <td class="${s.isAvailable ? 'status-available' : 'status-unavailable'}">
                                                ${s.isAvailable ? 'Có lịch' : 'Không có lịch'}
                                            </td>
                                            <td>${s.maxAppointments}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${s.isApproved}">Đã phê duyệt</c:when>
                                                    <c:otherwise>Chưa phê duyệt</c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <a href="${pageContext.request.contextPath}/manageSchedule?action=viewDetail&scheduleId=${s.scheduleId}" class="btn btn-detail">Xem chi tiết</a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>

                    <!-- Phân trang -->
                    <jsp:include page="../../common/pagination.jsp">
                        <jsp:param name="baseUrl" value="${baseUrl}" />
                        <jsp:param name="page" value="${page}" />
                        <jsp:param name="size" value="${size}" />
                        <jsp:param name="totalPages" value="${totalPages}" />
                    </jsp:include>
                </div>
            </div>
        </div>
    </body>
</html>
