<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<!-- TODO -> MOVE TO INDEX.JSP ALL CSS/JS -->
		
		
		<title>Other user</title>
		
			
			
			<script>
				$(document).ready(function(){
    			$('#submit_comment').click(function(){
    				     				 
     				 $.post("comment", {
     					comment_body : $('#your_comment').value,
     				});
         			
     				<!--alert($(this).value);-->
         			        			   			
    			});});
			</script>
			
				<script>
				$(document).ready(function(){
    			$('#add_to_favorites').click(function(){
    				     				 
     				 $.post("addToFavorites", {
     					song_to_favorites : $(this).value,
     				});
         			
     				<!--alert($(this).value);-->
         			        			   			
    			});});
			</script>
	</head>
	
	<body>
		<div id="sound_properties">
			<table >
   				
   					<tr>
   						<td>
   							<img alt="" src="<c:url value="/covers/${requestScope.sound.fileName}.jpg"/>">
   						</td>
   						<td>
   							<c:out value="${requestScope.sound.soundTitle}"/>
   						</td>
   						<td id="songid">
   							<c:out value="${requestScope.sound.soundId}"/>
   						</td>
   						<td>
   							<c:out value="${requestScope.sound.soundViewCount}"/>
   						</td>
   						<td>
   							<c:out value="${requestScope.sound.soundRating}"/>
   						</td>
   						<td>
   							<button id="add_to_favorites" value="${requestScope.sound.soundId}" >Add to favorites</button>
   						</td>
   						
					</tr>	
			
			</table>
		</div>
		
		<div id="sound_comments">
			<table >
   				<c:forEach var="comment" items="${requestScope.sound.soundComments}">
   					<tr>
   						<td>
   							<a onclick=""><img alt="" src="<c:url value="/covers/${comment.commentAuthor.avatarName}.jpg"/>"></a>
   						</td>
   						<td>
   							<c:out value="${comment.commentPostingDateTime}"/>
   						</td>
   						<td >
   							<c:out value="${comment.commentAuthor.username}"/>
   						</td>
   						<td >
   							<c:out value="${comment.commentBody}"/>
   						</td>
   					</tr>	
				</c:forEach>
			</table>
			<div id="write_comment">
				<form>
				<textarea id="your_comment" rows="" cols="" onsubmit=""></textarea>
				<input id="submit_comment" type="submit" value="Save comment" >
				</form>
			</div>
			
		</div>



	</body>
	
</html>