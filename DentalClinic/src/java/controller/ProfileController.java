/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.PatientDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Patients;
import model.Users;

/**
 *
 * @author TNO
 */
@WebServlet(name = "ProfileController", urlPatterns = {"/profile"})
public class ProfileController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/profile/profile.jsp").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users user = (Users) session.getAttribute("user");

        if (user == null) {
            response.sendRedirect("login");
            return;
        }

        if ("Patient".equalsIgnoreCase(user.getRole())) {
            PatientDao dao = new PatientDao();

            // Lấy thông tin từ form
            String bloodType = request.getParameter("bloodType");
            String allergies = request.getParameter("allergies");
            String insuranceInfo = request.getParameter("insuranceInfo");
            String medicalHistory = request.getParameter("medicalHistory");
            String emergencyContactName = request.getParameter("emergencyContactName");
            String emergencyContactPhone = request.getParameter("emergencyContactPhone");

            // Kiểm tra bệnh nhân theo userId
            Patients patient = dao.getPatientByUserId(user.getUserId());

            if (patient == null) {
                // Tạo mới bệnh nhân
                patient = new Patients();
                patient.setUserID(user);
                patient.setBloodType(bloodType);
                patient.setAllergies(allergies);
                patient.setInsuranceInfo(insuranceInfo);
                patient.setMedicalHistory(medicalHistory);
                patient.setEmergencyContactName(emergencyContactName);
                patient.setEmergencyContactPhone(emergencyContactPhone);

                dao.insertPatient(patient);
            } else {
                // Cập nhật bệnh nhân đã tồn tại
                patient.setBloodType(bloodType);
                patient.setAllergies(allergies);
                patient.setInsuranceInfo(insuranceInfo);
                patient.setMedicalHistory(medicalHistory);
                patient.setEmergencyContactName(emergencyContactName);
                patient.setEmergencyContactPhone(emergencyContactPhone);

                dao.updatePatient(patient);
            }

            // Lưu lại vào session để hiển thị lại
            session.setAttribute("patient", dao.getPatientByUserId(user.getUserId()));
            request.setAttribute("success", "Cập nhật thông tin thành công!");
        }

        request.getRequestDispatcher("views/profile/profile.jsp").forward(request, response);

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
