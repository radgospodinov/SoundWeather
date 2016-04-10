<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Following</title>


<script>
		$(".unfollow_user").on('click', function unfollowUser(e) {
			// alert(event.target.id);
			
			 var username = event.target.id;
								
					$.post({
					url : "unfollowUser",
					
					data : {
						
						username : username
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
				
			})
			
</script>
 <script type="text/javascript">
$(document).ready(function() {
    $('#following_table').DataTable( {
         scrollY :        '200px',
         scrollCollapse : true,
         paging :         false
    } );
} );

</script>


</head>


<body>
	<div class="following_block" id="following">
		<table id="following_table" class="following_table">
			<c:forEach var="followedUser" items="${requestScope.following}">
				<tr id="user${followedUser.username}">
					<td><img class="following_avatar"
						style=cursor:pointer
						src="<c:url value="/covers/${followedUser.avatarName}.jpg"/>"
						height="150" width="150"
						onclick="loadJSP('otherUser?username=${followedUser.username}')">
					</td>
					<td><b class="following_username"><c:out value="${followedUser.username}" /></b></td>
					<td>
						<button class="unfollow_user" id="${followedUser.username}" value="${followedUser.username}">Unfollow</button>
					</td>
					</tr>
			</c:forEach>
		</table>
	</div>


</body>

</html>