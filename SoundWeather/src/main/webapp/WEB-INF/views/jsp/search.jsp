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

  	 function getSelectedPage(searchWord, areSounds, searchGenre) {
    		var selectBox = document.getElementById("page");
    		var page = selectBox.options[selectBox.selectedIndex].value;
			
    		
    		$.ajax({
    			url : 'search',
    			data : 
    				{
    				requested_page : page,
    				search_word : searchWord,
    				are_sounds = areSounds,
    				search_genre : searchGenre} 
    		//if search_genre == null it means that we are requesting User results or generic Sound result 
    		//(depending on the value of are_sounds)
    				type : 'POST',
    				dataType : 'json'
    					,
    			success : 
    				
    				
    					,
    			error : 
    		});
    		
   	 }

  </script>
	
	 <script type="text/javascript">

  	 function getGenreResults(searchWord, areSounds)) {
    		   		
    		$.ajax({
    			url : 'search',
    			data : 
    				{
    				requested_page : 1,
    				search_word : searchWord,
    				are_sounds = areSounds}
    				type : 'POST',
    				dataType : 'json'
    					,
    			success : 
    				
    				
    					,
    			error : 
    		});
    		
   	 }

  </script>
	
	

 <script type="text/javascript">

  	 function getUserResults(searchWord, areSounds)) {
    		   		
    		$.ajax({
    			url : 'search',
    			data : 
    				{
    				requested_page : 1,
    				search_word : searchWord,
    				are_sounds = areSounds}
    				type : 'POST',
    				dataType : 'json'
    					,
    			success : 
    				
    				
    					,
    			error : 
    		});
    		
   	 }

  </script>
	
	 <script type="text/javascript">

  	 function getUserProfile(username) {
    		   		
    		$.ajax({
    			url : 'getUser',
    			data : 
    				{
    				user_name : username}
    				type : 'POST',
    				dataType : 'json'
    					,
    			success : 
    				
    				
    					,
    			error : 
    		});
    		
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
			<h3 id="search_word">Results for "<c:out value="${sessionScope.search_word}.jpg"/>"</h3>
			<button id="user_results" onclick="getUserResults('${sessionScope.search_word}','${sessionScope.are_sounds}')">Filter by users</button>
			<h4>Filter by genre</h4>
			<select id="genres" size="1" name="genres" onchange="getGenreResults('${sessionScope.search_word}','${sessionScope.are_sounds}')">
				<c:forEach items="${applicationScope.genres}" var="genre"> <!-- Maybe a good idea to init the genres in the application scope... ? -->
					<option value="${genre.getGenreId()}"><c:out value="${genre.getGenreName()}" /></option>
			</c:forEach>
		</select>
		</div>
		
		
		
		<!-- DEPENDING ON WEATHER WE ARE GETTING Sound OR User RESULT LIST -->
		<c:choose>
		<c:when test="${sessionScope.are_sounds == true}">
		
			<div id="search_results">
			
			<table id="results_table">
				<tr id="row_with_results">
					<c:forEach var="result" items="${sessionScope.result_list}">
						<td id="one_result">
							<div id="photos"
								onclick="addSongs('sounds/${result.getFileName()}.mp3','covers/${result.getFileName()}.jpg','${result.getSoundTitle()}','${result.getSoundAuthor().getUsername()}','')">
								<img id="sound_cover_photo" alt="Sound cover photo"
									src="<c:url value="/covers/${result.getFileName()}.jpg"/>" height="150" width="150" />
								<img id="play" src="<c:url value="/images/play.png"/>" />
							</div>

							<div id="sound_title">
								<c:out value="Sound Title ${result.getSoundTitle()}" />
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
					<c:forEach var="result" items="${sessionScope.result_list}">
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
	
	<div id="pages">
			<select id="page" onchange="getSelectedPage('${sessionScope.search_word}','${sessionScope.are_sounds}','${sessionScope.search_genre})">
				<c:forEach var="page" begin="1" end="${sessionScope.number_of_pages}" step="1">
					<c:if test="${page == sessionScope.current_page}">
						<option value="page" selected><c:out value="page"></c:out></option>
					</c:if>
					
					<option value="page"><c:out value="page"></c:out></option>
					
				</c:forEach>
				
			</select>
	
	
	
	
	</div>
	
	
	
	</body>
	
</html>