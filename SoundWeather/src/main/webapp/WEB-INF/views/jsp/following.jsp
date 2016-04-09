<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Following</title>
<!--  	<script src="//cdnjs.cloudflare.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>-->

<script>
	$(document).ready(function() {
		$("button").click(function() {

			$.post("unfollow", {
				user_id : $(this).value,
			});

			<!--alert($(this).value);
			-->

		});
	});
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
				<tr>
					<td><img class="following_avatar"
						style=cursor:pointer
						src="<c:url value="/covers/${followedUser.avatarName}.jpg"/>"
						height="150" width="150"
						onclick="loadJSP('otherUser?username=${followedUser.username}')">
					</td>
					<td><b class="following_username"><c:out value="${followedUser.username}" /></b></td>
					<td>
						<button id="unfollow" value="${followedUser.username}">Unfollow</button>
					</td>
			</c:forEach>
		</table>
	</div>


</body>

</html>