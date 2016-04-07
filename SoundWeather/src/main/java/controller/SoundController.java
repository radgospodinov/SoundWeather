package controller;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;

import model.HibernateUtil;
import model.Sound;
import model.User;
import scala.languageFeature.reflectiveCalls;

@Controller
public class SoundController {

	@RequestMapping(value = "/deleteSound", method = RequestMethod.POST)
	public @ResponseBody String deleteSound(HttpServletRequest request) {
		JsonObject rv = new JsonObject();
		int id = Integer.parseInt(request.getParameter("id"));
		User user = (User) request.getSession().getAttribute("loggedUser");
		Session session = HibernateUtil.getSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			User u = (User) session.get(User.class, user.getUsername());
			for (Sound sound : u.getSounds()) {
				if (sound.getSoundId() == id) {
					u.removeSoundFromSounds(sound);
					break;
				}
			}
			session.update(u);
			Sound sound = (Sound) session.get(Sound.class, id);
			session.delete(sound);
			request.getSession().setAttribute("loggedUser", u);
			tx.commit();

		} catch (Exception e) {
			if (tx != null) {
				tx.rollback();
			}
			e.printStackTrace();
			rv.addProperty("status", "bad");
			return rv.toString();
		} finally {
			session.close();
		}
		rv.addProperty("status", "ok");
		rv.addProperty("id", "#s" + id);
		return rv.toString();
	}
}
