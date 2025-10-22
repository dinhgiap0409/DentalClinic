/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.BookAppointment;

import dal.AppointmentsDao;
import dal.ScheduleDao;
import dal.ScheduleExceptionsDao;
import dto.AppointmentDto;
import dto.ScheduleDto;
import dto.ScheduleExceptionsDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import model.Appointments;
import model.ScheduleExceptions;
import model.Schedules;

@WebServlet(name = "DoctorWorkScheduleController", urlPatterns = {"/doctorWorkSchedule"})
public class DoctorWorkScheduleController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //Nhận doctorId và ngày
            int doctorId = Integer.parseInt(request.getParameter("doctorId"));
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));

            String dateParam = request.getParameter("date");
            LocalDate selectedDate = (dateParam != null)
                    ? LocalDate.parse(dateParam)
                    : LocalDate.now();

            int dayOfWeek = selectedDate.getDayOfWeek().getValue();

            //Lấy lịch làm việc (Schedules)
            ScheduleDao scheduleDAO = new ScheduleDao();
            ScheduleDto scheduleFilter = new ScheduleDto();
            scheduleFilter.setDoctorId(doctorId);
            scheduleFilter.setDayOfWeek(dayOfWeek);
            scheduleFilter.setIsAvailable(true);
            List<Schedules> baseSchedules = scheduleDAO.filterSchedules(scheduleFilter);

            //Lấy danh sách ngoại lệ (ScheduleExceptions)
            ScheduleExceptionsDao exDao = new ScheduleExceptionsDao();
            ScheduleExceptionsDto exFilter = new ScheduleExceptionsDto();
            exFilter.setDoctorId(doctorId);
            exFilter.setExceptionDate(java.sql.Date.valueOf(selectedDate));
            List<ScheduleExceptions> exceptions = exDao.filterScheduleExceptions(exFilter);

            //Lọc slot trống (loại ngày nghỉ)
            List<Schedules> availableSlots = new ArrayList<>();
            boolean isDayOff = exceptions.stream().anyMatch(ex -> !ex.isIsWorkingDay());
            if (!isDayOff) {
                for (Schedules schedule : baseSchedules) {
                    boolean overlapped = exceptions.stream().anyMatch(ex
                            -> ex.getStartTime() != null && ex.getEndTime() != null
                            && ex.getStartTime().before(schedule.getEndTime())
                            && ex.getEndTime().after(schedule.getStartTime())
                    );
                    if (!overlapped) {
                        availableSlots.add(schedule);
                    }
                }
            }

            //Kiểm tra slot đã được đặt trong Appointments
            AppointmentsDao appDao = new AppointmentsDao();
            AppointmentDto appFilter = new AppointmentDto();
            appFilter.setDoctorId(doctorId);
            appFilter.setAppointmentDate(java.sql.Date.valueOf(selectedDate));
            appFilter.setStatus("CONFIRMED");

            List<Appointments> bookedAppointments = appDao.filterAppointment(appFilter);

            List<Schedules> finalAvailableSlots = new ArrayList<>();

            for (Schedules slot : availableSlots) {
                boolean hasBooked = bookedAppointments.stream().anyMatch(app
                        -> app.getStartTime().before(slot.getEndTime())
                        && app.getEndTime().after(slot.getStartTime())
                );
                if (!hasBooked) {
                    finalAvailableSlots.add(slot);
                }
            }

            //Trả kết quả về view
            request.setAttribute("availableSlots", finalAvailableSlots);
            request.setAttribute("serviceId", serviceId);
            request.setAttribute("selectedDate", selectedDate);
            request.setAttribute("doctorId", doctorId);
            request.getRequestDispatcher("/views/guest/doctorSchedule.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Không thể tải lịch làm việc của bác sĩ.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
