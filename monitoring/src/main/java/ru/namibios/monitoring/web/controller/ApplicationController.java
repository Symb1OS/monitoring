package ru.namibios.monitoring.web.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Base64;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import ru.namibios.monitoring.model.User;
import ru.namibios.monitoring.utils.ImageUtils;
import ru.namibios.monitoring.web.service.Service;


@Controller
public class ApplicationController {
	
	@Autowired
	private Service service;
	
	@RequestMapping("/app")
	public ModelAndView app() {
	
		ModelAndView mav = new ModelAndView();
		mav.setViewName("app");
		
		return mav;
	}
	
	@RequestMapping("/monitoring")
	public ModelAndView status(HttpServletRequest request, HttpServletResponse response) {
		
		User user = new User();
		
		Map<String, Object> param = service.getSettings(user.getName());
		String hash = (String) param.get("licence_key");
		String url = (String) param.get("url_monitoring");
		
		ModelAndView mav = new ModelAndView();
		
		if(!url.startsWith("screen")) {
			
			mav.setViewName("monitoring");
			mav.addObject("url", url);
			return mav;
		}else {
			
			String path = service.getSnapshotName(hash);
			if(path.isEmpty()) return null;
			
			BufferedImage image = ImageUtils.read(new File(path));
			byte[] bytes = ImageUtils.imageToBytes(image);
			
			mav.setViewName("screen");
			mav.addObject("snapshot", "data:image/png;base64," + Base64.getEncoder().encodeToString(bytes));
			return mav;
		}
		
	}
}