/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.BookAppointment;

import dal.AppointmentsDao;
import dal.NotificationsDao;
import dal.PatientDao;
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
            // neu ma nguoi dung an dat lich , check xem nguoi dung da co thong tin nhu nhom mau, tien su,, ton tai trong bang Patient hay chua
            // neu co thong tin dat binh thuong
            // neu ko co thi dieu den trang view profile cua ho de cap nhat thong tin
    
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

            //Lấy thông tin từ form đặt lịch (client gửi lên)
            //int patientId = Integer.parseInt(request.getParameter("patientId"));
            int patientId = 3;
            
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
            AppointmentsDao appointmentDAO = new AppointmentsDao();
            Integer newId = appointmentDAO.insertAppointment(a);

            if (newId == null) {
                //Lỗi (bác sĩ bận hoặc dữ liệu sai)
                request.setAttribute("error", "Không thể đặt lịch. Có thể bác sĩ đã bận trong khung giờ này.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            //Gửi thông báo (Patient & Doctor)
            NotificationsDao notiDAO = new NotificationsDao();

            //Thông tin bác sĩ / bệnh nhân theo ID:
            String doctorName = "Bác sĩ #" + doctorId;
            String patientName = "Bệnh nhân #" + patientId;
            String messageForPatient = "Bạn đã đặt lịch khám với " + doctorName
                    + " vào ngày " + dateStr + " lúc " + startStr + ".";
            String messageForDoctor = patientName + " đã đặt lịch khám vào ngày "
                    + dateStr + " lúc " + startStr + ".";

            notiDAO.insert(patientId, "Xác nhận đặt lịch", messageForPatient, "Appointment");
            notiDAO.insert(doctorId, "Lịch hẹn mới", messageForDoctor, "Appointment");

            // Gửi về trang xác nhận thành công
            request.setAttribute("appointmentId", newId);
            request.getRequestDispatcher("/appointment-success.jsp").forward(request, response);

        } catch (Exception e) {
            
            request.setAttribute("error", "Đã xảy ra lỗi trong quá trình đặt lịch.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
