/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.BookAppointmentsDirectly;

import dal.AppointmentsDao;
import dal.DoctorDao;
import dal.PatientDao;
import dal.ScheduleDao;
import dal.ScheduleExceptionsDao;
import dal.ServiceDao;
import dto.AppointmentDto;
import dto.ScheduleDto;
import dto.ScheduleExceptionsDto;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Doctor;
import model.Patients;
import model.Service;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import model.Appointments;
import model.ScheduleExceptions;
import model.Schedules;

/**
 *
 * @author TNO
 */
@WebServlet(name = "BookAppointmentsDirectlyController", urlPatterns = {"/bookAppointmentsDirectly"})
public class BookAppointmentsDirectlyController extends HttpServlet {

    private final AppointmentsDao appointmentsDao = new AppointmentsDao();
    private final DoctorDao doctorsDao = new DoctorDao();
    private final PatientDao patientsDao = new PatientDao();
    private final ServiceDao servicesDao = new ServiceDao();
    private final ScheduleDao scheduleDao = new ScheduleDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // Load services và patients
            List<Service> services = servicesDao.getAllServices();
            List<Patients> patients = patientsDao.getAllPatients();
            req.setAttribute("services", services);
            req.setAttribute("patients", patients);

            // Lấy danh sách bác sĩ theo service
            String serviceIdStr = req.getParameter("serviceId");
            List<Doctor> doctors = new ArrayList<>();
            if (serviceIdStr != null && !serviceIdStr.isEmpty()) {
                int serviceId = Integer.parseInt(serviceIdStr);
                List<Integer> doctorIds = doctorsDao.getDoctorIdsByServiceId(serviceId);
                for (int doctorId : doctorIds) {
                    Doctor d = doctorsDao.getDoctorByID(doctorId);
                    if (d != null) {
                        doctors.add(d);
                    }
                }
                req.setAttribute("selectedServiceId", serviceId);
            }
            req.setAttribute("doctors", doctors);

