<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Chỉnh sửa dịch vụ</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
        <style>
            .detail-container {
                background: #fff;
                padding: 20px;
                border-radius: 10px;
                box-shadow: 0 1px 4px rgba(0,0,0,0.1);
                width: 70%;
                margin: 30px auto;
            }
            h1 {
                text-align: center;
                margin-bottom: 25px;
            }
            .form-group {
                margin-bottom: 15px;
            }
            label {
                display: block;
                font-weight: bold;
                margin-bottom: 5px;
            }
            input[type="text"],
            input[type="number"],
            textarea,
            select {
                width: 100%;
                padding: 8px;
                border: 1px solid #ccc;
                border-radius: 5px;
            }
            .service-img {
                width: 150px;
                height: 150px;
                object-fit: cover;
                border-radius: 10px;
                margin-bottom: 10px;
            }
            .btn {
                padding: 10px 16px;
                border: none;
                border-radius: 6px;
                cursor: pointer;
            }
            .btn-save {
                background: #28a745;
                color: #fff;
            }
            .btn-back {
                background: #6c757d;
                color: #fff;
                text-decoration: none;
                display: inline-block;
                padding: 10px 16px;
                border-radius: 6px;
                margin-left: 10px;
            }
        </style>
    </head>
    <body>
        <div class="dashboard">
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

            <div class="content">
                <div class="detail-container">
                    <h1>Chỉnh sửa dịch vụ</h1>

                    <form action="manageService" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="serviceId" value="${service.serviceId}">

                        <div class="form-group">
                            <label>Tên dịch vụ:</label>
                            <input type="text" name="serviceName" value="${service.serviceName}" required>
                        </div>

                        <div class="form-group">
                            <label>Mô tả:</label>
                            <textarea name="description" rows="4" required>${service.description}</textarea>
                        </div>

                        <div class="form-group">
                            <label>Giá (VNĐ):</label>
                            <input type="number" name="price" value="${service.price}" step="1000" required>
                        </div>

                        <div class="form-group">
                            <label>Thời lượng (phút):</label>
                            <input type="number" name="duration" value="${service.duration}" required>
                        </div>

                        <div class="form-group">
                            <label>Trạng thái:</label>
                            <select name="isActive">
                                <option value="true" ${service.isActive ? "selected" : ""}>Hoạt động</option>
                                <option value="false" ${!service.isActive ? "selected" : ""}>Ngừng</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label>Ảnh hiện tại:</label><br>
                            <c:if test="${not empty service.image}">
                                <img src="${pageContext.request.contextPath}/uploads/${service.image}" class="service-img">
                            </c:if>
                            <c:if test="${empty service.image}">
                                <img src="${pageContext.request.contextPath}/images/no-image.png" class="service-img">
                            </c:if>
                        </div>

                        <div class="form-group">
                            <label>Chọn ảnh mới (nếu muốn thay):</label>
                            <input type="file" name="image" accept="image/*">
                        </div>

                        <div style="text-align:center; margin-top:25px;">
                            <button type="submit" class="btn btn-save">💾 Lưu thay đổi</button>
                            <a href="${pageContext.request.contextPath}/manageService?action=view" class="btn-back">← Quay lại</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </body>
</html>
F