package controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;

import model.HibernateUtil;
import model.User;

@Controller
public class UserController {

	@Autowired
	ServletContext context;

	@RequestMapping(value = "/followUser", method = RequestMethod.POST)
	public @ResponseBody String followUser(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		String targetUserId = request.getParameter("id");
		User loggedUser = (User) request.getSession().getAttribute("loggedUser");
		if(loggedUser==null) {
			rv.addProperty("status", "bad");
			return rv.toString();
		}
		if(targetUserId.equals(loggedUser.getUsername())){
			rv.addProperty("status", "bad");
			return rv.toString();
		}
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User target = (User) session.get(User.class, targetUserId);
			User current = (User) session.get(User.class, loggedUser.getUsername());
			current.addToFollowing(target);
			target.addToFollowers(current);
			session.update(target);
			session.update(current);
			tx.commit();

		} catch (Exception e) {
			if(tx!=null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty("status", "bad");
		} finally {
			session.close();
		}
		rv.addProperty("status", "ok");
		return rv.toString();
	}
}
