package ru.namibios.monitoring.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value="/payment")
public class PaymentController {

	@RequestMapping
	public ModelAndView getFace() {
	
		ModelAndView mav = new ModelAndView();
		mav.setViewName("payment");
		
		return mav;
	}
}
