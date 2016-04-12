<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC>

<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Search</title>

</head>

<script type="text/javascript">
		function getSelectedPage(searchWord, areSounds) {
			var genre =  document.getElementById("filter_genres").value;
			var page = document.getElementById("page").value;
			var areAlbums = document.getElementById("are_albums").value;
			var areUsers = document.getElementById("are_users").value;
			
			
			if(searchWord.length == 0) {
			return;
			}
			
			if(areAlbums) {
				$('#sounds_space').load('search', {search_word : searchWord, requested_page : page, are_albums : areAlbums, search_genre : genre});

			} else {
				if (areUsers) {
					$('#sounds_space').load('search', {search_word : searchWord, requested_page : page, are_users : areUsers, search_genre : genre});

				} else {
					
					$('#sounds_space').load('search', {search_word : searchWord, requested_page : page, are_sounds : areSounds, search_genre : genre});

				}
			}
		
		};
  	</script>
<script>
	 function follow(userId) {
		 $.post({
				url : "followUser",
				data : {
					id : userId
				},
				dataType : "json",
				success : function(data, textStatus, jqXHR) {
					if (data.status == 'ok') {
						
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					alert("Something really bad happened " + textStatus + " - "
							+ errorThrown);
				}
			});
	 }
</script>
<script type="text/javascript">
  		 function getGenreResults(searchWord, areSounds) {
  		 	var genre =  document.getElementById("filter_genres").value;
  		 	var areAlbums = document.getElementById("are_albums").value;
  		 
  			if(searchWord.length == 0) {
  				return;
  			}
  		
  			if (areAlbums) {
  			
  				$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_albums : areAlbums, search_genre : genre});

  			} else {
  	  		
  				$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_sounds : areSounds, search_genre : genre});

  			}
  			  			
  		};
</script>

<script type="text/javascript">
	 function getUserResults(searchWord) {
	
	 	if(searchWord.length == 0) {
			return;
		}
	 
		$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_users : true});
	};
</script>

<script type="text/javascript">
	 function getSoundResults(searchWord) {
	
	 	if(searchWord.length == 0) {
			return;
		}
	
		$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_sounds : true});
	};
</script>

<script type="text/javascript">
	 function getAlbumResults(searchWord) {
		 	
		if(searchWord.length == 0) {
			return;
		}
	
		$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_albums : true, search_genre : "All"});
	};
</script>


