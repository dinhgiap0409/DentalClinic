/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import dal.PatientDao;
import dto.PatientDto;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Patients;
import model.Users;

/**
 *
 * @author Nguyen Dinh Giap
 */
@WebServlet(name="ProfileController", urlPatterns={"/profile"})
public class ProfileController extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProfileController</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProfileController at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final PatientDao patientDao = new PatientDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //Kiểm tra đăng nhập
        HttpSession session = request.getSession();
        Users userSession = (Users) session.getAttribute("user");
        if (userSession == null) {
            response.sendRedirect("login");
            return;
        }

        // Doc message tu session
        String success = (String) session.getAttribute("success");
        if (success != null) {
            request.setAttribute("success", success);
            session.removeAttribute("success");
        }
        String error = (String) session.getAttribute("error");
        if (error != null) {
            request.setAttribute("error", error);
            session.removeAttribute("error");
        }

        // lấy thông tin Patients theo user đăng nhập
        Patients patient = patientDao.getPatientByUserId(userSession.getUserId());
        if (patient == null) {
            // Nếu chưa có hồ sơ bệnh nhân, tạo mới
            Patients newPatient = new Patients();
            newPatient.setUserID(userSession);
            
            boolean created = patientDao.insertPatient(newPatient);
            if (!created) {
                request.setAttribute("error", "Không thể tạo hồ sơ bệnh nhân. Vui lòng thử lại!");
                request.getRequestDispatcher("/views/customer/profile.jsp").forward(request, response);
                return;
            }
            
            // Lấy lại thông tin patient vừa tạo
            patient = patientDao.getPatientByUserId(userSession.getUserId());
            if (patient == null) {
                request.setAttribute("error", "Có lỗi xảy ra khi tạo hồ sơ bệnh nhân!");
                request.getRequestDispatcher("/views/customer/profile.jsp").forward(request, response);
                return;
            }
        }

        //Map sang DTO de truyen sang View
        PatientDto dto = new PatientDto();
        dto.setPatientId(patient.getPatientID());
        dto.setUser(patient.getUserID()); // de JSP dung duoc ten/email neu can
        dto.setBloodType(patient.getBloodType());
        dto.setAllergies(patient.getAllergies());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setInsuranceInfo(patient.getInsuranceInfo());
        dto.setEmergencyContactName(patient.getEmergencyContactName());
        dto.setEmergencyContactPhone(patient.getEmergencyContactPhone());

        request.setAttribute("patient", dto);

        // Forward tới JSP
        request.getRequestDispatcher("/views/customer/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        HttpSession session = request.getSession();
        Users userSession = (Users) session.getAttribute("user");
        if (userSession == null) {
            response.sendRedirect("login");
            return;
        }

        // Lấy bản ghi hiện tại để biết PatientID
        Patients current = patientDao.getPatientByUserId(userSession.getUserId());
        if (current == null) {
            // Nếu chưa có hồ sơ bệnh nhân, tạo mới
            Patients newPatient = new Patients();
            newPatient.setUserID(userSession);
            
            boolean created = patientDao.insertPatient(newPatient);
            if (!created) {
                session.setAttribute("error", "Không thể tạo hồ sơ bệnh nhân. Vui lòng thử lại!");
                response.sendRedirect("profile");
                return;
            }
            
            // Lấy lại thông tin patient vừa tạo
            current = patientDao.getPatientByUserId(userSession.getUserId());
            if (current == null) {
                session.setAttribute("error", "Có lỗi xảy ra khi tạo hồ sơ bệnh nhân!");
                response.sendRedirect("profile");
                return;
            }
        }

        // Nhận dữ liệu từ form 
        String bloodType = request.getParameter("bloodType");
        String allergies = request.getParameter("allergies");
        String medicalHistory = request.getParameter("medicalHistory");
        String insuranceInfo = request.getParameter("insuranceInfo");
        String emergencyContactName = request.getParameter("emergencyContactName");
        String emergencyContactPhone = request.getParameter("emergencyContactPhone");

        // Build entity để UPDATE 
        Patients patientToUpdate = new Patients();
        patientToUpdate.setPatientID(current.getPatientID());
        patientToUpdate.setBloodType(bloodType);
        patientToUpdate.setAllergies(allergies);
        patientToUpdate.setMedicalHistory(medicalHistory);
        patientToUpdate.setInsuranceInfo(insuranceInfo);
        patientToUpdate.setEmergencyContactName(emergencyContactName);
        patientToUpdate.setEmergencyContactPhone(emergencyContactPhone);

        boolean success = patientDao.updatePatientProfile(patientToUpdate);

        if (success) {
            session.setAttribute("success", "Thông tin y tế đã được cập nhật thành công!");
        } else {
            session.setAttribute("error", "Cập nhật thông tin y tế thất bại. Vui lòng thử lại.");
        }

        response.sendRedirect("profile");
    }
}
