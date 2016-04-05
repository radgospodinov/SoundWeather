package controller;

import java.util.ArrayList;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.Genre;
import model.HibernateUtil;

@Controller
public class InitiController {
	
	@Autowired
	ServletContext context;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initIndexPage() {

		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			int number = ((Number) session.createCriteria(Genre.class).setProjection(Projections.rowCount()).uniqueResult())
					.intValue();
			System.out.println("number return: "+number);
			if(number==0){
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

		return "index";
	}
	
	@RequestMapping(value = "/home", method = RequestMethod.GET)
	public String initHomePage() {
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
}
