package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.mail.MessagingException;
import javax.security.auth.Subject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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

	private static final int MAX_PASSWORD_LENGTH = 6;
	private static final int MAX_USERNAME_LENGTH = 4;
	private static final String REDIRECT_URL_PARAM = "url";
	private static final String SUBJECT = "Activation at SoundWeather";
	private static final String MAIL_BODY = "Thanks for joining SoundWeather, click on the following link to activate your account.Happy listening! \n\n\n\n" ;

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
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Password doesn't match");
			rv.addProperty("fld", "#pass1");
			return rv.toString();
		}

		if (username.isEmpty() || password1.isEmpty() || password2.isEmpty() || email.isEmpty() || gender.isEmpty()
				|| location.isEmpty()) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Please, fill all the fields.");
			rv.addProperty("fld", "#username");
			return rv.toString();
		}
		if (username.length() < MAX_USERNAME_LENGTH) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Username too short.Username must be at least 4 characters");
			rv.addProperty("fld", "#username");
			return rv.toString();
		}
		if (password1.length() < MAX_PASSWORD_LENGTH) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Password too short.Username must be at least 6 characters");
			rv.addProperty("fld", "#pass1");
			return rv.toString();
		}
		// TODO : strong password validation

		Session session = HibernateUtil.getSession();
		if (session.get(User.class, username) != null) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "There is a user already registered with this username.");
			rv.addProperty("fld", "#username");
			session.close();
			return rv.toString();
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
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Something went wrong when we tried to save you.");
			rv.addProperty("fld", "#username");
			e.printStackTrace();
			return rv.toString();
		} finally {
			session.close();
		}
		new Thread(){
			public void run(){
				try {
					String mailMsg = MAIL_BODY + request.getRequestURL().toString().replace(request.getRequestURI(), request.getContextPath()) + "/emailConfirmation?ecr=" + MailUtil.encryptUsername(username);
					MailUtil.sendMail(email, SUBJECT, mailMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();
		
		// request.getSession().setAttribute("loggedUser", user);
		 rv.addProperty("status", "ok");
		 rv.addProperty("msg", "You have successfully completed the registration.Please check your email to activate your account!");

		return rv.toString();
	}

	@RequestMapping(value = "/updateUser", method = RequestMethod.POST)
	public @ResponseBody String updateUser(MultipartHttpServletRequest request) {
		JsonObject rv = new JsonObject();
		User user = (User) request.getSession().getAttribute("loggedUser");
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
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Password doesn't match");
			rv.addProperty("fld", "#pass1");
			return rv.toString();
		}

		if (!password1.isEmpty() && password1.length() < MAX_PASSWORD_LENGTH) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Password too short.Username must be at least 6 characters");
			rv.addProperty("fld", "#pass1");
			return rv.toString();
		}
		// TODO : strong password validation

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
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Something went wrong when we tried to save you.");
			rv.addProperty("fld", "#username");
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
		request.getSession().setAttribute("loggedUser", user);
		rv.addProperty("status", "ok");
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
		if (user == null || !user.comparePasswords(password)) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Invalid credentials");
			rv.addProperty("fld", "#username");
			return rv.toString();
		}

		rv.addProperty("status", "ok");
		rv.addProperty(REDIRECT_URL_PARAM, url);
		request.getSession().setAttribute("loggedUser", user);
		return rv.toString();
	}

	@RequestMapping(value = "/followUser", method = RequestMethod.POST)
	public @ResponseBody String followUser(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		String targetUserId = request.getParameter("id");
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if (loggedUser == null) {
			rv.addProperty("status", "bad");
			return rv.toString();
		}
		if (targetUserId.equals(loggedUser.getUsername())) {
			rv.addProperty("status", "bad");
			return rv.toString();
		}
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User target = (User) session.get(User.class, targetUserId);
			User current = (User) session.get(User.class, loggedUser.getUsername());
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
			rv.addProperty("status", "bad");
		} finally {
			session.close();
		}
		rv.addProperty("status", "ok");
		return rv.toString();
	}

	@RequestMapping(value = "/unfollowUser", method = RequestMethod.POST)
	public @ResponseBody String unfollowUser(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		String userToUnfollow = request.getParameter("username");
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		
		if (loggedUser == null) {
			rv.addProperty("status", "bad");
			return rv.toString();
		}
		if (userToUnfollow.equals(loggedUser.getUsername())) {
			rv.addProperty("status", "bad");
			return rv.toString();
		}
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User unfollowed = (User) session.get(User.class, userToUnfollow);
			User logged = (User) session.get(User.class, loggedUser.getUsername());
			logged.removeFromFollowing(unfollowed);;
			unfollowed.removeFromFollowers(logged);
			session.update(logged);
			session.update(unfollowed);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty("status", "bad");
		} finally {
			session.close();
		}
		rv.addProperty("status", "ok");
		rv.addProperty("id", "#user"+userToUnfollow);
		return rv.toString();
	}


	private void removeFailedActivations() {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Criteria criteria = session.createCriteria(User.class);
			criteria.add(Restrictions.eq("isActive", false));
			criteria.add(Restrictions.lt("registerDate", new Date().getTime()-20*60*1000));
			List<User> result = criteria.list();
			for(User user : result) {
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
}
