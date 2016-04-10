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
			
			
			
			
			function updateAlbum(albumId) {
				
				
				var file = $('#file'+albumId)[0].files[0];
				updateAlbumCover = file;
				name = file.name;
				size = file.size;
				type = file.type;
				if (file.name.length < 1) {
					alert("Please choose album cover");
					return;
				} else if (file.size > 1000000) {
					alert("File is to big");
					return;
				} else if (file.type != 'image/png' && file.type != 'image/jpg'
						&& file.type != 'image/gif'
						&& file.type != 'image/jpeg') {
					alert("File doesnt match png, jpg or gif");
					return;
				}				
				
				if ($('#updateTitle'+albumId).val() == "") {
					alert("please enter valid title");
					$('#updateTitle'+albumId).focus();
					return;
				}
				var form = new FormData();
			
				form.append("albumCover", updateAlbumCover);
				form.append('albumTitle', $('#updateTitle'+albumId).val());
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
							$('#title'+data.id).html(data.newName.bold());
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
			
			$('.update_album_button').on('click', function(e){
				 var albumToUpdate = event.target.id;
			    $("#update_album"+albumToUpdate).toggle(1000);
			    $(this).toggleClass('class1')
			});
			
			$('.create_album_button').on('click', function(e){

			    $("#create_new_album").toggle(1000);
			    $(this).toggleClass('class1')
			});
			
			</script>




</head>


<body>

	<button class="create_album_button" id="create_album_button">Create new album</button>
	<div id="create_new_album">
	
		<p>Enter album title</p>
		<input type="text" name="new_album_title"
			placeholder="enter album title" id="albumTitle" /> <br />
		<p>Select album cover</p>
		<select class="select_album_genres" id="genres" size="1" name="genres" multiple="multiple">


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
					
					
					<td>
						
						<img class="albums_covers" id="cover${album.albumId}" src="<c:url value="/covers/${album.fileName}.jpg"/>"> 
						<div class="album_title" id="title${album.albumId}">
							<b><c:out value="${album.albumTitle}" /></b>
						</div>
						<button id="${album.albumId}" class="update_album_button">Update</button>
						<button id="album_to_delete" value="Delete"
							onclick="deleteAlbum(${album.albumId})">Delete album</button>
					</td>
					
					<td>
						<c:forEach var="album_sound" items="${album.albumTracks}" varStatus="status">
							<div id="sound${album_sound.soundId}">
							
								<a id="song_from_album" 
								onclick="loadJSP('sound?soundId=${album_sound.soundId}')"><c:out
									value="${status.count}" /> : <c:out
									value="${album_sound.soundTitle}" /></a>
								
							<button id="remove_sound_from_album" value="Remove"
								onclick="deleteSound(${album_sound.soundId},${album.albumId})">Remove</button>
								
							</div>
						</c:forEach>
						</td>
						
					<td>
					
						<div class="update_album_form" id="update_album${album.albumId}">
							
							<input class="update_album_title" type="text" name="update_album_title"
								placeholder="change album title" id="updateTitle${album.albumId}" /> 
								
						
							<input type="file" name="update_cover_photo"
								id="file${album.albumId}" accept="image/*" />
								
								 <input
								type="submit" value="Update album"
								onclick="updateAlbum(${album.albumId})" />
						</div>
					</td>


					
				</tr>
			</c:forEach>
		</table>
	</div>



</body>
</html>
