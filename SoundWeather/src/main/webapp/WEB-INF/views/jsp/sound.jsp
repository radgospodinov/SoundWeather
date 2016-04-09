<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<!-- TODO -> MOVE TO INDEX.JSP ALL CSS/JS -->
		
		
		<title>Other user</title>
			<script type="text/javascript">
					 function createComment(sound_id) {
						 alert(sound_id);
						 var commentBody = document.getElementById('your_comment').value;
	 				
					$('#sounds_space').load('createComment', {soundId : sound_id, comment_body : commentBody});
					};
   			</script>	
			
			
			<script>
			$('#submit_comment').on('click', function () { alert(sound_id);
			 var commentBody = document.getElementById('your_comment').value;
			 var sound_id = document.getElementById('submit_comment').value;
			
			 
				$('#sounds_space').load('createComment', {soundId : sound_id, comment_body : commentBody});
				})
			
			
			
			
				/* $(document).ready(function(){
    			$('#submit_comment').click(function(){
    				     				 
     				 $.post("comment", {
     					comment_body : $('#your_comment').value,
     				});
         			
     				<!--alert($(this).value);-->
         			        			   			
    			});}); */ 
			</script>
			
			
	</head>
	
	<body>
		<div id="sound_properties">
			<table class="sound_table_with_sound_properties">
   				
   					<tr>
   						<td>
   							<img class="sound_page_cover_photo" alt="" src="<c:url value="/covers/${requestScope.sound.fileName}.jpg"/>" height="300" width="300" onclick="addSongs('sounds/${requestScope.sound.getFileName()}.mp3','covers/${requestScope.sound.getFileName()}.jpg','${requestScope.sound.getSoundTitle()}','${requestScope.sound.getSoundAuthor().getUsername()}','')">
   						</td>
   						<td>
   							<b class="sound_page_title" ><c:out value="${requestScope.sound.soundTitle}"/></b>
   						</td>
   					<!--  	<td id="songid">
   							<c:out value="${requestScope.sound.soundId}"/>
   						</td>
   						<td>
   							<c:out value="${requestScope.sound.soundViewCount}"/>
   						</td>
   						<td>
   							<c:out value="${requestScope.sound.soundRating}"/>
   						</td>-->
   						<td>
   							<button id="wLike${requestScope.sound.getSoundId()}" class="like" onclick="like('${requestScope.sound.getSoundId()}')">Like [${requestScope.sound.getSoundRating()}]</button>
							<button id="wFav${requestScope.sound.getSoundId()}" class="like" onclick="fav('${requestScope.sound.getSoundId()}')">Favorite </button>
   						</td>
   						
					</tr>	
			
			</table>
		</div>
		
		<div id="sound_comments">
			<table class="sound_table_with_comments">
   				<h1>Comments about this sound:</h1>
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
				
					<h1>Comment it here:</h1>
				<textarea id="your_comment" class="your_comment"></textarea>
			<!--  	<button id="submit_comment" onclick="createComment('${requestScope.sound.soundId}')">Save comment</button>-->
					<button id="submit_comment" value="${requestScope.sound.soundId}">Save comment</button>
			</div>
			
		</div>



	</body>
	
</html>