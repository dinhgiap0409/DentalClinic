<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Tạo đơn thuốc - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            table.data-table th, table.data-table td {
                padding: 8px;
                border: 1px solid #ddd;
            }
            table.data-table {
                border-collapse: collapse;
                width: 100%;
                margin-bottom: 20px;
            }
            .total-amount {
                font-weight: bold;
                margin-top: 10px;
            }
            input.quantity-input {
                width: 60px;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <div class="content">
                    <div class="header">
                        <h1>Tạo đơn thuốc</h1>
                    </div>

                <c:if test="${not empty error}">
                    <div class="alert alert-error">${error}</div>
                </c:if>

                <!-- Thông tin lịch hẹn -->
                <h3>Thông tin lịch hẹn</h3>
                <p>Mã lịch hẹn: ${appointment.appointmentId}</p>
                <p>Bệnh nhân: ${appointment.patientId.userID.fullName} - ${appointment.patientId.userID.phoneNumber}</p>
                <p>Bác sĩ: ${appointment.doctorId.userId.fullName}</p>
                <p>Ngày hẹn: <fmt:formatDate value="${appointment.appointmentDate}" pattern="dd/MM/yyyy"/></p>

                <!-- Phí khám bác sĩ -->
                <p>Phí khám bác sĩ: 
                    <span id="consultationFee">
                        <fmt:formatNumber value="${appointment.doctorId.consultationFee}" type="currency" currencySymbol="₫"/>
                    </span>
                </p>

                <!-- Thông tin hồ sơ -->
                <h3>Hồ sơ bệnh án</h3>
                <p>Mã hồ sơ: ${record.recordId}</p>
                <p>Chẩn đoán: ${record.diagnosis}</p>

                <!-- Thông tin đơn thuốc -->
                <c:if test="${not empty prescription}">
                    <h3>Đơn thuốc</h3>
                    <form action="${pageContext.request.contextPath}/createOrder" method="post" id="orderForm">
                        <input type="hidden" name="appointmentId" value="${appointment.appointmentId}">
                        <input type="hidden" name="patientId" value="${appointment.patientId.patientID}">

                        <table class="data-table">
                            <thead>
                                <tr>
                                    <th>STT</th>
                                    <th>Chọn</th>
                                    <th>Tên thuốc</th>
                                    <th>Liều lượng / hướng dẫn</th>
                                    <th>Giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="d" items="${details}" varStatus="status">
                                    <tr>
                                        <td>${status.index + 1}</td>
                                        <td>
                                            <input type="checkbox" class="med-checkbox">
                                        </td>
                                        <td>${d.medications.medicationName}</td>
                                        <td>${d.dosage} / ${d.duration}</td>
                                        <td><fmt:formatNumber value="${d.medications.price}" type="currency" currencySymbol="₫"/></td>
                                        <td>
                                            <!-- xóa name để tránh gửi thừa, chỉ dùng JS tạo input ẩn -->
                                            <input type="number" class="quantity-input" min="0" value="1"
                                                   data-price="${d.medications.price}" data-medid="${d.medications.medicationId}" disabled>
                                        </td>
                                        <td class="line-total">0 ₫</td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>

                        <p class="total-amount">
                            Tổng tiền: <span id="totalAmount">0 ₫</span> 
                            <small>(bao gồm phí khám bác sĩ)</small>
                        </p>

                        <!-- ẩn các input thuốc gửi server -->
                        <div id="orderDetailsInputs"></div>

                        <button type="submit">Tạo đơn</button>
                    </form>
                </c:if>

                <c:if test="${empty prescription}">
                    <p>Không có đơn thuốc nào được tạo cho hồ sơ này.</p>
                </c:if>
            </div>
        </div>

        <script>
            const checkboxes = document.querySelectorAll('.med-checkbox');
            const quantityInputs = document.querySelectorAll('input.quantity-input');
            const totalAmountEl = document.getElementById('totalAmount');
            const orderDetailsInputs = document.getElementById('orderDetailsInputs');

            // Lấy phí khám bác sĩ từ JSP
            const consultationFee = parseFloat('${appointment.doctorId.consultationFee}');

            function updateTotals() {
                let total = consultationFee; // bắt đầu bằng phí khám
                orderDetailsInputs.innerHTML = '';

                quantityInputs.forEach((input, index) => {
                    const checkbox = checkboxes[index];
                    const qty = checkbox.checked ? parseInt(input.value) || 0 : 0;
                    const price = parseFloat(input.dataset.price);
                    const medId = input.dataset.medid;
                    const lineTotal = qty * price;

                    const row = input.closest('tr');
                    row.querySelector('.line-total').textContent = lineTotal.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});

                    total += lineTotal;

                    if (qty > 0) {
                        // tạo input ẩn gửi server
                        const medInput = document.createElement('input');
                        medInput.type = 'hidden';
                        medInput.name = 'medicationId';
                        medInput.value = medId;
                        orderDetailsInputs.appendChild(medInput);

                        const qtyInput = document.createElement('input');
                        qtyInput.type = 'hidden';
                        qtyInput.name = 'quantity';
                        qtyInput.value = qty;
                        orderDetailsInputs.appendChild(qtyInput);
                    }
                });

                totalAmountEl.textContent = total.toLocaleString('vi-VN', {style: 'currency', currency: 'VND'});
            }

            checkboxes.forEach((cb, i) => {
                cb.addEventListener('change', () => {
                    quantityInputs[i].disabled = !cb.checked;
                    updateTotals();
                });
            });

            quantityInputs.forEach(input => input.addEventListener('input', updateTotals));

            updateTotals(); // khởi tạo tổng tiền khi load trang
        </script>
    </body>
</html>
