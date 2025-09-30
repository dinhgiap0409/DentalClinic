<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dental Clinic - Home</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 0; background: #f5f5f5; }
        .container { max-width: 1200px; margin: 0 auto; padding: 20px; }
        .hero { background: white; padding: 40px; text-align: center; margin: 20px 0; border: 1px solid #ddd; }
        .services { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; margin: 20px 0; }
        .service { background: white; padding: 20px; border: 1px solid #ddd; }
        .btn { background: #007bff; color: white; padding: 10px 20px; text-decoration: none; margin: 5px; }
        .btn:hover { background: #0056b3; }
        h1 { color: #333; }
        h2 { color: #333; margin-top: 30px; }
        h3 { color: #333; }
    </style>
</head>
<body>
    <!-- Header -->
    <jsp:include page="../../common/header.jsp"></jsp:include>

    <div class="container">
        <!-- Hero Section -->
        <div class="hero">
            <h1>Dental Clinic</h1>
            <p>Dịch vụ nha khoa chuyên nghiệp</p>
            <a href="/DentalClinic/login" class="btn">Đăng nhập</a>
            <a href="/DentalClinic/register" class="btn">Đăng ký</a>
        </div>

        <!-- Services Section -->
        <h2>Dịch vụ</h2>
        <div class="services">
            <div class="service">
                <img src="/DentalClinic/img/dept-1.jpg" alt="Khám răng" style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                <h3>Khám răng</h3>
                <p>Kiểm tra sức khỏe răng miệng</p>
            </div>
            <div class="service">
                <img src="/DentalClinic/img/dept-2.jpg" alt="Trám răng" style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                <h3>Trám răng</h3>
                <p>Điều trị sâu răng</p>
            </div>
            <div class="service">
                <img src="/DentalClinic/img/dept-3.jpg" alt="Tẩy trắng" style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                <h3>Tẩy trắng</h3>
                <p>Làm trắng răng</p>
            </div>
            <div class="service">
                <img src="/DentalClinic/img/dept-4.jpg" alt="Niềng răng" style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                <h3>Niềng răng</h3>
                <p>Chỉnh nha</p>
            </div>
        </div>

        <!-- Doctors Section -->
        <h2>Bác sĩ</h2>
        <div class="services">
            <div class="service">
                <img src="/DentalClinic/img/dept-5.jpg" alt="Bác sĩ Nguyễn Văn A" style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                <h3>Bác sĩ Nguyễn Văn A</h3>
                <p>Chuyên khoa: Niềng răng</p>
                <p>Kinh nghiệm: 10 năm</p>
            </div>
            <div class="service">
                <img src="/DentalClinic/img/dept-6.jpg" alt="Bác sĩ Trần Thị B" style="width: 100%; height: 200px; object-fit: cover; margin-bottom: 15px;">
                <h3>Bác sĩ Trần Thị B</h3>
                <p>Chuyên khoa: Phẫu thuật răng</p>
                <p>Kinh nghiệm: 8 năm</p>
            </div>
        </div>
    </div>

    <!-- Footer -->
    <jsp:include page="../../common/footer.jsp"></jsp:include>
</body>
</html>