<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn"  uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Lịch sử khám của tôi - Dental Clinic</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <!-- CSS -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/medical-history.css">
    </head>
    <body>

        <!-- Header -->
        <jsp:include page="../../common/header.jsp"></jsp:include>

            <div class="container">
                <h1>Lịch sử khám của tôi</h1>

                <!-- Bộ lọc -->
                <div class="filters">
                    <form method="get" action="${pageContext.request.contextPath}/medicalHistory">
                    <div class="field">
                        <label for="status">Trạng thái</label>
                        <select id="status" name="status">
                            <option value="">-- Tất cả --</option>
                            <option value="Scheduled"  <c:if test="${param.status == 'Scheduled'}">selected</c:if>>Scheduled</option>
                            <option value="Confirmed"  <c:if test="${param.status == 'Confirmed'}">selected</c:if>>Confirmed</option>
                            <option value="Completed"  <c:if test="${param.status == 'Completed'}">selected</c:if>>Completed</option>
                            <option value="Cancelled"  <c:if test="${param.status == 'Cancelled'}">selected</c:if>>Cancelled</option>
                            <option value="Rescheduled"  <c:if test="${param.status == 'Rescheduled'}">selected</c:if>>Rescheduled</option>
                            </select>
                        </div>

                        <!-- ĐỔI: tìm theo TÊN bác sĩ -->
                        <div class="field">
                            <label for="doctorName">Tên bác sĩ</label>
                            <input id="doctorName" name="doctorName" type="text"
                                   placeholder="VD: Nguyễn Văn A"
                                   value="${fn:escapeXml(param.doctorName)}">
                    </div>

                    <!-- ĐỔI: tìm theo TÊN dịch vụ -->
                    <div class="field">
                        <label for="serviceName">Tên dịch vụ</label>
                        <input id="serviceName" name="serviceName" type="text"
                               placeholder="VD: Lấy cao răng"
                               value="${fn:escapeXml(param.serviceName)}">
                    </div>

                    <div class="field">
                        <label for="fromDate">Từ ngày</label>
                        <input id="fromDate" name="fromDate" type="date" value="${param.fromDate}">
                    </div>

                    <div class="field">
                        <label for="toDate">Đến ngày</label>
                        <input id="toDate" name="toDate" type="date" value="${param.toDate}">
                    </div>

                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Lọc</button>
                        <a href="${pageContext.request.contextPath}/medicalHistory" class="btn btn-ghost">Xoá lọc</a>
                    </div>
                </form>
            </div>

            <!-- Danh sách lịch sử -->
            <c:choose>
                <c:when test="${empty appointments}">
                    <p class="muted">Bạn chưa có lịch sử khám hoặc không có dữ liệu phù hợp bộ lọc.</p>
                </c:when>
                <c:otherwise>
                    <div class="list">
                        <c:forEach var="a" items="${appointments}">
                            <c:set var="rec"  value="${recordsMap[a.appointmentId]}"/>
                            <c:set var="pres" value="${prescriptionsMap[a.appointmentId]}"/>

                            <div class="card">
                                <div class="card-head">
                                    <div class="doc-serv">
                                        <div class="doc-name">
                                            <c:choose>
                                                <c:when test="${not empty a.doctorId and not empty a.doctorId.userId}">
                                                    ${a.doctorId.userId.fullName}
                                                </c:when>
                                                <c:otherwise>
                                                    Bác sĩ #${a.doctorId.doctorID}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                        <div class="serv-name">
                                            <c:choose>
                                                <c:when test="${not empty a.serviceId}">
                                                    ${a.serviceId.serviceName}
                                                </c:when>
                                                <c:otherwise>
                                                    Dịch vụ #${a.serviceId.serviceId}
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>

                                    <div class="right">
                                        <div class="dt">
                                            Ngày: <fmt:formatDate value="${a.appointmentDate}" pattern="dd/MM/yyyy"/>
                                        </div>
                                        <div>Giờ: ${a.startTime} – ${a.endTime}</div>
                                        <div style="margin-top:6px;">
                                            <span class="status-badge status-${a.status}">
                                                ${a.status}
                                            </span>
                                        </div>
                                    </div>
                                </div>

                                <div class="split"></div>

                                <div class="sections">
                                    <!-- Hồ sơ khám -->
                                    <div>
                                        <div class="section-title">Kết quả khám (Medical Record)</div>
                                        <c:choose>
                                            <c:when test="${not empty rec}">
                                                <div class="muted">Chẩn đoán: <strong>${rec.diagnosis}</strong></div>
                                                <div class="muted">Triệu chứng: ${rec.symptoms}</div>
                                                <c:if test="${not empty rec.followUpDate}">
                                                    <div class="muted">Tái khám: <fmt:formatDate value="${rec.followUpDate}" pattern="dd/MM/yyyy"/></div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="muted">Chưa có hồ sơ khám cho lịch hẹn này.</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>

                                    <!-- Đơn thuốc -->
                                    <div>
                                        <div class="section-title">Đơn thuốc (Prescription)</div>
                                        <c:choose>
                                            <c:when test="${not empty pres}">
                                                <div class="muted">
                                                    Ngày kê: <fmt:formatDate value="${pres.issueDate}" pattern="dd/MM/yyyy HH:mm"/>
                                                </div>
                                                <c:if test="${not empty pres.instructions}">
                                                    <div class="muted">Hướng dẫn: ${pres.instructions}</div>
                                                </c:if>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="muted">Chưa có đơn thuốc cho lịch hẹn này.</div>
                                            </c:otherwise>
                                        </c:choose>
                                    </div>
                                </div>

                                <c:if test="${a.status == 'Scheduled' || a.status == 'Rescheduled'}">
                                    <div class="actions" style="margin-top:12px; display:flex; gap:8px; flex-wrap:wrap;">

                                        <!-- HỦY LỊCH HẸN -->
                                        <form method="post"
                                              action="${pageContext.request.contextPath}/cancelOrChangeAppointment"
                                              onsubmit="return confirm('Bạn có chắc chắn muốn hủy lịch hẹn này không?');">
                                            <input type="hidden" name="action" value="cancel"/>
                                            <input type="hidden" name="appointmentId" value="${a.appointmentId}"/>
                                            <button type="submit" class="btn btn-ghost" style="border-color:#dc3545; color:#dc3545;">
                                                Hủy lịch hẹn
                                            </button>
                                        </form>

                                        <!-- THAY ĐỔI LỊCH HẸN -->
                                        <form method="get"
                                              action="${pageContext.request.contextPath}/loadRescheduleAppointment">
                                            <input type="hidden" name="appointmentId" value="${a.appointmentId}"/>
                                            <button type="submit" class="btn btn-primary">
                                                Thay đổi lịch hẹn
                                            </button>
                                        </form>

                                    </div>
                                </c:if>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>

            <!-- PHÂN TRANG -->
            <jsp:include page="../../common/pagination.jsp">
                <jsp:param name="baseUrl" value="/medicalHistory"/>
                <jsp:param name="page" value="${page}"/>
                <jsp:param name="size" value="${size}"/>
                <jsp:param name="totalPages" value="${totalPages}"/>
                <jsp:param name="status" value="${param.status}"/>
                <jsp:param name="doctorName" value="${param.doctorName}"/>
                <jsp:param name="serviceName" value="${param.serviceName}"/>
                <jsp:param name="fromDate" value="${param.fromDate}"/>
                <jsp:param name="toDate" value="${param.toDate}"/>
            </jsp:include>
            <!-- /PHÂN TRANG -->

        </div>

        <!-- Footer -->
        <jsp:include page="../../common/footer.jsp"></jsp:include>

    </body>
</html>
