<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="register.css">
		<title>Register</title>
	
	<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
		
	<script>
		function checkPass(){
		   
		    var pass1 = document.getElementById('pass1');
		    var pass2 = document.getElementById('pass2');
			var message = document.getElementById('match_message');
		   
		    var goodColor = "#66cc66";
		    var badColor = "#ff6666";
		    
		    if(pass1.value == pass2.value){
		        pass2.style.backgroundColor = goodColor;
		        message.style.color = goodColor;
		        message.innerHTML = "Passwords match!"
		    }else{
		       	pass2.style.backgroundColor = badColor;
		        message.style.color = badColor;
		        message.innerHTML = "Passwords do not match!"
		    }
		}  
	</script>
	<script>
				
    			function register(){
    				  var pass1 = document.getElementById('pass1');
    				  var pass2 = document.getElementById('pass2');
    				  var user = document.getElementById('username');
    				$.post("register", {
    					username : user.value,
    					password1 : pass1.value,
    					password2 : pass2.value
    					   				
    				});
        			
    				<!--alert(document.getElementById('username').value);-->
        			   			
    			};
	</script>
	
	
	</head>


	<body>
	
		<div id="login_notification">
			<c:if test="${requestScope.user_exists == true}">
				<h5 id="login_message">There is a user already registered with this username.</h5>	
			</c:if>	
			<c:if test="${requestScope.partial_input == true}">
				<h5 id="login_message">Please, fill all the fields.</h5>	
			</c:if>		
		</div>
	
	
		<form action="register" method="POST">
			<input id="username" type="text" name="username" placeholder="choose username"  required/> 
				<br/>
			<input id="pass1" type="password" name="password1" placeholder="choose password" onkeyup="checkPass(); return false;" required/> 
				<br/>
			<input id="pass2" type="password" name="password2" placeholder="re-enter password" onkeyup="checkPass(); return false;" required/> 
				<br/>
			 <input type="submit" value="Login" onclick="register()"/>
				<br/>
			<span id="match_message"></span>
		</form>

	</body>
	
	
	
	
</html>