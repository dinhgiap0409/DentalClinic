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
import dal.*;
import dto.*;
import model.*;
import java.util.List;
import java.time.LocalDate;

/**
 *
 * @author ADMIN
 */
@WebServlet(name = "DoctorController", urlPatterns = {"/doctorController"})
public class DoctorController extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    private AppointmentsDao appointmentsDao = new AppointmentsDao();
    private PatientDao patientDao = new PatientDao();
    private MedicalRecordsDao medicalDao = new MedicalRecordsDao();
    private ServiceDao serviceDao = new ServiceDao();
  
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "patientsToday";

        switch (action) {
            case "record":
                viewPatientRecord(request, response);
                break;
            case "exam":
                examForm(request, response);
                break;
            case "prescribe":
                prescribeForm(request, response);
                break;
            default:
                listPatientsToday(request, response);
        }
    }
    
    // 1️⃣ Xem danh sách bệnh nhân trong ngày
    private void listPatientsToday(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
    int doctorId = (int) request.getSession().getAttribute("doctorId");

    // Dùng java.sql.Date thay vì LocalDate
    java.sql.Date today = new java.sql.Date(System.currentTimeMillis());

    List<Appointments> patients = appointmentsDao.getAppointmentsByDoctorAndDate(doctorId, today);
    request.setAttribute("patients", patients);
    request.getRequestDispatcher("doctor/patients-today.jsp").forward(request, response);
}


    // 2️⃣ Xem hồ sơ bệnh án
    private void viewPatientRecord(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int patientId = Integer.parseInt(request.getParameter("patientId"));
        MedicalRecords record = medicalDao.getMedicalRecordByPatientId(patientId);
        request.setAttribute("record", record);
        request.getRequestDispatcher("doctor/patient-record.jsp").forward(request, response);
    }

    // 3️⃣ Ghi kết quả khám
    private void examForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int patientId = Integer.parseInt(request.getParameter("patientId"));
        request.setAttribute("patientId", patientId);
        request.getRequestDispatcher("doctor/exam.jsp").forward(request, response);
    }


    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("saveExam".equals(action)) {
            saveExamResult(request, response);
        } else if ("savePrescription".equals(action)) {
            savePrescription(request, response);
        }
    }
    
    
    private void saveExamResult(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int patientId = Integer.parseInt(request.getParameter("patientId"));
        String result = request.getParameter("result");
        medicalDao.addExamResult(patientId, result);
        response.sendRedirect("doctor?action=patientsToday");
    }

    // 4️⃣ Kê dịch vụ
    private void prescribeForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int patientId = Integer.parseInt(request.getParameter("patientId"));
        List<Service> services = serviceDao.getAllServices();
        request.setAttribute("patientId", patientId);
        request.setAttribute("services", services);
        request.getRequestDispatcher("doctor/prescribe.jsp").forward(request, response);
    }

    private void savePrescription(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        int patientId = Integer.parseInt(request.getParameter("patientId"));
        String[] serviceIds = request.getParameterValues("serviceIds");
        if (serviceIds != null) {
            serviceDao.addServicesForPatient(patientId, serviceIds);
        }
        response.sendRedirect("doctor?action=patientsToday");
    }
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
