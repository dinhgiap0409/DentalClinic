/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.BookAppointment;

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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import model.ScheduleExceptions;
import model.Schedules;

@WebServlet(name = "DoctorWorkScheduleController", urlPatterns = {"/doctorWorkSchedule"})
public class DoctorWorkScheduleController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        try {
            int doctorId = Integer.parseInt(req.getParameter("doctorId"));
            int serviceId = Integer.parseInt(req.getParameter("serviceId"));

            // Tuần hiển thị: mặc định tuần hiện tại; có thể chỉnh bằng ?weekOffset=-1/0/1...
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

            ScheduleDao scheduleDAO = new ScheduleDao();
            ScheduleExceptionsDao exDao = new ScheduleExceptionsDao();

            // Cache base schedules theo dayOfWeek (1..7)
            Map<Integer, List<Schedules>> baseByDow = new HashMap<>();

            // Kết quả: date -> slots available
            Map<LocalDate, List<Schedules>> availableByDate = new LinkedHashMap<>();

            for (LocalDate d = monday; !d.isAfter(sunday); d = d.plusDays(1)) {
                int dow = d.getDayOfWeek().getValue(); // 1=Mon..7=Sun

                // Base schedules cho thứ dow
                List<Schedules> base = baseByDow.computeIfAbsent(dow, k -> {
                    ScheduleDto f = new ScheduleDto();
                    f.setDoctorId(doctorId);
                    f.setDayOfWeek(k);
                    f.setIsAvailable(true);
                    List<Schedules> list = scheduleDAO.filterSchedules(f);
                    list.sort(Comparator.comparing(Schedules::getStartTime));
                    return list;
                });

                if (base.isEmpty()) {
                    availableByDate.put(d, List.of());
                    continue;
                }

                // Exceptions trong ngày d
                ScheduleExceptionsDto exF = new ScheduleExceptionsDto();
                exF.setDoctorId(doctorId);
                exF.setExceptionDate(java.sql.Date.valueOf(d));
                List<ScheduleExceptions> exs = exDao.filterScheduleExceptions(exF);

                // Nghỉ cả ngày?
                boolean dayOff = exs.stream().anyMatch(ex -> !ex.isIsWorkingDay());
                if (dayOff) {
                    availableByDate.put(d, List.of());
                    continue;
                }

                // Loại các slot trùng với exception time-range
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
                availableByDate.put(d, afterEx);
            }

            req.setAttribute("availableByDate", availableByDate);
            req.setAttribute("doctorId", doctorId);
            req.setAttribute("serviceId", serviceId);
            req.setAttribute("monday", monday);
            req.setAttribute("sunday", sunday);
            req.setAttribute("weekOffset", weekOffset);

            req.getRequestDispatcher("/views/guest/doctorSchedule.jsp").forward(req, resp);

        } catch (Exception e) {
            e.printStackTrace();
            req.setAttribute("errorMessage", "Không thể tải lịch làm việc của bác sĩ.");
            req.getRequestDispatcher("/error.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
