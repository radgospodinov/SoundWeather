<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/home.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/main.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/register.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/upload.css"/>" />
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/playlists.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/search.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/albums.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/own_sounds.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/jquery-ui.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/sound.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/following.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/other_user.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/favorites.css"/>" />
	<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/profile.css"/>" />
<script src="<c:url value="/script/jquery-2.2.2.min.js"/>"></script>
<!--  <script src="<c:url value="/script/jquery.dataTables.min.js"/>"></script>
<script src="<c:url value="/script/jquery-ui.min.js"/>"></script>-->

<title>SoundWeather</title>

<script type="text/javascript">
	function loadJSP(target) {
		$('#sounds_space').load(target); 
	};

	function search() {
		if($('#searchText').val().trim().length == 0) {
			return;
		}
		$('#sounds_space').load('search', {search_word : $('#searchText').val().trim(),requested_page : 1,are_sounds : true});

	};
	
	$(function() {
		loadJSP('home');
	});
	function logout() {
		loadJSP('logout');
		$('#logoutHeader').hide();
		$('#loginHeader').show();
		$('#registerHeader').show();
	};
	
	</script>
	
	
	<script>
	
	
	var timer;
	var lat;
	var lon;
	function startLocationUpdate() {
		if (navigator.geolocation) {
			timer = setInterval(getLocation(), 10*1000)
			getLocation();			
	    } 
	}
	function getLocation(){
		 navigator.geolocation.getCurrentPosition(function(x){
			var changed = false;
			if(lat) {
				if(x.coords.latitude.toFixed(2)!= lat || x.coords.longitude.toFixed(2)!=lon) {
					changed=true;
				} 
			} else {
				changed=true;
			}
			if(changed && jQuery.active==0) {
				if($('#weather_sounds')) {
					lat = x.coords.latitude.toFixed(2);
					lon = x.coords.longitude.toFixed(2);
					loadJSP('updateLocationByCords?lat='+lat+'&lon='+lon);
				}
			}
		 
		 
		 },function(){
			 clearInterval(timer);
		 });
		 
	}
	</script>

</head>


<body id="body">


	<div id="header"
		style="background-image:url(<c:url value="/images/background1.jpg"/>);">
		
		<div id="logo"
			style="background-image:url(<c:url value="/images/logo.png"/>);"
			onclick="loadJSP('home')"></div>
		
			
			<div id="registerHeader" class="register" style="cursor: pointer;" onclick="loadJSP('register')">Register</div>
			<div id="loginHeader" class="login" style="cursor: pointer;" onclick="loadJSP('login')">Login</div>
			<div id="logoutHeader" class="login" style="cursor:pointer;display:none;" onclick="logout()">Logout</div>
				
		<div id="search">
			<form action="javascript:search()" >
				<input type="text" id="searchText" placeholder="explore sounds">
			</form>
		</div>
		
	</div>



	<div id="player">
		<div class="jAudio--player">

			<audio></audio>

			<div class="jAudio--ui">

				<div class="jAudio--thumb"></div>

				<div class="jAudio--status-bar">

					<div class="jAudio--details"></div>
					<div class="jAudio--volume-bar"></div>

					<div class="jAudio--progress-bar">
						<div class="jAudio--progress-bar-wrapper">
							<div class="jAudio--progress-bar-played">
								<span class="jAudio--progress-bar-pointer"></span>
							</div>
						</div>
					</div>

					<div class="jAudio--time">
						<span class="jAudio--time-elapsed">00:00</span> <span
							class="jAudio--time-total">00:00</span>
					</div>

				</div>

			</div>


			<div class="jAudio--playlist"></div>

			<div class="jAudio--controls">
				<ul>
					<li><button class="btn" data-action="prev" id="btn-prev">
							<span></span>
						</button></li>
					<li><button class="btn" data-action="play" id="btn-play">
							<span></span>
						</button></li>
					<li><button class="btn" data-action="next" id="btn-next">
							<span></span>
						</button></li>
				</ul>
			</div>

		</div>


	</div>



	<div id="user_navigation">

		<div id="navcontainer">
			<ul id="navlist">
				<li><a id="current" style="cursor: pointer;" onclick="loadJSP('upload')">UPLOAD</a></li>
				<li><a style="cursor: pointer;" onclick="loadJSP('own_sounds')">SOUNDS</a></li>
				<li><a style="cursor: pointer;" onclick="loadJSP('albums')">ALBUMS</a></li>
				<li><a style="cursor: pointer;" onclick="loadJSP('favorites')">FAVORITES</a></li>
				<li><a style="cursor: pointer;" onclick="loadJSP('following')">FOLLOWING</a></li>
				<li><a style="cursor: pointer;" onclick="loadJSP('profile')">PROFILE</a></li>
			</ul>
		</div>

	</div>



	<div id="sounds_space">
		<jsp:include page="playlists.jsp"></jsp:include>
	</div>

</body>

<script src="<c:url value="/script/jaudio.min.js"/>"></script>

<script>

<c:if test="${logStatus}">
	$('#logoutHeader').show();
	$('#loginHeader').hide();
	$('#registerHeader').hide();
</c:if>
startLocationUpdate();
</script>
</html>