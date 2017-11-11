package ru.namibios.monitoring.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

	@RequestMapping(value= {"/", "/login"})
	public ModelAndView login() {
		
		ModelAndView model = new ModelAndView();
		model.setViewName("login");

		return model;
		
	}
	
	@RequestMapping("/403")
	public ModelAndView accessDenied() {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("403");
		
		return mav;
		
	}
}