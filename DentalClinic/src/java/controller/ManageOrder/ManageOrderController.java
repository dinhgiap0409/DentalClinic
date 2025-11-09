/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.ManageOrder;

import dal.OrderDao;
import dto.OrderDto;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.OrderDetails;
import model.Orders;

/**
 *
 * @author TNO
 */
@WebServlet(name = "ManageOrderController", urlPatterns = {"/manageOrder"})
public class ManageOrderController extends HttpServlet {

    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "viewList";
        }

        switch (action) {
            case "viewDetail":
                viewDetail(request, response);
                break;
            case "viewList":
            default:
                viewList(request, response);
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "";
        }

        try {
            switch (action) {
                case "update":
                    updateOrder(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Lỗi: " + e.getMessage());
            request.getRequestDispatcher("error.jsp").forward(request, response);
            return;
        }

        response.sendRedirect("manageOrder?action=viewList");

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void viewList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        OrderDto filter = new OrderDto();
        filter.setPatientName(request.getParameter("patientName"));
        filter.setStatus(request.getParameter("status"));
        filter.setPaymentStatus(request.getParameter("paymentStatus"));

        String orderDateFromStr = request.getParameter("orderDateFrom");
        String orderDateToStr = request.getParameter("orderDateTo");
        if (orderDateFromStr != null && !orderDateFromStr.isEmpty()) {
            filter.setOrderDateFrom(java.sql.Date.valueOf(orderDateFromStr));
        }
        if (orderDateToStr != null && !orderDateToStr.isEmpty()) {
            filter.setOrderDateTo(java.sql.Date.valueOf(orderDateToStr));
        }

        // Phân trang
        int page = 1, size = 10;
        String pageStr = request.getParameter("page");
        String sizeStr = request.getParameter("size");
        if (pageStr != null) {
            page = Integer.parseInt(pageStr);
        }
        if (sizeStr != null) {
            size = Integer.parseInt(sizeStr);
        }
        filter.setPaginationMode(true);
        filter.setPage(page);
        filter.setSize(size);

        // Sắp xếp
        String sortModeStr = request.getParameter("sortMode");
        if (sortModeStr != null) {
            filter.setSortMode(Boolean.parseBoolean(sortModeStr));
        }

        // Lấy danh sách và tổng số
        List<Orders> orders = orderDao.filterOrders(filter);
        int totalCount = orderDao.countFilter(filter);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        request.setAttribute("orders", orders);
        request.setAttribute("page", page);
        request.setAttribute("size", size);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("baseUrl", "manageOrder");

        request.getRequestDispatcher("/views/manage/manageOrder.jsp").forward(request, response);
    }

    private void viewDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr != null && !orderIdStr.isEmpty()) {
            int orderId = Integer.parseInt(orderIdStr);
            Orders order = orderDao.getOrderById(orderId);
            List<OrderDetails> details = orderDao.getOrderDetailsByOrderId(orderId);

            request.setAttribute("order", order);
            request.setAttribute("details", details);
            request.getRequestDispatcher("/views/manage/manageOrderDetail.jsp").forward(request, response);
        } else {
            response.sendRedirect("manageOrder?action=viewList");
        }
    }

    private void updateOrder(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            return;
        }

        int orderId = Integer.parseInt(orderIdStr);


        Orders existingOrder = orderDao.getOrderById(orderId);
        if (existingOrder == null) {
            return;
        }

 
        String newStatus = request.getParameter("status");
        String newPaymentStatus = request.getParameter("paymentStatus");

        if (newStatus != null && !newStatus.isEmpty()) {
            existingOrder.setStatus(newStatus);
        }

        if (newPaymentStatus != null && !newPaymentStatus.isEmpty()) {
            existingOrder.setPaymentStatus(newPaymentStatus);
        }


        boolean updated = orderDao.updateOrder(existingOrder);

        if (!updated) {
            request.setAttribute("error", "Cập nhật thất bại!");
        }
    }

}
