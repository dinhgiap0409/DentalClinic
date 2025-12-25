/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.ServiceDao;
import dto.ServiceDto;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Nguyen Dinh Giap
 */
@WebServlet(name = "ServiceController", urlPatterns = {"/service"})
public class ServiceController extends HttpServlet {

    private ServiceDao serviceDao = new ServiceDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        // Tạo filter mặc định để hiển thị tất cả services
        ServiceDto filter = new ServiceDto();
        filter.setPaginationMode(true);
        filter.setSortMode(true);
        filter.setPage(1);
        filter.setSize(2);
        
        // Lấy dữ liệu
        List<ServiceDto> list = serviceDao.filterService(filter);
        int total = serviceDao.countServices();
        int totalPages = (total % 2) == 0 ? (total / 2) : (total / 2) + 1;
        
        // Set attributes
        request.setAttribute("list", list);
        request.setAttribute("page", 1);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("size", 2);
        
        request.getRequestDispatcher("/views/customer/service.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        //lay parameter tư request
        String serviceName = request.getParameter("serviceName");
        String priceFromStr = request.getParameter("priceFrom");
        String priceToStr = request.getParameter("priceTo");
        String isActiveStr = request.getParameter("isActive");
        
        int page;
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (Exception e) {
            page = 1;
        }
        int size = 2;
        
        // tao filter
        ServiceDto filter = new ServiceDto();
        filter.setServiceName(serviceName);
        
        // Set gia tien
        try {
            if (priceFromStr != null && !priceFromStr.trim().isEmpty()) {
                filter.setPriceFrom(Double.parseDouble(priceFromStr));
            }
        } catch (NumberFormatException e) {
            
        }
        
        try {
            if (priceToStr != null && !priceToStr.trim().isEmpty()) {
                filter.setPriceTo(Double.parseDouble(priceToStr));
            }
        } catch (NumberFormatException e) {
            
        }
        
        // Set hoat dong status
        if (isActiveStr != null && !isActiveStr.trim().isEmpty()) {
            filter.setIsActive(Boolean.parseBoolean(isActiveStr));
        }
        
        // Set phan trang
        filter.setPaginationMode(true);
        filter.setSortMode(true);
        filter.setPage(page);
        filter.setSize(size);
        
        // Validate trang
        if (page < 1) {
            page = 1;
            filter.setPage(page);
        }
        
        // lay du lieu tu filterService
        List<ServiceDto> list = serviceDao.filterService(filter);
        
        // tinh tong tang
        int total = serviceDao.countServices();
        int totalPages = (total % size) == 0 ? (total / size) : (total / size) + 1;
        
        if (page > totalPages && totalPages > 0) {
            page = totalPages;
            filter.setPage(page);
            list = serviceDao.filterService(filter);
        }
        
        request.setAttribute("list", list);
        request.setAttribute("page", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("size", size);
        request.setAttribute("filter", filter);
        request.getRequestDispatcher("/views/customer/service.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
