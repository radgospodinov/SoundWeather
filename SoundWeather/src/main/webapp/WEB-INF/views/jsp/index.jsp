<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/home.css" />">
	<title>SoundWeather</title>
	</head>
	
	<body id="body">
		
		<div id="header" style="background-image:url(<c:url value="/images/background1.jpg"/>);">
			<div id="logo" style="background-image:url(<c:url value="/images/logo.png"/>)"></div>
			<div id="register">Register</div>
			<div id="login">Login</div>
			<div id="logout"></div>
			<div id="search" >
				<form action="SearchMethod" method="GET">
					<input type="text" name="search_word" placeholder="explore sounds">
				</form>
			</div>
		</div>
		
		<div id="player"></div>
		
		<div id="buttons"></div>
		
		
		
		<div id="sounds_space">
			<div id="weather_sounds">Weather Sounds</div>
			<div id="trendy_sounds">Trendy Sounds</div>
			<div id="lastplayed_sounds"> Lastplayed Sounds</div>
		</div>
		
	</body>
	
</html>