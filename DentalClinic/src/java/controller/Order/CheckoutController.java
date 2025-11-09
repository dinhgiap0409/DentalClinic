/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.Order;

import dal.OrderDao;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import model.OrderDetails;
import model.Orders;

/**
 *
 * @author TNO
 */
@WebServlet(name = "CheckoutController", urlPatterns = {"/checkout"})
public class CheckoutController extends HttpServlet {

    private OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Thiếu orderId.");
            return;
        }

        try {
            int orderId = Integer.parseInt(orderIdParam);

            // Lấy Order
            Orders order = orderDao.getOrderById(orderId);
            if (order == null) {
                request.setAttribute("error", "Không tìm thấy đơn hàng.");
                request.getRequestDispatcher("error.jsp").forward(request, response);
                return;
            }

            // Lấy OrderDetails kèm thông tin thuốc
            List<OrderDetails> orderDetailsList = orderDao.getOrderDetailsByOrderId(orderId);

            // Tính tổng tiền thuốc (không bao gồm phí khám)
            BigDecimal totalMedAmount = BigDecimal.ZERO;
            for (OrderDetails od : orderDetailsList) {
                totalMedAmount = totalMedAmount.add(od.getPrice().multiply(BigDecimal.valueOf(od.getQuantity())));
            }

            // Lấy phí khám bác sĩ
            BigDecimal consultationFee = order.getPrescriptions() != null && order.getPrescriptions().getDoctorId() != null
                    ? order.getPrescriptions().getDoctorId().getConsultationFee()
                    : BigDecimal.ZERO;

            BigDecimal grandTotal = totalMedAmount.add(consultationFee);

            request.setAttribute("order", order);
            request.setAttribute("orderDetailsList", orderDetailsList);
            request.setAttribute("totalMedAmount", totalMedAmount);
            request.setAttribute("consultationFee", consultationFee);
            request.setAttribute("grandTotal", grandTotal);

            request.getRequestDispatcher("/views/patient/checkout.jsp").forward(request, response);

        } catch (NumberFormatException ex) {
            request.setAttribute("error", "orderId không hợp lệ.");
            request.getRequestDispatcher("error.jsp").forward(request, response);
        } catch (Exception ex) {
            ex.printStackTrace();
            request.setAttribute("error", "Có lỗi khi load đơn hàng.");
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
    }

}
