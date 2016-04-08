<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/profile.css"/>" />
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
	var avatarOk = false;
	var avatar = null;
	$('#avatar').change(
			function() {
				var file = this.files[0];
				avatar = file;
				name = file.name;
				size = file.size;
				type = file.type;
				avatarOk = false;
				if (file.name.length < 1) {
				} else if (file.size > 1000000) {
					alert("File is to big");
				} else if (file.type != 'image/png' && file.type != 'image/jpg'
						&& file.type != 'image/gif'
						&& file.type != 'image/jpeg') {
					alert("File doesnt match png, jpg or gif");
				} else {
					avatarOk = true;
				}
			});
	function updateUser() {
		if (!avatarOk) {
			alert("Invalid avatar");
			$('#avatar').focus();
			return;
		}
		var form = new FormData();

		form.append("avatar", avatar);
		form.append('password1', $("#pass1").val());
		form.append('password2', $("#pass2").val());
		form.append('email', $("#email").val());
		form.append('location', $("#location").val());

		$.ajax({
			url : 'updateUser',
			data : form,
			processData : false,
			contentType : false,
			type : 'POST',
			dataType : 'json',
			enctype : 'multipart/form-data',
			success : function(data) {
				if (data.status == 'ok') {
					$('#userAvatar').attr('src', data.img);
					$('#avatar').val('');
					$('#pass1').val('');
					$('#pass2').val('');
					$('#email').val('');
					$('#location').val('');

					avatarOk = false;
					$('#pass1').focus();
					//					$('#upload_message').text(data.msg);
					//					$('#upload_message').css('color', goodColor);
					//					$('#upload_message').show();
					return;
				}
				//				$('#upload_message').text(data.msg);
				//				$('#upload_message').css('color', badColor);
				//				$('#upload_message').show();

			},
			error : function(err) {
				alert(err);
			}
		});

	}
</script>


</head>

<body>

	<div id="my_profile">
		<table>
			<tr>
				<td><img id="userAvatar" alt=""
					src="<c:url value="/covers/${user.avatarName}.jpg"/>" height="150"
					width="150"></td>
				<td><c:out value="${user.username}" /></td>
				<td><c:out value="${user.location}" /></td>
			</tr>
		</table>
	</div>



	<div id="update_notification">

		<h5 id="update_message" style="display: none; color: #ff6666">Problem
			updating. /// Please, fill all the fields.</h5>

	</div>

	<div id="form">
		<p>Select cover photo</p>
		<input type="file" name="user_cover_photo" id="avatar"
			accept="image/*" /> <br /> <input id="pass1" type="password"
			name="password1" placeholder="choose new password"
			onkeyup="checkPass(); return false;" required /> <br /> <input
			id="pass2" type="password" name="password2"
			placeholder="re-enter new password"
			onkeyup="checkPass(); return false;" required /> <br /> <input
			id="email" type="text" name="email" placeholder="enter new email"
			onkeyup="validateEmail(); return false;" required /> <br /> <input
			id="location" type="text" name="location"
			placeholder="enter new location" required /> <br /> <input
			type="submit" value="Update" onclick="updateUser()" /> <br />
		<!--<span id="update_message"></span>-->
	</div>



</body>

</html>