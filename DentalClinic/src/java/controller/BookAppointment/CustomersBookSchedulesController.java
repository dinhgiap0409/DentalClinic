/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.BookAppointment;

import dal.AppointmentsDao;
import dal.DoctorDao;
import dal.NotificationsDao;
import dal.PatientDao;
import dal.UsersDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Appointments;
import model.Doctor;
import model.Patients;
import model.Service;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
@WebServlet(name = "CustomersBookSchedulesController", urlPatterns = {"/CustomersBookSchedules"})
public class CustomersBookSchedulesController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {    
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
            HttpSession session = request.getSession();

            // 1. Lấy thông tin người dùng đang đăng nhập từ session
            Users user = (Users) session.getAttribute("user");
            if (user == null) {
                // Nếu chưa đăng nhập, chuyển về trang đăng nhập
                response.sendRedirect(request.getContextPath() + "/login.jsp");
                return;
            }

            // 2. Dùng UserID để lấy thông tin bệnh án (Patient Profile)
            PatientDao patientDAO = new PatientDao();
            Patients patient = patientDAO.getPatientByUserId(user.getUserId());

            // 3. Kiểm tra và tạo hồ sơ bệnh nhân nếu chưa có
            if (patient == null) {
                // Tạo hồ sơ bệnh nhân mới
                Patients newPatient = new Patients();
                newPatient.setUserID(user);
                
                boolean created = patientDAO.insertPatient(newPatient);
                if (!created) {
                    request.setAttribute("error", "Không thể tạo hồ sơ bệnh nhân. Vui lòng thử lại!");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }
                
                // Lấy lại thông tin patient vừa tạo
                patient = patientDAO.getPatientByUserId(user.getUserId());
            }

            // 4. Kiểm tra thông tin quan trọng còn thiếu
            boolean missingInfo = (patient.getBloodType() == null || patient.getBloodType().trim().isEmpty()
                    || patient.getMedicalHistory() == null || patient.getMedicalHistory().trim().isEmpty());

            if (missingInfo) {
                session.setAttribute("success", "Vui lòng cập nhật đầy đủ thông tin bệnh án (Nhóm máu, Tiền sử bệnh) trước khi đặt lịch.");
                response.sendRedirect(request.getContextPath() + "/profile");
                return;
            }

            //Lấy thông tin từ form đặt lịch (client gửi lên)
            // Lấy patientId động từ đối tượng patient đã được kiểm tra ở trên
            int patientId = patient.getPatientID();

            int doctorId = Integer.parseInt(request.getParameter("doctorId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            String dateStr = request.getParameter("appointmentDate");
            String slot = request.getParameter("slot");
            String notes = request.getParameter("notes");

            if (slot == null || !slot.contains("-")) {
                request.setAttribute("error", "Dữ liệu khung giờ không hợp lệ.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // ===== Tách start và end =====
            String[] timeParts = slot.split("-");
            String startStr = timeParts[0].trim();
            String endStr = timeParts[1].trim();

            // ===== Parse sang SQL types =====
            java.sql.Date appointmentDate = java.sql.Date.valueOf(dateStr);
            java.sql.Time startTime = java.sql.Time.valueOf(startStr);
            java.sql.Time endTime = java.sql.Time.valueOf(endStr);

            // Tạo đối tượng Appointments
            Appointments a = new Appointments();
            a.setPatientId(new Patients(patientId));
            a.setDoctorId(new Doctor(doctorId));
            a.setServiceId(new Service(serviceId));
            a.setAppointmentDate(appointmentDate);
            a.setStartTime(startTime);
            a.setEndTime(endTime);
            a.setNotes(notes);
            a.setStatus("Scheduled");

            //Gọi DAO insertAppointment()
            AppointmentsDao appointmentsDao = new AppointmentsDao();
            Integer newId = appointmentsDao.insertAppointment(a);

            if (newId == null) {
                //Lỗi (bác sĩ bận hoặc dữ liệu sai)
                request.setAttribute("error", "Không thể đặt lịch. Có thể bác sĩ đã bận trong khung giờ này.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            //Gửi thông báo (Patient & Doctor)
            NotificationsDao notiDAO = new NotificationsDao();

            // Lấy thông tin đầy đủ của bác sĩ để gửi thông báo
            DoctorDao doctorDao = new DoctorDao();
            Doctor doctor = doctorDao.getDoctorByID(doctorId);
            String doctorName = "Bác sĩ không xác định";
            Integer doctorUserId = null;

            if (doctor != null && doctor.getUserId() != null) {
                doctorName = doctor.getUserId().getFullName();
                doctorUserId = doctor.getUserId().getUserId();
            }

            // Lấy tên bệnh nhân từ đối tượng user đã có
            String patientName = user.getFullName();
            String messageForPatient = "Bạn đã đặt lịch khám với " + doctorName
                    + " vào ngày " + dateStr + " lúc " + startStr + ".";
            String messageForDoctor = patientName + " đã đặt lịch khám vào ngày "
                    + dateStr + " lúc " + startStr + ".";

            notiDAO.insert(user.getUserId(), "Xác nhận đặt lịch", messageForPatient, "Appointment");
            if (doctorUserId != null) {
                notiDAO.insert(doctorUserId, "Lịch hẹn mới", messageForDoctor, "Appointment");
            }

            // Gửi về trang xác nhận thành công
            request.setAttribute("appointmentId", newId);
            request.getRequestDispatcher("/appointment-success.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace(); // In lỗi ra console để debug
            request.setAttribute("error", "Đã xảy ra lỗi không mong muốn trong quá trình đặt lịch.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
