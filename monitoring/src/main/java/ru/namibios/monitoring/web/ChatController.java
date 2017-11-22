package ru.namibios.monitoring.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import ru.namibios.monitoring.model.User;

@Controller
@RequestMapping("/chat")
public class ChatController {

	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView face() {
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("user", new User().getName());
		
		mav.setViewName("chat");
		
		return mav;
	}
	
}
