<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@page import="com.app.dao.*"%>
<%@page import="com.app.bean.*"%>
<%@page import="com.app.util.*"%>
<%@page import="java.util.*"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>

<form method="POST" action="index.jsp">
        
  <div class="form_row">
    <label>Name</label>
      <input class="text" name="nameField" type="text" />
    </div>
    <div class="form_row">
      <label >Description</label>
         <input class="text" name="descriptionField" type="text"/>
     </div>
        
   <div class="form_row">
    <label></label>
        <input type="submit" class="button" value="Save" />
    </div>
            
</form>
<%

     int currentPage = 1;
     int recordsPerPage = 5;
     if(request.getParameter("currentPage") != null)
         currentPage = Integer.parseInt(request.getParameter("currentPage"));
           
     List<ProductCategoryBean> productCategories = new ProductCategoryDao().getProductCategory((currentPage-1)*recordsPerPage, recordsPerPage);

       int noOfRecords = new ProductCategoryDao().getTotalRows();
       System.out.println("total record = " + noOfRecords);
       int noOfPages = (int) Math.ceil(noOfRecords * 1.0 / recordsPerPage);
       
       request.setAttribute("noOfPages", noOfPages);
       request.setAttribute("currentPage", currentPage);
      request.setAttribute("productCategories", productCategories);  
                                    
%>

<table border="1" style="width:50%;">
          <tr>
                   <th>Product Category Name</th>
                   <th>Product Category Desc</th>
          </tr>

          <c:forEach items="${productCategories}" var="productCategories">
          <tr>
                                    
          <td><c:out value="${productCategories.productCategoryName}"/></a></td>
          <td><c:out value="${productCategories.productCategoryDesc}" /></td>
                                    
          </tr>
          </c:forEach>
</table>



    <%--For displaying Page numbers.
    The when condition does not display a link for the current page--%>
    <table border="1" cellpadding="5" cellspacing="5">
      <tr>
       
        <%--For displaying Previous link except for the 1st page --%>
        <c:if test="${currentPage != 1}">
          <td><a href="index.jsp?currentPage=${currentPage - 1}" style="cursor:pointer;color: red">Previous</a></td>
        </c:if>
      
            <c:forEach begin="1" end="${noOfPages}" var="i">
                <c:choose>
                    <c:when test="${currentPage eq i}">
                        <td>${i}</td>
                    </c:when>
                    <c:otherwise>
                        <td><a href="index.jsp?currentPage=${i}" style="cursor:pointer;color: red">${i}</a></td>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
           
        
     <%--For displaying Next link --%>
      <c:if test="${currentPage lt noOfPages}">
        <td><a href="index.jsp?currentPage=${currentPage + 1}" style="cursor:pointer;color: red">Next</a></td>
     </c:if>
   
      </tr>
      
    </table>
    
</body>
</html>