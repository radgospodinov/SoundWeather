package controller;

import java.time.LocalDateTime;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.Album;
import model.Comment;
import model.Genre;
import model.HibernateUtil;
import model.Sound;
import model.User;

@Controller
public class CommentController {

	
	@Autowired
	ServletContext context;

	@RequestMapping(value = "/createComment", method = RequestMethod.POST)
	public String createComment(HttpServletRequest request, HttpServletResponse response) {
		
		//get data from the request (sound.jsp):
		User user = (User) request.getSession().getAttribute("loggedUser");
		int soundId = Integer.parseInt(request.getParameter("soundId"));
		String commentBody = request.getParameter("comment_body");
		
	
		// saving comment to DB
				Session session = HibernateUtil.getSession();
				Transaction tx = null;
				try {
					tx = session.beginTransaction();
					User author = (User) session.get(User.class, user.getUsername());
					Sound sound = (Sound) session.get(Sound.class, soundId);
					Comment newComment = new Comment().setCommentAuthor(author).setCommentBody(commentBody).setCommentPostingDateTime(LocalDateTime.now());
					
					session.save(newComment);
					author.addComment(newComment);
					sound.addCommentToSound(newComment);
					session.update(sound);
					session.update(author);
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
	
	
	
	
}
