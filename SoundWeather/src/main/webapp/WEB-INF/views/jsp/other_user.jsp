<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="<c:url value="/css/other_user.css"/>" />
		<!-- TODO -> MOVE TO INDEX.JSP ALL CSS/JS -->
		<title>Other user</title>
		<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
			
			
			<script>
				$(document).ready(function(){
    			$('#follow').click(function(){
    				     				 
     				 $.post("follow", {
     					user_to_follow : $(this).value,
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
	
		<div id="other_user_profile">
			<table>
   				<tr>
   					<td>
   						<img alt="" src="${requestScope.other_user.coverPhoto}">
   					</td>
   					<td>
   						<c:out value="${requestScope.other_user.username}"/>
   					</td>
   					<td>
   						<button id="follow" value="${requestScope.other_user.username}" >Follow</button>
   					</td>
   				</tr>		
			</table>
		
		
		
		
		
		
		</div>
	
	
	
	
		<div id="other_user_sounds">
			<table>
   				<c:forEach var="sound" items="${requestScope.other_user.sounds}">
   					<tr>
   						<td>
   							<a style="cursor: pointer;" onclick="loadJSP('sound?sound=${sound.soundId}')"><img alt="" src="${sound.soundCoverPhoto}"></a>
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
   							<button id="add_to_favorites" value="${sound.soundId}">Add to favorites</button>
   						</td>
   					</tr>
				</c:forEach>
			</table>
		</div>




	</body>
	
</html>