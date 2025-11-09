/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DoctorDao;
import dal.PatientDao;
import dal.UsersDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Doctor;
import model.Patients;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
@WebServlet(name = "loginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

    private final UsersDao userDao = new UsersDao();
    private final DoctorDao doctorDao = new DoctorDao();
    private final PatientDao patientDao = new PatientDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        if (email == null || password == null || email.trim().isEmpty() || password.trim().isEmpty()) {
            request.setAttribute("error", "Vui lòng nhập đầy đủ thông tin đăng nhập!");
            request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
            return;
        }

        Users user = userDao.login(email, password);

        if (user == null) {
            request.setAttribute("error", "Sai email hoặc mật khẩu!");
            request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
            return;
        }

        if (user.getIsActive() == null || !user.getIsActive()) {
            request.setAttribute("error", "Tài khoản của bạn đã bị khóa hoặc chưa được kích hoạt!");
            request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
            return;
        }

        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        String role = user.getRole() != null ? user.getRole().trim().toLowerCase() : "";

        switch (role) {
            case "doctor": {
                Doctor doctor = doctorDao.getDoctorByUserId(user.getUserId());
                if (doctor != null) {
                    session.setAttribute("doctor", doctor);
                }
                response.sendRedirect("patientsToday");
                break;
            }
            case "patient": {
                Patients patient = patientDao.getPatientByUserId(user.getUserId());
                if (patient != null) {
                    session.setAttribute("patient", patient);
                }
                response.sendRedirect("home");
                break;
            }
            case "admin":{
                  response.sendRedirect("manageOrder");
                  break;
            }
            case "receptionist": { 
                response.sendRedirect("lookUpAppointments");
                break;
            }
            default: {
                response.sendRedirect("home");
                break;
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
