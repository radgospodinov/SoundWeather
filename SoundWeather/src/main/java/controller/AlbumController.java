package controller;

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

import model.Album;
import model.Genre;
import model.HibernateUtil;
import model.Sound;


@Controller
public class AlbumController {

	
	private static final String SINGLE_ALBUM = "single_album";
	private static final String ALBUM = "album";
	private static final String LOGGED_USER = "loggedUser";
	private static final String REDIRECT_URL_PARAM = "url";
	private static final String GENRES = "genres";

		
	@Autowired
	ServletContext context;
	
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
