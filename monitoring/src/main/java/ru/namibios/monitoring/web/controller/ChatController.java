package ru.namibios.monitoring.web.controller;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import ru.namibios.monitoring.model.Children;
import ru.namibios.monitoring.model.TreeUser;
import ru.namibios.monitoring.model.User;
import ru.namibios.monitoring.web.service.ChatService;

@Controller
@RequestMapping("/chat")
public class ChatController {

	@Autowired
	private ChatService chatService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView face() {
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("user", new User().getName());
		
		mav.setViewName("chat");
		
		return mav;
	}
	
	@RequestMapping("/users")
	public @ResponseBody String treeUser() throws JsonGenerationException, JsonMappingException, IOException {
		
		User user = new User();
		
		List<Children> childrens = chatService.getUsers(user.getAuthority());
		
		TreeUser tree = new TreeUser();
		tree.setChildren(childrens);
		
		return new Gson().toJson(tree);
	}
	
}