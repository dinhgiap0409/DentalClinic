<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thêm dịch vụ mới</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/dashboard.css">
    <style>
        .form-container {
            background: #fff;
            width: 70%;
            margin: 30px auto;
            padding: 20px 30px;
            border-radius: 12px;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
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
            font-weight: 600;
            margin-bottom: 5px;
        }
        input[type="text"], input[type="number"], textarea, select {
            width: 100%;
            padding: 10px;
            border: 1px solid #ccc;
            border-radius: 6px;
            font-size: 15px;
        }
        textarea {
            resize: vertical;
            height: 100px;
        }
        .form-group img {
            display: block;
            margin-top: 10px;
            width: 150px;
            height: 150px;
            object-fit: cover;
            border-radius: 10px;
        }
        .btn {
            display: inline-block;
            padding: 10px 18px;
            border-radius: 6px;
            border: none;
            font-size: 15px;
            cursor: pointer;
        }
        .btn-primary {
            background-color: #007bff;
            color: #fff;
        }
        .btn-secondary {
            background-color: #6c757d;
            color: #fff;
            text-decoration: none;
        }
        .btn:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
    <div class="dashboard">
        <jsp:include page="../../common/sidebar.jsp"></jsp:include>

        <div class="content">
            <div class="form-container">
                <h1>Thêm dịch vụ mới</h1>
                <form action="${pageContext.request.contextPath}/manageService?action=add" 
                      method="post" enctype="multipart/form-data">

                    <div class="form-group">
                        <label for="serviceName">Tên dịch vụ</label>
                        <input type="text" name="serviceName" id="serviceName" required>
                    </div>

                    <div class="form-group">
                        <label for="description">Mô tả</label>
                        <textarea name="description" id="description" required></textarea>
                    </div>

                    <div class="form-group">
                        <label for="price">Giá (VNĐ)</label>
                        <input type="number" name="price" id="price" required min="0" step="1000">
                    </div>

                    <div class="form-group">
                        <label for="duration">Thời lượng (phút)</label>
                        <input type="number" name="duration" id="duration" required min="1">
                    </div>

                    <div class="form-group">
                        <label for="isActive">Trạng thái</label>
                        <select name="isActive" id="isActive">
                            <option value="true">Hoạt động</option>
                            <option value="false">Ngừng</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="image">Ảnh minh họa</label>
                        <input type="file" name="image" id="image" accept="image/*" onchange="previewImage(event)">
                        <img id="preview" src="${pageContext.request.contextPath}/images/no-image.png" alt="Preview">
                    </div>

                    <div style="text-align:center; margin-top:25px;">
                        <button type="submit" class="btn btn-primary">Thêm dịch vụ</button>
                        <a href="${pageContext.request.contextPath}/manageService?action=view" class="btn btn-secondary">← Quay lại</a>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script>
        function previewImage(event) {
            const reader = new FileReader();
            reader.onload = function() {
                document.getElementById('preview').src = reader.result;
            };
            reader.readAsDataURL(event.target.files[0]);
        }
    </script>
</body>
</html>
F