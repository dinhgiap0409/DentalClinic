<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Quản lý thuốc - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .filter-box {
                background: #fff;
                padding: 15px;
                margin-bottom: 20px;
                border-radius: 10px;
                box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            }
            .filter-box input, .filter-box select {
                padding: 6px;
                margin-right: 8px;
                border-radius: 6px;
                border: 1px solid #ccc;
            }
            .filter-box button, .filter-box .reset-btn {
                padding: 6px 12px;
                border-radius: 6px;
            }
            .btn-add {
                background: #28a745;
                color: white;
                padding: 8px 14px;
                border-radius: 6px;
                text-decoration: none;
                margin-bottom: 10px;
                display: inline-block;
            }
            .data-table th, .data-table td {
                text-align: center;
            }
            .status {
                font-weight: bold;
            }
            .status.true {
                color: green;
            }
            .status.false {
                color: red;
            }
        </style>
    </head>

    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <div class="content">
                    <div class="header">
                        <h1>Quản lý thuốc</h1>
                    </div>

                    <!-- Form lọc -->
                    <div class="filter-box">
                        <form action="${pageContext.request.contextPath}/manageMedications" method="get">
                        <input type="hidden" name="action" value="list">
                        <input type="text" name="name" placeholder="Tên thuốc" value="${param.name}">
                        <input type="text" name="manufacturer" placeholder="Nhà sản xuất" value="${param.manufacturer}">
                        <input type="number" step="0.01" name="priceFrom" placeholder="Giá từ" value="${param.priceFrom}">
                        <input type="number" step="0.01" name="priceTo" placeholder="Giá đến" value="${param.priceTo}">
                        <input type="date" name="expiryDateFrom" value="${param.expiryDateFrom}">
                        <input type="date" name="expiryDateTo" value="${param.expiryDateTo}">
                        <select name="isActive">
                            <option value="">--Trạng thái--</option>
                            <option value="true" ${param.isActive == 'true' ? 'selected' : ''}>Hoạt động</option>
                            <option value="false" ${param.isActive == 'false' ? 'selected' : ''}>Ngừng</option>
                        </select>
                        <select name="sort">
                            <option value="asc" ${param.sort == 'asc' ? 'selected' : ''}>Sắp xếp: Tăng dần</option>
                            <option value="desc" ${param.sort == 'desc' ? 'selected' : ''}>Giảm dần</option>
                        </select>
                        <button type="submit">Lọc</button>
                        <a href="${pageContext.request.contextPath}/manageMedications?action=list" class="reset-btn">Bỏ lọc</a>
                    </form>
                </div>

                <a href="${pageContext.request.contextPath}/views/manage/medication-form.jsp" class="btn-add">+ Thêm thuốc mới</a>

                <!-- Bảng danh sách -->
                <div class="table-container">
                    <c:choose>
                        <c:when test="${empty medications}">
                            <p class="muted text-center" style="padding:15px;">Không có thuốc nào được tìm thấy.</p>
                        </c:when>

                        <c:otherwise>
                            <table class="data-table">
                                <thead>
                                    <tr>
                                        <th>Mã</th>
                                        <th>Tên thuốc</th>
                                        <th>Giá</th>
                                        <th>Số lượng</th>
                                        <th>Hạn sử dụng</th>
                                        <th>Nhà sản xuất</th>
                                        <th>Trạng thái</th>
                                        <th>Thao tác</th>
                                    </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="m" items="${medications}">
                                    <tr>
                                        <td>${m.medicationId}</td>
                                        <td>${m.medicationName}</td>
                                        <td><fmt:formatNumber value="${m.price}" type="currency" currencySymbol="₫" maxFractionDigits="0"/></td>
                                    <td>${m.stockQuantity}</td>
                                    <td><fmt:formatDate value="${m.expiryDate}" pattern="dd/MM/yyyy"/></td>
                                    <td>${m.manufacturer}</td>
                                    <td><span class="status ${m.isActive}">${m.isActive ? 'Hoạt động' : 'Ngừng'}</span></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/manageMedications?action=view&id=${m.medicationId}" class="btn btn-detail">Chi tiết</a>
                                    </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </c:otherwise>
                    </c:choose>

                    <!-- Phân trang -->
                    <jsp:include page="../../common/pagination.jsp">
                        <jsp:param name="baseUrl" value="${pageContext.request.contextPath}/manageMedications?action=list" />
                        <jsp:param name="page" value="${currentPage}" />
                        <jsp:param name="size" value="10" />
                        <jsp:param name="totalPages" value="${totalPages}" />
                    </jsp:include>
                </div>
            </div>
        </div>
    </body>
</html>
