<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
		<link rel="stylesheet" type="text/css" href="upload.css">
		<title>Upload</title>
	</head>


	<body>
	
		<form action="upload2" enctype="multipart/form-data" method="POST">
			<p>Select sound file:</p>
			<input type="file" name="uploaded_sound"/> 
				<br/>
			<p>Enter sound title:</p>
			<input type="text" name="sound_title" placeholder="enter sound title"/> 
				<br/>
			<p>Select cover photo:</p>
			<input type="file" name="sound_cover_photo"/> 
				<br/>
			<p>Hold down (ctrl) to select multiple genres:</p>
			<select id="genres" size="1" name="genres" multiple="multiple">
				<option value="rock">rock</option>
				<option value="pop">pop</option>
				<option value="black metal">black metal</option>
				<!--<c:forEach items="${applicationScope.genres}" var="genre">
   					 <option value="${genre}"><c:out value="${genre}"/></option>
  				</c:forEach>-->
  			</select>
				<br/>
			<input type="hidden" name="author" value="${sessionScope.user.username}"/> 	
			<input type="submit" value="Upload sound"/>
		</form>

	</body>
	
</html>