<body>

	<div id="filters">
		<h3 id="search_word">
			RESULTS FOR "
			<c:out value="${requestScope.search_word}" />
			" :
			<c:out value="${requestScope.number_of_results}" />

			<c:if
				test="${requestScope.are_users == true && (requestScope.number_of_results > 1 || requestScope.number_of_results == 0)}">
				<b class="type_of_result">users</b>
			</c:if>
			<c:if
				test="${requestScope.are_users == true && requestScope.number_of_results == 1}">
				<b class="type_of_result">user</b>
			</c:if>
			<c:if
				test="${requestScope.are_sounds == true && (requestScope.number_of_results > 1 || requestScope.number_of_results == 0)}">
				<b class="type_of_result">sounds</b>
			</c:if>
			<c:if
				test="${requestScope.are_sounds == true && requestScope.number_of_results == 1}">
				<b class="type_of_result">sound</b>
			</c:if>
			<c:if
				test="${requestScope.are_albums == true &&  (requestScope.number_of_results > 1 || requestScope.number_of_results == 0)}">
				<b class="type_of_result">albums</b>
			</c:if>
			<c:if
				test="${requestScope.are_albums == true && requestScope.number_of_results == 1}">
				<b class="type_of_result">album</b>
			</c:if>

		</h3>

		<button id="user_results"
			onclick="getUserResults('${requestScope.search_word}')">Filter
			users</button>
		<button id="album_results"
			onclick="getAlbumResults('${requestScope.search_word}')">Filter
			albums</button>
		<button id="sound_results"
			onclick="getSoundResults('${requestScope.search_word}')">Filter
			sounds</button>
		<select id="filter_genres" size="1" name="genres"
			onchange="getGenreResults('${requestScope.search_word}','${requestScope.are_sounds}')">
			<h3>Filter by genre</h3>
			<option value="All" selected><c:out value="All" /></option>
			<c:forEach items="${requestScope.genres}" var="genre">
			
				<c:choose>
					<c:when
						test="${genre.getGenreName().equals(requestScope.genre_filter)}">
						<option value="${genre.getGenreName()}" selected><c:out
								value="${genre.getGenreName()}" /></option>
					</c:when>
					<c:otherwise>
						<option value="${genre.getGenreName()}"><c:out
								value="${genre.getGenreName()}" /></option>
					</c:otherwise>
				</c:choose>
				
			</c:forEach>
		</select>
	</div>

	<input id="are_albums" type="hidden" value="${requestScope.are_albums}">
	<input id="are_users" type="hidden" value="${requestScope.are_users}">
	
	<c:choose>
		<c:when test="${requestScope.are_sounds == true}">

			<div id="sound_search_results">

				<table id="results_table">
					<tr id="row_with_results">
						<c:forEach var="result" items="${requestScope.result_list}">
							<td id="one_result">
								<div id="photos"
									onclick="addSongs('sounds/${result.getFileName()}.mp3','covers/${result.getFileName()}.jpg','${result.getSoundTitle()}','${result.getSoundAuthor().getUsername()}','')">
									<img id="sound_cover_photo" alt="Sound cover photo"
										src="<c:url value="/covers/${result.getFileName()}.jpg"/>"
										height="150" width="150" /> <img id="play"
										src="<c:url value="/images/play.png"/>" />
								</div>

								<div id="sound_title">
									<b class="sound_title_in_search"
										onclick="loadJSP('sound?soundId=${result.soundId}')"><c:out
											value="${result.getSoundTitle()}" /></b>
								</div>
									<c:if test="${sessionScope.loggedUser != null}">
								<button id="wLike${result.getSoundId()}" class="like"
									onclick="like('${result.getSoundId()}')">Like
									[${result.getSoundRating()}]</button>
								<button id="wFav${result.getSoundId()}" class="like"
									onclick="fav('${result.getSoundId()}')">Favorite</button>
								</c:if>
							</td>
						</c:forEach>
					</tr>
				</table>
			</div>

		</c:when>

		<c:otherwise>

			<c:if test="${requestScope.are_users == true}">
				<div id="search_results">

					<table id="user_results_table">
						<tr id="row_with_results">
							<c:forEach var="result" items="${requestScope.result_list}">
								<td id="one_result">
									
									<c:if test="${sessionScope.loggedUser.username ne result.username}">
									<div class="user_serch_photos" id="photos">
										<a onclick="loadJSP('otherUser?username=${result.username}')"
											style="cursor: pointer"><img id="user_cover_photo"
											alt="User cover photo"
											src="<c:url value="/covers/${result.avatarName}.jpg"/>"
											height="150" width="150" /></a>
									</div>
									</c:if>
									<c:if test="${sessionScope.loggedUser.username eq result.username}">
									<div class="user_serch_photos" id="photos">
										<a onclick="loadJSP('profile')"
											style="cursor: pointer"><img id="user_cover_photo"
											alt="User cover photo"
											src="<c:url value="/covers/${result.avatarName}.jpg"/>"
											height="150" width="150" /></a>
									</div>
									</c:if>
									
									
									
									
									
									
									
									<div id="username_in_search">
										<b class="username_in_search"><c:out
												value="${result.username}" /></b>
									</div>
										<c:if test="${sessionScope.loggedUser != null && sessionScope.loggedUser.username ne result.username}">
									<button id="follow${result.username}" class="like"
										onclick="follow('${result.username}')">Follow</button>
										</c:if>
											<c:if test="${sessionScope.loggedUser.username eq result.username}">
									<div class="like" id="loggeduser_follow"
										style=opacity:0; style=cursor:none;></div>
										</c:if>
								</td>
							</c:forEach>
						</tr>
					</table>
				</div>
			</c:if>
		</c:otherwise>

	</c:choose>


	<c:if test="${requestScope.are_albums}">
		<div id="search_results">

			<table id="album_results_table">
				<tr id="row_with_results">
					<c:forEach var="result" items="${requestScope.result_list}">
						<td id="one_result">
							<div id="photos">
								<a onclick="loadJSP('getAlbum?requested_album_id=${result.albumId}')"><img
									id="album_cover_photo" alt="Album cover photo"
									src="<c:url value="/covers/${result.fileName}.jpg"/>"
									height="150" width="150" /></a>
							</div>

							<div id="album_name_in_search">
								<b class="album_title_in_search"><c:out
										value="${result.albumTitle}" /></b>
							</div>

						</td>
					</c:forEach>
				</tr>
			</table>
		</div>

	</c:if>


	<c:if test="${requestScope.result_list.size() != 0}">
		<div id="pages">
			<select id="page"
				onchange="getSelectedPage('${requestScope.search_word}','${requestScope.are_sounds}')">
				<c:forEach var="page" begin="1"
					end="${requestScope.number_of_pages}" step="1">

					<c:choose>
						<c:when test="${page eq requestScope.current_page}">
							<option selected><c:out value="${page}"></c:out></option>
						</c:when>
						<c:otherwise>
							<option value="${page}"><c:out value="${page}"></c:out></option>
						</c:otherwise>
					</c:choose>

				</c:forEach>

			</select>

		</div>
	</c:if>


</body>
</html>