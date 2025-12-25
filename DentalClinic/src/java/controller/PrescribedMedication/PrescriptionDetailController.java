/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.PrescribedMedication;

import dal.PrescriptionDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.PrescriptionDetails;

/**
 *
 * @author TNO
 */
@WebServlet(name = "PrescriptionDetailController", urlPatterns = {"/prescriptionDetail"})
public class PrescriptionDetailController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            //Lấy PrescriptionID từ request (VD: /prescriptionDetail?id=5)
            String idParam = request.getParameter("id");
            if (idParam == null || idParam.isBlank()) {
                request.setAttribute("error", "Thiếu mã đơn thuốc để xem chi tiết.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            int prescriptionId = Integer.parseInt(idParam);

            //Gọi DAO để lấy danh sách chi tiết thuốc trong đơn
            PrescriptionDao dao = new PrescriptionDao();
            List<PrescriptionDetails> detailList = dao.findByPrescriptionId(prescriptionId);

            if (detailList.isEmpty()) {
                request.setAttribute("message", "Không tìm thấy chi tiết cho đơn thuốc này.");
            }

            //Truyền dữ liệu sang JSP hiển thị
            request.setAttribute("details", detailList);
            request.setAttribute("prescriptionId", prescriptionId);

            request.getRequestDispatcher("prescriptionDetails.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            e.printStackTrace();
            request.setAttribute("error", "Mã đơn thuốc không hợp lệ.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Không thể tải chi tiết đơn thuốc.");
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
