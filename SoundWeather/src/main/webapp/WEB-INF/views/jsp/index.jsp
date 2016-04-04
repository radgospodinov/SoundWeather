<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script src="<c:url value="/script/jquery-2.2.2.min.js"/>"></script>
<title>Home</title>

<script type="text/javascript">
	function loadJSP(target) {
		$('#sounds_space').load(target);
	}
</script>




</head>

<body id="body">

	<div id="header"
		style="background-image:url(<c:url value="/images/background1.jpg"/>);">
		<div id="logo"
			style="background-image:url(<c:url value="/images/logo.png"/>);"
			onclick="loadJSP('home')"></div>
		<div id="register" style="cursor: pointer;"
			onclick="loadJSP('register')">Register</div>
		<div id="login" style="cursor: pointer;" onclick="loadJSP('login')">Login</div>
		<div id="logout"></div>
		<div id="search">
			<form action="SearchMethod" method="GET">
				<input type="text" name="search_word" placeholder="explore sounds">
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
				<li><a id="current" onclick="loadJSP('upload')">UPLOAD</a></li>
				<li><a href="#">SOUNDS</a></li>
				<li><a href="#">ALBUMS</a></li>
				<li><a href="#">FOLLOWING</a></li>
				<li><a href="#">PROFILE</a></li>
			</ul>
		</div>

	</div>



	<div id="sounds_space">
		<jsp:include page="playlists.jsp"></jsp:include>
	</div>

</body>
<script src="<c:url value="/script/jaudio.min.js"/>"></script>
</html>