package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Hibernate;
import org.hibernate.LockOptions;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

import model.Album;
import model.Genre;
import model.HibernateUtil;
import model.Sound;
import model.User;

@Controller
public class SoundController {

	private static final String RESPONSE_MSG = "msg";
	private static final String RESPONSE_GOOD = "ok";
	private static final String RESPONSE_BAD = "bad";
	private static final String RESPONSE_STATUS = "status";

	private static final String LOGGED_USER = "loggedUser";

	@Autowired
	ServletContext context;

	@RequestMapping(value = "/deleteSound", method = RequestMethod.POST)
	public @ResponseBody String deleteSound(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int id = Integer.parseInt(request.getParameter("id"));
		User user = (User) request.getSession().getAttribute(LOGGED_USER);
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User u = (User) session.get(User.class, user.getUsername());
			Sound sound = (Sound) session.get(Sound.class, id);
			u.removeFromFavorites(sound);
			u.removeSoundFromSounds(sound);
			for (Album album : u.getAlbums()) {
				album.removeSound(sound);
			}
			session.update(u);

			Query query = session.createQuery("from User u where exists(from u.playlist where soundId=" + id
					+ ") or exists(from u.favorites where soundId = " + id + ")");
			List<User> results = query.list();
			for (User usr : results) {
				System.out.println("username:" + usr.getUsername());
				usr.removeSoundFromLiked(sound);
				usr.removeFromFavorites(sound);
				session.update(usr);
			}

			session.delete(sound);
			request.getSession().setAttribute(LOGGED_USER, u);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		} finally {
			session.close();
		}
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty("id", "#s" + id);
		return rv.toString();
	}

	@RequestMapping(value = "/addSoundToAlbum", method = RequestMethod.POST)
	public @ResponseBody String addSoundToAlbum(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int soundId = Integer.parseInt(request.getParameter("soundId"));
		int albumId = Integer.parseInt(request.getParameter("albumId"));

		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Sound sound = (Sound) session.get(Sound.class, soundId);
			Album album = (Album) session.get(Album.class, albumId);
			album.addSound(sound);
			session.update(album);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		} finally {
			session.close();
		}
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		return rv.toString();
	}

	@RequestMapping(value = "/removeSoundFromAlbum", method = RequestMethod.POST)
	public @ResponseBody String removeSoundFromAlbum(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int soundId = Integer.parseInt(request.getParameter("soundId"));
		int albumId = Integer.parseInt(request.getParameter("albumId"));

		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Sound sound = (Sound) session.get(Sound.class, soundId);
			Album album = (Album) session.get(Album.class, albumId);
			album.removeSound(sound);
			session.update(album);
			session.update(sound);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		} finally {
			session.close();
		}
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty("id", "#sound" + soundId);

		return rv.toString();
	}

	@RequestMapping(value = "/removeFromFavorites", method = RequestMethod.POST)
	public @ResponseBody String removeFromFavorites(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int soundId = Integer.parseInt(request.getParameter("sound_Id"));
		for (int i = 0; i < 10; i++) {
			System.out.println(soundId);
		}
		User user = (User) request.getSession().getAttribute(LOGGED_USER);

		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Sound sound = (Sound) session.get(Sound.class, soundId);
			User u = (User) session.get(User.class, user.getUsername());
			Hibernate.initialize(u.getFavorites());
			Hibernate.initialize(sound.getSoundFans());
			u.removeFromFavorites(sound);
			sound.removeFan(u);

			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		} finally {
			session.close();
		}
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty("id", "#sound" + soundId);

		return rv.toString();
	}

	

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody String uploadForm(MultipartHttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {

		JsonObject rv = new JsonObject();
		// Get the author = loggedUser:
		User author = (User) request.getSession().getAttribute(LOGGED_USER);
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
		String genresTmp = request.getParameter("mp3genres");
		String[] genres = genresTmp.split(",");
		// Open hibernate session:
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
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
			request.getSession().setAttribute(LOGGED_USER, user);
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			rv.addProperty(RESPONSE_MSG, "We couldn't save your file, try again later!");
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
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty(RESPONSE_MSG, "File was saved successfully!");
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
			Sound sound = (Sound) session.get(Sound.class, soundId,LockOptions.UPGRADE);

			User u = (User) request.getSession().getAttribute(LOGGED_USER);

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
			request.getSession().setAttribute(LOGGED_USER, u);
			rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
			rv.addProperty("id", soundId);
			rv.addProperty("likes", sound.getSoundRating());
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
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
			User u = (User) request.getSession().getAttribute(LOGGED_USER);
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
			request.getSession().setAttribute(LOGGED_USER, u);
			rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
			rv.addProperty("id", soundId);
			rv.addProperty("likes", sound.getSoundRating());
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
			return rv.toString();
		} finally {
			session.close();
		}

		return rv.toString();
	}

}
