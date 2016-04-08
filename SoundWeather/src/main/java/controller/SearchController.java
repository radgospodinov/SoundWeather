package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.Album;
import model.Genre;
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
		
		Integer page = Integer.parseInt(pageString);
		boolean areSounds = request.getParameter("are_sounds") != null;
		boolean areUsers = request.getParameter("are_users") != null;
		boolean areAlbums = request.getParameter("are_albums") != null;
		
		for(int i = 0; i< 10; i++) {
			System.out.println(areSounds);
		}
		
		
		
		String genre = request.getParameter("search_genre");

		Session session = HibernateUtil.getSession();
		// get genres from db
		Criteria genreCriteria = session.createCriteria(Genre.class);
		ArrayList<Genre> genres = (ArrayList<Genre>) genreCriteria.list();
		Long numberOfResults = (long) 0;
		
		
		
		//IF WE NEED ALL GENRES:
		if (areSounds && (genre == null || genre.equalsIgnoreCase("All"))) {
			// Getting the number of results:
			try {
				session.beginTransaction();
				Criteria criteria = session.createCriteria(Sound.class);
				criteria.setProjection(Projections.rowCount());
				criteria.add(Restrictions.like("soundTitle", "%" + searchWord + "%"));
				numberOfResults =  (Long) criteria.uniqueResult(); 
				session.getTransaction().commit();
				
			} catch (HibernateException e) {
				session.getTransaction().rollback();
			}

			//Determining the number of pages:
			int numberOfPages = (int) (numberOfResults / MAX_RESULTS_PER_PAGE);
				if (numberOfResults % MAX_RESULTS_PER_PAGE != 0) {
					numberOfPages += 1;
				}
						
			// Getting one page of results according to the requested page:
			List<Sound> searchResults = null;
			try {
				session.beginTransaction();
				Criteria criteria = session.createCriteria(Sound.class);
				criteria.setFirstResult((page - 1) * MAX_RESULTS_PER_PAGE);
				criteria.setMaxResults(page * MAX_RESULTS_PER_PAGE);
				criteria.add(Restrictions.like("soundTitle", "%" + searchWord + "%"));
				searchResults = (List<Sound>) criteria.list();		
				
			} catch (HibernateException e) {
				session.getTransaction().rollback();
			} finally {
				session.close();
			}
			
			// Putting stuff in the request and returning the search.jsp:
			request.setAttribute("number_of_results", numberOfResults);
			request.setAttribute("search_word", searchWord);
			request.setAttribute("number_of_pages", numberOfPages);
			request.setAttribute("current_page", page);
			request.setAttribute("result_list", searchResults);
			request.setAttribute("are_sounds", areSounds);
			request.setAttribute("genres", genres);
			System.out.println(pageString);
			System.out.println(areSounds);
			return "search";
		}

		
		
		
		//FILTERED BY GENRE:
		if (areSounds && !genre.equalsIgnoreCase("All") && !areAlbums) {
			
			// Getting the number of results:
			try {
				session.beginTransaction();
				Criteria criteria = session.createCriteria(Sound.class);
				criteria.setProjection(Projections.rowCount());
				criteria.add(Restrictions.like("soundTitle", "%" + searchWord + "%"));
				criteria.createAlias("soundGenres", "genre");
				criteria.add(Restrictions.eq("genre.genreName", genre));
				numberOfResults =  (Long) criteria.uniqueResult(); 
				session.getTransaction().commit();
				
			} catch (HibernateException e) {
				session.getTransaction().rollback();
			}

			//Determining the number of pages:
			int numberOfPages = (int) (numberOfResults / MAX_RESULTS_PER_PAGE);
				if (numberOfResults % MAX_RESULTS_PER_PAGE != 0) {
					numberOfPages += 1;
				}
			
			// Getting one page of results according to the requested page:
				List<Sound> searchResults = null;
				try {
					session.beginTransaction();
					Criteria criteria = session.createCriteria(Sound.class);
					criteria.setFirstResult((page - 1) * MAX_RESULTS_PER_PAGE);
					criteria.setMaxResults(page * MAX_RESULTS_PER_PAGE);
					criteria.add(Restrictions.like("soundTitle", "%" + searchWord + "%"));
					criteria.createAlias("soundGenres", "genre");
					criteria.add(Restrictions.eq("genre.genreName", genre));
					searchResults = (List<Sound>) criteria.list();		
					
				} catch (HibernateException e) {
					session.getTransaction().rollback();
				} finally {
					session.close();
				}
							
			// Putting stuff in the request and returning the search.jsp:
			request.setAttribute("genre_filter", genre);
			request.setAttribute("number_of_results", numberOfResults);
			request.setAttribute("search_word", searchWord);
			request.setAttribute("number_of_pages", numberOfPages);
			request.setAttribute("current_page", page);
			request.setAttribute("result_list", searchResults);
			request.setAttribute("are_sounds", areSounds);
			request.setAttribute("genres", genres);
			return "search";
		}
		
		//WHEN WE SEARCH ALBUMS
				if (areAlbums && genre.equalsIgnoreCase("All")) {
					
				// Getting the number of results:
					try {
						session.beginTransaction();
						Criteria criteria = session.createCriteria(Album.class);
						criteria.add(Restrictions.like("albumTitle", "%" + searchWord + "%"));
						criteria.setProjection(Projections.rowCount());
						numberOfResults =  (Long) criteria.uniqueResult(); 
						session.getTransaction().commit();
									
					} catch (HibernateException e) {
						session.getTransaction().rollback();
					}

					//Determining the number of pages:
					int numberOfPages = (int) (numberOfResults / MAX_RESULTS_PER_PAGE);
						if (numberOfResults % MAX_RESULTS_PER_PAGE != 0) {
							numberOfPages += 1;
					}
					
					// Getting one page of results according to the requested page:
					List<Album> searchResults = null;
						try {
							session.beginTransaction();
							Criteria criteria = session.createCriteria(Album.class);
							criteria.setFirstResult((page - 1) * MAX_RESULTS_PER_PAGE);
							criteria.setMaxResults(page * MAX_RESULTS_PER_PAGE);
							criteria.add(Restrictions.like("albumTitle", "%" + searchWord + "%"));
							searchResults = (List<Album>) criteria.list();		
							for (Album a : searchResults) {
								a.getAlbumTracks();
							}
							
							
						} catch (HibernateException e) {
							session.getTransaction().rollback();
						} finally {
							session.close();
						}
					request.setAttribute("genre_filter", genre);
					request.setAttribute("are_albums", areAlbums);
					request.setAttribute("number_of_results", numberOfResults);
					request.setAttribute("search_word", searchWord);
					request.setAttribute("number_of_pages", numberOfPages);
					request.setAttribute("current_page", page);
					request.setAttribute("result_list", searchResults);
					System.out.println(searchResults.size());
				//	request.setAttribute("are_sounds", areSounds);
					request.setAttribute("genres", genres);
					return "search";
				}
				
				//WHEN WE SEARCH ALBUMS BY GENRE
				if (areAlbums && !genre.equalsIgnoreCase("All")) {
					
					// Getting the number of results:
						try {
							session.beginTransaction();
							Criteria criteria = session.createCriteria(Album.class);
							criteria.add(Restrictions.like("albumTitle", "%" + searchWord + "%"));
							criteria.setProjection(Projections.rowCount());
							criteria.createAlias("albumGenres", "genre");
							criteria.add(Restrictions.eq("genre.genreName", genre));
							numberOfResults =  (Long) criteria.uniqueResult(); 
							session.getTransaction().commit();
										
						} catch (HibernateException e) {
							session.getTransaction().rollback();
						}

						//Determining the number of pages:
						int numberOfPages = (int) (numberOfResults / MAX_RESULTS_PER_PAGE);
							if (numberOfResults % MAX_RESULTS_PER_PAGE != 0) {
								numberOfPages += 1;
						}
						
						// Getting one page of results according to the requested page:
						List<Album> searchResults = null;
							try {
								session.beginTransaction();
								Criteria criteria = session.createCriteria(Album.class);
								criteria.setFirstResult((page - 1) * MAX_RESULTS_PER_PAGE);
								criteria.setMaxResults(page * MAX_RESULTS_PER_PAGE);
								criteria.add(Restrictions.like("albumTitle", "%" + searchWord + "%"));
								criteria.createAlias("albumGenres", "genre");
								criteria.add(Restrictions.eq("genre.genreName", genre));
								searchResults = (List<Album>) criteria.list();		
								
							} catch (HibernateException e) {
								session.getTransaction().rollback();
							} finally {
								session.close();
							}
						request.setAttribute("genre_filter", genre);
						request.setAttribute("are_albums", areAlbums);
						request.setAttribute("number_of_results", numberOfResults);
						request.setAttribute("search_word", searchWord);
						request.setAttribute("number_of_pages", numberOfPages);
						request.setAttribute("current_page", page);
						request.setAttribute("result_list", searchResults);
					//	request.setAttribute("are_sounds", areSounds);
						request.setAttribute("genres", genres);
						return "search";
					}
		
		//WHEN WE SEARCH USERS
		if (areUsers) {
			
		// Getting the number of results:
			try {
				session.beginTransaction();
				Criteria criteria = session.createCriteria(User.class);
				criteria.add(Restrictions.like("username", "%" + searchWord + "%"));
				criteria.setProjection(Projections.rowCount());
				numberOfResults =  (Long) criteria.uniqueResult(); 
				session.getTransaction().commit();
							
			} catch (HibernateException e) {
				session.getTransaction().rollback();
			}

			//Determining the number of pages:
			int numberOfPages = (int) (numberOfResults / MAX_RESULTS_PER_PAGE);
				if (numberOfResults % MAX_RESULTS_PER_PAGE != 0) {
					numberOfPages += 1;
			}
			
			// Getting one page of results according to the requested page:
			List<User> searchResults = null;
				try {
					session.beginTransaction();
					Criteria criteria = session.createCriteria(User.class);
					criteria.setFirstResult((page - 1) * MAX_RESULTS_PER_PAGE);
					criteria.setMaxResults(page * MAX_RESULTS_PER_PAGE);
					criteria.add(Restrictions.like("username", "%" + searchWord + "%"));
					searchResults = (List<User>) criteria.list();		
					
				} catch (HibernateException e) {
					session.getTransaction().rollback();
				} finally {
					session.close();
				}
			request.setAttribute("are_users", areUsers);
			request.setAttribute("number_of_results", numberOfResults);
			request.setAttribute("search_word", searchWord);
			request.setAttribute("number_of_pages", numberOfPages);
			request.setAttribute("current_page", page);
			request.setAttribute("result_list", searchResults);
			request.setAttribute("are_sounds", areSounds);
			request.setAttribute("genres", genres);
			return "search";
		}
		return "search";

	}

}
