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
			</script>
					
	</head>


	<body>
		<div id="own_sounds">
			<table class="myProfileTeamNameList">
   				<c:forEach var="sound" items="${requestScope.sounds}">
   					<tr id="s${sound.getSoundId()}">
   						<td>
   							<img alt="" src="<c:url value="/covers/${sound.getFileName()}.jpg"/>" height="150" width="150" >
   						</td>
   						<td>
   							<c:out value="${sound.soundTitle}"/>
   						</td>
   						<td>
   							<c:out value="${sound.soundViewCount}"/>
   						</td>
   						<td>
   							<c:out value="${sound.soundFans.size()}"/>
   						</td>
   						<td>
   							<button id="sound_id_to_delete" onclick="deleteSound(${sound.getSoundId()})">Delete</button>
   						</td>
   						<td>
   							<select id="add_to_album" name="album" >
								<c:forEach items="${requestScope.albums}" var="album">
   					 				<option value="${album.albumId}"><c:out value="${album.albumTitle}"/></option>
   					 				<button id="sound_id_to_delete" onclick="addToAlbum()">Add to album</button>
  								</c:forEach>
  							</select>
						</td>
					</tr>	
				</c:forEach>
			</table>
		</div>
		
		

	</body>
</html>
	