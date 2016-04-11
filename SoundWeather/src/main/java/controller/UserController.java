package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import model.HibernateUtil;
import model.MailUtil;
import model.User;

@Controller
public class UserController {

	private static final String LOGGED_USER = "loggedUser";
	private static final String RESPONSE_GOOD = "ok";
	private static final String RESPONSE_BAD = "bad";
	private static final String RESPONSE_FIELD = "fld";
	private static final String RESPONSE_MSG = "msg";
	private static final String RESPONSE_STATUS = "status";
	private static final int MAX_PASSWORD_LENGTH = 6;
	private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-zA-Z]).{"+ MAX_PASSWORD_LENGTH +",}$";
	private static final int MAX_USERNAME_LENGTH = 4;
	private static final String REDIRECT_URL_PARAM = "url";
	private static final String SUBJECT = "Activation at SoundWeather";
	private static final String MAIL_BODY = "Thanks for joining SoundWeather, click on the following link to activate your account.Happy listening! \n\n\n\n";

	@Autowired
	ServletContext context;

	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody String registerForm(HttpServletRequest request) {

		JsonObject rv = new JsonObject();
		String username = request.getParameter("username");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String birth_month = request.getParameter("birth_month");
		String birth_year = request.getParameter("birth_year");
		String email = request.getParameter("email");
		String location = request.getParameter("location");
		String gender = request.getParameter("gender");

		if (!password1.equals(password2)) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Password doesn't match");
			rv.addProperty(RESPONSE_FIELD, "#pass1");
			return rv.toString();
		}

		if (username.isEmpty() || password1.isEmpty() || password2.isEmpty() || email.isEmpty() || gender.isEmpty()
				|| location.isEmpty()) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Please, fill all the fields.");
			rv.addProperty(RESPONSE_FIELD, "#username");
			return rv.toString();
		}
		if (username.length() < MAX_USERNAME_LENGTH) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Username too short.Username must be at least 4 characters");
			rv.addProperty(RESPONSE_FIELD, "#username");
			return rv.toString();
		}
		
		if (!password1.matches(PASSWORD_REGEX)) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG,
					"Your password must have at least 1 digit and 1 letter must be at least "
							+ MAX_PASSWORD_LENGTH + " chars long!");
			rv.addProperty(RESPONSE_FIELD, "#pass1");
			return rv.toString();
		}

		Session session = HibernateUtil.getSession();
		if (session.get(User.class, username) != null) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "There is a user already registered with this username.");
			rv.addProperty(RESPONSE_FIELD, "#username");
			session.close();
			return rv.toString();
		}
		Criteria criteria = session.createCriteria(User.class);
		criteria.add(Restrictions.eq("email", email)).setProjection(Projections.rowCount());
		List mailCounts = criteria.list();
		if (mailCounts != null) {
			if ((Long) mailCounts.get(0) > 0) {
				rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
				rv.addProperty(RESPONSE_MSG,
						"There is already registered user with that email,please use another one.");
				rv.addProperty(RESPONSE_FIELD, "#email");
				session.close();
				return rv.toString();
			}
		}

		User user = new User(username).setEmail(email).setBirthMonth(birth_month).setBirthYear(birth_year)
				.setGender(gender).setPassword(password1).setLocation(location);

		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(user);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Something went wrong when we tried to save you.");
			rv.addProperty(RESPONSE_FIELD, "#username");
			e.printStackTrace();
			return rv.toString();
		} finally {
			session.close();
		}
		String mailLink = request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath())
				+ "/emailConfirmation?ecr=";
		new Thread() {
			public void run() {
				sendActivationEmail(username, mailLink, email);
			}
		}.start();

		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty(RESPONSE_MSG,
				"You have successfully completed the registration.Please check your email to activate your account!");

		return rv.toString();
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public @ResponseBody String updateUser(MultipartHttpServletRequest request) {
		JsonObject rv = new JsonObject();
		User user = (User) request.getSession().getAttribute(LOGGED_USER);
			
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String email = request.getParameter("email");
		String location = request.getParameter("location");
		String fileName = user.getUsername() + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

		MultipartFile avatarTmp = request.getFile("avatar");
		byte[] avatar = null;
		if (avatarTmp != null && !avatarTmp.isEmpty()) {
			try {
				avatar = avatarTmp.getBytes();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		if (!password1.equals(password2)) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Password doesn't match");
			rv.addProperty(RESPONSE_FIELD, "#pass1");
			return rv.toString();
		}

		if (!password1.isEmpty() && !password1.matches(PASSWORD_REGEX)) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Invalid password.Please enter at least 1 digit and letter and at least "+ MAX_PASSWORD_LENGTH + " chars");
			rv.addProperty(RESPONSE_FIELD, "#pass1");
			return rv.toString();
		}

		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User currentUser = (User) session.get(User.class, user.getUsername());
			if (!password1.isEmpty()) {
				currentUser.setPassword(password1);
			}
			if (!email.isEmpty()) {
				currentUser.setEmail(email);
			}
			if (!location.isEmpty()) {
				currentUser.setLocation(location);
			}
			if (avatarTmp != null && !avatarTmp.isEmpty()) {
				currentUser.setAvatarName(fileName);
			}
			session.update(currentUser);
			user = currentUser;
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Something went wrong when we tried to save you.");
			rv.addProperty(RESPONSE_FIELD, "#username");
			e.printStackTrace();
			return rv.toString();
		} finally {
			session.close();
		}

		if (avatarTmp != null && !avatarTmp.isEmpty()) {

			File avatarFile = new File(context.getRealPath("/static/covers/" + fileName + ".jpg"));
			FileOutputStream fos = null;
			try {
				avatarFile.createNewFile();
				fos = new FileOutputStream(avatarFile);
				fos.write(avatar);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			String img = "data:image/gif;base64," + Base64.encode(avatar);
			rv.addProperty("img", img);
		}
		request.getSession().setAttribute(LOGGED_USER, user);
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty("newLoc", user.getLocation());
		return rv.toString();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody String loginForm(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		removeFailedActivations();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String url = request.getParameter("url");
		Session session = HibernateUtil.getSession();
		System.out.println(username + " , " + password);
		User user = (User) session.get(User.class, username);
		if (user == null || !user.comparePasswords(password) ) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Invalid credentials");
			rv.addProperty(RESPONSE_FIELD, "#username");
			return rv.toString();
		}
		if(!user.isActive()) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Please activate your account. Check your email.");
			rv.addProperty(RESPONSE_FIELD, "#username");
			return rv.toString();
		}

		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty(REDIRECT_URL_PARAM, url);
		request.getSession().setAttribute(LOGGED_USER, user);
		return rv.toString();
	}

	@RequestMapping(value = "/followUser", method = RequestMethod.POST)
	public @ResponseBody String followUser(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		String targetUserId = request.getParameter("id");
		User loggedUser = (User) request.getSession().getAttribute(LOGGED_USER);
		if (loggedUser == null) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		}
		if (targetUserId.equals(loggedUser.getUsername())) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		}
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User target = (User) session.get(User.class, targetUserId);
			User current = (User) session.get(User.class, loggedUser.getUsername());

			if (current.getFollowing().contains(target)) {
				rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
				return rv.toString();
			}

			current.addToFollowing(target);
			target.addToFollowers(current);
			session.update(target);
			session.update(current);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
		} finally {
			session.close();
		}
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		return rv.toString();
	}

	@RequestMapping(value = "/unfollowUser", method = RequestMethod.POST)
	public @ResponseBody String unfollowUser(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		String userToUnfollow = request.getParameter("username");
		User loggedUser = (User) request.getSession().getAttribute(LOGGED_USER);

		if (loggedUser == null) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		}
		if (userToUnfollow.equals(loggedUser.getUsername())) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		}
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User unfollowed = (User) session.get(User.class, userToUnfollow);
			User logged = (User) session.get(User.class, loggedUser.getUsername());
			logged.removeFromFollowing(unfollowed);
			;
			unfollowed.removeFromFollowers(logged);
			session.update(logged);
			session.update(unfollowed);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
		} finally {
			session.close();
		}
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty("id", "#user" + userToUnfollow);
		return rv.toString();
	}

	@RequestMapping(value = "/forgottenPass", method = RequestMethod.POST)
	public @ResponseBody String forgottenPass(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		String email = request.getParameter("email");

		if (email == null || email.isEmpty()) {
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Invalid Email");
			return rv.toString();
		}
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("email", email));
			List<User> results = criteria.list();
			if (results.size() == 0) {
				rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
				rv.addProperty(RESPONSE_MSG, "Invalid Email");
				return rv.toString();
			}
			User user = results.get(0);
			String newPass = generateNewPass();
			new Thread() {
				public void run() {
					sendNewPassMail(newPass, email);
				};
			}.start();

			user.setPassword(newPass);
			session.update(user);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "Internal error, we are sorry!");
			return rv.toString();
		} finally {
			session.close();
		}

		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty(RESPONSE_MSG, "Your new password has been sent successfully!");
		return rv.toString();
	}


	private void sendNewPassMail(String newPass, String email) {
		try {
			MailUtil.sendMail(email, "forgotten pass", "Your new password is:\n" + newPass);
		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

	private void removeFailedActivations() {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			System.out.println("V REMOVE-a SYM");
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("isActive", false));
			criteria.add(Restrictions.lt("registerDate", new Date().getTime() - 20 * 60 * 1000));
			List<User> result = criteria.list();
			System.out.println(result.size());
			for (User user : result) {
				session.delete(user);
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.clear();
		}

	}

	private String generateNewPass() {
		String rv = "";
		int length = 8;
		boolean useLetters = true;
		boolean useNumbers = true;
		rv = RandomStringUtils.random(length, useLetters, useNumbers);

		return rv;
	}

	private void sendActivationEmail(String username, String mailLink, String email) {

		try {
			String mailMsg = MAIL_BODY + mailLink + MailUtil.encryptUsername(username);
			MailUtil.sendMail(email, SUBJECT, mailMsg);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
