package controller;

import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import dal.PatientDao;
import dto.PatientDto;
import model.Patients;
import model.Users;
import constant.ConstantsBloodType;

@WebServlet(name="ProfileController", urlPatterns={"/patient/profile"})
public class ProfileController extends HttpServlet {
    
    private final PatientDao patientDao = new PatientDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Kiem tra dang nhap
        HttpSession session = request.getSession();
        Users userSession = (Users) session.getAttribute("user");
        if (userSession == null) {
            response.sendRedirect("login");
            return;
        }

        // Lay thong tin patient
        Patients patient = patientDao.getPatientByUserId(userSession.getUserId());
        if (patient == null) {
            // Tao patient moi neu chua co
            Patients newPatient = new Patients();
            newPatient.setUserID(userSession);
            patientDao.insertPatient(newPatient);
            patient = patientDao.getPatientByUserId(userSession.getUserId());
        }

        // Tao DTO
        PatientDto dto = new PatientDto();
        dto.setPatientId(patient.getPatientID());
        dto.setUser(patient.getUserID());
        dto.setBloodType(patient.getBloodType());
        dto.setAllergies(patient.getAllergies());
        dto.setMedicalHistory(patient.getMedicalHistory());
        dto.setInsuranceInfo(patient.getInsuranceInfo());
        dto.setEmergencyContactName(patient.getEmergencyContactName());
        dto.setEmergencyContactPhone(patient.getEmergencyContactPhone());

        // Gửi danh sách nhóm máu sang JSP
        request.setAttribute("bloodTypes", ConstantsBloodType.BLOOD_TYPES);

        request.setAttribute("patient", dto);
        request.getRequestDispatcher("/views/customer/profile.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession();
        Users userSession = (Users) session.getAttribute("user");
        if (userSession == null) {
            response.sendRedirect("login");
            return;
        }

        // Lay du lieu tu form
        String bloodType = request.getParameter("bloodType");
        String allergies = request.getParameter("allergies");
        String medicalHistory = request.getParameter("medicalHistory");
        String insuranceInfo = request.getParameter("insuranceInfo");
        String emergencyContactName = request.getParameter("emergencyContactName");
        String emergencyContactPhone = request.getParameter("emergencyContactPhone");

        // Lay patient hien tai
        Patients current = patientDao.getPatientByUserId(userSession.getUserId());
        if (current == null) {
            response.sendRedirect("profile");
            return;
        }

        // Cap nhat thong tin
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
            session.setAttribute("success", "Cap nhat thanh cong!");
        } else {
            session.setAttribute("error", "Cap nhat that bai!");
        }

        response.sendRedirect("profile");
    }
}
