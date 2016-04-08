<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Albums</title>


<script>
			var coverOk = false;
			var albumCover = null;
			var updateCoverOk = false;
			var updateAlbumCover = null;
			$('#albumCover').change(
					function() {
						var file = this.files[0];
						albumCover = file;
						name = file.name;
						size = file.size;
						type = file.type;
						coverOk = false;
						if (file.name.length < 1) {
						} else if (file.size > 1000000) {
							alert("File is to big");
						} else if (file.type != 'image/png' && file.type != 'image/jpg'
								&& file.type != 'image/gif'
								&& file.type != 'image/jpeg') {
							alert("File doesnt match png, jpg or gif");
						} else {
							coverOk = true;
						}
					});
			
			
			function createAlbum() {
				if (!coverOk || $('#albumTitle').val() == "") {
					alert("please enter valid input");
					$('#albumTitle').focus();
					return;
				}
				var form = new FormData();
			
				form.append("albumCover", albumCover);
				form.append('albumTitle', $('#albumTitle').val());
				form.append('albumGenres', $('#genres').val());

				$.ajax({
					url : 'createAlbum',
					data : form,
					processData : false,
					contentType : false,
					type : 'POST',
					dataType : 'json',
					enctype : 'multipart/form-data',
					success : function(data) {
						if (data.status == 'ok') {
							$('#albumTitle').val('');
							$('#albumCover').val('');
							$('#genres').val('');
							
							coverOk = false;
							$('#albumTitle').focus();
// 							$('#upload_message').text(data.msg);
// 							$('#upload_message').css('color', goodColor);
// 							$('#upload_message').show();
							return;
						}
// 						$('#upload_message').text(data.msg);
// 						$('#upload_message').css('color', badColor);
// 						$('#upload_message').show();

					},
					error : function(err) {
						alert(err);
					}
				});
				
			}
			
			// NEEDS MORE TWEAKS - currently broken
			$('#updateAlbumCover').change(
					function() {
						var file = this.files[0];
						updateAlbumCover = file;
						name = file.name;
						size = file.size;

						type = file.type;
						updateCoverOk = false;
						if (file.name.length < 1) {
						} else if (file.size > 1000000) {
							alert("File is to big");
						} else if (file.type != 'image/png' && file.type != 'image/jpg'
								&& file.type != 'image/gif'
								&& file.type != 'image/jpeg') {
							alert("File doesnt match png, jpg or gif");
						} else {
							updateCoverOk = true;
						}
					});
			
			function updateAlbum(albumId) {
				if (!updateCoverOk || $('#updateAlbumTitle').val() == "") {
					alert("please enter valid input");
					$('#update_album').focus();
					return;
				}
				var form = new FormData();
			
				form.append("albumCover", updateAlbumCover);
				form.append('albumTitle', $('#updateAlbumTitle').val());
				form.append('albumId', albumId);
		//		form.append('albumGenres', $('#genres').val());    -- TO BE ADDED IN JSP MAYBE?

				$.ajax({
					url : 'updateAlbum',
					data : form,
					processData : false,
					contentType : false,
					type : 'POST',
					dataType : 'json',
					enctype : 'multipart/form-data',
					success : function(data) {
						if (data.status == 'ok') {
							$('#updateAlbumTitle').val('');
							$('#updateAlbumCover').val('');
							$('#title'+data.id).text(data.newName);
							$('#cover'+data.id).attr('src', data.newFilePath);
						//	$('#genres').val('');             -- TO BE ADDED IN JSP MAYBE?
							
							coverOk = false;
							$('#albumTitle').focus();
// 							$('#upload_message').text(data.msg);
// 							$('#upload_message').css('color', goodColor);
// 							$('#upload_message').show();
							return;
						}
// 						$('#upload_message').text(data.msg);
// 						$('#upload_message').css('color', badColor);
// 						$('#upload_message').show();

					},
					error : function(err) {
						alert(err);
					}
				});
				
			}

			function deleteAlbum(albumId) {
				$.post({
					url : "deleteAlbum",
					data : {
						id : albumId
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
				
			}
			
			
			

			function deleteSound(soundId,albumId) {
				$.post({
					url : "removeSoundFromAlbum",
					data : {
						soundId : soundId,
						albumId : albumId
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
				
			}
			
			</script>




</head>


<body>


	<div id="create_new_album">
		<h3>Create new album</h3>
		<p>Enter album title</p>
		<input type="text" name="new_album_title"
			placeholder="enter album title" id="albumTitle" /> <br />
		<p>Select album cover</p>
		<select id="genres" size="1" name="genres" multiple="multiple">


			<c:forEach items="${requestScope.genres}" var="genre">
				<option value="${genre.getGenreId()}"><c:out
						value="${genre.getGenreName()}" /></option>
			</c:forEach>
		</select> <input type="file" name="new_album_cover_photo" id="albumCover"
			accept="image/*" /> <input type="submit" value="Create album"
			onclick="createAlbum()" />
	</div>




	<div id="own_albums">
		<table>
			<c:forEach var="album" items="${requestScope.albums}">
				<tr class="one_album" id="album${album.albumId}"  >   <!-- class="individual_album" AKO IMA NUJDA OT STYLING -->
					<td><img id="cover${album.albumId}" src="/covers/${album.fileName}" height="150" width="150" > 
					<div id="title${album.albumId}">
						<c:out value="${album.albumTitle}" />
					</div>
					</td>
					<td><c:forEach var="album_sound" items="${album.albumTracks}" varStatus="status">
							<div id="sound${album_sound.soundId}">
							<a id="song_from_album" href=""
								onclick="loadJSP('sound?soundId=${album_sound.soundId}')"><c:out
									value="${status.count}" /> : <c:out
									value="${album_sound.soundTitle}" /></a>
							<button id="remove_sound_from_album" value="Remove"
								onclick="deleteSound(${album_sound.soundId},${album.albumId})">Remove</button>
							<br>
							</div>
						</c:forEach></td>
					<td>
						<div id="update_album">
							
							<input type="text" name="update_album_title"
								placeholder="change album title" id="updateAlbumTitle" /> 
						
							<input type="file" name="update_cover_photo"
								id="updateAlbumCover" accept="image/*" /> <input
								type="submit" value="Update album"
								onclick="updateAlbum(${album.albumId})" />
						</div>
					</td>


					<td>
						<button id="album_to_delete" value="Delete"
							onclick="deleteAlbum(${album.albumId})">Delete album</button>
					</td>

				</tr>
			</c:forEach>
		</table>
	</div>



</body>
</html>
