/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.AppointmentsDao;
import dto.AppointmentDetailDto;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import utils.ProgramException;
import utils.Validation;

/**
 *
 * @author vuhieu
 */
@WebServlet(name = "ReceptionistSerchController", urlPatterns = { "/receptionist-searchandcheckin" })
public class ReceptionistSerchController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String phoneNumber = request.getParameter("phoneNumber");

        // Chỉ thực hiện tìm kiếm nếu có số điện thoại được cung cấp
        if (phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            try {
                // Bước 1: Kiểm tra tính hợp lệ của số điện thoại
                Validation.checkPhoneNumber(phoneNumber);

                // Bước 2: Nếu hợp lệ, gọi DAO để tìm kiếm các cuộc hẹn
                AppointmentsDao appointmentsDao = new AppointmentsDao();
                List<AppointmentDetailDto> appointmentList = appointmentsDao
                        .getAppointmentDetailsByPatientPhone(phoneNumber);

                // Bước 3: Đặt kết quả tìm kiếm vào request để hiển thị trên JSP
                request.setAttribute("appointmentList", appointmentList);

            } catch (ProgramException e) {
                // Nếu có lỗi validation, đặt thông báo lỗi vào request
                request.setAttribute("error", e.getMessage());
            }
        }

        // Dù thành công hay thất bại, vẫn giữ lại số điện thoại đã nhập
        request.setAttribute("phoneNumber", phoneNumber);
        // Thêm ngày hiện tại vào request để so sánh trong JSP
        request.setAttribute("currentDate", new java.sql.Date(System.currentTimeMillis()));
        // Chuyển tiếp về trang search.jsp để hiển thị kết quả hoặc lỗi
        RequestDispatcher dispatcher = request.getRequestDispatcher("views/receptionist/search.jsp");
        dispatcher.forward(request, response);
    }
}