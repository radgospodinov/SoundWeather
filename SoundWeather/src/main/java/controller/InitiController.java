package controller;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.RC2ParameterSpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.loader.plan.exec.process.spi.ReturnReader;
import org.omg.PortableServer.POAPackage.WrongAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

import model.Album;
import model.Comment;
import model.Genre;
import model.HibernateUtil;
import model.Sound;
import model.User;
import scala.math.Ordering.StringOrdering;

@Controller
public class InitiController {

	private static final int MAX_SOUNDS_PER_ROW = 9;

	@Autowired
	ServletContext context;

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initIndexPage() {
		initGenres();

		return "index";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String initHomePage(HttpServletRequest request) {

		String location = "Sofia";
		if (request.getSession().getAttribute("loggedUser") != null) {
			location = ((User) request.getSession().getAttribute("loggedUser")).getLocation();
		}
		request.setAttribute("weatherSounds", initWeatherSounds(getWeatherDesc(location)));
		request.setAttribute("trendySounds", initTrendySounds());
		return "playlists";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String initRegisterPage() {
		return "register";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String initLoginPage() {
		return "login";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String initUploadPage(HttpServletRequest request) {

		request.setAttribute("genres", getAllGenres());
		return "upload";
	}

	@RequestMapping(value = "/own_sounds", method = RequestMethod.GET)
	public String initOwnSounds(HttpServletRequest request) {
		Session session = HibernateUtil.getSession();
		User u = (User) request.getSession().getAttribute("loggedUser");

		try {
			session.beginTransaction();
			User tmp = (User) session.get(User.class, u.getUsername());

			for (Sound sound : tmp.getSounds()) {
				sound.getSoundFans().size();
				sound.getSoundComments().size();
			}
			tmp.getAlbums().size();

			request.setAttribute("sounds", tmp.getSounds());
			request.setAttribute("albums", tmp.getAlbums());
			session.getTransaction().commit();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.getTransaction().rollback();
		} finally {
			session.close();
		}

		return "own_sounds";
	}

	@RequestMapping(value = "/albums", method = RequestMethod.GET)
	public String initAlbums(HttpServletRequest request) {
		request.setAttribute("genres", getAllGenres());
		User u = (User) request.getSession().getAttribute("loggedUser");
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User user = (User) session.get(User.class, u.getUsername());
			user.getAlbums().size();
			List<Album> albums = user.getAlbums();
			System.out.println(albums.size());

			for (Album a : albums) {
				a.getAlbumTracks();
				for (Sound s : a.getAlbumTracks()) {
					System.out.println(s.getSoundTitle());
				}
			}

			request.setAttribute("albums", albums);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "albums";
	}

	@RequestMapping(value = "/sound", method = RequestMethod.POST)
	public String initSound(HttpServletRequest request) {

		int soundId = Integer.parseInt(request.getParameter("soundId"));
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			Sound sound = (Sound) session.get(Sound.class, soundId);
			List<Comment> comments = sound.getSoundComments();

			System.out.println(comments.size());

			request.setAttribute("sound", sound);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "sound";
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public String initProfile(HttpServletRequest request) {
		User user = (User) request.getSession().getAttribute("loggedUser");

		request.setAttribute("user", user);
		return "profile";
	}

	@RequestMapping(value = "/following", method = RequestMethod.GET)
	public String initFollowing(HttpServletRequest request) {
		// TODO list of following users to be initialised
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User current = (User) session.get(User.class, loggedUser.getUsername());
			current.getFollowing().size();
			request.setAttribute("following", current.getFollowing());
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

		return "following";
	}

	private void initGenres() {
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			int number = ((Number) session.createCriteria(Genre.class).setProjection(Projections.rowCount())
					.uniqueResult()).intValue();
			System.out.println("number return: " + number);
			if (number == 0) {
				session.save(new Genre().setGenreName("Pop"));
				session.save(new Genre().setGenreName("Rock"));
				session.save(new Genre().setGenreName("Rap"));
				session.save(new Genre().setGenreName("Classic"));
				session.save(new Genre().setGenreName("Techno"));
				// TODO MORE TO BE ADDED
			}
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
		} finally {
			session.close();
		}
	}

	private String getWeatherDesc(String location) {

		RestTemplate restTemplate = new RestTemplate();

		// GET WEATHER DESC FROM OPENWEATHERAPI

		// TODO : String[] apiKeys to use multiple keys from openweather in case
		// of overload of system
		String apiKey = "c63d60d1d5efdd704911c9add93638e8";
		String url = "http://api.openweathermap.org/data/2.5/weather?q=" + location + "&APPID=" + apiKey;
		String result = restTemplate.getForObject(url, String.class);
		System.out.println("result=" + result);
		int p1 = result.indexOf("\"main\"");
		String weatherDesc = "";
		if (p1 > -1) {
			int p2 = result.indexOf("\"", p1 + 8);
			System.out.println("p1=" + p1 + " p2=" + p2);
			weatherDesc = result.substring(p1 + 8, p2);
		}

		return weatherDesc;

	}

	private List<Sound> initWeatherSounds(String weatherDescription) {
		int genreId = -1;
		switch (weatherDescription.toLowerCase()) {
		// TODO better logic and stuff / maybe use constants not magic numbers

		case "clear":
			genreId = 1; // Pop
			break;
		case "thunderstorm":
			genreId = 2; // Rock
			break;
		case "atmosphere":
			genreId = 3; // Rap
			break;
		case "rain":
			genreId = 4; // Classic
			break;
		case "snow":
			genreId = 5; // Techno
			break;
		default:
			genreId = 1; // Pop
			break;
		}
		List<Sound> rv = null;

		// TODO : SELECT TO BE MADE
		Session session = HibernateUtil.getSession();
		Criteria c = session.createCriteria(Sound.class);
		c.createAlias("soundGenres", "genre");
		c.setMaxResults(MAX_SOUNDS_PER_ROW);
		c.add(Restrictions.eq("genre.genreId", genreId));
		rv = c.list();
		session.close();
		return rv;
	}

	private List<Sound> initTrendySounds() {
		List<Sound> rv = null;
		Session session = HibernateUtil.getSession();
		Criteria c = session.createCriteria(Sound.class);
		c.addOrder(Order.desc("soundRating"));
		c.setMaxResults(MAX_SOUNDS_PER_ROW);
		rv = c.list();
		session.close();
		return rv;
	}

	private List<Genre> getAllGenres() {
		List<Genre> rv = null;
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(Genre.class);
		rv = criteria.list();
		return rv;
	}

}
