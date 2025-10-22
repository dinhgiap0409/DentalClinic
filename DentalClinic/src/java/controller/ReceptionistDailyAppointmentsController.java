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
@WebServlet(name = "ReceptionistDailyAppointmentsController", urlPatterns = { "/receptionist/daily-appointments" })
public class ReceptionistDailyAppointmentsController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 1. Lấy ngày hiện tại
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis());

        // 2. Gọi DAO để lấy danh sách tất cả các cuộc hẹn trong ngày
        AppointmentsDao appointmentsDao = new AppointmentsDao();
        List<AppointmentDetailDto> dailyAppointmentsList = appointmentsDao.getAppointmentsByDate(currentDate);

        // 3. Đặt danh sách và ngày đang xem vào request attribute để JSP có thể truy
        // cập
        request.setAttribute("dailyAppointmentsList", dailyAppointmentsList);
        request.setAttribute("viewDate", currentDate);
        request.setAttribute("currentDate", currentDate); // Thêm để logic JSP nhất quán

        // 4. Chuyển tiếp đến trang JSP để hiển thị
        RequestDispatcher dispatcher = request.getRequestDispatcher("/views/receptionist/daily-appointments.jsp");
        dispatcher.forward(request, response);
    }

}