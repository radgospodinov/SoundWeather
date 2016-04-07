package controller;

import java.awt.event.FocusAdapter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.neo4j.cypher.internal.compiler.v2_1.ast.rewriters.flattenBooleanOperators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.gson.JsonObject;

import model.Album;
import model.Genre;
import model.HibernateUtil;
import model.Sound;
import model.User;
import scala.languageFeature.reflectiveCalls;

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
			for (Sound sound : u.getSounds()) {
				if (sound.getSoundId() == id) {
					u.removeSoundFromSounds(sound);
					break;
				}
			}
			for (Album album : u.getAlbums()) {
				for (Sound sound : album.getAlbumTracks()) {
					if (sound.getSoundId() == id) {
						album.removeSound(sound);
						break;
					}
				}
			}
			session.update(u);
			Sound sound = (Sound) session.get(Sound.class, id);
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

	@RequestMapping(value = "/createAlbum", method = RequestMethod.POST)
	public @ResponseBody String createAlbum(MultipartHttpServletRequest request) throws IOException {
		JsonObject rv = new JsonObject();
		// getting Album params
		User user = (User) request.getSession().getAttribute("loggedUser");
		String fileName = user.getUsername() + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
		String title = request.getParameter("albumTitle");
		String genresTmp = request.getParameter("albumGenres");
		String[] genres = genresTmp.split(",");

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
		coverFile.createNewFile();
		fos = new FileOutputStream(coverFile);
		fos.write(coverPhoto);
		fos.close();
		return rv.toString();
	}

}
