<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	
		<title>Following</title>
		<!--  	<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>-->
					
			<script>
				$(document).ready(function(){
    			$("button").click(function(){
    				     				 
     				 $.post("unfollow", {
     					user_id : $(this).value,
     				});
         			
     				<!--alert($(this).value);-->
         			        			   			
    			});});
			</script>
			
	</head>


	<body>
		<div id="following">
			<table class="myProfileTeamNameList">
   				<c:forEach var="followedUser" items="${sessionScope.loggedUser.following}">
   					<tr>
   						<td>
   							<a onclick="loadJSP('register?username=${followedUser.username}')"><img alt="" src="${followedUser.userCoverPhoto}"></a> <!-- TODO: byte[] userCoverPhoto in User class  -->
   						</td>
   						<td>
   							<c:out value="${followedUser.username}"/>
   						</td>
   						<td>
   							<button id="unfollow" value="${followedUser.username}" >Unfollow</button>
   						</td>
   				</c:forEach>
			</table>
		</div>
		

	</body>
	
</html>