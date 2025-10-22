/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.AppointmentsDao;
import dto.AppointmentDetailDto;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

/**
 *
 * @author vuhieu
 */
@WebServlet(name = "ReceptionistDashboardController", urlPatterns = { "/receptionist/dashboard" })
public class ReceptionistDashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy ngày hiện tại
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        // 2. Gọi DAO để lấy danh sách các cuộc hẹn đã check-in trong ngày
        AppointmentsDao appointmentsDao = new AppointmentsDao();
        List<AppointmentDetailDto> checkedInList = appointmentsDao.getCheckedInAppointmentsForDate(currentDate);

        // 3. Đặt danh sách vào request attribute để JSP có thể truy cập
        request.setAttribute("checkedInList", checkedInList);

        // 4. Chuyển tiếp đến trang dashboard
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/receptionist/dashboard.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
