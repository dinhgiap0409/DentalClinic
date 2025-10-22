package filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.Users;

/**
 * This filter checks if a user is authorized to access certain pages.
 */
// Annotation @WebFilter khai báo đây là một bộ lọc (Filter).
// Nó sẽ tự động "chặn" và kiểm tra tất cả các yêu cầu (request) có đường dẫn
// khớp với các mẫu trong `urlPatterns`.
@WebFilter(filterName = "AuthorizationFilter", urlPatterns = { "/receptionist/*", "/admin/*", "/doctor/*",
        "/patient/*"})
public class AuthorizationFilter implements Filter {

    /**
     * Phương thức chính của Filter, được gọi mỗi khi có một request khớp với
     * urlPatterns.
     * 
     * @param request  Đối tượng request từ client.
     * @param response Đối tượng response để gửi về client.
     * @param chain    Chuỗi các filter, dùng để chuyển tiếp request đến đích tiếp
     *                 theo.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Ép kiểu request và response sang loại HTTP để có thể sử dụng các phương thức
        // đặc thù của HTTP.
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Lấy session hiện tại. `false` có nghĩa là không tạo session mới nếu chưa có.
        HttpSession session = httpRequest.getSession(false);

        // Bước 1: KIỂM TRA ĐĂNG NHẬP
        // Thử lấy đối tượng 'user' đã được lưu trong session lúc đăng nhập thành công.
        Users user = (session != null) ? (Users) session.getAttribute("user") : null;

        // Nếu 'user' là null, có nghĩa là người dùng chưa đăng nhập.
        if (user == null) {
            // Chuyển hướng họ về trang đăng nhập.
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            // Dừng xử lý request tại đây, không cho đi tiếp.
            return;
        }

        // Nếu code chạy đến đây, có nghĩa là người dùng ĐÃ đăng nhập.
        // Bước 2: KIỂM TRA VAI TRÒ (PHÂN QUYỀN)

        // Lấy đường dẫn mà người dùng đang cố gắng truy cập (ví dụ:
        // "/receptionist-dashboard").
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());

        // Lấy vai trò của người dùng từ đối tượng 'user' trong session.
        String role = user.getRole();
        boolean authorized = false;

        // Bắt đầu logic kiểm tra quyền.
        // Nếu người dùng muốn vào khu vực của nhân viên tiếp đón...
        if (path.startsWith("/receptionist")) {
            // ...thì kiểm tra xem vai trò của họ có phải là "Receptionist" không.
            if ("Receptionist".equals(role)) {
                authorized = true; // Nếu đúng, cấp quyền truy cập.
            }
        }
        // Nếu người dùng muốn vào khu vực của admin...
        else if (path.startsWith("/admin")) {
            // ...thì kiểm tra xem vai trò của họ có phải là "Admin" không.
            if ("Admin".equals(role)) {
                authorized = true; // Nếu đúng, cấp quyền truy cập.
            }
        }
        // Nếu người dùng muốn vào khu vực của bác sĩ...
        else if (path.startsWith("/doctor")) {
            // ...thì kiểm tra xem vai trò của họ có phải là "Doctor" không.
            if ("Doctor".equals(role)) {
                authorized = true; // Nếu đúng, cấp quyền truy cập.
            }
        }
        
        // Nếu người dùng muốn vào khu vực của bệnh nhân...
        else if (path.startsWith("/patient")) {
            if ("Patient".equals(role)) {
                authorized = true; // Nếu đúng, cấp quyền truy cập.
            }
        }

        // Bước 3: RA QUYẾT ĐỊNH
        if (authorized) {
            // Nếu biến `authorized` là true, cho phép request đi tiếp đến đích của nó (ví
            // dụ: ReceptionistDashboardController).
            chain.doFilter(request, response);
        } else {
            // Nếu `authorized` là false (ví dụ: một Patient cố vào trang admin),
            // chuyển hướng họ về trang chủ. Đây là hành động "từ chối truy cập".
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/home");
        }
    }

    // Các phương thức init và destroy có thể để trống cho các filter đơn giản.
    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}