            // Xử lý lịch theo bác sĩ
            String doctorIdStr = req.getParameter("doctorId");
            if (doctorIdStr != null && !doctorIdStr.isEmpty()) {
                int doctorId = Integer.parseInt(doctorIdStr);
                int serviceId = serviceIdStr != null && !serviceIdStr.isEmpty() ? Integer.parseInt(serviceIdStr) : 0;

                int weekOffset = 0;
                try {
                    String off = req.getParameter("weekOffset");
                    if (off != null) {
                        weekOffset = Integer.parseInt(off);
                    }
                } catch (NumberFormatException ignore) {
                }

                LocalDate today = LocalDate.now();
                LocalDate monday = today.with(java.time.DayOfWeek.MONDAY).plusWeeks(weekOffset);
                LocalDate sunday = monday.plusDays(6);

                ScheduleExceptionsDao exDao = new ScheduleExceptionsDao();

                // Map: java.sql.Date -> List<Schedules>
                Map<java.sql.Date, List<Schedules>> availableByDate = new LinkedHashMap<>();
                Map<Integer, List<Schedules>> baseByDow = new HashMap<>();

                for (LocalDate d = monday; !d.isAfter(sunday); d = d.plusDays(1)) {
                    int dow = d.getDayOfWeek().getValue(); // 1=Mon .. 7=Sun

                    // Base schedules theo thứ
                    List<Schedules> base = baseByDow.computeIfAbsent(dow, k -> {
                        ScheduleDto f = new ScheduleDto();
                        f.setDoctorId(doctorId);
                        f.setDayOfWeek(k);
                        f.setIsAvailable(true);
                        List<Schedules> list = scheduleDao.filterSchedules(f);
                        list.sort(Comparator.comparing(Schedules::getStartTime));
                        return list;
                    });

                    if (base.isEmpty()) {
                        availableByDate.put(java.sql.Date.valueOf(d), List.of());
                        continue;
                    }

                    // Kiểm tra exceptions
                    ScheduleExceptionsDto exF = new ScheduleExceptionsDto();
                    exF.setDoctorId(doctorId);
                    exF.setExceptionDate(java.sql.Date.valueOf(d));
                    List<ScheduleExceptions> exs = exDao.filterScheduleExceptions(exF);

                    boolean dayOff = exs.stream().anyMatch(ex -> !ex.isIsWorkingDay());
                    if (dayOff) {
                        availableByDate.put(java.sql.Date.valueOf(d), List.of());
                        continue;
                    }

                    // Loại slot trùng exception
                    List<Schedules> afterEx = new ArrayList<>();
                    for (Schedules s : base) {
                        boolean overlapEx = exs.stream().anyMatch(ex
                                -> ex.getStartTime() != null && ex.getEndTime() != null
                                && s.getStartTime().before(ex.getEndTime())
                                && s.getEndTime().after(ex.getStartTime())
                        );
                        if (!overlapEx) {
                            afterEx.add(s);
                        }
                    }

                    afterEx.sort(Comparator.comparing(Schedules::getStartTime));
                    availableByDate.put(java.sql.Date.valueOf(d), afterEx);
                }

                req.setAttribute("availableByDate", availableByDate);
                req.setAttribute("monday", monday);
                req.setAttribute("sunday", sunday);
                req.setAttribute("weekOffset", weekOffset);
                req.setAttribute("selectedDoctorId", doctorId);
                req.setAttribute("selectedServiceId", serviceId);
            }

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Không thể tải dữ liệu form đặt lịch.");
        }

        req.getRequestDispatcher("/views/receptionist/create_appointment.jsp").forward(req, resp);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            int patientId = Integer.parseInt(request.getParameter("patientId"));
            int doctorId = Integer.parseInt(request.getParameter("doctorId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));
            String selectedSlot = request.getParameter("selectedSlot");
            String notes = request.getParameter("notes");

            // --- Parse selectedSlot: "2025-01-01,22:15:00,23:00:00" ---
            if (selectedSlot == null || !selectedSlot.contains(",")) {
                request.setAttribute("errorMessage", "Vui lòng chọn khung giờ hợp lệ!");
                doGet(request, response);
                return;
            }

            String[] parts = selectedSlot.split(",");
            Time startTime = Time.valueOf(parts[1]);
            Time endTime = Time.valueOf(parts[2]);
            Date appointmentDate = Date.valueOf(parts[0]);

            // ===== Kiểm tra bác sĩ có bị trùng lịch không =====
            AppointmentDto checkDoctor = new AppointmentDto();
            checkDoctor.setDoctorId(doctorId);
            checkDoctor.setStartTime(startTime);
            checkDoctor.setEndTime(endTime);
            checkDoctor.setPaginationMode(false);

//            List<Appointments> doctorBusyList = appointmentsDao.filterAppointment(checkDoctor);
//            if (!doctorBusyList.isEmpty()) {
//                request.setAttribute("errorMessage", "Bác sĩ đã có lịch trong khoảng thời gian này!");
//                doGet(request, response);
//                return;
//            }
            // ===== Kiểm tra bệnh nhân có bị trùng lịch không =====
            AppointmentDto checkPatient = new AppointmentDto();
            checkPatient.setPatientId(patientId);
            checkPatient.setStartTime(startTime);
            checkPatient.setEndTime(endTime);
            checkPatient.setPaginationMode(false);

//            List<Appointments> patientBusyList = appointmentsDao.filterAppointment(checkPatient);
//            if (!patientBusyList.isEmpty()) {
//                request.setAttribute("errorMessage", "Bệnh nhân đã có lịch hẹn trong thời gian này!");
//                doGet(request, response);
//                return;
//            }
            // ===== Kiểm tra lịch làm việc hợp lệ của bác sĩ =====
            ScheduleDto checkSchedule = new ScheduleDto();
            checkSchedule.setDoctorId(doctorId);
            checkSchedule.setStartTime(startTime);
            checkSchedule.setEndTime(endTime);
            checkSchedule.setIsAvailable(true);
            checkSchedule.setPaginationMode(false);

            List<Schedules> validSchedule = scheduleDao.filterSchedules(checkSchedule);
            if (validSchedule.isEmpty()) {
                request.setAttribute("errorMessage", "Lịch hẹn nằm ngoài thời gian làm việc của bác sĩ!");
                doGet(request, response);
                return;
            }

            // ===== Tạo đối tượng Appointment =====
            Appointments appointment = new Appointments();

            Patients patient = new Patients();
            patient.setPatientID(patientId);

            Doctor doctor = new Doctor();
            doctor.setDoctorID(doctorId);

            Service service = new Service();
            service.setServiceId(serviceId);

            appointment.setPatientId(patient);
            appointment.setDoctorId(doctor);
            appointment.setServiceId(service);
            appointment.setStartTime(startTime);
            appointment.setEndTime(endTime);
            appointment.setStatus("Scheduled");
            appointment.setNotes(notes);
            appointment.setAppointmentDate(appointmentDate);

            Integer newId = appointmentsDao.insertAppointment(appointment);

            if (newId != null) {
                request.setAttribute("successMessage", "Đặt lịch thành công! Mã lịch hẹn: " + newId);
            } else {
                request.setAttribute("errorMessage", "Không thể tạo lịch hẹn. Vui lòng kiểm tra lại.");
            }

            doGet(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Lỗi khi tạo lịch hẹn. Vui lòng thử lại!");
            doGet(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
