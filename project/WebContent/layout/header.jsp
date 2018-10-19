<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
<head>
<title>회원관리</title>
<meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
<%-- view 폴더 아래에 있는 jsp 페이지가 포함해서 보여줘야 하는 페이지이기 때문에 경로설정은 아래와 같이  --%>
<link href="../css/bootstrap.min.css"  rel="stylesheet">
<link href="../css/starter-template.css" rel="stylesheet">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="../js/jquery.validate.min.js"></script>
<script src="../js/joinform.js"></script>
<script src="../js/popper.min.js"></script>
<script src="../js/bootstrap.min.js"></script>
<script>
$(function(){
	$('.nav-item a').filter(function(){
		return this.href==location.href;
		}).parent().addClass('active').siblings().removeClass('active');		
});
</script>

</head>
<body>
    <!-- Fixed navbar -->
    <header>
   		<div  style="background-image: url('./img/background1.jpg');">
			<div class="overlay"></div>
		</div>
      <nav class="navbar navbar-expand-md navbar-dark bg-dark fixed-top">
        <a class="navbar-brand" href="../index.html">
        	<img src="../img/log.png">
        </a>        
        <div class="collapse navbar-collapse" id="menu">
          <ul class="main-nav nav navbar-nav navbar-right">
					<li><a href="#home">Home</a></li>
					<li><a href="#produce">Experience</a></li>
					<li><a href="#board">게시판</a></li>
					<li><a href="#team">Team</a></li>
					<li><a href="#contact">Contact</a></li>
					<li><a href="view/loginForm.jsp">로그인</a></li>
				</ul>          
        </div>
      </nav>
     </header>
      <main role="main" class="container">






