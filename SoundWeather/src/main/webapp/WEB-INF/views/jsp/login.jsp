<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="login.css">
		<title>Login</title>
		<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
		<script>
				
    			function login(){
    				  var user = document.getElementById('username');
    				  var pass = document.getElementById('pass');
    				 $.post("register", {
    					username : user.value,
    					password : pass.value
    				});
        			
    				<!--alert(document.getElementById('username').value);-->
        			   			
    			};
	</script>
	</head>


	<body>
	
		<div id="login_notification">
			<c:if test="${requestScope.retry_login == true}">
				<h5 id="login_message">Wrong username or password.</h5>	
			</c:if>	
		</div>
	
	
		<form action="login" method="POST">
			<input id="user" type="text" name="username" placeholder="enter username"/> 
				<br/>
			<input id="pass" type="password" name="password" placeholder="enter spassword"/> 
				<br/>
			<input type="submit" value="Login" onclick="login()"/>
		</form>

	</body>
	
	
	
	
</html>