/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.ManageMedication;

import dal.MedicationsDao;
import dto.MedicationDto;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import model.Medications;

/**
 *
 * @author TNO
 */
@WebServlet(name = "ManageMedicationsController", urlPatterns = {"/manageMedications"})
public class ManageMedicationsController extends HttpServlet {

    private final MedicationsDao medicationsDao = new MedicationsDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null || action.equalsIgnoreCase("list")) {
            handleList(request, response);
        } else if (action.equalsIgnoreCase("view")) {
            handleView(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");
        if ("add".equalsIgnoreCase(action)) {
            handleAdd(request, response);
        } else if ("update".equalsIgnoreCase(action)) {
            handleUpdate(request, response);
        } else {
            response.sendRedirect("error.jsp");
        }

    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private void handleList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            MedicationDto filter = new MedicationDto();

            // Lấy tham số filter từ form / query string
            filter.setName(trimParam(request.getParameter("name")));
            filter.setManufacturer(trimParam(request.getParameter("manufacturer")));
            filter.setIsActive(parseBooleanParam(request.getParameter("isActive")));

            if (request.getParameter("priceFrom") != null && !request.getParameter("priceFrom").isBlank()) {
                filter.setPriceFrom(new BigDecimal(request.getParameter("priceFrom")));
            }
            if (request.getParameter("priceTo") != null && !request.getParameter("priceTo").isBlank()) {
                filter.setPriceTo(new BigDecimal(request.getParameter("priceTo")));
            }

            if (request.getParameter("expiryDateFrom") != null && !request.getParameter("expiryDateFrom").isBlank()) {
                filter.setExpiryDateFrom(Date.valueOf(request.getParameter("expiryDateFrom")));
            }
            if (request.getParameter("expiryDateTo") != null && !request.getParameter("expiryDateTo").isBlank()) {
                filter.setExpiryDateTo(Date.valueOf(request.getParameter("expiryDateTo")));
            }

            // Phân trang
            int page = 1;
            int size = 10;
            try {
                if (request.getParameter("page") != null) {
                    page = Integer.parseInt(request.getParameter("page"));
                }
            } catch (NumberFormatException ignored) {
            }

            filter.setPage(page);
            filter.setSize(size);
            filter.setPaginationMode(true);

            // Sort mode
            String sort = request.getParameter("sort");
            filter.setSortMode(sort == null || sort.equalsIgnoreCase("asc"));

            // Lấy danh sách + tổng số trang
            List<Medications> medications = medicationsDao.filterMedications(filter);
            int totalRecords = medicationsDao.countFilter(filter);
            int totalPages = (int) Math.ceil((double) totalRecords / size);

            request.setAttribute("medications", medications);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("filter", filter);
            request.getRequestDispatcher("/views/manage/medication-list.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private void handleView(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Medications medication = medicationsDao.getMedicationById(id);
            if (medication == null) {
                response.sendRedirect("manageMedications?action=list");
                return;
            }
            request.setAttribute("medication", medication);
            request.getRequestDispatcher("/views/manage/medication-detail.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private Medications extractMedicationFromRequest(HttpServletRequest request) {
        Medications m = new Medications();
        m.setMedicationName(request.getParameter("medicationName"));
        m.setDescription(request.getParameter("description"));

        String price = request.getParameter("price");
        if (price != null && !price.isBlank()) {
            m.setPrice(new BigDecimal(price));
        } else {
            m.setPrice(BigDecimal.ZERO);
        }

        String quantity = request.getParameter("stockQuantity");
        m.setStockQuantity((quantity != null && !quantity.isBlank()) ? Integer.parseInt(quantity) : 0);

        String expiry = request.getParameter("expiryDate");
        if (expiry != null && !expiry.isBlank()) {
            m.setExpiryDate(Date.valueOf(expiry));
        }

        m.setManufacturer(request.getParameter("manufacturer"));
        m.setIsActive("on".equalsIgnoreCase(request.getParameter("isActive")));

        return m;
    }

    private void handleAdd(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Medications m = extractMedicationFromRequest(request);
            boolean success = medicationsDao.insertMedication(m);

            if (success) {
                response.sendRedirect("manageMedications?action=list");
            } else {
                request.setAttribute("error", "Không thể thêm thuốc mới!");
                request.getRequestDispatcher("/views/manage/medication-form.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private void handleUpdate(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Medications m = extractMedicationFromRequest(request);
            m.setMedicationId(Integer.parseInt(request.getParameter("medicationId")));
            boolean success = medicationsDao.updateMedication(m);

            if (success) {
                response.sendRedirect("manageMedications?action=view&id=" + m.getMedicationId());
            } else {
                request.setAttribute("error", "Không thể cập nhật thuốc!");
                request.getRequestDispatcher("/views/manage/medication-detail.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error.jsp");
        }
    }

    private String trimParam(String param) {
        return (param == null || param.isBlank()) ? null : param.trim();
    }

    private Boolean parseBooleanParam(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        if (value.equals("true") || value.equals("1")) {
            return true;
        }
        if (value.equals("false") || value.equals("0")) {
            return false;
        }
        return null;
    }

}
