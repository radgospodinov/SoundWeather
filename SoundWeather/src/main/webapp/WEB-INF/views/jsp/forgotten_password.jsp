<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Forgotten password</title>

<script>
	
</script>
</head>


<body>

	<div>
	<div >
		<h3 >Enter email to get new password</h3>
	</div>


	<input id="email" type="text" name="email"
		placeholder="enter email" />
	<br />
	
	<input type="submit" value="Submit" onclick="" />
	<input id="url" type="hidden" value="${requestScope.url}"/>
	</div>
	
</body>




</html>