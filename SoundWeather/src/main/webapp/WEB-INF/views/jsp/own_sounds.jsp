<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="own_sounds.css">
		<title>Own sounds</title>
		<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
			
			
			<script>
				$(document).ready(function(){
    			$("button").click(function(){
    				     				 
     				 $.post("deleteSound", {
     					sound_id : $(this).value,
     				});
         			
     				<!--alert($(this).value);-->
         			        			   			
    			});});
			</script>
					
	</head>


	<body>
		<div id="own_sounds">
			<table class="myProfileTeamNameList">
   				<c:forEach var="sound" items="${sessionScope.user.sounds}">
   					<tr>
   						<td>
   							<img alt="" src="${sound.soundCoverPhoto}">
   						</td>
   						<td>
   							<c:out value="${sound.soundTitle}"/>
   						</td>
   						<td id=@songid${sound.soundId }Songid>
   							<c:out value="${sound.soundId}"/>
   						</td>
   						<td>
   							<c:out value="${sound.soundViewCount}"/>
   						</td>
   						<td>
   							<c:out value="${sound.soundFans.size()}"/>
   						</td>
   						<td>
   							<button id="sound_id_to_delete" value="${sound.soundId}" onclick="deleteSound()">Delete</button>
   						</td>
   						<td>
   							<select id="add_to_album" name="album" >
								<c:forEach items="${sessionScope.user.albums}" var="album">
   					 				<option value="${album.albumId}"><c:out value="${album.albumTitle}"/></option>
  								</c:forEach>
  							</select>
						</td>
					</tr>	
				</c:forEach>
			</table>
		</div>
		
		

	</body>
</html>
	