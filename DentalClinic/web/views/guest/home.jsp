<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Dental Clinic - Home</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="font-sans text-gray-800">
        <!-- Header -->
        <jsp:include page="../../common/header.jsp"></jsp:include>

        <!-- Hero Section -->
        <jsp:include page="../../common/banner.jsp"></jsp:include>

        <!-- Services Section -->
        <jsp:include page="../../common/feature.jsp"></jsp:include>

        <!-- Book Appointment Section -->
        <jsp:include page="../../common/body.jsp"></jsp:include>

        <!-- Footer -->
        <jsp:include page="../../common/footer.jsp"></jsp:include>

        <!-- JavaScript for Mobile Menu Toggle -->
        <script>
            const menuToggle = document.getElementById('menu-toggle');
            const mobileMenu = document.getElementById('mobile-menu');

            menuToggle.addEventListener('click', () => {
                mobileMenu.classList.toggle('hidden');
            });
        </script>
    </body>
</html>