<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Thêm thuốc mới</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .form-container {
                background: #fff;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 1px 5px rgba(0,0,0,0.15);
                width: 70%;
                margin: 30px auto;
            }
            h1 {
                text-align: center;
                margin-bottom: 25px;
            }
            form label {
                display: block;
                font-weight: bold;
                margin-top: 12px;
            }
            form input[type="text"],
            form input[type="number"],
            form input[type="date"],
            form textarea {
                width: 100%;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 6px;
                margin-top: 5px;
            }
            textarea {
                resize: vertical;
                height: 100px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .form-actions {
                margin-top: 25px;
                text-align: center;
            }
            .btn {
                padding: 8px 15px;
                border-radius: 6px;
                text-decoration: none;
                font-weight: bold;
            }
            .btn-add {
                background: #28a745;
                color: #fff;
                border: none;
            }
            .btn-add:hover {
                background: #1e7e34;
            }
            .back-btn {
                background: #6c757d;
                color: #fff;
                margin-right: 10px;
            }
            .status-checkbox {
                margin-top: 10px;
            }
        </style>
    </head>

    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <div class="content">
                    <div class="form-container">
                        <h1>Thêm thuốc mới</h1>

                    <c:if test="${not empty error}">
                        <div style="color:red; text-align:center;">${error}</div>
                    </c:if>

                    <form action="${pageContext.request.contextPath}/manageMedications?action=add" method="post">

                        <div class="form-group">
                            <label>Tên thuốc:</label>
                            <input type="text" name="medicationName" placeholder="Nhập tên thuốc..." required>
                        </div>

                        <div class="form-group">
                            <label>Mô tả:</label>
                            <textarea name="description" placeholder="Nhập mô tả thuốc..."></textarea>
                        </div>

                        <div class="form-group">
                            <label>Giá (VNĐ):</label>
                            <input type="number" step="0.01" name="price" placeholder="Nhập giá..." required>
                        </div>

                        <div class="form-group">
                            <label>Số lượng tồn kho:</label>
                            <input type="number" name="stockQuantity" placeholder="Nhập số lượng..." min="0" required>
                        </div>

                        <div class="form-group">
                            <label>Hạn sử dụng:</label>
                            <input type="date" name="expiryDate">
                        </div>

                        <div class="form-group">
                            <label>Nhà sản xuất:</label>
                            <input type="text" name="manufacturer" placeholder="Nhập tên nhà sản xuất...">
                        </div>

                        <div class="form-group">
                            <label>Trạng thái:</label>
                            <div class="status-checkbox">
                                <input type="checkbox" name="isActive" checked> Hoạt động
                            </div>
                        </div>

                        <div class="form-actions">
                            <a href="${pageContext.request.contextPath}/manageMedications?action=list" class="btn back-btn">← Quay lại</a>
                            <button type="submit" class="btn btn-add">Thêm thuốc</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
