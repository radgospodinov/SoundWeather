package controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
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
public class AlbumController {

	
	private static final String SINGLE_ALBUM = "single_album";
	private static final String ALBUM = "album";
	private static final String LOGGED_USER = "loggedUser";
	private static final String REDIRECT_URL_PARAM = "url";
	private static final String GENRES = "genres";
	private static final String RESPONSE_GOOD = "ok";
	private static final String RESPONSE_BAD = "bad";
	private static final String RESPONSE_STATUS = "status";

		
	@Autowired
	ServletContext context;
	
	
	
	@RequestMapping(value = "/createAlbum", method = RequestMethod.POST)
	public @ResponseBody String createAlbum(MultipartHttpServletRequest request) {
		JsonObject rv = new JsonObject();
		// getting Album params
		User user = (User) request.getSession().getAttribute(LOGGED_USER);
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
			rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
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
			rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
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
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		return rv.toString();
	}

	@RequestMapping(value = "/deleteAlbum", method = RequestMethod.POST)
	public @ResponseBody String deleteAlbum(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int id = Integer.parseInt(request.getParameter("id"));
		User user = (User) request.getSession().getAttribute(LOGGED_USER);
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
		rv.addProperty("id", "#album" + id);
		return rv.toString();
	}

	@RequestMapping(value = "/updateAlbum", method = RequestMethod.POST)
	public @ResponseBody String updateAlbum(MultipartHttpServletRequest request) {
		JsonObject rv = new JsonObject();
		// getting Album params
		User user = (User) request.getSession().getAttribute(LOGGED_USER);
		String fileName = user.getUsername() + "_"
				+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
		String title = request.getParameter("albumTitle");
		int albumId = Integer.parseInt(request.getParameter("albumId"));
		MultipartFile albumCover = request.getFile("albumCover");
		byte[] coverPhoto = null;
		if (albumCover != null && !albumCover.isEmpty()) {
			try {
				coverPhoto = albumCover.getBytes();
			} catch (IOException e) {
				e.printStackTrace();
				rv.addProperty(RESPONSE_STATUS, RESPONSE_BAD);
				return rv.toString();
			}
		}
		// saving album to DB
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User author = (User) session.get(User.class, user.getUsername());
			Album album = (Album) session.get(Album.class, albumId);
			if (title != null && !title.isEmpty())
				album.setAlbumTitle(title);
			if (albumCover != null && !albumCover.isEmpty())
				album.setFileName(fileName);
			session.update(album);
			session.update(author);
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
		// save cover to server's file system
		if (albumCover != null && !albumCover.isEmpty()) {
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
			rv.addProperty("newFilePath", img);
		}
		if(title!=null && !title.isEmpty())
			rv.addProperty("newName", title);	
		rv.addProperty(RESPONSE_STATUS, RESPONSE_GOOD);
		rv.addProperty("id", albumId);
		return rv.toString();
	}
	
	@RequestMapping(value = "/getAlbum", method = RequestMethod.GET)
	public String initAlbums(HttpServletRequest request) {
		if (request.getSession().getAttribute(LOGGED_USER) == null) {
			request.setAttribute(REDIRECT_URL_PARAM, ALBUM);
			return "login";
		}
		request.setAttribute(GENRES, getAllGenres());

		int requestedAlbumId = Integer.parseInt(request.getParameter("requested_album_id"));
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Album requestedAlbum = (Album) session.get(Album.class, requestedAlbumId);
			requestedAlbum.getAlbumTracks().size();
			List<Sound> sounds = requestedAlbum.getAlbumTracks();
			System.out.println(sounds.size());
			for (Sound s : sounds) {
				System.out.println(s.getSoundTitle());
				
			}
			request.setAttribute(SINGLE_ALBUM, requestedAlbum);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "single_album";
	}
	
	private List<Genre> getAllGenres() {
		List<Genre> rv = null;
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(Genre.class);
		rv = criteria.list();
		return rv;
	}
	
	
}
