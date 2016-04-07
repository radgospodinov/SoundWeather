<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC>

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="albums.css">
		<title>Albums</title>
		
		<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
			
			<script>

			function deleteAlbum(albumId) {
				
			}

			</script>
				
			<script>

			function createAlbum() {
				
			}

			</script>	
			
			<script>

			function updateAlbum(albumId) {
				
			}

			</script>			
	</head>


	<body>
	
	
		<div id="create_new_album">
			<h3>Create new album</h3>
			<p>Enter album title</p>
			<input type="text" name="new_album_title" placeholder="enter album title" id="new_album_title" /> 
			<br/>
			<p>Select album cover</p>
			<input type="file" name="new_album_cover_photo" id="new_album_cover_photo" accept="image/*" /> 
			<input type="submit" value="Create album" onclick="createAlbum()" />
		</div>
	
	
	
	
		<div id="own_albums">
			<table>
   				<c:forEach var="album" items="${sessionScope.loggedUser.albums}">
   					<tr id="individual_album">
   						<td>
   							<img src="${album.albumCover}">
   							<c:out value="${album.albumTitle}"/>
   						</td>
   						<td>
   							<c:forEach var="album_sound" items="${album.albumTracks}">
								<a id="song_from_album" onclick="loadJSP('sound?soundId=${album_sound.soundId}')"><c:out value="${album_sound.soundTitle}"/></a>
								<button id="remove_sound_from_album" value="Remove" onclick="deleteSound(${album_sound.soundId})">Remove</button>
								<br>
							</c:forEach>
   						</td>
   						<td>
   						<div id="update_album">
							<p>Enter new title</p>
							<input type="text" name="update_album_title" placeholder="change album title" id="update_album_title" /> 
							<br/>
							<p>Select new cover</p>
							<input type="file" name="update_cover_photo" id="update_cover_photo" accept="image/*" /> 
							<input type="submit" value="Update album" onclick="updateAlbum(${album.albumId})" />
						</div>
   						</td>
   						
   						
   						<td>
   							<button id="album_to_delete" value="Delete" onclick="deleteAlbum(${album.albumId})">Delete</button>
   						</td>
   						
					</tr>	
				</c:forEach>
			</table>
		</div>
		
		

	</body>
</html>
	