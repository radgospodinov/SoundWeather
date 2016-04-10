package controller;

import java.awt.event.FocusAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.neo4j.cypher.internal.compiler.v2_1.ast.rewriters.flattenBooleanOperators;
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

	@Autowired
	ServletContext context;

	@RequestMapping(value = "/deleteSound", method = RequestMethod.POST)
	public @ResponseBody String deleteSound(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int id = Integer.parseInt(request.getParameter("id"));
		User user = (User) request.getSession().getAttribute("loggedUser");
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
			System.out.println("update user");
			session.update(u);
			// wokring slow
			
//			Criteria criteria = session.createCriteria(User.class);
//			criteria.createAlias("playlist", "likes").add(Restrictions.eq("likes.soundId", id));
//			List<User> results = criteria.list();
//			System.out.println("soundId:" + id);
//			for (User usr : results) {
//				System.out.println("username:" + usr.getUsername());
//				usr.removeSoundFromLiked(sound);
//				session.update(usr);
//			}
//			Criteria criteria2 = session.createCriteria(User.class);
//			criteria2.createAlias("favorites", "favorites").add(Restrictions.eq("favorites.soundId", id));
//			List<User> results2 = criteria2.list();
//			for (User usr : results2) {
//				System.out.println("username:" + usr.getUsername());
//				usr.removeFromFavorites(sound);
//				session.update(usr);
//			}

			Query query = session.createQuery("from User u where exists(from u.playlist where soundId=" +id + ") or exists(from u.favorites where soundId = " +id + ")");
			List<User> results = query.list();
			for (User usr : results) {
				System.out.println("username:" + usr.getUsername());
				usr.removeSoundFromLiked(sound);
				usr.removeFromFavorites(sound);
				session.update(usr);
			}
			
			session.delete(sound);
			request.getSession().setAttribute("loggedUser", u);
			tx.commit();
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
		rv.addProperty("status", "ok");
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
			rv.addProperty("status", "bad");
			return rv.toString();
		} finally {
			session.close();
		}
		rv.addProperty("status", "ok");
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
			rv.addProperty("status", "bad");
			return rv.toString();
		} finally {
			session.close();
		}
		rv.addProperty("status", "ok");
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
		User user = (User) request.getSession().getAttribute("loggedUser");

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
			rv.addProperty("status", "bad");
			return rv.toString();
		} finally {
			session.close();
		}
		rv.addProperty("status", "ok");
		rv.addProperty("id", "#sound" + soundId);

		return rv.toString();
	}

	@RequestMapping(value = "/createAlbum", method = RequestMethod.POST)
	public @ResponseBody String createAlbum(MultipartHttpServletRequest request) {
		JsonObject rv = new JsonObject();
		// getting Album params
		User user = (User) request.getSession().getAttribute("loggedUser");
		String fileName = user.getUsername() + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
		String title = request.getParameter("albumTitle");
		String genresTmp = request.getParameter("albumGenres");
		String[] genres = genresTmp.split(",");
		System.out.println(Arrays.toString(genres));
		MultipartFile albumCover = request.getFile("albumCover");
		byte[] coverPhoto = null;
		try {
			coverPhoto = albumCover.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			rv.addProperty("status", "bad");
			return rv.toString();
		}
		// saving album to DB
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User author = (User) session.get(User.class, user.getUsername());
			Album album = new Album().setAlbumAuthor(author).setAlbumTitle(title).setFileName(fileName);
			for (String string : genres) {
				Genre genre = (Genre) session.get(Genre.class, Integer.parseInt(string));
				album.addGenre(genre);
			}
			session.save(album);
			author.addAlbum(album);
			session.update(author);
			tx.commit();
			rv.addProperty("status", "ok");
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
		// TODO save cover to server's file system
		FileOutputStream fos = null;
		File coverFile = new File(context.getRealPath("/static/covers/" + fileName + ".jpg"));
		try {
			coverFile.createNewFile();
			fos = new FileOutputStream(coverFile);
			fos.write(coverPhoto);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		rv.addProperty("status", "ok");
		return rv.toString();
	}

	@RequestMapping(value = "/deleteAlbum", method = RequestMethod.POST)
	public @ResponseBody String deleteAlbum(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int id = Integer.parseInt(request.getParameter("id"));
		User user = (User) request.getSession().getAttribute("loggedUser");
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User u = (User) session.get(User.class, user.getUsername());
			for (Album album : u.getAlbums()) {
				if (album.getAlbumId() == id) {
					u.removeAlbumFromAlbums(album);
					break;
				}
			}
			session.update(u);
			Album album = (Album) session.get(Album.class, id);
			session.delete(album);
			request.getSession().setAttribute("loggedUser", u);
			tx.commit();

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
		rv.addProperty("status", "ok");
		rv.addProperty("id", "#album" + id);
		return rv.toString();
	}

	@RequestMapping(value = "/updateAlbum", method = RequestMethod.POST)
	public @ResponseBody String updateAlbum(MultipartHttpServletRequest request) {
		JsonObject rv = new JsonObject();
		// getting Album params
		User user = (User) request.getSession().getAttribute("loggedUser");
		String fileName = user.getUsername() + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
		String title = request.getParameter("albumTitle");
		// String genresTmp = request.getParameter("albumGenres");
		int albumId = Integer.parseInt(request.getParameter("albumId"));
		// String[] genresS = genresTmp.split(",");
		MultipartFile albumCover = request.getFile("albumCover");
		byte[] coverPhoto = null;
		try {
			coverPhoto = albumCover.getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			rv.addProperty("status", "bad");
			return rv.toString();
		}
		// saving album to DB
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User author = (User) session.get(User.class, user.getUsername());
			Album album = (Album) session.get(Album.class, albumId);
			// for (String string : genresS) {
			// for(Genre genres : album.getAlbumGenres()) {
			// if(Integer.parseInt(string) != genres.getGenreId()) {
			// TODO: GENRE LOGIKA
			// }
			// }
			// Genre genre = (Genre) session.get(Genre.class,
			// Integer.parseInt(string));
			// album.addGenre(genre);
			// }
			album.setAlbumTitle(title).setFileName(fileName);
			session.save(album);
			session.update(author);
			tx.commit();
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
		// TODO save cover to server's file system
		FileOutputStream fos = null;
		File coverFile = new File(context.getRealPath("/static/covers/" + fileName + ".jpg"));
		try {
			coverFile.createNewFile();
			fos = new FileOutputStream(coverFile);
			fos.write(coverPhoto);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String img = "data:image/gif;base64," + Base64.encode(coverPhoto);
		rv.addProperty("status", "ok");
		rv.addProperty("id", albumId);
		rv.addProperty("newName", title);
		rv.addProperty("newFilePath", img);
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
