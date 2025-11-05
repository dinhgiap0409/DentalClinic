<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%-- 
  EXPECTED:
    - baseUrl    : URL gốc (vd: ${pageContext.request.contextPath}/medicalHistory)
    - page       : trang hiện tại (int)
    - size       : số item/trang (int)
    - totalPages : tổng số trang (int)
  Bạn có thể truyền qua <jsp:param> hoặc setAttribute ở controller.
--%>

<c:set var="__baseUrl"    value="${not empty param.baseUrl    ? param.baseUrl    : (not empty baseUrl    ? baseUrl    : pageContext.request.requestURI)}" />
<c:set var="__page"       value="${not empty param.page       ? param.page       : (not empty page       ? page       : 1)}" />
<c:set var="__size"       value="${not empty param.size       ? param.size       : (not empty size       ? size       : 10)}" />
<c:set var="__totalPages" value="${not empty param.totalPages ? param.totalPages : (not empty totalPages ? totalPages : 1)}" />
<c:if test="${__totalPages < 1}"><c:set var="__totalPages" value="1"/></c:if>

    <style>
        .pager {
            display:flex;
            gap:8px;
            justify-content:center;
            align-items:center;
            margin:18px 0;
        }
        .pager a.btn{
            display:inline-block;
            padding:8px 12px;
            border-radius:8px;
            text-decoration:none;
            font-weight:600
        }
        .pager a.btn-ghost{
            background:#fff;
            color:#0d6efd;
            border:2px solid #0d6efd
        }
        .pager a.btn-ghost:hover{
            background:#e9f2ff
        }
        .pager a.btn-primary{
            background:#0d6efd;
            color:#fff;
            border:2px solid #0d6efd
        }
        .pager a.btn-primary:hover{
            background:#0b5ed7;
            border-color:#0b5ed7
        }
    </style>

    <div class="pager">
        <!-- Prev -->
    <c:set var="__prevPage" value="${__page > 1 ? __page - 1 : 1}" />
    <c:url var="__prevUrl" value="${__baseUrl}">
        <c:param name="page" value="${__prevPage}"/>
        <c:param name="size" value="${__size}"/>
        <!-- Giữ lại mọi filter hiện có -->
        <c:forEach var="name" items="${pageContext.request.parameterMap.keySet()}">
            <c:if test="${name ne 'page' and name ne 'size' and name ne 'baseUrl' and name ne 'totalPages'}">
                <c:param name="${name}" value="${param[name]}"/>
            </c:if>
        </c:forEach>
    </c:url>
    <a class="btn btn-ghost" href="${__prevUrl}">« Trước</a>

    <!-- Window trang ±2 -->
    <c:set var="__start" value="${__page - 2}"/>
    <c:set var="__end"   value="${__page + 2}"/>
    <c:if test="${__start < 1}"><c:set var="__start" value="1"/></c:if>
    <c:if test="${__end > __totalPages}"><c:set var="__end" value="${__totalPages}"/></c:if>

    <c:forEach var="p" begin="${__start}" end="${__end}">
        <c:url var="__pUrl" value="${__baseUrl}">
            <c:param name="page" value="${p}"/>
            <c:param name="size" value="${__size}"/>
            <c:forEach var="name" items="${pageContext.request.parameterMap.keySet()}">
                <c:if test="${name ne 'page' and name ne 'size' and name ne 'baseUrl' and name ne 'totalPages'}">
                    <c:param name="${name}" value="${param[name]}"/>
                </c:if>
            </c:forEach>
        </c:url>
        <a class="btn ${p == __page ? 'btn-primary' : 'btn-ghost'}" href="${__pUrl}">${p}</a>
    </c:forEach>

    <!-- Next -->
    <c:set var="__nextPage" value="${__page < __totalPages ? __page + 1 : __totalPages}" />
    <c:url var="__nextUrl" value="${__baseUrl}">
        <c:param name="page" value="${__nextPage}"/>
        <c:param name="size" value="${__size}"/>
        <c:forEach var="name" items="${pageContext.request.parameterMap.keySet()}">
            <c:if test="${name ne 'page' and name ne 'size' and name ne 'baseUrl' and name ne 'totalPages'}">
                <c:param name="${name}" value="${param[name]}"/>
            </c:if>
        </c:forEach>
    </c:url>
    <a class="btn btn-ghost" href="${__nextUrl}">Sau »</a>
</div>
