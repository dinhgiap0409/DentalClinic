/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dal.UsersDao;
import model.Users;
import java.io.IOException;
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
@WebServlet(name = "UserController", urlPatterns = {"/user"})
public class UserController extends HttpServlet {

    private UsersDao userDao;

    @Override
    public void init() throws ServletException {
        userDao = new UsersDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        switch (action) {
            case "login":
                showLoginPage(request, response);
                break;
            case "register":
                showRegisterPage(request, response);
                break;
            case "profile":
                showProfile(request, response);
                break;
            case "logout":
                logout(request, response);
                break;
            default:
                showLoginPage(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        switch (action) {
            case "login":
                handleLogin(request, response);
                break;
            case "register":
                handleRegister(request, response);
                break;
            case "update":
                handleUpdate(request, response);
                break;
        }
    }

    private void showLoginPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/customer/login.jsp").forward(request, response);
        request.getAuthType();
    }

    private void showRegisterPage(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/customer/register.jsp").forward(request, response);
    }

    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect("user?action=login");
            return;
        }
        
        request.setAttribute("user", user);
        request.getRequestDispatcher("views/customer/profile.jsp").forward(request, response);
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");
        
        // Lưu thông tin user trước khi xóa session
        request.setAttribute("user", user);
        
        // Xóa session
        session.invalidate();
        
        // Hiển thị trang logout
        request.getRequestDispatcher("views/customer/logout.jsp").forward(request, response);
    }

    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        Users user = userDao.login(email, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            if ("admin".equals(user.getRole())) {
                response.sendRedirect("views/dashboard/dashboard.jsp");
            } else {
                response.sendRedirect("views/guest/home.jsp");
            }
        } else {
            request.setAttribute("error", "Email hoặc mật khẩu không đúng!");
            request.getRequestDispatcher("views/customer/login.jsp").forward(request, response);
        }
    }

    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Users user = new Users();
        user.setUserName(request.getParameter("username"));
        user.setPassWord(request.getParameter("password"));
        user.setEmail(request.getParameter("email"));
        user.setFullName(request.getParameter("fullname"));
        user.setPhoneNumber(request.getParameter("phone"));
        user.setGender(request.getParameter("gender"));
        user.setAddress(request.getParameter("address"));
        user.setRole("patient"); // Mặc định là patient
        user.setIsActive(true);
        
        int result = userDao.insertUser(user);
        
        if (result > 0) {
            request.setAttribute("success", "Đăng ký thành công! Vui lòng đăng nhập.");
            request.getRequestDispatcher("views/customer/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Đăng ký thất bại! Email hoặc username đã tồn tại.");
            request.getRequestDispatcher("views/customer/register.jsp").forward(request, response);
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        
        if (currentUser == null) {
            response.sendRedirect("user?action=login");
            return;
        }
        
        // Cập nhật thông tin user
        currentUser.setUserName(request.getParameter("username"));
        currentUser.setEmail(request.getParameter("email"));
        currentUser.setFullName(request.getParameter("fullname"));
        currentUser.setPhoneNumber(request.getParameter("phone"));
        currentUser.setGender(request.getParameter("gender"));
        currentUser.setAddress(request.getParameter("address"));
        
        boolean success = userDao.updateUser(currentUser);
        
        if (success) {
            session.setAttribute("user", currentUser);
            request.setAttribute("success", "Cập nhật thông tin thành công!");
        } else {
            request.setAttribute("error", "Cập nhật thất bại!");
        }
        
        request.getRequestDispatcher("views/customer/profile.jsp").forward(request, response);
    }
}