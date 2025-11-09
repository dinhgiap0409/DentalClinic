<%-- 
    Document   : report
    Created on : Nov 3, 2025
    Author     : TNO
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Báo cáo doanh thu - Hệ thống phòng khám</title>
        <link rel="stylesheet" href="<%=request.getContextPath()%>/css/dashboard.css">
        <style>
            body {
                font-family: "Segoe UI", sans-serif;
                background-color: #f5f7fa;
                margin: 0;
                padding: 0;
            }
            .report-summary {
                background: #eaf4ff;
                border-left: 6px solid #007bff;
                padding: 20px;
                border-radius: 12px;
                margin-bottom: 25px;
                box-shadow: 0 3px 6px rgba(0,0,0,0.1);
            }
            .report-summary h2 {
                margin: 0;
                font-size: 22px;
                color: #007bff;
            }
            .report-summary .total {
                font-size: 28px;
                font-weight: bold;
                color: #28a745;
                margin-top: 10px;
            }
            .report-section {
                margin-bottom: 40px;
            }
            .report-section h3 {
                color: #222;
                margin-bottom: 15px;
                border-bottom: 2px solid #007bff;
                padding-bottom: 8px;
            }
            .data-table {
                width: 100%;
                border-collapse: collapse;
                background: white;
                border-radius: 10px;
                overflow: hidden;
                box-shadow: 0 2px 5px rgba(0,0,0,0.1);
            }
            .data-table th, .data-table td {
                padding: 12px 15px;
                text-align: center;
                border-bottom: 1px solid #eee;
            }
            .data-table th {
                background: #007bff;
                color: white;
                text-transform: uppercase;
                letter-spacing: 0.5px;
            }
            .data-table tr:hover {
                background: #f9fbff;
            }
            .no-data {
                text-align: center;
                color: #888;
                padding: 20px;
                font-style: italic;
            }
            .pagination {
                display: flex;
                justify-content: center;
                align-items: center;
                margin-top: 15px;
                gap: 8px;
            }
            .pagination button {
                background: #007bff;
                color: white;
                border: none;
                border-radius: 6px;
                padding: 6px 12px;
                cursor: pointer;
            }
            .pagination button:disabled {
                background: #ccc;
                cursor: not-allowed;
            }
        </style>
    </head>

    <body>
        <div class="dashboard">
            <!-- Sidebar -->
            <jsp:include page="../../common/sidebar.jsp"></jsp:include>

                <!-- Main Content -->
                <div class="content">
                    <div class="header">
                        <h1>Báo cáo doanh thu</h1>
                    </div>

                    <!-- Tổng doanh thu -->
                    <div class="report-summary">
                        <h2>Tổng doanh thu toàn hệ thống</h2>
                        <div class="total">
                        <fmt:formatNumber value="${totalRevenue}" type="currency" currencySymbol="₫"/>
                    </div>
                </div>

                <!-- Doanh thu thuốc -->
                <div class="report-section">
                    <h3>Doanh thu thuốc theo ngày</h3>
                    <c:choose>
                        <c:when test="${empty medicationList}">
                            <div class="no-data">Không có dữ liệu doanh thu thuốc.</div>
                        </c:when>
                        <c:otherwise>
                            <table class="data-table" id="medicationTable">
                                <thead>
                                    <tr>
                                        <th>Ngày</th>
                                        <th>Doanh thu thuốc</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="m" items="${medicationList}">
                                        <tr>
                                            <td><fmt:formatDate value="${m.date}" pattern="dd/MM/yyyy"/></td>
                                            <td><fmt:formatNumber value="${m.amount}" type="currency" currencySymbol="₫"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="pagination" id="medicationPagination"></div>
                        </c:otherwise>
                    </c:choose>
                </div>

                <!-- Doanh thu dịch vụ -->
                <div class="report-section">
                    <h3>Doanh thu dịch vụ theo ngày</h3>
                    <c:choose>
                        <c:when test="${empty serviceList}">
                            <div class="no-data">Không có dữ liệu doanh thu dịch vụ.</div>
                        </c:when>
                        <c:otherwise>
                            <table class="data-table" id="serviceTable">
                                <thead>
                                    <tr>
                                        <th>Ngày</th>
                                        <th>Doanh thu dịch vụ</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="s" items="${serviceList}">
                                        <tr>
                                            <td><fmt:formatDate value="${s.date}" pattern="dd/MM/yyyy"/></td>
                                            <td><fmt:formatNumber value="${s.amount}" type="currency" currencySymbol="₫"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <div class="pagination" id="servicePagination"></div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>

        <script>
            function setupPagination(tableId, paginationId, rowsPerPage) {
                var table = document.getElementById(tableId);
                var pagination = document.getElementById(paginationId);
                if (!table || !pagination)
                    return;

                var rows = table.querySelectorAll("tbody tr");
                var totalRows = rows.length;
                var totalPages = Math.ceil(totalRows / rowsPerPage);
                var currentPage = 1;

                function renderTable() {
                    for (var i = 0; i < totalRows; i++) {
                        rows[i].style.display = (i >= (currentPage - 1) * rowsPerPage && i < currentPage * rowsPerPage) ? "" : "none";
                    }
                    renderPagination();
                }

                function renderPagination() {
                    pagination.innerHTML = "";
                    var prevBtn = document.createElement("button");
                    prevBtn.textContent = "Trước";
                    prevBtn.disabled = currentPage === 1;
                    prevBtn.onclick = function () {
                        if (currentPage > 1) {
                            currentPage--;
                            renderTable();
                        }
                    };
                    pagination.appendChild(prevBtn);

                    var pageInfo = document.createElement("span");
                    pageInfo.textContent = "Trang " + currentPage + "/" + totalPages;
                    pagination.appendChild(pageInfo);

                    var nextBtn = document.createElement("button");
                    nextBtn.textContent = "Sau";
                    nextBtn.disabled = currentPage === totalPages;
                    nextBtn.onclick = function () {
                        if (currentPage < totalPages) {
                            currentPage++;
                            renderTable();
                        }
                    };
                    pagination.appendChild(nextBtn);
                }

                renderTable();
            }

            window.onload = function () {
                setupPagination("medicationTable", "medicationPagination", 10);
                setupPagination("serviceTable", "servicePagination", 10);
            };
        </script>
    </body>
</html>
