package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.HibernateUtil;
import model.Sound;
import model.User;

	@Controller
	public class SearchController {

		private static final int MAX_RESULTS_PER_PAGE = 9;

		@Autowired
		ServletContext context;
		
		
		@RequestMapping(value = "/search", method = RequestMethod.POST)
		public String search(HttpServletRequest request, HttpServletResponse response) {
			
			String searchWord = request.getParameter("search_word");
			String pageString = request.getParameter("requested_page");
			int page = Integer.parseInt(pageString);
			boolean areSounds = request.getParameter("are_sounds") != null;
			String genre = request.getParameter("search_genre");
					
			Session session = HibernateUtil.getSession();
			
			if (areSounds && genre == null) {
				List<Sound> soundSearchResults = null;
				
				//Getting the number of results:
				try {
					session.beginTransaction();
					Criteria criteria = session.createCriteria(Sound.class);
					criteria.add(Restrictions.like("soundTitle", "%"+searchWord+"%"));
					soundSearchResults = (List<Sound>) criteria.list();
					session.getTransaction().commit();
				
				} catch (HibernateException e) {
					session.getTransaction().rollback();
				}
								
				int numberOfPages = soundSearchResults.size()/MAX_RESULTS_PER_PAGE;
				if (soundSearchResults.size()%MAX_RESULTS_PER_PAGE != 0) {
					numberOfPages+=1;
				}
				
				//Getting one page of results according to the requested page:
				List<Sound> searchResults = new ArrayList<Sound>();
				
				for (int i = ((page - 1) * MAX_RESULTS_PER_PAGE); i <= (page * MAX_RESULTS_PER_PAGE); i++) {
					searchResults.add(soundSearchResults.get(i));
				}
				
				//Putting stuff in the Session and returning the search.jsp:
				request.getSession().setAttribute("search_word", searchWord);
				request.getSession().setAttribute("number_of_pages", searchWord);
				request.getSession().setAttribute("current_page", page);
				request.getSession().setAttribute("result_list", searchResults);
				return "search";
			}
			

			if (areSounds && genre != null) {
				List<Sound> soundSearchResults = null;
				
				//Getting the number of results:
				try {
					session.beginTransaction();
					Criteria criteria = session.createCriteria(Sound.class);
					criteria.add(Restrictions.like("soundTitle", "%"+searchWord+"%"));
					
					criteria.createAlias("soundGenres", "genre");
					criteria.add(Restrictions.eq("genre.genreName", genre));
									
					soundSearchResults = (List<Sound>) criteria.list();
					session.getTransaction().commit();
				
				} catch (HibernateException e) {
					session.getTransaction().rollback();
				} finally {
					session.close();
				}
								
				int numberOfPages = soundSearchResults.size();
				
				//Getting one page of results according to the requested page:
				List<Sound> searchResults = new ArrayList<Sound>();
				
				for (int i = ((page - 1) * MAX_RESULTS_PER_PAGE); i <= (page * MAX_RESULTS_PER_PAGE); i++) {
					searchResults.add(soundSearchResults.get(i));
				}
				
				request.getSession().setAttribute("search_word", searchWord);
				request.getSession().setAttribute("number_of_pages", searchWord);
				request.getSession().setAttribute("current_page", page);
				request.getSession().setAttribute("result_list", soundSearchResults);
				return "search";
			}
			
			//If are not sounds but users:
				List<User> userSearchResults = null;
				
				//Getting the number of results:
				try {
					session.beginTransaction();
					Criteria criteria = session.createCriteria(User.class);
					criteria.add(Restrictions.like("username", "%"+searchWord+"%"));
					userSearchResults = (List<User>) criteria.list();
					session.getTransaction().commit();
				
				} catch (HibernateException e) {
					session.getTransaction().rollback();
				}  finally {
					session.close();
				}
								
				int numberOfPages = userSearchResults.size();
				
				//Getting one page of results according to the requested page:
				List<User> searchResults = new ArrayList<User>();
				
				for (int i = ((page - 1) * MAX_RESULTS_PER_PAGE); i <= (page * MAX_RESULTS_PER_PAGE); i++) {
					searchResults.add(userSearchResults.get(i));
				}
				
				request.getSession().setAttribute("search_word", searchWord);
				request.getSession().setAttribute("number_of_pages", searchWord);
				request.getSession().setAttribute("current_page", page);
				request.getSession().setAttribute("result_list", searchResults);
				return "search";
			
			
			
			
		}
		
		
		
		
		
		
}
