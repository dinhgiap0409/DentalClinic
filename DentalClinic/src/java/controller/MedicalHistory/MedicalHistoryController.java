/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.MedicalHistory;

import dal.AppointmentsDao;
import dal.MedicalRecordDao;
import dal.PrescriptionDao;
import dto.AppointmentDto;
import dto.MedicalRecordDto;
import dto.PrescriptionDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Appointments;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import model.MedicalRecords;
import model.Prescriptions;
import utils.SessionUtils;

/**
 *
 * @author TNO
 */
@WebServlet(name = "MedicalHistoryController", urlPatterns = {"/medicalHistory"})
public class MedicalHistoryController extends HttpServlet {

    private final AppointmentsDao appointmentsDao = new AppointmentsDao();
    private final MedicalRecordDao recordDao = new MedicalRecordDao();
    private final PrescriptionDao prescriptionDao = new PrescriptionDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            HttpSession session = req.getSession(false);
            if (session == null) {
                resp.sendRedirect("login");
                return;
            }

            // Lấy patientId từ session
            int patientId = SessionUtils.getPatientId(session);
            if (patientId <= 0) {
                resp.sendRedirect("login");
                return;
            }

            String status = req.getParameter("status");
            String doctorId = req.getParameter("doctorId");
            String serviceId = req.getParameter("serviceId");
            String doctorName = req.getParameter("doctorName");   
            String serviceName = req.getParameter("serviceName");  //
            String fromDate = req.getParameter("fromDate");
            String toDate = req.getParameter("toDate");
            String pageStr = req.getParameter("page");
            String sizeStr = req.getParameter("size");        

            AppointmentDto filter = new AppointmentDto();
            filter.setPatientId(patientId);

            if (status != null && !status.isBlank()) {
                filter.setStatus(status.trim());
            }
            if (doctorId != null && !doctorId.isBlank()) {
                filter.setDoctorId(Integer.parseInt(doctorId));
            }
            if (serviceId != null && !serviceId.isBlank()) {
                filter.setServiceId(Integer.parseInt(serviceId));
            }
            if (doctorName != null && !doctorName.isBlank()) {
                filter.setDoctorName(doctorName.trim());
            }
            if (serviceName != null && !serviceName.isBlank()) {
                filter.setServiceName(serviceName.trim());
            }
            if (fromDate != null && !fromDate.isBlank()) {
                filter.setAppointmentDateFrom(Date.valueOf(fromDate));
            }
            if (toDate != null && !toDate.isBlank()) {
                filter.setAppointmentDateTo(Date.valueOf(toDate));
            }

            int page = (pageStr != null && !pageStr.isBlank()) ? Integer.parseInt(pageStr) : 1;
            int size = (sizeStr != null && !sizeStr.isBlank()) ? Integer.parseInt(sizeStr) : 10;
            filter.setPage(page);
            filter.setSize(size);
            filter.setPaginationMode(true);
            filter.setSortMode(true);

            // Đếm tổng trước (cho phân trang)
            int totalItems = appointmentsDao.countAppointments(filter);
            int totalPages = (int) Math.ceil(totalItems / (double) size);

            // Lấy danh sách
            List<Appointments> appointments = appointmentsDao.filterAppointment(filter);

            // ===== TẠO MAP CHỨA RECORD & PRESCRIPTION =====
            Map<Integer, MedicalRecords> recordsMap = new HashMap<>();
            Map<Integer, Prescriptions> prescriptionsMap = new HashMap<>();

            for (Appointments a : appointments) {
                int appointmentId = a.getAppointmentId();

                //Filter MedicalRecord theo AppointmentID
                MedicalRecordDto recordDto = new MedicalRecordDto();
                recordDto.setAppointmentId(appointmentId);
                recordDto.setPaginationMode(false);
                recordDto.setSortMode(false);
                List<MedicalRecords> recordList = recordDao.filterMedicalRecord(recordDto);
                if (!recordList.isEmpty()) {
                    recordsMap.put(appointmentId, recordList.get(0)); // mỗi appointment chỉ có 1 record
                }

                //Filter Prescription theo RecordID (nếu có record)
                if (!recordList.isEmpty()) {
                    int recordId = recordList.get(0).getRecordId();
                    PrescriptionDto presDto = new PrescriptionDto();
                    presDto.setRecordId(recordId);
                    presDto.setPaginationMode(false);
                    presDto.setSortMode(false);
                    List<Prescriptions> presList = prescriptionDao.filterPrescription(presDto);
                    if (!presList.isEmpty()) {
                        prescriptionsMap.put(appointmentId, presList.get(0));
                    }
                }
            }

            // ===== GỬI DỮ LIỆU SANG JSP =====
            req.setAttribute("appointments", appointments);
            req.setAttribute("recordsMap", recordsMap);
            req.setAttribute("prescriptionsMap", prescriptionsMap);

            req.setAttribute("page", page);
            req.setAttribute("size", size);
            req.setAttribute("totalItems", totalItems);
            req.setAttribute("totalPages", totalPages);

            req.getRequestDispatcher("/views/patient/medical_history.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("error", "Đã xảy ra lỗi khi tải lịch sử khám!");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doGet(req, resp);
    }
}
