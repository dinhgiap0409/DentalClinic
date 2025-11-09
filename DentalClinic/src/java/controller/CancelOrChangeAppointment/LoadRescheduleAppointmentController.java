/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.CancelOrChangeAppointment;

import dal.AppointmentsDao;
import dal.ScheduleDao;
import dal.ScheduleExceptionsDao;
import dto.ScheduleDto;
import dto.ScheduleExceptionsDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Appointments;
import model.Schedules;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Comparator;
import model.ScheduleExceptions;
import java.sql.Date;
/**
 *
 * @author TNO
 */
@WebServlet(name = "LoadRescheduleAppointmentController", urlPatterns = {"/loadRescheduleAppointment"})
public class LoadRescheduleAppointmentController extends HttpServlet {

    private final AppointmentsDao appDao = new AppointmentsDao();
    private final ScheduleDao scheduleDao = new ScheduleDao();
    private final ScheduleExceptionsDao exDao = new ScheduleExceptionsDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            // 1) Lấy appointment hiện tại
            String idParam = req.getParameter("appointmentId");
            if (idParam == null || idParam.trim().isEmpty()) {
                req.setAttribute("errorMessage", "Thiếu tham số appointmentId.");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            int appointmentId;
            try {
                appointmentId = Integer.parseInt(idParam.trim());
            } catch (NumberFormatException ex) {
                req.setAttribute("errorMessage", "appointmentId không hợp lệ.");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            Appointments ap = appDao.getAppointmentsById(appointmentId);
            if (ap == null) {
                req.setAttribute("errorMessage", "Không tìm thấy lịch hẹn!");
                req.getRequestDispatcher("/error.jsp").forward(req, resp);
                return;
            }

            // 2) Tuần hiển thị: tuần chứa ngày hẹn hiện tại (có thể dịch chuyển bằng weekOffset)
            int weekOffset = 0;
            String off = req.getParameter("weekOffset");
            if (off != null) {
                try {
                    weekOffset = Integer.parseInt(off);
                } catch (NumberFormatException ignore) {
                }
            }

            // ap.getAppointmentDate() là java.sql.Date (có toLocalDate() từ Java 8)
            LocalDate base = ap.getAppointmentDate().toLocalDate();
            LocalDate monday = base.with(DayOfWeek.MONDAY).plusWeeks(weekOffset);
            LocalDate sunday = monday.plusDays(6);

            int doctorId = ap.getDoctorId().getDoctorID();

            // 3) Tính slot trống: chỉ base schedules - exceptions (KHÔNG trừ các appointments khác)
            Map<Integer, List<Schedules>> baseByDow = new HashMap<>();
            Map<LocalDate, List<Schedules>> availableByDate = new LinkedHashMap<>();

            for (LocalDate d = monday; !d.isAfter(sunday); d = d.plusDays(1)) {
                int dow = d.getDayOfWeek().getValue(); // 1..7 (Mon..Sun)

                // 3.1) Base schedules theo thứ
                List<Schedules> baseList = baseByDow.computeIfAbsent(dow, k -> {
                    ScheduleDto f = new ScheduleDto();
                    f.setDoctorId(doctorId);
                    f.setDayOfWeek(k);
                    f.setIsAvailable(true);
                    List<Schedules> list = scheduleDao.filterSchedules(f);
                    list.sort(Comparator.comparing(Schedules::getStartTime));
                    return list;
                });

                if (baseList.isEmpty()) {
                    availableByDate.put(d, Collections.emptyList());
                    continue;
                }

                // 3.2) Áp exceptions theo ngày d
                ScheduleExceptionsDto exF = new ScheduleExceptionsDto();
                exF.setDoctorId(doctorId);
                exF.setExceptionDate(Date.valueOf(d));
                List<ScheduleExceptions> exs = exDao.filterScheduleExceptions(exF);

                boolean dayOff = exs.stream().anyMatch(ex -> !ex.isIsWorkingDay());
                if (dayOff) {
                    availableByDate.put(d, Collections.emptyList());
                    continue;
                }

                List<Schedules> afterEx = new ArrayList<>();
                for (Schedules s : baseList) {
                    boolean overlapEx = exs.stream().anyMatch(ex
                            -> ex.getStartTime() != null && ex.getEndTime() != null
                            && s.getStartTime().before(ex.getEndTime())
                            && s.getEndTime().after(ex.getStartTime())
                    );
                    if (!overlapEx) {
                        afterEx.add(s);
                    }
                }

                if (afterEx.isEmpty()) {
                    availableByDate.put(d, Collections.emptyList());
                    continue;
                }

                // 3.3) KHÔNG loại slot đã book của người khác.
                //      Chỉ loại đúng KHUNG GIỜ CŨ của chính appointment đang reschedule (tránh chọn y hệt).
                List<Schedules> finalSlots = new ArrayList<>();
                LocalDate apDate = ap.getAppointmentDate().toLocalDate();
                for (Schedules s : afterEx) {
                    boolean sameDate = d.equals(apDate);
                    boolean sameStart = s.getStartTime().equals(ap.getStartTime());
                    boolean sameEnd = s.getEndTime().equals(ap.getEndTime());

                    if (sameDate && sameStart && sameEnd) {
                        // Bỏ slot trùng hoàn toàn với khung giờ cũ
                        continue;
                    }
                    finalSlots.add(s);
                }

                finalSlots.sort(Comparator.comparing(Schedules::getStartTime));
                availableByDate.put(d, finalSlots);
            }

            // 4) Trả dữ liệu sang view
            req.setAttribute("appointment", ap);
            req.setAttribute("availableByDate", availableByDate);
            req.setAttribute("monday", monday);
            req.setAttribute("sunday", sunday);
            req.setAttribute("weekOffset", weekOffset);

            req.getRequestDispatcher("/views/patient/reschedule_appointment.jsp")
                    .forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Không thể tải trang thay đổi lịch hẹn!");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
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
