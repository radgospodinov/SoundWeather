package controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Projections;
import org.hibernate.service.ServiceRegistry;
import org.neo4j.cypher.internal.compiler.v2_1.commands.expressions.ProjectedPath.singleOutgoingRelationshipProjector;
import org.neo4j.cypher.internal.compiler.v2_1.planner.logical.steps.idSeekLeafPlanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import model.Genre;
import model.HibernateUtil;
import model.Sound;
import model.User;

@Controller
public class MainController {

	private static final int MAX_PASSWORD_LENGTH = 6;
	private static final int MAX_USERNAME_LENGTH = 4;

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
		request.getSession().setAttribute("loggedUser", user);
		rv.addProperty("status", "ok");

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
		if (!avatarTmp.isEmpty()) {
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
			if (!avatarTmp.isEmpty()) {
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
		request.getSession().setAttribute("loggedUser", user);
		rv.addProperty("status", "ok");
		rv.addProperty("img", "/covers/"+fileName);

		return rv.toString();
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody String loginForm(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Session session = HibernateUtil.getSession();
		System.out.println(username + " , " + password);
		User user = (User) session.get(User.class, username);
		session.close();
		if (user == null || !user.comparePasswords(password)) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Invalid credentials");
			rv.addProperty("fld", "#username");
			return rv.toString();
		}
		rv.addProperty("status", "ok");
		request.getSession().setAttribute("loggedUser", user);
		return rv.toString();
	}

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String uploadForm(MultipartHttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		// Get the author = loggedUser:
		User author = (User) request.getSession().getAttribute("loggedUser");
		String fileName = author.getUsername() + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));

		// Get the title:
		String soundTitle = request.getParameter("mp3title");
		System.out.println("mp3title: " + soundTitle);

		// Get the audio file:

		MultipartFile mp3sound = request.getFile("mp3sound");
		byte[] audioFile = mp3sound.getBytes();
		System.out.println("fileType sound: " + mp3sound.getContentType());
		// Get the cover photo:

		MultipartFile mp3cover = request.getFile("mp3cover");
		byte[] coverPhoto = mp3cover.getBytes();
		System.out.println("fileType cover: " + mp3cover.getContentType());

		// Create Sound object:
		Sound newSound = new Sound().setSoundTitle(soundTitle).setFileName(fileName).setSoundAuthor(author);

		// Get the genres and set them to the Sound object:
		ArrayList<Genre> genreDummyObjects = new ArrayList<Genre>();
		String genresTmp = request.getParameter("mp3genres");
		System.out.println("TMP=" + genresTmp);
		String[] genres = genresTmp.split(",");
		System.out.println("array=" + Arrays.toString(genres));
		// TODO: Saving file paths to DB
		// Open hibernate session:
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		JsonObject rv = new JsonObject();
		try {
			tx = session.beginTransaction();

			// Fetch the Genre objects (we need the genre id in order to set the
			// Song object properly):
			// Set the genres to the newSound:
			for (String string : genres) {
				Genre genre = (Genre) session.get(Genre.class, Integer.parseInt(string));
				System.out.println("NAME OF GENRE : " + genre.getGenreName() + " ID: " + genre.getGenreId()
						+ " STRING = " + string);
				newSound.addGenre(genre);
			}
			// Save uploaded sound to DB;
			session.save(newSound);
			User user = (User) session.get(User.class, author.getUsername());
			// Set newSound to user and update it to DB
			user.addSoundToSounds(newSound);
			session.update(user);
			tx.commit();
			request.getSession().setAttribute("loggedUser", user);
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "We couldn't save your file, try again later!");
			return rv.toString();
		} finally {
			session.close();
		}

		// Write the audio byte[] into a file on the hd:

		File soundFile = new File(context.getRealPath("/static/sounds/" + fileName + ".mp3"));
		soundFile.createNewFile();
		FileOutputStream fos = new FileOutputStream(soundFile);
		fos.write(audioFile);

		// Write the picture byte[] into a file on the hd:
		File photoFile = new File(context.getRealPath("/static/covers/" + fileName + ".jpg"));
		photoFile.createNewFile();
		FileOutputStream fos2 = new FileOutputStream(photoFile);
		fos2.write(coverPhoto);

		fos.close();
		fos2.close();
		// is.close();
		rv.addProperty("status", "ok");
		rv.addProperty("msg", "File was saved successfully!");
		// TODO serve error msgs
		return rv.toString();
	}

	@RequestMapping(value = "/likeSound", method = RequestMethod.POST)
	public @ResponseBody String likeSound(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int soundId = Integer.parseInt(request.getParameter("id"));
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Sound sound = (Sound) session.get(Sound.class, soundId);

			User u = (User) request.getSession().getAttribute("loggedUser");

			u = (User) session.get(User.class, u.getUsername());
			for (Sound s : u.getPlaylist()) {
				if (s.getSoundId() == sound.getSoundId()) {
					throw new IllegalArgumentException("Sound already liked");
				}
			}
			sound.setSoundRating(sound.getSoundRating() + 1);

			u.addSoundToLiked(sound);
			session.update(sound);
			session.update(u);
			tx.commit();
			request.getSession().setAttribute("loggedUser", u);
			rv.addProperty("status", "ok");
			rv.addProperty("id", soundId);
			rv.addProperty("likes", sound.getSoundRating());
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty("status", "bad");
			return rv.toString();
		} finally {
			session.close();
		}

		return rv.toString();
	}

	@RequestMapping(value = "/favSound", method = RequestMethod.POST)
	public @ResponseBody String favSound(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int soundId = Integer.parseInt(request.getParameter("id"));
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Sound sound = (Sound) session.get(Sound.class, soundId);
			User u = (User) request.getSession().getAttribute("loggedUser");
			u = (User) session.get(User.class, u.getUsername());
			for (Sound s : u.getFavorites()) {
				if (s.getSoundId() == sound.getSoundId()) {
					throw new IllegalArgumentException("Sound already favourited");
				}
			}
			sound.addFan(u);
			u.addSoundToFavorites(sound);
			session.update(sound);
			session.update(u);
			tx.commit();
			request.getSession().setAttribute("loggedUser", u);
			rv.addProperty("status", "ok");
			rv.addProperty("id", soundId);
			rv.addProperty("likes", sound.getSoundRating());
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty("status", "bad");
			return rv.toString();
		} finally {
			session.close();
		}

		return rv.toString();
	}
}
