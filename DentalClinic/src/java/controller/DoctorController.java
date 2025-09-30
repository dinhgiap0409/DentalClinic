/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dal.DoctorDao;
import dal.UsersDao;
import model.Doctor;
import model.Users;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Nguyen Dinh Giap
 */
@WebServlet(name = "DoctorController", urlPatterns = {"/doctor"})
public class DoctorController extends HttpServlet {

    private DoctorDao doctorDao;
    private UsersDao userDao;

    @Override
    public void init() throws ServletException {
        doctorDao = new DoctorDao();
        userDao = new UsersDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        switch (action) {
            case "list":
                showDoctorList(request, response);
                break;
            case "detail":
                showDoctorDetail(request, response);
                break;
            case "add":
                showAddDoctorForm(request, response);
                break;
            case "edit":
                showEditDoctorForm(request, response);
                break;
            case "delete":
                deleteDoctor(request, response);
                break;
            default:
                showDoctorList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        switch (action) {
            case "add":
                handleAddDoctor(request, response);
                break;
            case "update":
                handleUpdateDoctor(request, response);
                break;
        }
    }

    private void showDoctorList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Doctor> doctors = doctorDao.getAllDoctors();
        request.setAttribute("doctors", doctors);
        request.getRequestDispatcher("views/dashboard/doctorList.jsp").forward(request, response);
    }

    private void showDoctorDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int doctorId = Integer.parseInt(request.getParameter("id"));
        Doctor doctor = doctorDao.getDoctorByID(doctorId);
        
        if (doctor != null) {
            request.setAttribute("doctor", doctor);
            request.getRequestDispatcher("views/dashboard/doctor-detail.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy bác sĩ!");
            request.getRequestDispatcher("views/dashboard/doctorList.jsp").forward(request, response);
        }
    }

    private void showAddDoctorForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/dashboard/doctorAdd.jsp").forward(request, response);
    }

    private void showEditDoctorForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int doctorId = Integer.parseInt(request.getParameter("id"));
        Doctor doctor = doctorDao.getDoctorByID(doctorId);
        
        if (doctor != null) {
            request.setAttribute("doctor", doctor);
            request.getRequestDispatcher("views/dashboard/doctorEdit.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy bác sĩ!");
            request.getRequestDispatcher("views/dashboard/doctorList.jsp").forward(request, response);
        }
    }

    private void deleteDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int doctorId = Integer.parseInt(request.getParameter("id"));
        boolean success = doctorDao.deleteDoctor(doctorId);
        
        if (success) {
            request.setAttribute("success", "Xóa bác sĩ thành công!");
        } else {
            request.setAttribute("error", "Xóa bác sĩ thất bại!");
        }
        
        response.sendRedirect("doctor?action=list");
    }

    private void handleAddDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            response.sendRedirect("user?action=login");
            return;
        }
        
        Doctor doctor = new Doctor();
        doctor.setUserId(currentUser);
        doctor.setSpecialization(request.getParameter("specialization"));
        doctor.setLicenseNumber(request.getParameter("licenseNumber"));
        doctor.setYearsOfExperience(Integer.parseInt(request.getParameter("yearsOfExperience")));
        doctor.setEducation(request.getParameter("education"));
        doctor.setBiography(request.getParameter("biography"));
        doctor.setConsultationFee(new BigDecimal(request.getParameter("consultationFee")));
        
        int result = doctorDao.insertDoctor(doctor);
        
        if (result > 0) {
            request.setAttribute("success", "Thêm bác sĩ thành công!");
        } else {
            request.setAttribute("error", "Thêm bác sĩ thất bại!");
        }
        
        request.getRequestDispatcher("views/dashboard/doctorAdd.jsp").forward(request, response);
    }

    private void handleUpdateDoctor(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            response.sendRedirect("user?action=login");
            return;
        }
        
        Doctor doctor = new Doctor();
        doctor.setDoctorID(Integer.parseInt(request.getParameter("doctorId")));
        doctor.setUserId(currentUser);
        doctor.setSpecialization(request.getParameter("specialization"));
        doctor.setLicenseNumber(request.getParameter("licenseNumber"));
        doctor.setYearsOfExperience(Integer.parseInt(request.getParameter("yearsOfExperience")));
        doctor.setEducation(request.getParameter("education"));
        doctor.setBiography(request.getParameter("biography"));
        doctor.setConsultationFee(new BigDecimal(request.getParameter("consultationFee")));
        
        boolean success = doctorDao.updateDoctor(doctor);
        
        if (success) {
            request.setAttribute("success", "Cập nhật bác sĩ thành công!");
        } else {
            request.setAttribute("error", "Cập nhật bác sĩ thất bại!");
        }
        
        request.getRequestDispatcher("views/dashboard/doctorEdit.jsp").forward(request, response);
    }
}
