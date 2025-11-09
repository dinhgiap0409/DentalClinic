/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.PrescribedMedication;

import dal.PrescriptionDao;
import dto.PrescriptionDto;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Prescriptions;

/**
 *
 * @author TNO
 */
@WebServlet(name = "PrescriptionListController", urlPatterns = {"/prescriptionList"})
public class PrescriptionListController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            //Giả định bạn đã có PatientID từ session đăng nhập
            // Ở đây demo tạm là 3
            int patientId = 3;

            // Tạo DTO để filter
            PrescriptionDto dto = new PrescriptionDto();
            dto.setPatientId(patientId);

            // Gọi DAO
            PrescriptionDao dao = new PrescriptionDao();
            List<Prescriptions> prescriptions = dao.filterPrescription(dto);

            // Gửi sang JSP hiển thị
            request.setAttribute("prescriptions", prescriptions);
            request.getRequestDispatcher("prescriptions.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải danh sách đơn thuốc.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
