/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dal.AppointmentsDao;
import dal.DoctorDao;
import dal.ScheduleDao;
import dal.ScheduleExceptionsDao;
import dal.ServiceDao;
import dto.AppointmentDto;
import dto.ScheduleDto;
import dto.ScheduleExceptionsDto;
import dto.TimeSlotDto;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Appointments;
import model.Doctor;
import model.Patients;
import model.ScheduleExceptions;
import model.Schedules;
import model.Service;
import model.Users; // Assuming patient is a User

/**
 * Servlet for handling appointment booking flow.
 * This controller guides the patient through selecting a service, doctor, and time slot.
 *
 * @author Nguyen Dinh Giap
 */
//@WebServlet(name = "AppointmentController", urlPatterns = {"/appointment"})
public class AppointmentController extends HttpServlet {

    private ServiceDao serviceDao;
    private DoctorDao doctorDao;
    private ScheduleDao scheduleDao;
    private ScheduleExceptionsDao scheduleExceptionsDao;
    private AppointmentsDao appointmentsDao;

    @Override
    public void init() throws ServletException {
        super.init();
        serviceDao = new ServiceDao();
        doctorDao = new DoctorDao();
        scheduleDao = new ScheduleDao();
        scheduleExceptionsDao = new ScheduleExceptionsDao();
        appointmentsDao = new AppointmentsDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null || action.isEmpty()) {
            // Step 1: Display services
            List<Service> services = serviceDao.getAllServices();
            request.setAttribute("services", services);
            request.getRequestDispatcher("/views/customer/appointmentSelectService.jsp").forward(request, response);
        } else if ("selectDoctor".equals(action)) {
            // Step 2: Display doctors for selected service (or all doctors for now)
            try {
                int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                Service selectedService = serviceDao.getServiceById(serviceId);
                if (selectedService == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service not found.");
                    return;
                }
                // In a real scenario, you might filter doctors by specialization linked to the service
                List<Doctor> doctors = doctorDao.getAllDoctors();
                
                request.setAttribute("selectedService", selectedService);
                request.setAttribute("doctors", doctors);
                request.getRequestDispatcher("/views/customer/appointmentSelectDoctor.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid service ID.");
            }
        } else if ("selectTime".equals(action)) {
            // Step 3: Display available time slots
            try {
                int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                int doctorId = Integer.parseInt(request.getParameter("doctorId"));
                String dateStr = request.getParameter("appointmentDate");

                if (dateStr == null || dateStr.isEmpty()) {
                    dateStr = LocalDate.now().toString(); // Default to today if no date provided
                }
                Date appointmentDate = Date.valueOf(dateStr);
                
                Service selectedService = serviceDao.getServiceById(serviceId);
                Doctor selectedDoctor = doctorDao.getDoctorByID(doctorId);

                if (selectedService == null || selectedDoctor == null) {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Service or Doctor not found.");
                    return;
                }

                int serviceDuration = getServiceDuration(serviceId);
                List<TimeSlotDto> availableSlots = getAvailableTimeSlots(doctorId, appointmentDate, serviceDuration);

                request.setAttribute("selectedService", selectedService);
                request.setAttribute("selectedDoctor", selectedDoctor);
                request.setAttribute("appointmentDate", appointmentDate);
                request.setAttribute("availableSlots", availableSlots);
                request.getRequestDispatcher("/views/customer/appointmentSelectTime.jsp").forward(request, response);
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID format.");
            } catch (IllegalArgumentException e) { // For Date.valueOf
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date format.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("confirmAppointment".equals(action)) {
            // Step 4: Confirm and book appointment
            Users currentUser = (Users) request.getSession().getAttribute("user"); 
            if (currentUser == null) {
                response.sendRedirect(request.getContextPath() + "/login.jsp"); // Redirect to login if not logged in
                return;
            }
            // IMPORTANT: Assuming PatientID in Appointments table is the same as UserID from Users table.
            // If there's a separate Patients table with its own PK (PatientID) and a FK to Users.UserID,
            // you'd need to fetch the PatientID based on currentUser.getUserId().
            int patientId = currentUser.getUserId(); 

            try {
                int serviceId = Integer.parseInt(request.getParameter("serviceId"));
                int doctorId = Integer.parseInt(request.getParameter("doctorId"));
                Date appointmentDate = Date.valueOf(request.getParameter("appointmentDate"));
                Time startTime = Time.valueOf(request.getParameter("startTime"));
                String notes = request.getParameter("notes");

                Integer newAppointmentId = bookAppointment(patientId, doctorId, serviceId, appointmentDate, startTime, notes);

                if (newAppointmentId != null) {
                    // Appointment booked successfully
                    // Fetch details to show on success page
                    Service bookedService = serviceDao.getServiceById(serviceId);
                    Doctor bookedDoctor = doctorDao.getDoctorByID(doctorId);

                    request.setAttribute("message", "Appointment booked successfully! Your Appointment ID: " + newAppointmentId);
                    request.setAttribute("bookedService", bookedService);
                    request.setAttribute("bookedDoctor", bookedDoctor);
                    request.setAttribute("appointmentDate", appointmentDate);
                    request.setAttribute("startTime", startTime);
                    request.getRequestDispatcher("/views/customer/appointmentSuccess.jsp").forward(request, response);
                } else {
                    // Booking failed (e.g., time conflict, max appointments reached, invalid data) - Dat lich that bai
                    request.setAttribute("errorMessage", "Failed to book appointment. The selected slot might be unavailable or there was an internal error. Please try again or choose a different slot.");
                    
                    // Re-populate data for selectTime.jsp to allow user to re-select
                    Service selectedService = serviceDao.getServiceById(serviceId);
                    Doctor selectedDoctor = doctorDao.getDoctorByID(doctorId);
                    int serviceDuration = getServiceDuration(serviceId);
                    List<TimeSlotDto> availableSlots = getAvailableTimeSlots(doctorId, appointmentDate, serviceDuration);

                    request.setAttribute("selectedService", selectedService);
                    request.setAttribute("selectedDoctor", selectedDoctor);
                    request.setAttribute("appointmentDate", appointmentDate);
                    request.setAttribute("availableSlots", availableSlots);
                    request.getRequestDispatcher("/views/customer/appointmentSelectTime.jsp").forward(request, response);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid ID or duration format.");
            } catch (IllegalArgumentException e) { // For Date.valueOf, Time.valueOf
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid date or time format.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }
    
    // --- Business Logic Methods (moved from AppointmentService) ---

    /**
     * Retrieves a list of available time slots for a given doctor on a specific date.
     */
    private List<TimeSlotDto> getAvailableTimeSlots(int doctorId, Date appointmentDate, int serviceDurationMinutes) {
        List<TimeSlotDto> availableSlots = new ArrayList<>();

        // 1. Determine the base schedule for the given date
        LocalDate localAppointmentDate = appointmentDate.toLocalDate();
        int dayOfWeek = localAppointmentDate.getDayOfWeek().getValue(); // 1=Monday, 7=Sunday (ISO-8601)

        ScheduleDto scheduleFilter = new ScheduleDto();
        scheduleFilter.setDoctorId(doctorId);
        scheduleFilter.setDayOfWeek(dayOfWeek);
        scheduleFilter.setPaginationMode(false);
        List<Schedules> baseSchedules = scheduleDao.filterSchedules(scheduleFilter);

        Schedules effectiveSchedule = null;
        if (!baseSchedules.isEmpty()) {
            effectiveSchedule = baseSchedules.get(0);
        }

        // 2. Apply schedule exceptions
        ScheduleExceptionsDto exceptionFilter = new ScheduleExceptionsDto();
        exceptionFilter.setDoctorId(doctorId);
        exceptionFilter.setExceptionDate(appointmentDate);
        exceptionFilter.setPaginationMode(false);
        List<ScheduleExceptions> exceptions = scheduleExceptionsDao.filterScheduleExceptions(exceptionFilter);

        if (!exceptions.isEmpty()) {
            ScheduleExceptions exception = exceptions.get(0);
            if (!exception.isIsWorkingDay()) {
                return availableSlots; // Doctor is not working
            } else {
                if (effectiveSchedule == null) {
                    effectiveSchedule = new Schedules();
                    Doctor doctor = doctorDao.getDoctorByID(doctorId);
                    if (doctor != null) {
                        effectiveSchedule.setDoctorId(doctor);
                    } else {
                        System.err.println("Doctor not found for ID: " + doctorId);
                        return availableSlots;
                    }
                    effectiveSchedule.setDayOfWeek(dayOfWeek);
                    effectiveSchedule.setIsAvailable(true);
                    effectiveSchedule.setMaxAppointments(0);
                }
                effectiveSchedule.setStartTime(exception.getStartTime());
                effectiveSchedule.setEndTime(exception.getEndTime());
                if (exception.getMaxAppointments() != null) {
                    effectiveSchedule.setMaxAppointments(exception.getMaxAppointments());
                }
            }
        }

        if (effectiveSchedule == null || !effectiveSchedule.isIsAvailable()) {
            return availableSlots;
        }

        // 3. Get existing appointments
        AppointmentDto appointmentFilter = new AppointmentDto();
        appointmentFilter.setDoctorId(doctorId);
        appointmentFilter.setAppointmentDate(appointmentDate);
        appointmentFilter.setPaginationMode(false);
        List<Appointments> existingAppointments = appointmentsDao.filterAppointment(appointmentFilter)
                .stream()
                .filter(app -> !"Cancelled".equalsIgnoreCase(app.getStatus()))
                .collect(Collectors.toList());

        int currentAppointmentsCount = existingAppointments.size();
        int maxAppointments = effectiveSchedule.getMaxAppointments();

        if (maxAppointments > 0 && currentAppointmentsCount >= maxAppointments) {
            return availableSlots; // Max appointments reached
        }

        // 4. Generate potential slots and check availability
        LocalTime scheduleStart = effectiveSchedule.getStartTime().toLocalTime();
        LocalTime scheduleEnd = effectiveSchedule.getEndTime().toLocalTime();

        if (localAppointmentDate.isEqual(LocalDate.now())) {
            LocalTime now = LocalTime.now();
            if (now.getMinute() % 15 != 0) {
                now = now.plusMinutes(15 - (now.getMinute() % 15));
            }
            if (scheduleStart.isBefore(now)) {
                scheduleStart = now;
            }
        }

        LocalTime currentTime = scheduleStart;
        int slotGranularityMinutes = 15;

        while (currentTime.plusMinutes(serviceDurationMinutes).isBefore(scheduleEnd)
                || currentTime.plusMinutes(serviceDurationMinutes).equals(scheduleEnd)) {

            LocalTime slotEndTime = currentTime.plusMinutes(serviceDurationMinutes);
            boolean isConflict = false;

            for (Appointments app : existingAppointments) {
                LocalTime appStart = app.getStartTime().toLocalTime();
                LocalTime appEnd = app.getEndTime().toLocalTime();

                if (currentTime.isBefore(appEnd) && slotEndTime.isAfter(appStart)) {
                    isConflict = true;
                    break;
                }
            }

            if (!isConflict) {
                availableSlots.add(new TimeSlotDto(Time.valueOf(currentTime), Time.valueOf(slotEndTime), true));
            }
            currentTime = currentTime.plusMinutes(slotGranularityMinutes);
        }

        return availableSlots;
    }

    /**
     * Books a new appointment.
     */
    private Integer bookAppointment(int patientId, int doctorId, int serviceId, Date appointmentDate, Time startTime, String notes) {
        // IMPORTANT: Assuming PatientID in Appointments table is the same as UserID from Users table.
        // If there's a separate Patients table with its own PK (PatientID) and a FK to Users.UserID,
        // you'd need to fetch the PatientID based on currentUser.getUserId().
        Patients patient = new Patients();
        patient.setPatientID(patientId);

        Doctor doctor = new Doctor();
        doctor.setDoctorID(doctorId);

        Service service = serviceDao.getServiceById(serviceId);
        if (service == null) {
            System.err.println("Service not found for ID: " + serviceId);
            return null;
        }
        int serviceDuration = service.getDuration();
        LocalTime localStartTime = startTime.toLocalTime();
        Time endTime = Time.valueOf(localStartTime.plusMinutes(serviceDuration));

        Appointments newAppointment = new Appointments();
        newAppointment.setPatientId(patient);
        newAppointment.setDoctorId(doctor);
        newAppointment.setServiceId(service);
        newAppointment.setAppointmentDate(appointmentDate);
        newAppointment.setStartTime(startTime);
        newAppointment.setEndTime(endTime);
        newAppointment.setStatus("Scheduled");
        newAppointment.setNotes(notes);

        Integer newAppointmentId = appointmentsDao.insertAppointment(newAppointment);

        if (newAppointmentId != null) {
            // TODO: Implement notification logic here - Trien khai logic thong bao o day
            System.out.println("Appointment booked. Notification placeholder here.");
        }
        return newAppointmentId;
    }

    /**
     * Gets the duration of a service in minutes.
     */
    private int getServiceDuration(int serviceId) {
        Service service = serviceDao.getServiceById(serviceId);
        return service != null ? service.getDuration() : 0;
    }
}