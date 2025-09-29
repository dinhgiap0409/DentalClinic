<%-- 
    Document   : feature
    Created on : Sep 17, 2025, 12:05:55 AM
    Author     : Nguyen Dinh Giap
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<section class="services">
    <div class="service">
        <img src="/DentalClinic/img/dept-1.jpg" alt="Dental Implants">
        <h3>Dental Implants</h3>
        <p>Even the all-powerful Pointing has no control...</p>
    </div>

    <div class="service">
        <img src="/DentalClinic/img/dept-2.jpg" alt="Cosmetic Dentistry">
        <h3>Cosmetic Dentistry</h3>
        <p>Even the all-powerful Pointing has no control...</p>
    </div>

    <div class="service">
        <img src="/DentalClinic/img/dept-3.jpg" alt="Dental Care">
        <h3>Dental Care</h3>
        <p>Even the all-powerful Pointing has no control...</p>
    </div>

    <div class="service">
        <img src="/DentalClinic/img/dept-4.jpg" alt="Teeth Whitening">
        <h3>Teeth Whitening</h3>
        <p>Even the all-powerful Pointing has no control...</p>
    </div>

    <div class="service">
        <img src="/DentalClinic/img/dept-5.jpg" alt="Dental Calculus">
        <h3>Dental Calculus</h3>
        <p>Even the all-powerful Pointing has no control...</p>
    </div>

    <div class="service">
        <img src="/DentalClinic/img/dept-6.jpg" alt="Periodontics">
        <h3>Periodontics</h3>
        <p>Even the all-powerful Pointing has no control...</p>
    </div>
</section>



<!--css-->
<style>
    .services {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
        gap: 20px;
        padding: 40px;
        background: #f9f9f9;
    }

    .service {
        background: #fff;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 4px 6px rgba(0,0,0,0.1);
        text-align: center;
        transition: transform 0.3s ease;
    }

    .service:hover {
        transform: translateY(-5px);
    }

    .service img {
        width: 100%;
        height: 200px;
        object-fit: cover;
        border-radius: 8px;
        margin-bottom: 15px;
    }

    .service h3 {
        font-size: 20px;
        margin-bottom: 10px;
        color: #333;
    }

    .service p {
        font-size: 14px;
        color: #666;
    }
</style>
