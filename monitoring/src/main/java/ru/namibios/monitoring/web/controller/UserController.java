package ru.namibios.monitoring.web.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ru.namibios.monitoring.model.Notification;
import ru.namibios.monitoring.model.PasswordGenerator;
import ru.namibios.monitoring.model.ShaEncoder;
import ru.namibios.monitoring.model.TelegramNotification;
import ru.namibios.monitoring.utils.JSON;
import ru.namibios.monitoring.web.service.UserService;


@Controller
public class UserController {
	
	private static final Logger logger = Logger.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;

	@RequestMapping(value="/user", method=RequestMethod.GET)
	public ModelAndView face() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("user");
		
		return mav;
	}
	
	@RequestMapping("/user/add")
	public void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		logger.info("add");
		
		PasswordGenerator passwordGenerator = new PasswordGenerator.PasswordGeneratorBuilder()
    	        .useDigits(true)
    	        .useLower(true)
    	        .useUpper(true)
    	        .build();
		
		List<Map<String, Object>> json = JSON.getList(JSON.getStringFromReader(request.getReader()));
		
		ArrayList<Map<String, Object>> dataList = new ArrayList<>();
		
		json.stream().forEach(map -> {
						
			String password = passwordGenerator.generate(8);
			String licenseKey = ShaEncoder.encode((String) map.get("username"));
			
			Map<String, Object> data = new HashMap<>();
			data.put("username", (String) map.get("username"));
			data.put("password", password);
			data.put("licence_key", licenseKey);
			
			dataList.add(data);
			
			map.put("password", ShaEncoder.encode(password));
			map.put("licence_key", licenseKey);
		});

		userService.add(json);
		
		for (Map<String, Object> regData : dataList) {
			Notification notification = new TelegramNotification(regData);
			notification.send();
		}
		
	}
	
	@RequestMapping("/user/update")
	public void updateUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
	
		logger.info("update");
		
		String json = JSON.getStringFromReader(request.getReader());
		List<Map<String, Object>> list = JSON.getList(json);
		
		userService.update(list);
		
	}
	
	@RequestMapping("/user/delete")
	public void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		logger.info("delete");
		
		String json = JSON.getStringFromReader(request.getReader());
		List<Map<String, Object>> list = JSON.getList(json);
		
		userService.delete(list);
	}
	
	@RequestMapping("/user/read")
	public @ResponseBody String getUser(HttpServletRequest request, HttpServletResponse response) throws JsonGenerationException, JsonMappingException, IOException {
		
		List<Map<String, Object>> list = userService.get();
		
		return JSON.getInstance().writeValueAsString(list);
	}
	
}