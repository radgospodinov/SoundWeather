<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="home.css">
	<title>Home</title>
	</head>
	
	<body id="body">
		
		<div id="header">
			<div id="logo"></div>
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
		
		<div id="user_navigation">
	
			<div id="navcontainer">
				<ul id="navlist">
					<li><a href="#" id="current">UPLOAD</a></li>
					<li><a href="#">SOUNDS</a></li>
					<li><a href="#">ALBUMS</a></li>
					<li><a href="#">FOLLOWING</a></li>
					<li><a href="#">PROFILE</a></li>
				</ul>
			</div>
		
		</div>
		
		
		
		<div id="sounds_space">
			<div id="weather_sounds">Weather Sounds</div>
			<div id="trendy_sounds">Trendy Sounds</div>
			<div id="lastplayed_sounds"> Lastplayed Sounds</div>
		</div>
		
	</body>
	
</html>