<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Favorites</title>
<script>
		$(".remove_from_favorites").on('click', function removeFromFavorites(e) {
						
			 var soundId = event.target.id;
							
					$.post({
					url : "removeFromFavorites",
					
					data : {
						
						sound_Id : soundId
					},
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						if (data.status == 'ok') {
							$(data.id).remove();
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						alert("Something really bad happened " + textStatus + " - "
								+ errorThrown);
					}
				});
				
			})
			
</script>
	
</head>


<body>
	<div id="favorite_sounds">
		<table class="table_with_favorite_sounds">
			<c:forEach var="favorite" items="${requestScope.favorites}">
			
				<tr id="sound${favorite.getSoundId()}">
					<td><img class="favorite_sound_cover_photo" alt=""
						src="<c:url value="/covers/${favorite.getFileName()}.jpg"/>"
						height="150" width="150"
						onclick="addSongs('sounds/${favorite.getFileName()}.mp3','covers/${favorite.getFileName()}.jpg','${favorite.getSoundTitle()}','${favorite.getSoundAuthor().getUsername()}','')">
					</td>
					<td><a class="favorite_sound_title" id="song_from_album"
						onclick="loadJSP('sound?soundId=${favorite.soundId}')"><c:out
								value="${favorite.soundTitle}" /></a></td>
										
					<td>
						<button class="remove_from_favorites" id="${favorite.soundId}" value="${favorite.soundId}"
							>Remove</button>
									
					</td>
				</tr>
				
			</c:forEach>
		</table>
	</div>



</body>
</html>
