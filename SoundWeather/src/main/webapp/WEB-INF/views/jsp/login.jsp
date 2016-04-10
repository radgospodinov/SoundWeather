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
	};


function showForgottenPass() {
	$('#login_space').hide(1000);
	$('#forgottenPass').show(1000);
}


function sendNewPass() {
	$.post({
		url : "forgottenPass",
		data : {
			email : $('#emailRecipient').val()
		
		},
		dataType : "json",
		success : function(data, textStatus, jqXHR) {
			
			$('#login_space').show(1000);
			$('#forgottenPass').hide(1000);	
			if(data.status == ok) {
			//	$('#login_message') style
			}
			$('#login_message').text(data.msg);
			$('#login_message').show(1000);
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert("Something really bad happened " + textStatus + " - "
					+ errorThrown);
		}
	});
}

	function validateEmailRecipient() {
		var mail = document.getElementById('emailRecipient');
		var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		var goodColor = "#66cc66";
		var badColor = "#ff6666";
		if (mail.value.match(mailformat)) {
			mail.style.backgroundColor = goodColor;
		} else {
			mail.style.backgroundColor = badColor;
		}
	}
</script>
</head>


<body>

	<div id="login_space">
		<div id="login_notification">
			<h5 id="login_message" style="display: none; color: #ff6666">Wrong
				username or password.</h5>
		</div>


		<input id="user" type="text" name="username"
			placeholder="enter username" /> <br /> <input id="pass"
			type="password" name="password" placeholder="enter spassword" /> <br />
		<input type="submit" value="Login" onclick="login()" /> <input
			id="url" type="hidden" value="${requestScope.url}" /> <br> 
			<a
			onclick="showForgottenPass()">Forgotten password?
			</a>
	</div>
	
	<div id="forgottenPass" style="display: none">
		<div>
			<h3>Enter your email and we are gonna send you a new password</h3>
		</div>

		<input id="emailRecipient" type="text" name="email"
			placeholder="enter email" onkeyup="validateEmailRecipient(); return false;"
			required /> <br /> <input type="submit" value="Send new password"
			onclick="sendNewPass()" />
	</div>

</body>
</html>