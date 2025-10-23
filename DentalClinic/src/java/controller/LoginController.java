/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.UsersDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Users;

/**
 *
 * @author Nguyen Dang Khang
 */
@WebServlet(name = "loginController", urlPatterns = {"/login"})
public class LoginController extends HttpServlet {

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

        if (email != null && password != null) {
            UsersDao userDao = new UsersDao();
            Users user = userDao.login(email, password);

            if (user != null) {
                HttpSession session = request.getSession();
                session.setAttribute("user", user);

                // Điều hướng dựa trên vai trò của người dùng
                String role = user.getRole();
                String contextPath = request.getContextPath();

                if (role != null) {
                    switch (role) {
                        case "Receptionist":
                            response.sendRedirect(contextPath + "/receptionist/dashboard");
                            break;
                        case "Admin":
                            response.sendRedirect(contextPath + "/admin/dashboard"); // Sẵn sàng cho tương lai
                            break;
                        case "Doctor":
                            response.sendRedirect(contextPath + "/doctor/dashboard"); // Sẵn sàng cho tương lai
                            break;
                        default: // Mặc định cho 'Patient' và các vai trò khác
                            response.sendRedirect(contextPath + "/home");
                    }
                } else {
                    response.sendRedirect(contextPath + "/home"); // Fallback nếu không có vai trò
                }
            } else {
                request.setAttribute("error", "Email or password error!");
                request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
            }
        } else {
            request.setAttribute("error", "Please input full information!");
            request.getRequestDispatcher("/views/customer/login.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
