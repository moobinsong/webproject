<%@page import="member.MemberVO"%>
<%@page import="member.MemberDAO"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	request.setCharacterEncoding("utf-8");
	//joinForm.jsp에서 사용자가 입력한 값들을 가져온 후	
	String userid=request.getParameter("userid");
		
	//db 테이블에 삽입하기
	MemberDAO dao=new MemberDAO();	
	boolean result=dao.checkId(userid);
	
	if(result){
		out.print(0);		
	}else{
		out.print(1);	
	}
%>    
