/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.PatientDao;
import dal.UsersDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Patients;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
@WebServlet(name="resigterController", urlPatterns={"/register"})
public class ResigterController extends HttpServlet {
    private UsersDao userDao = new UsersDao();
    private PatientDao patientDao = new PatientDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         request.getRequestDispatcher("/views/customer/register.jsp").forward(request, response);
    } 

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
         Users user = new Users();
        user.setUserName(request.getParameter("username"));
        user.setPassWord(request.getParameter("password"));
        user.setEmail(request.getParameter("email"));
        user.setFullName(request.getParameter("fullname"));
        user.setPhoneNumber(request.getParameter("phone"));
        user.setGender(request.getParameter("gender"));
        user.setAddress(request.getParameter("address") != null ? request.getParameter("address") : "");
        // Lấy vai trò từ form, nếu không có thì mặc định là "patient"
        // String role = request.getParameter("role");
        // user.setRole(role != null ? role : "patient");
        user.setRole("patient");
        user.setIsActive(true);
        
        // Bước 1: Thêm người dùng vào bảng Users và lấy về ID
        int newUserId = userDao.insertUser(user);
        
        if (newUserId > 0) {
            // Bước 2: Kiểm tra vai trò. Nếu là "patient", tự động tạo hồ sơ bệnh nhân
            if ("patient".equalsIgnoreCase(user.getRole())) {
                Users createdUser = new Users();
                createdUser.setUserId(newUserId);
                
                Patients newPatient = new Patients();
                newPatient.setUserID(createdUser); // Liên kết với UserID vừa tạo
                
                patientDao.insertPatient(newPatient); // Thêm vào bảng Patients
            }
            request.setAttribute("success", "Register successful! Please login");
            request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Resigter fail! Email or username existed.");
            request.getRequestDispatcher("/views/customer/register.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
