<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Login</title>

<script>
	function login() {
		$.post({
			url : "login",
			data : {
				username : $("#user").val(),
				password : $("#pass").val(),
				url : $('#url').val()
			},
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				if (data.status == 'ok') {
					$('#registerHeader').hide();
					$('#loginHeader').hide();
					$('#logoutHeader').show();
					loadJSP(data.url);
				} else {
					$("#login_message").text(data.msg);
					$("#login_message").show();
					$(data.fld).focus();

				}

			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert("Something really bad happened " + textStatus + " - "
						+ errorThrown);
			}
		});

		<!--alert(document.getElementById('username').value);
		-->

	};
</script>
</head>


<body>

	<div id="login_space">
	<div id="login_notification">
		<h5 id="login_message" style="display: none; color: #ff6666">Wrong
			username or password.</h5>
	</div>


	<input id="user" type="text" name="username"
		placeholder="enter username" />
	<br />
	<input id="pass" type="password" name="password"
		placeholder="enter spassword" />
	<br />
	<input type="submit" value="Login" onclick="login()" />
	<input id="url" type="hidden" value="${requestScope.url}"/>
	</div>
	
</body>




</html>