<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/profile.css"/>" />
		<!-- TODO -> MOVE TO INDEX.JSP ALL CSS/JS -->
		<title>Profile</title>
		
			
		<script>
	function checkPass() {

		var pass1 = document.getElementById('pass1');
		var pass2 = document.getElementById('pass2');
		var message = document.getElementById('match_message');

		var goodColor = "#66cc66";
		var badColor = "#ff6666";

		if (pass1.value == pass2.value) {
			pass2.style.backgroundColor = goodColor;
			message.style.color = goodColor;
			message.innerHTML = "Passwords match!"
		} else {
			pass2.style.backgroundColor = badColor;
			message.style.color = badColor;
			message.innerHTML = "Passwords do not match!"
		}
	}
</script>

<script>
	function validateEmail() {
		var mail = document.getElementById('email');
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

<script>
	function updateUser() {
		$.post({
			url : "updateProfile",
			data : {
				password1 : $("#pass1").val(),
				password2 : $("#pass2").val(),
				email : $("#email").val(),
				location : $("#location").val(),
				
				
				
			},
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				if (data.status != 'ok') {
					$("#update_message").text(data.msg);
					$("#update_message").show();
					$(data.fld).focus();
				}

			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert("Something really bad happened " + textStatus + " - "
						+ errorThrown);
			}
		});
	};
</script>


	</head>
	
	<body>
	
		<div id="my_profile">
			<table>
   				<tr>
   					<td>
   						<img alt="" src="/covers/${user.avatarName}">
   					</td>
   					<td>
   						<c:out value="${user.username}"/>
   					</td>
   					<td>
   						<c:out value="${user.location}"/>
   					</td>
   				</tr>		
			</table>
		</div>
	
	
	
	<div id="update_notification">

		<h5 id="update_message" style="display: none;color:#ff6666 ">Problem updating. /// Please, fill all the
			fields.</h5>

	</div>

	<div id="form">
	<p>Select cover photo</p>
	<input type="file" name="user_cover_photo" id="user_cover"
		accept="image/*" /> 
	<br />
	<input id="pass1" type="password" name="password1"
		placeholder="choose new password" onkeyup="checkPass(); return false;"
		required />
	<br />
	<input id="pass2" type="password" name="password2"
		placeholder="re-enter new password" onkeyup="checkPass(); return false;"
		required />
	<br />
	<input id="email" type="text" name="email" placeholder="enter new email"
		onkeyup="validateEmail(); return false;" required />
	<br />
	<input id="location" type="text" name="location"
		placeholder="enter new location" required />
	<br />
	
	<input type="submit" value="Update" onclick="updateUser()" />
	<br />
	<!--<span id="update_message"></span>-->
	</div>



	</body>
	
</html>