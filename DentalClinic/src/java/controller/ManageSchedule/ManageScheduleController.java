package controller.ManageSchedule;

import dal.DoctorDao;
import dal.ScheduleDao;
import dto.ScheduleDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Schedules;
import java.sql.Time;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Doctor;

/**
 *
 * @author TNO
 */
@WebServlet(urlPatterns = {"/manageSchedule"})
public class ManageScheduleController extends HttpServlet {

    private final ScheduleDao scheduleDao = new ScheduleDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "viewList";
        }

        switch (action) {
            case "add":
                viewAddPage(request, response);
                break;
            case "viewDetail":
                viewDetail(request, response);
                break;
            case "viewList":
            default:
                viewList(request, response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            boolean forwardOccurred = false; // đánh dấu nếu đã forward
            switch (action) {
                case "add":
                    forwardOccurred = addSchedule(request, response);
                    break;
                case "update":
                    forwardOccurred = updateSchedule(request, response);
                    break;
            }
            if (!forwardOccurred) {
                response.sendRedirect("manageSchedule");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void viewList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // ====== Lấy filter từ request ======
        ScheduleDto filter = new ScheduleDto();
        String doctorName = request.getParameter("doctorName");
        String dayOfWeekStr = request.getParameter("dayOfWeek");
        String isAvailableStr = request.getParameter("isAvailable");
        String requiresApprovalStr = request.getParameter("requiresApproval");
        String isApprovedStr = request.getParameter("isApproved");
        String startTimeStr = request.getParameter("startTime");
        String endTimeStr = request.getParameter("endTime");

        filter.setDoctorName(doctorName);
        if (dayOfWeekStr != null && !dayOfWeekStr.isEmpty()) {
            filter.setDayOfWeek(Integer.parseInt(dayOfWeekStr));
        }
        if (isAvailableStr != null && !isAvailableStr.isEmpty()) {
            filter.setIsAvailable(Boolean.parseBoolean(isAvailableStr));
        }
        if (requiresApprovalStr != null) {
            filter.setRequiresApproval(Boolean.parseBoolean(requiresApprovalStr));
        }
        if (isApprovedStr != null) {
            filter.setIsApproved(Boolean.parseBoolean(isApprovedStr));
        }
        if (startTimeStr != null && !startTimeStr.isEmpty()) {
            filter.setStartTime(Time.valueOf(startTimeStr));
        }
        if (endTimeStr != null && !endTimeStr.isEmpty()) {
            filter.setEndTime(Time.valueOf(endTimeStr));
        }

        // ====== Phân trang & sắp xếp ======
        int page = 1;
        int size = 10;
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }
        if (sizeStr != null) {
            size = Integer.parseInt(sizeStr);
        }

        filter.setPaginationMode(true);
        filter.setPage(page);
        filter.setSize(size);

        String sortModeStr = request.getParameter("sortMode");
        if (sortModeStr != null) {
            filter.setSortMode(Boolean.parseBoolean(sortModeStr));
        }

        // ====== Lấy danh sách và tổng số ======
        List<Schedules> schedules = scheduleDao.filterSchedules(filter);
        int totalCount = scheduleDao.countFilter(filter);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        // ====== Set attributes cho JSP ======
        request.setAttribute("schedules", schedules);
        request.setAttribute("page", page);
        request.setAttribute("size", size);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("baseUrl", "manageSchedule");

        request.getRequestDispatcher("/views/manage/manageSchedule.jsp").forward(request, response);
    }

    private void viewDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String scheduleIdStr = request.getParameter("scheduleId");
        if (scheduleIdStr != null && !scheduleIdStr.isEmpty()) {
            int scheduleId = Integer.parseInt(scheduleIdStr);
            Schedules s = scheduleDao.getScheduleById(scheduleId);
            request.setAttribute("schedule", s);
            request.getRequestDispatcher("/views/manage/manageScheduleDetail.jsp").forward(request, response);
        } else {
            response.sendRedirect("manageSchedule");
        }
    }

    private boolean addSchedule(HttpServletRequest request, HttpServletResponse response) throws Exception {
        Schedules s = parseScheduleFromRequest(request);

        if (isScheduleConflict(s, 0)) {
            request.setAttribute("error", "Lịch trùng với một lịch đã có sẵn!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return true; // đã forward
        }

        scheduleDao.insertSchedule(s);
        return false;
    }

    private boolean updateSchedule(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String idStr = request.getParameter("scheduleId");
        if (idStr == null || idStr.isEmpty()) {
            response.sendRedirect("manageSchedule");
            return true; // đã redirect
        }
        int scheduleId = Integer.parseInt(idStr);

        Schedules existing = scheduleDao.getScheduleById(scheduleId);
        if (existing == null) {
            response.sendRedirect("manageSchedule");
            return true;
        }

        updateScheduleFromRequest(existing, request);

        if (existing.getStartTime() != null && existing.getEndTime() != null
                && !existing.getStartTime().before(existing.getEndTime())) {
            request.setAttribute("error", "Giờ bắt đầu phải nhỏ hơn giờ kết thúc");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return true;
        }

        if (isScheduleConflict(existing, scheduleId)) {
            request.setAttribute("error", "Lịch trùng với một lịch đã có sẵn!");
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return true;
        }

        scheduleDao.updateSchedule(existing);
        return false;
    }

// Helper update dữ liệu từ request
    private void updateScheduleFromRequest(Schedules s, HttpServletRequest request) {
        String doctorIdStr = request.getParameter("doctorId");
        if (doctorIdStr != null && !doctorIdStr.isEmpty()) {
            Doctor d = new Doctor();
            d.setDoctorID(Integer.parseInt(doctorIdStr));
            s.setDoctorId(d);
        }

        String dayOfWeekStr = request.getParameter("dayOfWeek");
        if (dayOfWeekStr != null && !dayOfWeekStr.isEmpty()) {
            s.setDayOfWeek(Integer.parseInt(dayOfWeekStr));
        }

        String startTimeStr = request.getParameter("startTime");
        if (startTimeStr != null && !startTimeStr.isEmpty()) {
            s.setStartTime(parseTimeWithSeconds(startTimeStr));
        }

        String endTimeStr = request.getParameter("endTime");
        if (endTimeStr != null && !endTimeStr.isEmpty()) {
            s.setEndTime(parseTimeWithSeconds(endTimeStr));
        }

        String isAvailableStr = request.getParameter("isAvailable");
        if (isAvailableStr != null && !isAvailableStr.isEmpty()) {
            s.setIsAvailable(Boolean.parseBoolean(isAvailableStr));
        }

        String maxAppointmentsStr = request.getParameter("maxAppointments");
        if (maxAppointmentsStr != null && !maxAppointmentsStr.isEmpty()) {
            s.setMaxAppointments(Integer.parseInt(maxAppointmentsStr));
        }

        String requiresApprovalStr = request.getParameter("requiresApproval");
        if (requiresApprovalStr != null && !requiresApprovalStr.isEmpty()) {
            s.setRequiresApproval(Boolean.parseBoolean(requiresApprovalStr));
        }

        String isApprovedStr = request.getParameter("isApproved");
        if (isApprovedStr != null && !isApprovedStr.isEmpty()) {
            s.setIsApproved(Boolean.parseBoolean(isApprovedStr));
        }

        String validFromStr = request.getParameter("validFrom");
        if (validFromStr != null && !validFromStr.isEmpty()) {
            s.setValidFrom(java.sql.Date.valueOf(validFromStr));
        }

        String validToStr = request.getParameter("validTo");
        if (validToStr != null && !validToStr.isEmpty()) {
            s.setValidTo(java.sql.Date.valueOf(validToStr));
        }
    }

// Helper parse HH:mm → HH:mm:ss tránh lỗi
    private Time parseTimeWithSeconds(String timeStr) {
        if (timeStr == null || timeStr.isEmpty()) {
            return null;
        }
        if (!timeStr.contains(":")) {
            return null;
        }
        String[] parts = timeStr.split(":");
        if (parts.length == 2) {
            timeStr += ":00";
        }
        try {
            return Time.valueOf(timeStr);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private Schedules parseScheduleFromRequest(HttpServletRequest request) {
        Schedules s = new Schedules();

        String doctorIdStr = request.getParameter("doctorId");
        if (doctorIdStr != null && !doctorIdStr.isEmpty()) {
            model.Doctor d = new model.Doctor();
            d.setDoctorID(Integer.parseInt(doctorIdStr));
            s.setDoctorId(d);
        }

        String dayOfWeekStr = request.getParameter("dayOfWeek");
        if (dayOfWeekStr != null && !dayOfWeekStr.isEmpty()) {
            s.setDayOfWeek(Integer.parseInt(dayOfWeekStr));
        }

        String startTimeStr = request.getParameter("startTime");
        if (startTimeStr != null && !startTimeStr.isEmpty()) {
            s.setStartTime(parseTimeWithSeconds(startTimeStr));
        }

        String endTimeStr = request.getParameter("endTime");
        if (endTimeStr != null && !endTimeStr.isEmpty()) {
            s.setEndTime(parseTimeWithSeconds(endTimeStr));
        }

        String isAvailableStr = request.getParameter("isAvailable");
        if (isAvailableStr != null) {
            s.setIsAvailable(Boolean.parseBoolean(isAvailableStr));
        }

        String maxAppointmentsStr = request.getParameter("maxAppointments");
        if (maxAppointmentsStr != null && !maxAppointmentsStr.isEmpty()) {
            s.setMaxAppointments(Integer.parseInt(maxAppointmentsStr));
        }

        String requiresApprovalStr = request.getParameter("requiresApproval");
        if (requiresApprovalStr != null) {
            s.setRequiresApproval(Boolean.parseBoolean(requiresApprovalStr));
        }

        String isApprovedStr = request.getParameter("isApproved");
        if (isApprovedStr != null) {
            s.setIsApproved(Boolean.parseBoolean(isApprovedStr));
        }

        // Optional: validFrom, validTo
        String validFromStr = request.getParameter("validFrom");
        if (validFromStr != null && !validFromStr.isEmpty()) {
            s.setValidFrom(java.sql.Date.valueOf(validFromStr));
        }
        String validToStr = request.getParameter("validTo");
        if (validToStr != null && !validToStr.isEmpty()) {
            s.setValidTo(java.sql.Date.valueOf(validToStr));
        }

        return s;
    }

    private boolean isScheduleConflict(Schedules schedule, int currentScheduleId) {
        ScheduleDto filter = new ScheduleDto();
        filter.setDoctorId(schedule.getDoctorId().getDoctorID());
        filter.setDayOfWeek(schedule.getDayOfWeek());
        filter.setIsAvailable(true);
        filter.setPaginationMode(false);

        List<Schedules> existingSchedules = scheduleDao.filterSchedules(filter);

        // Loại bỏ chính nó nếu update
        if (currentScheduleId > 0) {
            existingSchedules.removeIf(s -> s.getScheduleId() == currentScheduleId);
        }

        // Kiểm tra trùng giờ
        return existingSchedules.stream().anyMatch(s
                -> !schedule.getEndTime().before(s.getStartTime())
                && !schedule.getStartTime().after(s.getEndTime())
        );
    }

    private void viewAddPage(HttpServletRequest request, HttpServletResponse response) {
        try {
            DoctorDao doctorDao = new DoctorDao();

            List<Doctor> doctors = doctorDao.getAllDoctors();
            request.setAttribute("doctors", doctors);
            request.getRequestDispatcher("views/manage/manageScheduleAdd.jsp").forward(request, response);
        } catch (ServletException ex) {
            Logger.getLogger(ManageScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManageScheduleController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
