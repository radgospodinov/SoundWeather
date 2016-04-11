<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Single_album</title>

</head>



<body>

	



	<div id="own_albums">
		<table>
			
				<tr class="one_album" id="album${requestScope.single_album.albumId}"  >  
									
					<td>
						<img class="albums_covers" id="cover${requestScope.single_album.albumId}" src="<c:url value="/covers/${requestScope.single_album.fileName}.jpg"/>"> 
						<div class="album_title" id="title${requestScope.single_album.albumId}">
							<b><c:out value="${requestScope.single_album.albumTitle}" /></b>
							<br>
							<b class="by">by</b><b class="sound_autor"
					onclick="loadJSP('otherUser?username=${requestScope.single_album.albumAuthor.username}')"
					style="cursor: pointer"><c:out
							value="${requestScope.single_album.albumAuthor.username}" /></b>
							
							
							<!--  <b><c:out value="${requestScope.single_album.albumAuthor.username}" /></b>-->
						</div>
						
					</td>
					
					<td>
						<c:forEach var="album_sound" items="${requestScope.single_album.albumTracks}" varStatus="status">
							<div id="sound${album_sound.soundId}">
							
								<a id="song_from_album" 
								onclick="loadJSP('sound?soundId=${album_sound.soundId}')"><c:out
									value="${status.count}" /> : <c:out
									value="${album_sound.soundTitle}" /></a>
																	
							</div>
							
						</c:forEach>
						</td>
					
				
				</tr>
					
		</table>
	</div>


</body>
</html>
