<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/other_user.css"/>" />
<!-- TODO -> MOVE TO INDEX.JSP ALL CSS/JS -->
<title>Other user</title>


<script>
	$(document).ready(function() {
		$('#follow').click(function() {

			$.post("follow", {
				user_to_follow : $(this).value,
			});

			<!--alert($(this).value);
			-->

		});
	});
</script>

<script>
	$(document).ready(function() {
		$('#add_to_favorites').click(function() {

			$.post("addToFavorites", {
				song_to_favorites : $(this).value,
			});

			<!--alert($(this).value);
			-->

		});
	});
</script>
</head>

<body>

	<div id="other_user_profile">
		<table class="other_user_characteristics">
			<tr>
				<td><img class="other_user_avatar"
					src="<c:url value="/covers/${other_user.avatarName}.jpg"/>" >
				</td>
				<td><b class="other_user_name"><c:out value="${requestScope.other_user.username}" /></b></td>
				<td><b class="other_user_location"><c:out value="${requestScope.other_user.location}" /></b></td>
				<td>
					<button class="follow_other_user" id="follow" value="${requestScope.other_user.username}" onclick="follow('${requestScope.other_user.username}')">Follow</button>
				</td>
			</tr>
		</table>






	</div>




	<div class="other_user_sounds" id="other_user_sounds">
		<table class="other_user_sounds_table">
			<c:forEach var="sound" items="${requestScope.other_user.sounds}">
				<tr>
					<td><img class="other_user_sound_photo" style="cursor: pointer;"
						src="<c:url value="/covers/${sound.fileName}.jpg"/>" height="150"
						width="150"
						onclick="addSongs('sounds/${sound.getFileName()}.mp3','covers/${sound.getFileName()}.jpg','${sound.getSoundTitle()}','${sound.getSoundAuthor().getUsername()}','')">
					</td>
					<td class="other_user_sound_title" style="cursor: pointer;"
						onclick="loadJSP('sound?soundId=${sound.soundId}')"><c:out
							value="${sound.soundTitle}" /></td>
					<td><b class="own_sound_fans"><c:out value="${sound.soundFans.size()}" /></b><img class="fans_heart"
						src="<c:url value="/images/heart.png"/>"></td>
					<td>
						<!--  <button id="add_to_favorites"
							onclick="fav('${sound.getSoundId()}')">Add to favorites</button>-->
					
					 <button id="tLike${sound.getSoundId()}" class="like" onclick="like('${sound.getSoundId()}')">Like [${sound.getSoundRating()}]</button>
					
					
					</td>
					<td>
						<!--  <button id="likeSound" onclick="like('${sound.getSoundId()}')">Like</button>-->
						<button id="tFav${sound.getSoundId()}" class="like" onclick="fav('${sound.getSoundId()}')">Favorite </button> 
						
					</td>
				</tr>
			</c:forEach>
		</table>
	</div>




</body>

</html>