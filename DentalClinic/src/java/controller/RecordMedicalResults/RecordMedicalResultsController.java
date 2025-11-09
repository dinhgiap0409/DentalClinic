package controller.RecordMedicalResults;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import dal.AppointmentServicesDao;
import dal.AppointmentsDao;
import dal.MedicalRecordDao;
import dal.MedicationsDao;
import dal.PrescriptionDao;
import dal.ServiceDao;
import dto.MedicationDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Appointments;
import model.Medications;
import java.sql.Date;
import model.Doctor;
import model.MedicalRecords;
import model.PrescriptionDetails;
import model.Prescriptions;
import java.sql.Time;
import model.AppointmentServices;
import model.Service;

/**
 *
 * @author TNO
 */
@WebServlet(urlPatterns = {"/recordMedicalResults"})
public class RecordMedicalResultsController extends HttpServlet {

    private AppointmentsDao appointmentDAO = new AppointmentsDao();
    private MedicalRecordDao recordDAO = new MedicalRecordDao();
    private MedicationsDao medicationDAO = new MedicationsDao();
    private PrescriptionDao prescriptionDAO = new PrescriptionDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

        // Lấy thông tin Appointment + Bệnh nhân
        Appointments appointment = appointmentDAO.getAppointmentsById(appointmentId);

        // ----- DÙNG FILTER -----
        MedicationDto mDto = new MedicationDto();
        // chỉ lấy thuốc còn dùng & chưa hết hạn
        mDto.setIsActive(true);
        mDto.setExpiryDateFrom(new Date(System.currentTimeMillis())); // >= hôm nay (không lấy thuốc đã hết hạn)
        mDto.setPaginationMode(false); // lấy hết (hoặc để true + set page/size nếu muốn phân trang)
        mDto.setSortMode(true);        // ORDER BY ExpiryDate ASC, Name ASC

        // tuỳ chọn: search theo tên / hãng từ query
        String qName = request.getParameter("medName");
        String qManu = request.getParameter("manu");
        if (qName != null && !qName.isBlank()) {
            mDto.setName(qName);
        }
        if (qManu != null && !qManu.isBlank()) {
            mDto.setManufacturer(qManu);
        }

        List<Medications> medicationList = medicationDAO.filterMedications(mDto);
        // -----------------------

        ServiceDao serviceDAO = new ServiceDao();
        List<Service> services = serviceDAO.getAllServices();
        request.setAttribute("services", services);

        // Gửi dữ liệu sang JSP
        request.setAttribute("appointment", appointment);
        request.setAttribute("medications", medicationList);

