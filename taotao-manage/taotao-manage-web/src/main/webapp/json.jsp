<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    String fname = request.getParameter("callback");
    if("".equals(fname) || null == fname){
        out.print("{\"abc\":123}");
    }else{
        out.print(fname + "({\"abc\":123})");
    }

%>
