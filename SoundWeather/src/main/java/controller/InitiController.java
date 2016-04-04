package controller;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class InitiController {
	
	@Autowired
	ServletContext context;
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initIndexPage() {
		System.out.println("az bqh tyk");
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
	public String initUploadPage() {
		return "upload";
	}
}
