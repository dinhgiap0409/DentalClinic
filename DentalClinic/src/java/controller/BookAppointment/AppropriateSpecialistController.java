/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.BookAppointment;

import dal.DoctorDao;
import dal.ServiceDao;
import dal.UsersDao;
import dto.DoctorDTO;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Doctor;
import model.Service;
import model.Users;

/**
 *
 * @author TNO
 */
@WebServlet(name="AppropriateSpecialistController", urlPatterns={"/appropriateSpecialist"})
public class AppropriateSpecialistController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        try {
            // Lấy serviceId từ request
            int serviceId = Integer.parseInt(request.getParameter("serviceId"));

            DoctorDao doctorDAO = new DoctorDao();
            UsersDao userDAO = new UsersDao();
            ServiceDao serviceDAO = new ServiceDao();
            
            Service service = serviceDAO.getServiceById(serviceId);
            if (service == null) {
                request.setAttribute("errorMessage", "Không tìm thấy dịch vụ với ID: " + serviceId);
                request.getRequestDispatcher("/error.jsp").forward(request, response);
                return;
            }

            // Lấy danh sách doctorId theo serviceId
            List<Integer> doctorIds = doctorDAO.getDoctorIdsByServiceId(serviceId);

            List<DoctorDTO> doctorDTOs = new ArrayList<>();

            // Với mỗi doctorId, lấy thông tin đầy đủ
            for (int doctorId : doctorIds) {
                Doctor doctor = doctorDAO.getDoctorByID(doctorId);
                if (doctor != null && doctor.getUserId() != null) {
                    int userId = doctor.getUserId().getUserId();
                            Users user = userDAO.getUserById(userId);

                    // Gói vào DTO
                    DoctorDTO dto = new DoctorDTO();
                    dto.setDoctorId(doctor.getDoctorID());
                    dto.setFullName(user != null ? user.getFullName() : "Không rõ");
                    dto.setEmail(user != null ? user.getEmail() : null);
                    dto.setPhoneNumber(user != null ? user.getPhoneNumber() : null);
                    dto.setSpecialization(doctor.getSpecialization());
                    dto.setYearsOfExperience(doctor.getYearsOfExperience());
                    dto.setBiography(doctor.getBiography());
                    dto.setConsultationFee(doctor.getConsultationFee());
                    dto.setImage(user.getImage());
                    doctorDTOs.add(dto);
                }
            }

             //Truyền danh sách bác sĩ + thông tin dịch vụ sang JSP
            request.setAttribute("doctors", doctorDTOs);
            request.setAttribute("service", service);
            request.getRequestDispatcher("/views/guest/doctorList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            // Nếu lỗi → chuyển đến trang lỗi
            request.setAttribute("errorMessage", "Có lỗi xảy ra khi tải danh sách bác sĩ phù hợp.");
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        doGet(request, response);
    }
}