<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>


<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Upload</title>
</head>
<script type="text/javascript">
	var soundOk = false;
	var coverOk = false;
	var soundFile = null;
	var soundCover = null;
	var goodColor = "#66cc66";
	var badColor = "#ff6666";
	$('#mp3sound')
			.change(
					function() {
						var file = this.files[0];
						soundFile = file;
						name = file.name;
						size = file.size;
						type = file.type;
						soundOk = false;
						if (file.name.length < 1) {
						} else if (file.size > 31457280) {
							alert("File is to big");
						} else if (file.type != 'audio/mpeg'
								&& file.type != 'audio/mp3'
								&& file.type != 'audio/mp4') {
							alert("File doesnt match mp3");
						} else {
							soundOk = true;
						}
					});
	$('#mp3cover').change(
			function() {
				var file = this.files[0];
				soundCover = file;
				name = file.name;
				size = file.size;
				type = file.type;
				coverOk = false;
				if (file.name.length < 1) {
				} else if (file.size > 1000000) {
					alert("File is to big");
				} else if (file.type != 'image/png' && file.type != 'image/jpg'
						&& file.type != 'image/gif'
						&& file.type != 'image/jpeg') {
					alert("File doesnt match png, jpg or gif");
				} else {
					coverOk = true;
				}
			});
	function uploadFile() {
		if (!coverOk || !soundOk || $('#soundTitle').val() == "") {
			alert("please enter valid input");
			$('#mp3sound').focus();
			return;
		}
		var form = new FormData();
		form.append("mp3sound", soundFile);
		form.append("mp3cover", soundCover);
		form.append('mp3title', $('#soundTitle').val());
		form.append('mp3genres', $('#genres').val());
		console.log("form data " + form.mp3sound);
		$('#upload_message').hide();

		$.ajax({
			url : 'upload',
			data : form,
			processData : false,
			contentType : false,
			type : 'POST',
			dataType : 'json',
			enctype : 'multipart/form-data',
			success : function(data) {
				if (data.status == 'ok') {
					$('#mp3sound').val('');
					$('#soundTitle').val('');
					$('#mp3cover').val('');
					$('#genres').val('');
					soundOk = false;
					coverOk = false;
					$('#mp3sound').focus();
					$('#upload_message').text(data.msg);
					$('#upload_message').css('color', goodColor);
					$('#upload_message').show();
					return;
				}
				$('#upload_message').text(data.msg);
				$('#upload_message').css('color', badColor);
				$('#upload_message').show();

			},
			error : function(err) {
				alert(err);
			}
		});
	}
</script>

<body>

	<div id="upload_space">
		<div id="upload_notification">
			<h3 id="upload_message" style="display: none; color: #ff6666"></h3>
		</div>
		<!-- 
	
		<form action="upload2" e	method="POST">
	 -->
		<p>Select sound file</p>
		<input type="file" name="uploaded_sound" id="mp3sound"
			accept="audio/*" /> <br />
		<p>Enter sound title</p>
		<input type="text" name="sound_title" placeholder="enter sound title"
			id="soundTitle" /> <br />
		<p>Select cover photo</p>
		<input type="file" name="sound_cover_photo" id="mp3cover"
			accept="image/*" /> <br />
		<p>Hold down (ctrl) to select multiple genres</p>
		<select id="genres" size="1" name="genres" multiple="multiple">
			<option value="rock1">rock</option>
			<option value="pop2">pop</option>
			<option value="black metal3">black metal</option>
			<!--<c:forEach items="${applicationScope.genres}" var="genre">
	
   					 <option value="${genre}"><c:out value="${genre}"/></option>
  				</c:forEach>-->
		</select> <br /> <input type="hidden" name="author"
			value="${sessionScope.loggedUser.username}" /> <input type="submit"
			value="Upload sound" onclick="uploadFile()" />

	</div>
</body>

</html>