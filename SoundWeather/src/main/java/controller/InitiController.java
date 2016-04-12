package controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;


import model.Album;
import model.Comment;
import model.Genre;
import model.HibernateUtil;
import model.MailUtil;
import model.Sound;
import model.User;

@Controller
public class InitiController {

	private static final String ALBUMS = "albums";
	private static final int MAX_SOUNDS_PER_ROW = 8;
	private static final String DEFAULT_LOCATION = "Sofia";
	
	private static final String API_PROVIDER_REQUEST = "http://api.openweathermap.org/data/2.5/weather?q=";
	private static final String API_PROVIDER_KEY_PARAM = "&APPID=";
	private static final String API_KEY1 = "c63d60d1d5efdd704911c9add93638e8";
	private static final String API_KEY2 = "c358dab4ad6389a50bb2ab26051556a1";
	private static final String API_KEY3 = "7c53134e21b6d4e83c16b8ea305f679b";
	
	private static final String LOGGED_USER = "loggedUser";
	private static final String REDIRECT_URL_PARAM = "url";
	private static final String LOG_STATUS = "logStatus";
	private static final String WEATHER_LIST = "weatherSounds";
	private static final String TRENDY_LIST = "trendySounds";
	private static final String GENRES = "genres";
	private static final String SOUNDS = "sounds";
	private static final int MAIN_CHARS_TO_DESC = 8;
	private static final int POP_GENRE = 1;
	private static final int ROCK_GENRE = 2;
	private static final int RAP_GENRE = 3;
	private static final int CLASSIC_GENRE = 4;
	private static final int TECHNO_GENRE = 5;


	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initIndexPage(HttpServletRequest request) {
		initGenres();
		if(request.getSession().getAttribute(LOGGED_USER)== null) {
			request.setAttribute(LOG_STATUS, false);
		} else {
			request.setAttribute(LOG_STATUS, true);
		}
		return "index";
	}

	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String initHomePage(HttpServletRequest request) {

		String location = DEFAULT_LOCATION;
		if (request.getSession().getAttribute(LOGGED_USER) != null) {
			location = ((User) request.getSession().getAttribute(LOGGED_USER)).getLocation();
		}
		request.setAttribute(WEATHER_LIST, initWeatherSounds(getWeatherDesc(location)));
		request.setAttribute(TRENDY_LIST, initTrendySounds());
		return "playlists";
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public String initRegisterPage() {
		return "register";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String initLogout(HttpServletRequest request) {
		request.getSession().removeAttribute(LOGGED_USER);
		String location = DEFAULT_LOCATION;
		request.setAttribute(WEATHER_LIST, initWeatherSounds(getWeatherDesc(location)));
		request.setAttribute(TRENDY_LIST, initTrendySounds());
		return "playlists";
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String initLoginPage(HttpServletRequest request) {
		request.setAttribute(REDIRECT_URL_PARAM, "home");
		return "login";
	}

	@RequestMapping(value = "/upload", method = RequestMethod.GET)
	public String initUploadPage(HttpServletRequest request) {
		if (request.getSession().getAttribute(LOGGED_USER) == null) {
			request.setAttribute(REDIRECT_URL_PARAM, "upload");
			return "login";
		}
		request.setAttribute(GENRES, getAllGenres());
		return "upload";
	}

	@RequestMapping(value = "/own_sounds", method = RequestMethod.GET)
	public String initOwnSounds(HttpServletRequest request) {
		if (request.getSession().getAttribute(LOGGED_USER) == null) {
			request.setAttribute(REDIRECT_URL_PARAM, "own_sounds");
			return "login";
		}
		Session session = HibernateUtil.getSession();
		User u = (User) request.getSession().getAttribute(LOGGED_USER);

		try {
			session.beginTransaction();
			User tmp = (User) session.get(User.class, u.getUsername());
			for (Sound sound : tmp.getSounds()) {
				sound.getSoundFans().size();
				sound.getSoundComments().size();
			}
			tmp.getAlbums().size();
			
			request.setAttribute(SOUNDS, tmp.getSounds());
			request.setAttribute(ALBUMS, tmp.getAlbums());
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
		if (request.getSession().getAttribute(LOGGED_USER) == null) {
			request.setAttribute(REDIRECT_URL_PARAM, ALBUMS);
			return "login";
		}
		request.setAttribute(GENRES, getAllGenres());
		User u = (User) request.getSession().getAttribute(LOGGED_USER);
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
			request.setAttribute(ALBUMS, albums);
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

	@RequestMapping(value = "/sound", method = RequestMethod.GET)
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
		if (request.getSession().getAttribute(LOGGED_USER) == null) {
			request.setAttribute(REDIRECT_URL_PARAM, "profile");
			return "login";
		}
		User user = (User) request.getSession().getAttribute(LOGGED_USER);

		request.setAttribute("user", user);
		return "profile";
	}

	@RequestMapping(value = "/following", method = RequestMethod.GET)
	public String initFollowing(HttpServletRequest request) {

		if (request.getSession().getAttribute(LOGGED_USER) == null) {
			request.setAttribute(REDIRECT_URL_PARAM, "following");
			return "login";
		}
		User loggedUser = (User) request.getSession().getAttribute(LOGGED_USER);
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

	@RequestMapping(value = "/favorites", method = RequestMethod.GET)
	public String initFavorites(HttpServletRequest request) {
		
		if(request.getSession().getAttribute("loggedUser")==null) {
			request.setAttribute(REDIRECT_URL_PARAM, "following");
			return "login";
		}
		User loggedUser = (User) request.getSession().getAttribute(LOGGED_USER);
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User current = (User) session.get(User.class, loggedUser.getUsername());
			current.getFavorites().size();
			request.setAttribute("favorites", current.getFavorites());
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

		return "favorites";
	}
	
	
	
	
	@RequestMapping(value = "/otherUser", method = RequestMethod.GET)
	public String initOtherUser(HttpServletRequest request) {
		if (request.getSession().getAttribute(LOGGED_USER) == null) {
			request.setAttribute(REDIRECT_URL_PARAM, "otherUser");
			return "login";
		}
		String userId = request.getParameter("username");
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User result = (User) session.get(User.class, userId);
			result.getAlbums().size();
			result.getComments().size();
			result.getSounds().size();
			for (Sound sound : result.getSounds()) {
				sound.getSoundFans().size();
				sound.getSoundComments().size();
			}
			request.setAttribute("other_user", result);
			tx.commit();
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "other_user";
	}
	
	@RequestMapping(value = "/emailConfirmation", method = RequestMethod.GET)
	public String emailConfirmation(HttpServletRequest request) {
		initGenres();
		String uname = request.getParameter("ecr");
		String username = "";
		request.setAttribute(LOG_STATUS, false);
		try {
			username = MailUtil.dencryptUsername(uname);
			System.out.println(username);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User user = (User) session.get(User.class, username);
			if (user != null) {
				if (user.isActive()) {
					return "index";
				} else {
					user.setActive(true);
				}
				session.update(user);
				request.getSession().setAttribute(LOGGED_USER, user);
				tx.commit();
			} else {
				return "index";
			}
		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}
		request.setAttribute(LOG_STATUS, true);
		return "index";
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
		// more to be added if we have issues with these 3
		String[] apiKeys = {API_KEY1,API_KEY2,API_KEY3}; 
		String weatherDesc = "";
		for(String apiKey : apiKeys) {
			String url = API_PROVIDER_REQUEST + location + API_PROVIDER_KEY_PARAM + apiKey;
			String result = "";
			try {
				 result = restTemplate.getForObject(url, String.class);
			} catch (Exception e) {
				continue;
			}
			int startOfDesc = result.indexOf("\"main\"")+MAIN_CHARS_TO_DESC;
			if (startOfDesc > -1) {
				int endOfDesc = result.indexOf("\"", startOfDesc);
				weatherDesc = result.substring(startOfDesc, endOfDesc);
				break;
			} 
		}
		return weatherDesc;

	}

	private List<Sound> initWeatherSounds(String weatherDescription) {
		int genreId = -1;
		switch (weatherDescription.toLowerCase()) {
		case "clear":
			genreId = POP_GENRE; // Pop
			break;
		case "thunderstorm":
			genreId = ROCK_GENRE; // Rock
			break;
		case "atmosphere":
			genreId = RAP_GENRE; // Rap
			break;
		case "rain":
			genreId = CLASSIC_GENRE; // Classic
			break;
		case "snow":
			genreId = TECHNO_GENRE; // Techno
			break;
		default:
			genreId = POP_GENRE; // Pop
			break;
		}
		List<Sound> rv = null;

		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(Sound.class);
		criteria.createAlias("soundGenres", "genre");
		criteria.add(Restrictions.eq("genre.genreId", genreId));
		criteria.add(Restrictions.sqlRestriction("1=1 order by rand()"));
		criteria.setMaxResults(MAX_SOUNDS_PER_ROW);
		rv = criteria.list();
		session.close();
		return rv;
	}

	private List<Sound> initTrendySounds() {
		List<Sound> rv = null;
		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(Sound.class);
		criteria.addOrder(Order.desc("soundRating"));
		criteria.setMaxResults(MAX_SOUNDS_PER_ROW);
		rv = criteria.list();
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
