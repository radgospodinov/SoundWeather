<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="own_sounds.css">
		<title>Own sounds</title>
			
			
			<script>
			function deleteSound(soundId){
				$.post({
					url : "deleteSound",
					data : {
						id : soundId
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
			};
			function addSoundToAlbum(soundId,albumId){
				$.post({
					url : "addSoundToAlbum",
					data : {
						soundId : soundId,
						albumId : albumId
					},
					dataType : "json",
					success : function(data, textStatus, jqXHR) {
						
						if (data.status == 'ok') {
							// TODO controller method + result to be made
						}
					},
					error : function(jqXHR, textStatus, errorThrown) {
						alert("Something really bad happened " + textStatus + " - "
								+ errorThrown);
					}
				});
			};
			</script>
				<script type="text/javascript">
					 function getSound(sound_id) {
		 			
	 				alert(sound_id);
					$('#sounds_space').load('sound', {soundId : sound_id});
					};
   	</script>	
	</head>


	<body>
		<div id="own_sounds">
			<table class="table_with_own_sounds">
   				<c:forEach var="sound" items="${requestScope.sounds}">
   					<tr id="s${sound.getSoundId()}">
   						<td>
   							<img class="own_sound_cover_photo" alt="" src="<c:url value="/covers/${sound.getFileName()}.jpg"/>" height="150" width="150" onclick="addSongs('sounds/${sound.getFileName()}.mp3','covers/${sound.getFileName()}.jpg','${sound.getSoundTitle()}','${sound.getSoundAuthor().getUsername()}','')">
   						</td>
   						<td>
   							
   							<a class="own_sound_title" id="song_from_album" href=""
								onclick="getSound('${sound.soundId}')"><c:out
									value="${sound.soundTitle}" /></a>
   						
   						
   						
   						
   						
   						</td>
   					<!--  <td>
   							<c:out value="${sound.soundViewCount}"/>
   						</td>-->	
   						<td>
   							<b class="own_sound_fans"><c:out value="${sound.soundFans.size()}"/></b>
   							<img class="fans_heart" src="<c:url value="/images/heart.png"/>">
   						</td>
   						<td>
   							<button id="sound_id_to_delete" onclick="deleteSound(${sound.getSoundId()})">Delete</button>
   						</td>
   						<td>
   							<select id="add_to_album" name="album" >
								<c:forEach items="${requestScope.albums}" var="album">
   					 				<option  value="${album.albumId}"><c:out value="${album.albumTitle}"/></option>
  								</c:forEach>
  							</select>
						</td>
						<td>
						   <button id="sound_id_to_delete" onclick="addSoundToAlbum(${sound.getSoundId()},$('#add_to_album').val())">Add to album</button>
						
						</td>
					</tr>	
				</c:forEach>
			</table>
		</div>
		
		

	</body>
</html>
	