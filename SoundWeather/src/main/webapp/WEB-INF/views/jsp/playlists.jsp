<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="playlists.css">
		<title>Playlists</title>
		
	</head>
	
	<body id="playlists_body">
	
	<div>
		
		<!-- THE DUMMY STUFF -->
		<div id="weather_sounds">
			<h3 id="playlist_name">Weather sounds</h3>
			<table id="playlist_table">
   				<tr id="row_with_sounds">
   				 <c:forEach var="i" begin="1" end="6">
           				<td id="individual_sound">
   							<div id="images_container">
   								<a onclick="">
   									<img id="sound_cover_photo" alt="Sound cover photo" src="<c:url value="/images/sound_cover.jpg"/>"/>
   									<img id="play" src="<c:url value="/images/play.png"/>"/>
   								</a>
   							</div>
   							
   							<div id="sound_title">
   								<c:out value="Sound Title ${i}"/>
   							</div>
   							
   							<button id="like" value="Like" >Like</button>
   						</td>
   				</c:forEach>
				</tr>
			</table>
		</div>
		
					
		<div id="weather_sounds">
			<h3 id="playlist_name">Trendy sounds</h3>
			<table id="playlist_table">
   				<tr id="row_with_sounds">
   				 <c:forEach var="i" begin="1" end="6">
           				<td id="individual_sound">
   							<div id="images_container">
   								<a onclick="">
   									<img id="sound_cover_photo" alt="Sound cover photo" src="<c:url value="/images/sound_cover.jpg"/>"/>
   									<img id="play" src="<c:url value="/images/play.png"/>"/>
   								</a>
   							</div>
   							
   							<div id="sound_title">
   								<c:out value="Sound Title ${i}"/>
   							</div>
   							
   							<button id="like" value="Like" >Like</button>
   						</td>
   				</c:forEach>
				</tr>
			</table>
		</div>
		
		
			
		<!-- THE REAL STUFF -->
		<!--  <div id="weather_sounds">
			<h3 id="playlist_name">Weather sounds</h3>
			<table id="playlist_table">
   				<tr id="row_with_sounds">
   				<c:forEach var="weather_sound" items="">
   					
   						<td id="individual_sound">
   						<div id="images_container">
   							<a onclick="">
   								<img id="sound_cover_photo" alt="" src="<c:url value=""/>">
   								<img id="play" src="<c:url value="/images/play.png"/>"/>
   							</a>
   						</div>
   							<div id="sound_title">
   							<c:out value="Sound Title"/>
   							</div>
   							<button id="like" value="Like" >Like</button>
   						</td>
   											
				</c:forEach>
				</tr>
			</table>
		</div>
		
					
		<!--  <div id="trendy_sounds">
			<h3 id="playlist_name">Trendy sounds</h3>
			<table id="playlist_table">
   				<tr id="row_with_sounds">
   				<c:forEach var="trendy_sound" items="">
   					
   						<td id="individual_sound">
   						<div id="images_container">
   							<a onclick="">
   								<img id="sound_cover_photo" alt="" src="<c:url value=""/>">
   								<img id="play" src="<c:url value="/images/play.png"/>"/>
   							</a>
   						</div>
   							<div id="sound_title">
   							<c:out value="Sound Title"/>
   							</div>
   							<button id="like" value="Like" >Like</button>
   						</td>
   											
				</c:forEach>
				</tr>
			</table>
		</div>
				
		
		<!--  <div id="lastplayed_sounds">Lastplayed Sounds</div>-->

	</div>
	
	</body>
	
</html>