<%@page import="member.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");

	String userid=request.getParameter("userid");	
	
	MemberDAO dao=new MemberDAO();	
	boolean flag=dao.checkId(userid);
	
	if(flag){ //formValidate가 작동하기 위해서는 false를 돌려줘야 인식함
		out.print("false");
	}else{
		out.print("true");
	}
%>   
