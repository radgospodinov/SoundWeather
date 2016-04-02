<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
			<input type="file" name="uploaded_sound"/> 
				<br/>
			<input type="text" name="sound_title" placeholder="enter sound title"/> 
				<br/>
			<input type="file" name="sound_cover_photo"/> 
				<br/>
			<input type="submit" value="Upload sound"/>
		</form>

	</body>
	
</html>