<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html PUBLIC>

<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<link rel="stylesheet" type="text/css" href="search.css">
		<title>Search</title>
	</head>
	
	 <script type="text/javascript">
		function getSelectedPage(searchWord, areSounds) {
			var genre =  document.getElementById("genres").value;
			var page = document.getElementById("page").value;
			var areAlbums = document.getElementById("are_albums").value;
			var areUsers = document.getElementById("are_users").value;
			
			
			if(searchWord.length == 0) {
			return;
			}
			
			if(areAlbums != null && areAlbums == true) {
				$('#sounds_space').load('search', {search_word : searchWord, requested_page : page, are_albums : areAlbums, search_genre : genre});

			} else {
				if (areUsers != null && areUsers == true) {
					$('#sounds_space').load('search', {search_word : searchWord, requested_page : page, are_users : areUsers, search_genre : genre});

				} else {
					$('#sounds_space').load('search', {search_word : searchWord, requested_page : page, are_sounds : areSounds, search_genre : genre});

				}
			}
			
			
			
			//$('#sounds_space').load('search', {search_word : searchWord, requested_page : page, are_sounds : areSounds, search_genre : genre});
		};
  	</script>
	
	 <script type="text/javascript">
  		 function getGenreResults(searchWord, areSounds) {
  		 	var genre =  document.getElementById("genres").value;
  		 	var areAlbums = document.getElementById("are_albums").value;
  		 	//alert(genre);
  			//var page = document.getElementById("page").value;
  			if(searchWord.length == 0) {
  				return;
  			}
  			//alert(genre);
  			if (areAlbums != null && areAlbums == true) {
  			
  				$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_albums : areAlbums, search_genre : genre});

  			} else {
  	  			$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_sounds : areSounds, search_genre : genre});

  			}
  			
  			//$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_sounds : areSounds, search_genre : genre});
  		};
    </script>
	
 	<script type="text/javascript">
	 function getUserResults(searchWord) {
		 //var page = document.getElementById("page").value;
	 	if(searchWord.length == 0) {
			return;
		}
	 	//alert(searchWord);
		$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_users : true});
	};
   	</script>
	
	<script type="text/javascript">
	 function getSoundResults(searchWord) {
		// var page = document.getElementById("page").value;
	 	if(searchWord.length == 0) {
			return;
		}
	 	//alert(searchWord);
		$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_sounds : true});
	};
   	</script>
	
	<script type="text/javascript">
	 function getAlbumResults(searchWord) {
		// var page = document.getElementById("page").value;
	 	
		if(searchWord.length == 0) {
			return;
		}
	 	//alert(searchWord);
		$('#sounds_space').load('search', {search_word : searchWord, requested_page : 1, are_albums : true, search_genre : "All"});
	};
   	</script>
	
	
	
	 <script type="text/javascript">
  	 function getUserProfile(username) {
    	 	    		
   	 }
	</script>
	
	
	<!-- Pagination idea:
			
			When the Controller method search() gets the search word
			it gets the results list size and divides it by MAX_NUMBER_OF_RESULTS ( = 9).
			If the modulus is 0 number of pages is number_of_pages = number_of_sounds_result/MAX_NUMBER_OF_RESULTS.
			If modulus is != 0 then number_of_pages = number_of_sounds_result/MAX_NUMBER_OF_RESULTS + 1.
			
			Then we get the first 9 results (page = 1). So the first index of the result List<Sound> is 0, and the last is page-1=8.
			(The formula is: first index = (page-1*MAX_NUMBER_OF_RESULTS) and the end index is (page*MAX_NUMBER_OF_RESULTS).
			
			Then we put this data in the Session ("search_word" = the_serach_word, "number_of_pages" = number_of_pages, "current_page" = page, 
			"result_list" = List<Sound> ) and return search.jsp to display these 9 results.
			
			There we can have the list of all pages... 1 2 3 4 5 6 7 .... When you choose a certain page, then you make a new request
			that takes two parameters - again the search word + the new page number. Then in the search() method we fetch the results
			for the requested page following the above described formula and again return the 'search.jsp' overriding the data in the Session.
			
			The pros: 
				1. The security that we get the actual results (if some user uploads a sound while we display the search results). So
				these are actual results.
				2. We are memory wise
			The cons: 
				1. We make a new request for every page
		
			P.S.: Also we need a boolean flag in the Session to indicate the type of the results: 
			are_sounds = true for Sound results, and are_sounds = false for User results: see the .jsp logic below realised
			with c:choose/c:when/c:otherwise tags.
			This flag is set on the first search() method call.
	 -->
	
	
	
	<body>
	
		
		<div id="filters">
			<h3 id="search_word">RESULTS FOR "<c:out value="${requestScope.search_word}"/>" : <c:out value="${requestScope.number_of_results}"/></h3>
			<button id="sound_results" onclick="getSoundResults('${requestScope.search_word}')">Filter sounds</button>
			<button id="user_results" onclick="getUserResults('${requestScope.search_word}')">Filter users</button>
			<button id="album_results" onclick="getAlbumResults('${requestScope.search_word}')">Filter albums</button>
			<select id="genres" size="1" name="genres" onchange="getGenreResults('${requestScope.search_word}','${requestScope.are_sounds}')">
				<h3>Filter by genre</h3>
				<option value="All" selected><c:out value="All" /></option>
				<c:forEach items="${requestScope.genres}" var="genre"> <!-- Maybe a good idea to init the genres in the application scope... ? -->
					<c:choose>
						<c:when test="${genre.getGenreName().equals(requestScope.genre_filter)}">
							<option value="${genre.getGenreName()}" selected><c:out value="${genre.getGenreName()}" /></option>
						</c:when>
						<c:otherwise>
							<option value="${genre.getGenreName()}"><c:out value="${genre.getGenreName()}" /></option>
						</c:otherwise>
					</c:choose>
					<!--  <option value="${genre.getGenreName()}"><c:out value="${genre.getGenreName()}" /></option>-->
			</c:forEach>
		</select>
		</div>
		
		<input id="are_albums" type="hidden" value="${requestScope.are_albums}">
		<input id="are_users" type="hidden" value="${requestScope.are_users}">
		<!--<c:out value="Albums: ${requestScope.are_albums}"></c:out>
			<c:out value="Sounds: ${requestScope.are_sounds}"></c:out>
				<c:out value="Users: ${requestScope.are_users}"></c:out>-->
		<!-- DEPENDING ON WEATHER WE ARE GETTING Sound OR User RESULT LIST -->
		<c:choose>
		<c:when test="${requestScope.are_sounds == true}">
		
			<div id="search_results">
			
			<table id="results_table">
				<tr id="row_with_results">
					<c:forEach var="result" items="${requestScope.result_list}">
						<td id="one_result">
							<div id="photos"
								onclick="addSongs('sounds/${result.getFileName()}.mp3','covers/${result.getFileName()}.jpg','${result.getSoundTitle()}','${result.getSoundAuthor().getUsername()}','')">
								<img id="sound_cover_photo" alt="Sound cover photo"
									src="<c:url value="/covers/${result.getFileName()}.jpg"/>" height="150" width="150" />
								<img id="play" src="<c:url value="/images/play.png"/>" />
							</div>

							<div id="sound_title">
								<c:out value="${result.getSoundTitle()}" />
							</div>

							<button id="wLike${result.getSoundId()}" class="like" onclick="like('${result.getSoundId()}')">Like [${result.getSoundRating()}]</button>
							<button id="wFav${result.getSoundId()}" class="like" onclick="fav('${result.getSoundId()}')">Favorite </button>
						
						</td>
					</c:forEach>
				</tr>
			</table>
		</div>
			
		</c:when>
		
		<c:otherwise>
		
			<div id="search_results">
			
			<table id="results_table">
				<tr id="row_with_results">
					<c:forEach var="result" items="${requestScope.result_list}">
						<td id="one_result">
							<div id="photos" >
								<a onclick="getUserProfile(${result.username})"><img id="user_cover_photo" alt="User cover photo" src="<c:url value="${result.avatarName}"/>" height="150" width="150" /></a>
							</div>

							<div id="username">
								<c:out value="${result.username}" />
							</div>
							<button id="follow${result.username}" class="like" onclick="follow('${result.username}')">Follow </button>
						
						</td>
					</c:forEach>
				</tr>
			</table>
		</div>
		
	</c:otherwise>
	
	</c:choose>
	
	
	<c:if test="${requestScope.are_albums}">
		<div id="search_results">
			<h3>Albums</h3>
			<table id="results_table">
				<tr id="row_with_results">
					<c:forEach var="result" items="${requestScope.result_list}">
						<td id="one_result">
							<div id="photos" >
								<a onclick="getAlbum(${result.albumId})"><img id="album_cover_photo" alt="Album cover photo" src="<c:url value="${result.albumCover}"/>" height="150" width="150" /></a>
							</div>

							<div id="username">
								<c:out value="${result.albumTitle}" />
							</div>
													
						</td>
					</c:forEach>
				</tr>
			</table>
		</div>
	
	</c:if>
	
	
	
	
	
	<c:if test="${requestScope.result_list.size() != 0}">
	<div id="pages">
			<select id="page" onchange="getSelectedPage('${requestScope.search_word}','${requestScope.are_sounds}')">
				<c:forEach var="page" begin="1" end="${requestScope.number_of_pages}" step="1">
					
					<c:choose>
						<c:when test="${page eq requestScope.current_page}">
							<option selected><c:out value="${page}"></c:out></option>
						</c:when>
						<c:otherwise>
							<option value="${page}"><c:out value="${page}"></c:out></option>
						</c:otherwise>
					</c:choose>
					
					<!--<c:if test="${page eq requestScope.current_page}">
						<option selected><c:out value="${page}"></c:out></option>
					</c:if>					
					<option value="${page}"><c:out value="${page}"></c:out></option>-->
					
				</c:forEach>
				
			</select>
	
	
	
	
	</div>
	</c:if>
	
	
	</body>
	
</html>