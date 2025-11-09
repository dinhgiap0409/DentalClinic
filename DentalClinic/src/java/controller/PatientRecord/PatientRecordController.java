/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.PatientRecord;

import dal.AppointmentsDao;
import dal.DoctorDao;
import dal.MedicalRecordDao;
import dal.OrderDao;
import dal.PatientDao;
import dal.PrescriptionDao;
import dto.AppointmentDto;
import dto.MedicalRecordDto;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Appointments;
import model.MedicalRecords;
import model.Patients;
import model.Prescriptions;
import utils.SessionUtils;

/**
 *
 * @author TNO
 */
@WebServlet(name = "PatientRecordController", urlPatterns = {"/patientRecord"})
public class PatientRecordController extends HttpServlet {

    private final PatientDao patientDao = new PatientDao();
    private final AppointmentsDao appointmentsDao = new AppointmentsDao();
    private final MedicalRecordDao medicalRecordDao = new MedicalRecordDao();
    private final PrescriptionDao prescriptionDao = new PrescriptionDao();
    private final OrderDao orderDao = new OrderDao();
    private final DoctorDao doctorDao = new DoctorDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pid = request.getParameter("patientId");
            if (pid == null || pid.isBlank()) {
                request.setAttribute("error", "Thiếu PatientID.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            int patientId = Integer.parseInt(pid);

            HttpSession session = request.getSession(false);
            model.Users currentUser = session != null ? (model.Users) session.getAttribute("user") : null;
            String currentRole = currentUser != null ? currentUser.getRole() : "Guest";

            boolean allowed = false;
            if ("Admin".equalsIgnoreCase(currentRole) || "Receptionist".equalsIgnoreCase(currentRole)) {
                allowed = true;
            } else if ("Doctor".equalsIgnoreCase(currentRole)) {
                int doctorId = SessionUtils.getDoctorId(session);
                if (doctorId > 0) {
                    // Dùng filterAppointment để kiểm tra quan hệ
                    AppointmentDto dto = new AppointmentDto();
                    dto.setDoctorId(doctorId);
                    dto.setPatientId(patientId);
                    dto.setPaginationMode(false);
                    List<Appointments> relatedList = appointmentsDao.filterAppointment(dto);
                    allowed = (relatedList != null && !relatedList.isEmpty());
                }
            } else if ("Patient".equalsIgnoreCase(currentRole)) {
                Integer userIdOfPatient = patientDao.getUserIdByPatientId(patientId);
                if (userIdOfPatient != null && currentUser != null && userIdOfPatient.equals(currentUser.getUserId())) {
                    allowed = true;
                }
            }

            if (!allowed) {
                request.setAttribute("error", "Bạn không có quyền truy cập hồ sơ bệnh án này.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // 1) Thông tin bệnh nhân (đúng theo patientId)
            Patients patient = patientDao.getPatientById(patientId);
            if (patient == null) {
                request.setAttribute("error", "Không tìm thấy thông tin bệnh nhân.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // 2) Appointments theo patientId (dùng filterAppointment)
            AppointmentDto apDto = new AppointmentDto();
            apDto.setPatientId(patientId);
            apDto.setPaginationMode(false);
            apDto.setSortMode(true);
            List<Appointments> appointments = appointmentsDao.filterAppointment(apDto);

            // 3) MedicalRecords theo patientId (DÙNG FILTER MỚI)
            MedicalRecordDto mrDto = new MedicalRecordDto();
            mrDto.setPatientId(patientId);      // <- quan trọng
            mrDto.setPaginationMode(false);
            mrDto.setSortMode(true);
            List<MedicalRecords> records = medicalRecordDao.filterMedicalRecord(mrDto);

            // 4) Với mỗi record -> lấy prescriptions
            Map<Integer, List<Prescriptions>> prescriptionsMap = new HashMap<>();
            for (MedicalRecords r : records) {
                dto.PrescriptionDto pdto = new dto.PrescriptionDto();
                pdto.setRecordId(r.getRecordId());
                pdto.setPaginationMode(false);
                List<Prescriptions> pres = prescriptionDao.filterPrescription(pdto);
                prescriptionsMap.put(r.getRecordId(), pres);
            }

            // 5) Orders theo từng prescription 
            Map<Integer, java.util.List<model.Orders>> ordersByPrescription = new java.util.HashMap<>();
            for (List<Prescriptions> presList : prescriptionsMap.values()) {
                for (model.Prescriptions pres : presList) {
                    dto.OrderDto odto = new dto.OrderDto();
                    odto.setPrescriptionId(pres.getPrescriptionId());
                    odto.setPaginationMode(false);
                    List<model.Orders> orders = orderDao.filterOrders(odto);
                    ordersByPrescription.put(pres.getPrescriptionId(), orders);
                }
            }

            // 6) Set attribute & forward
            request.setAttribute("patient", patient);
            request.setAttribute("appointments", appointments);
            request.setAttribute("records", records);
            request.setAttribute("prescriptionsMap", prescriptionsMap);
            request.setAttribute("ordersByPrescription", ordersByPrescription);

            request.getRequestDispatcher("/views/manage/patient-record.jsp").forward(request, response);

        } catch (NumberFormatException ex) {
            request.setAttribute("error", "PatientID không hợp lệ.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Lỗi khi tải hồ sơ bệnh án.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

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
