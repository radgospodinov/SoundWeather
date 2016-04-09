<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="playlists.css">
<title>Playlists</title>

<script>
	function addSongs(mp3File, jpegFile, soundTitle, soundAuthor, trackAlbum) {

		$(".jAudio--controls").unbind();
		$(".jAudio--playlist").unbind();
		t.playlist.unshift({
			file : mp3File,
			thumb : jpegFile,
			trackName : soundTitle,
			trackArtist : soundAuthor,
			trackAlbum : trackAlbum,
		});
		$('#btn-pause').data("action", "play");
		$('#btn-pause').attr("id", "btn-play");
		$('#btn-play').data("action", "play");
		$('#btn-play').attr("id", "btn-play");
		$(".jAudio--player").jAudio(t);
		$('#btn-play').click();
	};
	function like(soundId){
		$.post({
			url : "likeSound",
			data : {
				id : soundId
			},
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				if (data.status == 'ok') {
					$('#wLike'+data.id).text('Like ['+data.likes+']');
					$('#tLike'+data.id).text('Like ['+data.likes+']');
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert("Something really bad happened " + textStatus + " - "
						+ errorThrown);
			}
		});
	};
	function fav(soundId){
		$.post({
			url : "favSound",
			data : {
				id : soundId
			},
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				if (data.status == 'ok') {
// 					surce cvqt se smenq
				}
			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert("Something really bad happened " + textStatus + " - "
						+ errorThrown);
			}
		});
	};
</script>


</head>

<body id="playlists_body">

	<div>

		<!-- THE DUMMY STUFF -->
		<div id="weather_sounds">
			<h3 id="playlist_name">Sound forecast for 
			
			<c:if test="${sessionScope.loggedUser == null}">
				<c:out value="Sofia"/>
			
			</c:if>
			
			<c:out value="${sessionScope.loggedUser.location}"/>
			
			
			
			</h3>
			<table id="playlist_table">
				<tr id="row_with_sounds">
					<c:forEach var="wSound" items="${requestScope.weatherSounds}">
						<td id="individual_sound">
							<div id="images_container"
								onclick="addSongs('sounds/${wSound.getFileName()}.mp3','covers/${wSound.getFileName()}.jpg','${wSound.getSoundTitle()}','${wSound.getSoundAuthor().getUsername()}','')">
								<img id="sound_cover_photo" alt="Sound cover photo"
									src="<c:url value="/covers/${wSound.getFileName()}.jpg"/>" height="150" width="150" />
								<img id="play" src="<c:url value="/images/play.png"/>" />
							</div>

							<div id="sound_title">
								<c:out value="${wSound.getSoundTitle()}" />
							</div>

							 <button id="wLike${wSound.getSoundId()}" class="like" onclick="like('${wSound.getSoundId()}')">Like [${wSound.getSoundRating()}]</button>
							<button id="wFav${wSound.getSoundId()}" class="like" onclick="fav('${wSound.getSoundId()}')">Favorite </button>
							<!-- <div>
							<b>${wSound.getSoundRating()}</b><img id="wLike${wSound.getSoundId()}" class="fans_heart" src="<c:url value="/images/like.png"/>" onclick="like('${wSound.getSoundId()}')" style=cursor:pointer>
							<img id="wFav${wSound.getSoundId()}" class="fans_heart" src="<c:url value="/images/heart.png"/>" onclick="fav('${wSound.getSoundId()}')" style=cursor:pointer>-->
							</div>
						
						</td>
					</c:forEach>
				</tr>
			</table>
		</div>


		<div id="weather_sounds">
			<h3 id="playlist_name">Trendy sounds</h3>
			<table id="playlist_table">
				<tr id="row_with_sounds">
					<c:forEach var="tSound" items="${requestScope.trendySounds}">
						<td id="individual_sound">
							<div id="images_container"
								onclick="addSongs('sounds/${tSound.getFileName()}.mp3','covers/${tSound.getFileName()}.jpg','${tSound.getSoundTitle()}','${tSound.getSoundAuthor().getUsername()}','')">
								<img id="sound_cover_photo" alt="Sound cover photo"
									src="<c:url value="/covers/${tSound.getFileName()}.jpg"/>" height="150" width="150" />
								<img id="play" src="<c:url value="/images/play.png"/>" />
							</div>

							<div id="sound_title">
								<c:out value="${tSound.getSoundTitle()}" />
							</div>

							 <button id="tLike${tSound.getSoundId()}" class="like" onclick="like('${tSound.getSoundId()}')">Like [${tSound.getSoundRating()}]</button>
							<button id="tFav${tSound.getSoundId()}" class="like" onclick="fav('${tSound.getSoundId()}')">Favorite </button> 
						<!--  <div>
							<b class="number_of_likes">${tSound.getSoundRating()}</b><img id="tLike${tSound.getSoundId()}" class="fans_heart" src="<c:url value="/images/like.png"/>" onclick="like('${tSound.getSoundId()}')" style=cursor:pointer>
							<img id="tFav${tSound.getSoundId()}" class="fans_heart" src="<c:url value="/images/heart.png"/>" onclick="fav('${tSound.getSoundId()}')" style=cursor:pointer>
							</div>-->	
						</td>
					</c:forEach>
				</tr>
			</table>
		</div>



		
		<!--  <div id="lastplayed_sounds">Lastplayed Sounds</div>-->

	</div>

</body>

</html>