        request.getRequestDispatcher("/views/doctor/record-medical.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // Flow:
        // 1) Lấy appointment
        // 2) Insert MedicalRecords -> lấy recordId
        // 3) Insert Prescriptions -> lấy prescriptionId
        // 4) Insert các PrescriptionDetails
        // 5) Cập nhật Appointment: Status = "Completed"
        try {
            int appointmentId = Integer.parseInt(request.getParameter("appointmentId"));

            Appointments ap = appointmentDAO.getAppointmentsById(appointmentId);
            if (ap == null) {
                request.setAttribute("error", "Không tìm thấy lịch hẹn #" + appointmentId);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // -------- 1) Nhận dữ liệu form của MedicalRecords --------
            String diagnosis = trimOrNull(request.getParameter("diagnosis"));
            String symptoms = trimOrNull(request.getParameter("symptoms"));
            String treatmentPlan = trimOrNull(request.getParameter("treatmentPlan"));
            String followUpDateStr = trimOrNull(request.getParameter("followUpDate"));

            // Tối thiểu: chẩn đoán/symptoms có thể yêu cầu theo business rule của bạn
            MedicalRecords mr = new MedicalRecords();
            // set appointment vào record
            Appointments apRef = new Appointments();
            apRef.setAppointmentId(appointmentId);
            mr.setAppointmentId(apRef);

            mr.setDiagnosis(diagnosis);
            mr.setSymptoms(symptoms);
            mr.setTreatmentPlan(treatmentPlan);
            if (followUpDateStr != null && !followUpDateStr.isBlank()) {
                mr.setFollowUpDate(Date.valueOf(followUpDateStr));
            } else {
                mr.setFollowUpDate(null);
            }

            int newRecordId = recordDAO.insertMedicalRecord(mr);
            if (newRecordId <= 0) {
                request.setAttribute("error", "Không thể lưu hồ sơ khám.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // -------- 2) Insert Prescriptions --------
            String instructions = trimOrNull(request.getParameter("instructions"));

            Prescriptions pres = new Prescriptions();

            // set MedicalRecords ref
            MedicalRecords mrRef = new MedicalRecords();
            mrRef.setRecordId(newRecordId);
            pres.setRecordId(mrRef);

            // Lấy DoctorID từ appointment (đã có trong Appointments)
            // ap.getDoctorId() là object Doctor, chỉ cần ID:
            if (ap.getDoctorId() == null || ap.getDoctorId().getDoctorID() <= 0) {
                // fallback nếu dữ liệu appointment thiếu doctor
                request.setAttribute("error", "Không xác định được bác sĩ của lịch hẹn #" + appointmentId);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }
            Doctor docRef = new Doctor();
            docRef.setDoctorID(ap.getDoctorId().getDoctorID());
            pres.setDoctorId(docRef);

            pres.setInstructions(instructions);

            int newPresId = prescriptionDAO.insertPrescription(pres);
            if (newPresId <= 0) {
                request.setAttribute("error", "Không thể lưu đơn thuốc.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // -------- 3) Insert các PrescriptionDetails --------
            // Form nên gửi theo dạng mảng cùng index:
            // medicationId[], dosage[], quantity[], duration[]
            String[] medIds = request.getParameterValues("medicationId");
            String[] dosages = request.getParameterValues("dosage");
            String[] quantities = request.getParameterValues("quantity");
            String[] durations = request.getParameterValues("duration");

            if (medIds != null && dosages != null && quantities != null && durations != null) {
                int n = medIds.length;
                // đảm bảo 4 mảng bằng nhau
                if (dosages.length != n || quantities.length != n || durations.length != n) {
                    request.setAttribute("error", "Dữ liệu chi tiết đơn thuốc không đồng bộ.");
                    request.getRequestDispatcher("/error.jsp").forward(request, response);
                    return;
                }

                for (int i = 0; i < n; i++) {
                    String medIdStr = medIds[i];
                    if (medIdStr == null || medIdStr.isBlank()) {
                        continue;
                    }

                    int medId = Integer.parseInt(medIdStr);
                    String dosage = trimOrNull(dosages[i]);
                    String duration = trimOrNull(durations[i]);
                    int qty = 0;
                    if (quantities[i] != null && !quantities[i].isBlank()) {
                        qty = Integer.parseInt(quantities[i]);
                    }

                    // chỉ insert nếu có medication & qty > 0
                    if (medId > 0 && qty > 0) {
                        PrescriptionDetails detail = new PrescriptionDetails();

                        Prescriptions pRef = new Prescriptions();
                        pRef.setPrescriptionId(newPresId);
                        detail.setPrescriptionId(pRef);

                        Medications mRef = new Medications();
                        mRef.setMedicationId(medId);
                        detail.setMedications(mRef);

                        detail.setDosage(dosage);
                        detail.setQuantity(qty);
                        detail.setDuration(duration);

                        prescriptionDAO.insertPrescriptionDetail(detail);
                    }
                }
            }

            // -------- 4) Cập nhật trạng thái Appointment => Completed --------
            ap.setStatus("Completed");
            // Gợi ý: lưu giờ kết thúc
            ap.setEndTime(new Time(System.currentTimeMillis()));
            // có thể set notes nếu muốn:
            // ap.setNotes("Đã ghi hồ sơ & kê đơn");

            // -------- 4) Insert các dịch vụ tùy chọn --------
            String[] extraServiceIds = request.getParameterValues("extraServiceIds");
            String extraServiceNotes = trimOrNull(request.getParameter("extraServiceNotes"));
            if (extraServiceIds != null) {
                AppointmentServicesDao serviceDAO = new AppointmentServicesDao();
                for (String serviceIdStr : extraServiceIds) {
                    if (serviceIdStr == null || serviceIdStr.isBlank()) {
                        continue;
                    }
                    int serviceId = Integer.parseInt(serviceIdStr);
                    AppointmentServices appService = new model.AppointmentServices();
                    Appointments aRef = new Appointments();
                    aRef.setAppointmentId(appointmentId);
                    appService.setAppointmentId(aRef);

                    Service sRef = new Service();
                    sRef.setServiceId(serviceId);
                    appService.setServiceId(sRef);

                    appService.setNotes(extraServiceNotes);
                    int insertApSer = serviceDAO.insertAppointmentService(appService);
                    if (insertApSer == -1) {
                        throw new Exception("Lỗi khi thêm các dịch vụ tùy chọn");
                    }
                }
            }

            boolean updated = appointmentDAO.updateAppointment(ap);
            if (!updated) {
                request.setAttribute("error", "Lưu xong nhưng không cập nhật được trạng thái lịch hẹn.");
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Điều hướng sau khi thành công
            // Quay về hồ sơ bệnh nhân để xem lại:
            int patientId = ap.getPatientId() != null ? ap.getPatientId().getPatientID() : -1;
            if (patientId > 0) {
                response.sendRedirect(request.getContextPath() + "/patientRecord?patientId=" + patientId);
            } else {
                // fallback: quay lại trang nhập
                request.setAttribute("success", "Đã lưu kết quả khám & đơn thuốc, và hoàn tất lịch hẹn.");
                request.setAttribute("appointment", ap);
                request.getRequestDispatcher("/record-medical.jsp").forward(request, response);
            }

        } catch (NumberFormatException ex) {
            request.setAttribute("error", "Dữ liệu số không hợp lệ.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Có lỗi khi lưu kết quả khám.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private String trimOrNull(String s) {
        return (s == null) ? null : s.trim();
    }

}
