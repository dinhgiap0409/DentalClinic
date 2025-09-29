<%-- 
    Document   : header
    Created on : Sep 16, 2025, 11:49:47 PM
    Author     : Nguyen Dinh Giap
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<header class="bg-blue-600 text-white sticky top-0 z-50 shadow-md">
    <div class="container mx-auto px-4 py-4 flex justify-between items-center">
        <div class="text-2xl font-bold">
            <a href="/">Dental Clinic</a>
        </div>
        <nav class="hidden md:flex space-x-6">
            <a href="#home" class="hover:text-blue-200">Home</a>
            <a href="#services" class="hover:text-blue-200">Services</a>
            <a href="#book-appointment" class="hover:text-blue-200">Book Appointment</a>
            <a href="#login" class="hover:text-blue-200">Login</a>
            <a href="#register" class="hover:text-blue-200">Register</a>
        </nav>
        <div class="md:hidden">
            <button id="menu-toggle" class="focus:outline-none">
                <svg class="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24" xmlns="http://www.w3.org/2000/svg">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16m-7 6h7"></path>
                </svg>
            </button>
        </div>
    </div>
    <!-- Mobile Menu -->
    <div id="mobile-menu" class="hidden md:hidden bg-blue-600 text-white">
        <nav class="flex flex-col space-y-4 py-4 px-4">
            <a href="#home" class="hover:text-blue-200">Home</a>
            <a href="#services" class="hover:text-blue-200">Services</a>
            <a href="#book-appointment" class="hover:text-blue-200">Book Appointment</a>
            <a href="#login" class="hover:text-blue-200">Login</a>
            <a href="#register" class="hover:text-blue-200">Register</a>
        </nav>
    </div>
</header>
