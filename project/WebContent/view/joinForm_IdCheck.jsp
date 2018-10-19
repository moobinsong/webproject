<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<form>
	아이디 : <input type="text" id="userid" 
			name="userid" oninput="checkId()">
	<div id="result" style="display:inline"></div>
</form>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script>
	function checkId(){
		var id=$("#userid").val();
		$.ajax({
			url:'idCheck_result.jsp',
			type:'post',
			data:{userid:id},
			success:function(data){
				console.log(data);
				if(data==0){
					$("#result").html("<span style='color:red'>사용불가</span>");
				}else{
					$("#result").html("<span style='color:red'>사용가능</span>");
				}
			}
		});
	}
</script>
</body>
</html>