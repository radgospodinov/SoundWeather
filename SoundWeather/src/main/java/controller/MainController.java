package controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Example;
import org.hibernate.service.ServiceRegistry;
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
import model.Sound;
import model.User;

@Controller
public class MainController {
	@Autowired
	ServletContext context;

	private Session getSession() {
		if (context.getAttribute("sf") == null) {
			Configuration configuration = new Configuration();
			configuration.configure("hibernate.cfg.xml");
			ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
					.applySettings(configuration.getProperties()).build();
			SessionFactory sFactory = new Configuration().configure().buildSessionFactory(serviceRegistry);
			context.setAttribute("sf", sFactory);
		}
		return ((SessionFactory) context.getAttribute("sf")).openSession();
	}

	
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public @ResponseBody String registerForm(HttpServletRequest request) {

		String username = request.getParameter("username");
		String password1 = request.getParameter("password1");
		String password2 = request.getParameter("password2");
		String birth_month = request.getParameter("birth_month");
		String birth_year = request.getParameter("birth_year");
		String email = request.getParameter("email");
		String location = request.getParameter("location");
		String gender = request.getParameter("gender");

		JsonObject rv = new JsonObject();
		if (!password1.equals(password2)) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Password doesn't match");
			rv.addProperty("fld", "#pass1");
			return rv.toString();
		}

		if (username.isEmpty() || password1.isEmpty() || password2.isEmpty() || email.isEmpty() || gender.isEmpty()) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Please, fill all the fields.");
			rv.addProperty("fld", "#username");
			return rv.toString();
		}
		if (username.length() <= 3) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Username too short.Username must be at least 4 characters");
			rv.addProperty("fld", "#username");
			return rv.toString();
		}
		if (password1.length() < 6) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "Password too short.Username must be at least 6 characters");
			rv.addProperty("fld", "#pass1");
			return rv.toString();
		}
		// TODO : strong password validation

		Session session = getSession();
		if (session.get(User.class, username) != null) {
			rv.addProperty("status", "bad");
			rv.addProperty("msg", "There is a user already registered with this username.");
			rv.addProperty("fld", "#username");
			session.close();
			return rv.toString();
		}
		User user = new User(username).setEmail(email).setBirthMonth(birth_month).setBirthYear(birth_year)
				.setGender(gender).setPassword(password1);

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

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public @ResponseBody String loginForm(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		Session session = getSession();
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
		// @RequestParam("mp3sound") MultipartFile mpfMp3Sound,
		// @RequestParam("mp3cover") MultipartFile mpfMp3Cover,
		// @RequestParam("mp3title") String mp3title,
		// HttpServletRequest request

		// Get the author = loggedUser:
		User author = (User) request.getSession().getAttribute("loggedUser");

		// Get the title:
		String soundTitle = request.getParameter("mp3title");
		System.out.println("mp3title: " + soundTitle);

		// Get the audio file:
		MultipartFile mp3sound = request.getFile("mp3sound");
		MultipartFile mp3cover = request.getFile("mp3cover");

		byte[] audioFile = mp3sound.getBytes();

		System.out.println("fileType sound: " + mp3sound.getContentType());
		System.out.println("fileType cover: " + mp3cover.getContentType());
		String fileName = author.getUsername() + "_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
		// Get the cover photo:

		byte[] coverPhoto = mp3cover.getBytes();

		// Create Sound object:
		Sound newSound = new Sound().setSoundTitle(soundTitle).setAudioFile(audioFile).setSoundCoverPhoto(coverPhoto)
				.setSoundAuthor(author);

		// Get the genres and set them to the Sound object:
		ArrayList<Genre> genreDummyObjects = new ArrayList<Genre>();
		String[] genres = request.getParameterValues("mp3genres");

		System.out.println(Arrays.toString(genres));

		if (genres != null) {
			for (int i = 0; i < genres.length; i++) {
				String genre = genres[i];
				Genre dummyGenre = new Genre().setGenreName(genre);
				genreDummyObjects.add(dummyGenre);
			}

		}
		// TODO: Saving file paths to DB 
		// // Open hibernate session:
		// Session session = getSession();
		// Transaction tx = null;
		// try {
		// tx = session.beginTransaction();
		//
		// // Fetch the Genre objects (we need the genre id in order to set the
		// // Song object properly):
		// Criteria criteria = session.createCriteria(Genre.class);
		// for (Genre gnr : genreDummyObjects) {
		// Example example = Example.create(gnr);
		// criteria.add(example);
		// }
		//
		// List<Genre> fetchedGenres = (List<Genre>) criteria.list();
		//
		// // Set the genres to the newSound:
		// newSound.getSoundGenres().addAll(fetchedGenres);
		//
		// // Hopefully save the newSound object:
		// session.save(newSound);
		//
		// // DO WE NEED TO ADD THE NEWSOUND TO THE AUTHOR?... hmmm?
		//
		// tx.commit();
		// } catch (Exception e) {
		// if (tx != null) {
		// tx.rollback();
		// }
		// } finally {
		// session.close();
		// }

		// Write the audio byte[] into a file on the hd:
		String relativeWebPath = "/static/css/home.css";
		String absoluteDiskPath = context.getRealPath(relativeWebPath);
		System.out.println(absoluteDiskPath);
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
		JsonObject rv = new JsonObject();
		rv.addProperty("status", "ok");
		rv.addProperty("msg", "File was saved successfully!");
		// TODO serve error msgs
		return rv.toString();
	}

}
