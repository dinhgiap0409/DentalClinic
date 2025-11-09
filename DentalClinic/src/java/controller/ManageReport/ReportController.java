/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.ManageReport;

import dal.ReportDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import model.DailyRevenue;

/**
 *
 * @author TNO
 */
@WebServlet(name = "ReportController", urlPatterns = {"/report"})
public class ReportController extends HttpServlet {

    private final ReportDao reportDao = new ReportDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<DailyRevenue> medicationList = reportDao.findAllMedicationRevenue();
        List<DailyRevenue> serviceList = reportDao.findAllServiceRevenue();
        BigDecimal totalRevenue = reportDao.getTotalRevenue();

        request.setAttribute("medicationList", medicationList);
        request.setAttribute("serviceList", serviceList);
        request.setAttribute("totalRevenue", totalRevenue);

        request.getRequestDispatcher("/views/manage/report.jsp").forward(request, response);

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
