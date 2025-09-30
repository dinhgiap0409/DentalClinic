/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dal.ServiceDao;
import model.Service;
import model.Users;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 *
 * @author Nguyen Dinh Giap
 */
@WebServlet(name = "ServiceController", urlPatterns = {"/service"})
public class ServiceController extends HttpServlet {

    private ServiceDao serviceDao;

    @Override
    public void init() throws ServletException {
        serviceDao = new ServiceDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        switch (action) {
            case "list":
                showServiceList(request, response);
                break;
            case "detail":
                showServiceDetail(request, response);
                break;
            case "add":
                showAddServiceForm(request, response);
                break;
            case "edit":
                showEditServiceForm(request, response);
                break;
            case "delete":
                deleteService(request, response);
                break;
            default:
                showServiceList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        
        switch (action) {
            case "add":
                handleAddService(request, response);
                break;
            case "update":
                handleUpdateService(request, response);
                break;
        }
    }

    private void showServiceList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Service> services = serviceDao.getAllServices();
        request.setAttribute("services", services);
        request.getRequestDispatcher("views/dashboard/service-list.jsp").forward(request, response);
    }

    private void showServiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int serviceId = Integer.parseInt(request.getParameter("id"));
        Service service = serviceDao.getServiceById(serviceId);
        
        if (service != null) {
            request.setAttribute("service", service);
            request.getRequestDispatcher("views/dashboard/service-detail.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy dịch vụ!");
            request.getRequestDispatcher("views/dashboard/service-list.jsp").forward(request, response);
        }
    }

    private void showAddServiceForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("views/dashboard/service-add.jsp").forward(request, response);
    }

    private void showEditServiceForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int serviceId = Integer.parseInt(request.getParameter("id"));
        Service service = serviceDao.getServiceById(serviceId);
        
        if (service != null) {
            request.setAttribute("service", service);
            request.getRequestDispatcher("views/dashboard/service-edit.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Không tìm thấy dịch vụ!");
            request.getRequestDispatcher("views/dashboard/service-list.jsp").forward(request, response);
        }
    }

    private void deleteService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int serviceId = Integer.parseInt(request.getParameter("id"));
        boolean success = serviceDao.deleteService(serviceId);
        
        if (success) {
            request.setAttribute("success", "Xóa dịch vụ thành công!");
        } else {
            request.setAttribute("error", "Xóa dịch vụ thất bại!");
        }
        
        response.sendRedirect("service?action=list");
    }

    private void handleAddService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            response.sendRedirect("user?action=login");
            return;
        }
        
        Service service = new Service();
        service.setServiceName(request.getParameter("serviceName"));
        service.setDescription(request.getParameter("description"));
        service.setPrice(new BigDecimal(request.getParameter("price")));
        service.setDuration(Integer.parseInt(request.getParameter("duration")));
        service.setIsActive(Boolean.parseBoolean(request.getParameter("isActive")));
        service.setCreatedBy(currentUser);
        
        int result = serviceDao.insertService(service);
        
        if (result > 0) {
            request.setAttribute("success", "Thêm dịch vụ thành công!");
        } else {
            request.setAttribute("error", "Thêm dịch vụ thất bại!");
        }
        
        request.getRequestDispatcher("views/dashboard/service-add.jsp").forward(request, response);
    }

    private void handleUpdateService(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Users currentUser = (Users) session.getAttribute("user");
        
        if (currentUser == null || !"admin".equals(currentUser.getRole())) {
            response.sendRedirect("user?action=login");
            return;
        }
        
        Service service = new Service();
        service.setServiceId(Integer.parseInt(request.getParameter("serviceId")));
        service.setServiceName(request.getParameter("serviceName"));
        service.setDescription(request.getParameter("description"));
        service.setPrice(new BigDecimal(request.getParameter("price")));
        service.setDuration(Integer.parseInt(request.getParameter("duration")));
        service.setIsActive(Boolean.parseBoolean(request.getParameter("isActive")));
        
        boolean success = serviceDao.updateService(service);
        
        if (success) {
            request.setAttribute("success", "Cập nhật dịch vụ thành công!");
        } else {
            request.setAttribute("error", "Cập nhật dịch vụ thất bại!");
        }
        
        request.getRequestDispatcher("views/dashboard/service-edit.jsp").forward(request, response);
    }
}
