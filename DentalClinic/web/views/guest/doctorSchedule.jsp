<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="schedule-section">
    <h4>Chọn khung giờ làm việc</h4>

    <c:choose>
        <c:when test="${not empty availableSlots}">
            <form action="/DentalClinic/CustomersBookSchedules" method="post">
                <input type="hidden" name="doctorId" value="${doctorId}">
                <input type="hidden" name="serviceId" value="${serviceId}">
                <input type="hidden" name="appointmentDate" value="${selectedDate}">

                <div class="slot-list">
                    <c:forEach var="slot" items="${availableSlots}">
                        <label class="slot-item">
                            <input 
                                type="radio" 
                                name="slot"
                                value="${slot.startTime}-${slot.endTime}"
                                required
                                onclick="selectSlot('${doctorId}', '${slot.startTime}', '${slot.endTime}')">
                            ${slot.startTime} - ${slot.endTime}
                        </label>
                    </c:forEach>
                </div>

                <div class="notes-box">
                    <label for="notes-${doctorId}">Ghi chú (nếu có):</label>
                    <textarea name="notes" id="notes-${doctorId}" placeholder="Ví dụ: đau răng bên trái, khám nhanh..." rows="3"></textarea>
                </div>

                <button type="submit" class="btn-confirm">Xác nhận đặt lịch</button>
            </form>
        </c:when>
        <c:otherwise>
            <p style="color: gray;">Không có khung giờ trống cho ngày hôm nay.</p>
        </c:otherwise>
    </c:choose>
</div>

<script>
    // Hàm chọn khung giờ -> lưu vào hidden input
    function selectSlot(doctorId, start, end) {
        document.getElementById("startTime-" + doctorId).value = start;
        document.getElementById("endTime-" + doctorId).value = end;
    }
</script>
