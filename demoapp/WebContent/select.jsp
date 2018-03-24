<%@page import="demoapp.ConnectionTest"%>
<%@page import="org.hibernate.Session"%>
<%@page import="org.hibernate.SessionFactory"%>
<%@page import="org.hibernate.query.Query"%>
<%@page import="demoapp.stock"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>

<% 
String name=request.getParameter("selected");
out.println(name);

%>