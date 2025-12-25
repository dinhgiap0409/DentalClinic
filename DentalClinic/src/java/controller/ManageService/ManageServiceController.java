/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.ManageService;

import dal.ServiceDao;
import dto.ServiceDto;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.math.BigDecimal;
import java.util.List;
import model.Service;
import model.Users;
import utils.UploadUtils;

/**
 *
 * @author TNO
 */
@WebServlet(name = "ManageServiceController", urlPatterns = {"/manageService"})
@MultipartConfig( 
        fileSizeThreshold = 1024 * 1024, // 1MB: bộ nhớ tạm trước khi ghi ra đĩa
        maxFileSize = 1024 * 1024 * 5, // 5MB: kích thước tối đa file
        maxRequestSize = 1024 * 1024 * 20 // 20MB: tổng dung lượng tối đa của request
)
public class ManageServiceController extends HttpServlet {

    private final ServiceDao serviceDao = new ServiceDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "view";
        }

        switch (action) {
            case "detail" ->
                showServiceDetail(request, response);
            case "view" ->
                listServices(request, response);
            default ->
                response.sendRedirect("error.jsp");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            response.sendRedirect("manageService?action=view");
            return;
        }

        switch (action) {
            case "add" ->
                addService(request, response);
            case "update" ->
                updateService(request, response);
            default ->
                response.sendRedirect("manageService?action=view");
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void listServices(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            ServiceDto filter = new ServiceDto();

            // Nhận filter từ form/search
            String name = request.getParameter("name");
            String from = request.getParameter("priceFrom");
            String to = request.getParameter("priceTo");
            String active = request.getParameter("isActive");

            filter.setServiceName(name);
            if (from != null && !from.isEmpty()) {
                filter.setPriceFrom(Double.valueOf(from));
            }
            if (to != null && !to.isEmpty()) {
                filter.setPriceTo(Double.valueOf(to));
            }
            if (active != null && !active.isEmpty()) {
                filter.setIsActive(Boolean.valueOf(active));
            }

            // Pagination
            int page = 1, size = 10;
            try {
                page = Integer.parseInt(request.getParameter("page"));
                size = Integer.parseInt(request.getParameter("size"));
            } catch (Exception ignored) {
            }

            filter.setPage(page);
            filter.setSize(size);
            filter.setPaginationMode(true);

            // Sort
            String sort = request.getParameter("sort");
            filter.setSortMode("asc".equalsIgnoreCase(sort));

            // Gọi DAO
            List<Service> list = serviceDao.filterServices(filter);
            int total = serviceDao.countFilter(filter);
            int totalPages = (int) Math.ceil((double) total / size);

            // Gửi sang JSP
            request.setAttribute("services", list);
            request.setAttribute("page", page);
            request.setAttribute("size", size);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("baseUrl", "/manageService");

            request.getRequestDispatcher("/views/manage/service-list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Lỗi khi tải danh sách dịch vụ");
        }
    }

    private void showServiceDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Service service = serviceDao.getServiceById(id);
        request.setAttribute("service", service);
        request.getRequestDispatcher("/views/manage/service-detail.jsp").forward(request, response);
    }

    private void addService(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        Service service = new Service();
        service.setServiceName(request.getParameter("serviceName"));
        service.setDescription(request.getParameter("description"));
        service.setPrice(new BigDecimal(request.getParameter("price")));
        service.setDuration(Integer.parseInt(request.getParameter("duration")));
        service.setIsActive(Boolean.parseBoolean(request.getParameter("isActive")));

        // Upload ảnh (nếu có)
        Part imagePart = request.getPart("image");
        String uploadPath = request.getServletContext().getRealPath("/uploads");
        String imageName = UploadUtils.uploadImage(imagePart, uploadPath);
        service.setImage(imageName);

        // Lấy người tạo (giả định đang đăng nhập)
        Users creator = new Users();
        creator.setUserId(1); // tạm hardcode
        service.setCreatedBy(creator);

        serviceDao.insertService(service);
        response.sendRedirect("manageService?action=view");
    }

    private void updateService(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        int id = Integer.parseInt(request.getParameter("serviceId"));
        Service service = serviceDao.getServiceById(id);

        service.setServiceName(request.getParameter("serviceName"));
        service.setDescription(request.getParameter("description"));
        service.setPrice(new BigDecimal(request.getParameter("price")));
        service.setDuration(Integer.parseInt(request.getParameter("duration")));
        service.setIsActive(Boolean.parseBoolean(request.getParameter("isActive")));

        // upload ảnh mới nếu có
        Part imagePart = request.getPart("image");
        String uploadPath = request.getServletContext().getRealPath("/uploads");
        String newImage = UploadUtils.uploadImage(imagePart, uploadPath);
        if (newImage != null) {
            service.setImage(newImage);
        }

        serviceDao.updateService(service);
        response.sendRedirect("manageService?action=view");
    }

}
