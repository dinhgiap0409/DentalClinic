<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Integer currentPage = (Integer) request.getAttribute("currentPage");
    Integer totalPages = (Integer) request.getAttribute("totalPages");
%>
<div class="pagination">
    <button onclick="loadPage(${currentPage-1})" ${currentPage <= 1 ? 'disabled' : ''}>Previous</button>
    
    <span class="current">Page ${currentPage} / ${totalPages}</span>
    
    <button onclick="loadPage(${currentPage+1})" ${currentPage >= totalPages ? 'disabled' : ''}>Next</button>
</div>




