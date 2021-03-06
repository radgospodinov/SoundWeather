package controller;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import model.Comment;
import model.HibernateUtil;
import model.Sound;
import model.User;

@Controller
public class CommentController {

	@RequestMapping(value = "/createComment", method = RequestMethod.POST)
	public String createComment(HttpServletRequest request, HttpServletResponse response) {

		// get data from the request (sound.jsp):
		User user = (User) request.getSession().getAttribute("loggedUser");
		int soundId = Integer.parseInt(request.getParameter("soundId"));
		String commentBody = request.getParameter("comment_body");

		// saving comment to DB
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		Sound sound = null;
		try {
			tx = session.beginTransaction();
			User author = (User) session.get(User.class, user.getUsername());
			sound = (Sound) session.get(Sound.class, soundId);
			
			Comment newComment = new Comment().setCommentAuthor(author).setCommentBody(commentBody)
					.setCommentPostingDateTime(LocalDateTime.now());
			session.save(newComment);
			
			author.addComment(newComment);
			sound.addCommentToSound(newComment);
			session.update(sound);
			session.update(author);

			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
		} finally {
			session.close();
		}

		List<Comment> comments = sound.getSoundComments();

		request.setAttribute("sound", sound);

		return "sound";
	}

}
