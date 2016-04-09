<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html >
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css"
	href="<c:url value="/css/register.css"/>" />
<!-- TODO -> MOVE TO INDEX.JSP ALL CSS/JS -->
<title>Register</title>



<script>
	function checkPass() {

		var pass1 = document.getElementById('pass1');
		var pass2 = document.getElementById('pass2');
		var message = document.getElementById('match_message');

		var goodColor = "#66cc66";
		var badColor = "#ff6666";

		if (pass1.value == pass2.value) {
			pass2.style.backgroundColor = goodColor;
			message.style.color = goodColor;
			message.innerHTML = "Passwords match!"
		} else {
			pass2.style.backgroundColor = badColor;
			message.style.color = badColor;
			message.innerHTML = "Passwords do not match!"
		}
	}
</script>
<script>
	function register() {
		$.post({
			url : "register",
			data : {
				username : $("#username").val(),
				password1 : $("#pass1").val(),
				password2 : $("#pass2").val(),
				birth_month : $("#birth_month").val(),
				birth_year : $("#birth_year").val(),
				email : $("#email").val(),
				location : $("#location").val(),
				gender : $("#gender").val()
			},
			dataType : "json",
			success : function(data, textStatus, jqXHR) {
				if (data.status == 'ok') {
					$('#registerHeader').hide();
					$('#loginHeader').hide();
					$('#logoutHeader').show();
					loadJSP('home');
				} else {
					$("#login_message").text(data.msg);
					$("#login_message").show();
					$(data.fld).focus();

				}

			},
			error : function(jqXHR, textStatus, errorThrown) {
				alert("Something really bad happened " + textStatus + " - "
						+ errorThrown);
			}
		});

		<!--alert(document.getElementById('username').value);
		-->

	};
</script>

<script>
	function validateEmail() {
		var mail = document.getElementById('email');
		var mailformat = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
		var goodColor = "#66cc66";
		var badColor = "#ff6666";
		if (mail.value.match(mailformat)) {
			mail.style.backgroundColor = goodColor;
		} else {
			mail.style.backgroundColor = badColor;
		}
	}
</script>

</head>


<body id="register_space">

	<div id="login_notification">

		<h5 id="login_message" style="display: none;color:#ff6666 ">There is a user
			already registered with this username. /// Please, fill all the
			fields.</h5>

	</div>

	<div id="form">

	<input id="username" type="text" name="username"
		placeholder="choose username" required />
	<br />
	<input id="pass1" type="password" name="password1"
		placeholder="choose password" onkeyup="checkPass(); return false;"
		required />
	<br />
	<input id="pass2" type="password" name="password2"
		placeholder="re-enter password" onkeyup="checkPass(); return false;"
		required />
	<br />
	<input id="email" type="text" name="email" placeholder="enter emaild"
		onkeyup="validateEmail(); return false;" required />
	<br />
	<input id="location" type="text" name="location"
		placeholder="enter location" required />
	<br />
	  <!--<p>Select your gender:</p>-->
	<select id="gender" name="gender">
		<option value="Male" selected>Male</option>
		<option value="Female">Female</option>
		<option value="Other">Other</option>
	</select>
	<br />
	<!--<p>Select your birth month:</p>-->
	<select id="birth_month" name="birth_month">
		<option value="January" selected>January</option>
		<option value="February">February</option>
		<option value="March">March</option>
		<option value="April">April</option>
		<option value="May">May</option>
		<option value="June">June</option>
		<option value="July">July</option>
		<option value="August">August</option>
		<option value="September">September</option>
		<option value="October">October</option>
		<option value="November">November</option>
		<option value="December">December</option>
	</select>
	<br />
	<!--<p>Select your birth year:</p>-->
	<select id="birth_year" name="birth_year">
		<option value="2016">2016</option>
		<option value="2015">2015</option>
		<option value="2014">2014</option>
		<option value="2013">2013</option>
		<option value="2012">2012</option>
		<option value="2011">2011</option>
		<option value="2010">2010</option>
		<option value="2009">2009</option>
		<option value="2008">2008</option>
		<option value="2007">2007</option>
		<option value="2006">2006</option>
		<option value="2005">2005</option>
		<option value="2004">2004</option>
		<option value="2003">2003</option>
		<option value="2002">2002</option>
		<option value="2001">2001</option>
		<option value="2000">2000</option>
		<option value="1999">1999</option>
		<option value="1998">1998</option>
		<option value="1997">1997</option>
		<option value="1996">1996</option>
		<option value="1995">1995</option>
		<option value="1994">1994</option>
		<option value="1993">1993</option>
		<option value="1992">1992</option>
		<option value="1991">1991</option>
		<option value="1990">1990</option>
		<option value="1989">1989</option>
		<option value="1988">1988</option>
		<option value="1987">1987</option>
		<option value="1986">1986</option>
		<option value="1985">1985</option>
		<option value="1984">1984</option>
		<option value="1983">1983</option>
		<option value="1982">1982</option>
		<option value="1981">1981</option>
		<option value="1980">1980</option>
		<option value="1979">1979</option>
		<option value="1978">1978</option>
		<option value="1977">1977</option>
		<option value="1976">1976</option>
		<option value="1975">1975</option>
		<option value="1974">1974</option>
		<option value="1973">1973</option>
		<option value="1972">1972</option>
		<option value="1971">1971</option>
		<option value="1970">1970</option>
		<option value="1969">1969</option>
		<option value="1968">1968</option>
		<option value="1967">1967</option>
		<option value="1966">1966</option>
		<option value="1965">1965</option>
		<option value="1964">1964</option>
		<option value="1963">1963</option>
		<option value="1962">1962</option>
		<option value="1961">1961</option>
		<option value="1960">1960</option>
		<option value="1959">1959</option>
		<option value="1958">1958</option>
		<option value="1957">1957</option>
		<option value="1956">1956</option>
		<option value="1955">1955</option>
		<option value="1954">1954</option>
		<option value="1953">1953</option>
		<option value="1952">1952</option>
		<option value="1951">1951</option>
		<option value="1950">1950</option>
		<option value="1949">1949</option>
		<option value="1948">1948</option>
		<option value="1947">1947</option>
		<option value="1946">1946</option>
		<option value="1945">1945</option>
		<option value="1944">1944</option>
		<option value="1943">1943</option>
		<option value="1942">1942</option>
		<option value="1941">1941</option>
		<option value="1940">1940</option>
		<option value="1939">1939</option>
		<option value="1938">1938</option>
		<option value="1937">1937</option>
		<option value="1936">1936</option>
		<option value="1935">1935</option>
		<option value="1934">1934</option>
		<option value="1933">1933</option>
		<option value="1932">1932</option>
		<option value="1931">1931</option>
		<option value="1930">1930</option>
		<option value="1929">1929</option>
		<option value="1928">1928</option>
		<option value="1927">1927</option>
		<option value="1926">1926</option>
		<option value="1925">1925</option>
		<option value="1924">1924</option>
		<option value="1923">1923</option>
		<option value="1922">1922</option>
		<option value="1921">1921</option>
		<option value="1920">1920</option>
		<option value="1919">1919</option>
		<option value="1918">1918</option>
		<option value="1917">1917</option>
		<option value="1916">1916</option>
	</select>
	<br/>
	<input type="submit" value="Register" onclick="register()" />
	<br />
	<!--<span id="match_message"></span>-->
	</div>

</body>




</html>