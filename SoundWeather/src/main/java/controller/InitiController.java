package controller;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.spec.RC2ParameterSpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.omg.PortableServer.POAPackage.WrongAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.google.gson.JsonObject;

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

		// TESTS!
		// ArrayList<Sound> wokringSounds = null;
		// Transaction tx = null;
		// Session session = HibernateUtil.getSession();
		// try {
		// tx = session.beginTransaction();
		//
		// Criteria criteria = session.createCriteria(Sound.class);
		// wokringSounds = (ArrayList<Sound>) criteria.list();
		// for (Sound sound : wokringSounds) {
		// System.out.println(sound.getSoundAuthor().getUsername());
		// }
		//
		// tx.commit();
		// } catch (Exception e) {
		// if (tx != null) {
		// tx.rollback();
		// }
		// } finally {
		// session.close();
		// }

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

		Session session = HibernateUtil.getSession();
		Criteria criteria = session.createCriteria(Genre.class);
		ArrayList<Genre> genres = (ArrayList<Genre>) criteria.list();
		request.setAttribute("genres", genres);
		return "upload";
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
		String hql = "FROM Sound as s WHERE exists(from Genre as g where g.genreId ="+genreId +" and s.soundId=g.genreId)";
		Session session = HibernateUtil.getSession();
		Query query = session.createQuery(hql);
		 
		
//		Criteria c = session.createCriteria(Sound.class,"sound");
//		 c.setMaxResults(MAX_SOUNDS_PER_ROW);
//		 DetachedCriteria genreCriteria = DetachedCriteria.forClass(Genre.class,"genre");
//		 rv = c.list();
//		 
		 rv = query.list();
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

}
