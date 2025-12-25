/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.ManageUser;

import dal.DoctorDao;
import dal.PatientDao;
import dal.UsersDao;
import dto.UserDto;
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
import model.Users;

/**
 *
 * @author TNO
 */
@WebServlet(name = "ManageUserController", urlPatterns = {"/manageUser"})
public class ManageUserController extends HttpServlet {

    private final UsersDao usersDao = new UsersDao();
    private final PatientDao patientDao = new PatientDao();
    private final DoctorDao doctorDao = new DoctorDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if (action == null) {
            action = "list";
        }

        switch (action) {
            case "view":
                viewUserDetail(request, response);
                break;
            case "list":
            default:
                listUsers(request, response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("updateStatus".equalsIgnoreCase(action)) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            boolean isActive = Boolean.parseBoolean(request.getParameter("isActive"));

            Users user = usersDao.getUserById(userId);
            if (user != null) {
                user.setIsActive(isActive);
                usersDao.updateUser(user);
            }

            response.sendRedirect("manageUser?action=view&userId=" + userId);
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

    private void listUsers(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // === Lấy filter từ request ===
            String fullName = request.getParameter("fullName");
        String phone = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        String isActiveStr = request.getParameter("isActive");
        Boolean isActive = (isActiveStr != null && !isActiveStr.isBlank())
                ? Boolean.valueOf(isActiveStr)
                : null;

        // === Phân trang ===
        int page = 1;
        int size = 10;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception ignored) {
        }
        try {
            size = Integer.parseInt(request.getParameter("size"));
        } catch (Exception ignored) {
        }

        // === Tạo DTO filter ===
        UserDto dto = new UserDto();
        dto.setFullName(fullName);
        dto.setPhoneNumber(phone);
        dto.setAddress(address);
        dto.setIsActive(isActive);
        dto.setPaginationMode(true);
        dto.setPage(page);
        dto.setSize(size);

        // === Query dữ liệu ===
        List<Users> users = usersDao.filterUser(dto);
        int total = usersDao.countFilterUser(dto);
        int totalPages = (int) Math.ceil((double) total / size);

        // === Set attribute cho JSP ===
        request.setAttribute("users", users);
        request.setAttribute("filter", dto);
        request.setAttribute("page", page);
        request.setAttribute("size", size);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("baseUrl","manageUser?action=list");

        // Chuyển tới JSP
        request.getRequestDispatcher("/views/admin/manageUser.jsp").forward(request, response);
    }

    private void viewUserDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int userId = Integer.parseInt(request.getParameter("userId"));
        Users user = usersDao.getUserById(userId);

        if (user == null) {
            response.sendRedirect("manageUser?action=list");
            return;
        }

        // Nếu là bệnh nhân hoặc bác sĩ thì lấy thêm thông tin
        if ("Patient".equalsIgnoreCase(user.getRole())) {
            Patients patient = patientDao.getPatientByUserId(userId);
            request.setAttribute("patient", patient);
        } else if ("Doctor".equalsIgnoreCase(user.getRole())) {
            Doctor doctor = doctorDao.getDoctorByUserId(userId);
            request.setAttribute("doctor", doctor);
        }

        request.setAttribute("user", user);
        request.getRequestDispatcher("/views/admin/userDetail.jsp").forward(request, response);
    }

}
