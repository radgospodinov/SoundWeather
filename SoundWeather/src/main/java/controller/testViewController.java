package controller;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class testViewController {
	
	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String initIndexPage() {
		return "index";
	}

}
