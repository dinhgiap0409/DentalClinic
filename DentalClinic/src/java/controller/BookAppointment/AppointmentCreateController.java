/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.BookAppointment;

import dal.DoctorDao;
import dal.PatientDao;
import dal.ServiceDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Doctor;
import model.Patients;
import model.Service;

/**
 *
 * @author TNO
 */
@WebServlet(name = "AppointmentCreateController", urlPatterns = {"/appointmentCreate"})
public class AppointmentCreateController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // Lấy dữ liệu từ URL
        int doctorId = Integer.parseInt(req.getParameter("doctorId"));
        int serviceId = Integer.parseInt(req.getParameter("serviceId"));
        String date = req.getParameter("date");
        String start = req.getParameter("start");
        String end = req.getParameter("end");

        DoctorDao doctorDao = new DoctorDao();
        ServiceDao serviceDao = new ServiceDao();

        // Tải thông tin bác sĩ, dịch vụ, bệnh nhân đang đăng nhập
        Doctor doctor = doctorDao.getDoctorByID(doctorId);
        Service service = serviceDao.getServiceById(serviceId);
        PatientDao patientDao = new PatientDao();
        Patients patient = patientDao.getPatientByUserId(3);

        // Gửi dữ liệu sang trang xác nhận đặt lịch
        req.setAttribute("patient", patient);
        req.setAttribute("doctor", doctor);
        req.setAttribute("service", service);
        req.setAttribute("date", date);
        req.setAttribute("start", start);
        req.setAttribute("end", end);

        req.getRequestDispatcher("/views/customer/appointmentConfirm.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
