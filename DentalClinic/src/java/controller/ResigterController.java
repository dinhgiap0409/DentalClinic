/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.UsersDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
@WebServlet(name="resigterController", urlPatterns={"/register"})
public class ResigterController extends HttpServlet {
    private UsersDao userDao = new UsersDao();

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
        user.setRole("patient");
        user.setIsActive(true);
        
        int result = userDao.insertUser(user);
        
        if (result > 0) {
            request.setAttribute("success", "Resigter successful! Please login");
            request.getRequestDispatcher("views/customer/login.jsp").forward(request, response);
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
