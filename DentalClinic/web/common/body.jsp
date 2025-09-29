<%-- 
    Document   : body
    Created on : Sep 17, 2025, 12:06:56 AM
    Author     : Nguyen Dinh Giap
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<section id="book-appointment" class="bg-blue-50 py-16">
            <div class="container mx-auto px-4">
                <h2 class="text-3xl font-bold text-center mb-12">Book Your Appointment</h2>
                <div class="max-w-lg mx-auto bg-white p-6 rounded-lg shadow-md">
                    <form>
                        <div class="mb-4">
                            <label for="doctor" class="block text-gray-700">Select Doctor</label>
                            <select id="doctor" class="w-full p-2 border rounded-lg">
                                <option value="">Choose a doctor</option>
                                <option value="dr1">Dr. John Smith</option>
                                <option value="dr2">Dr. Jane Doe</option>
                            </select>
                        </div>
                        <div class="mb-4">
                            <label for="date" class="block text-gray-700">Select Date</label>
                            <input type="date" id="date" class="w-full p-2 border rounded-lg">
                        </div>
                        <div class="mb-4">
                            <label for="service" class="block text-gray-700">Select Service</label>
                            <select id="service" class="w-full p-2 border rounded-lg">
                                <option value="">Choose a service</option>
                                <option value="checkup">General Checkup</option>
                                <option value="cleaning">Teeth Cleaning</option>
                                <option value="orthodontics">Orthodontics</option>
                            </select>
                        </div>
                        <button type="submit" class="w-full bg-blue-600 text-white py-3 rounded-lg hover:bg-blue-700">Book Now</button>
                    </form>
                </div>
            </div>
        </section>