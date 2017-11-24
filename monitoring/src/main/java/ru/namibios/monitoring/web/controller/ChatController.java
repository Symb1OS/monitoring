package ru.namibios.monitoring.web.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.apache.log4j.Logger;
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
import ru.namibios.monitoring.websocket.chat.Message;

@Controller
@RequestMapping("/chat")
public class ChatController {

	private static final Logger logger = Logger.getLogger(ChatController.class);
	
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
	
	@RequestMapping(value = "/history", method = RequestMethod.GET)
	public @ResponseBody List<Map<String, Object>> history(@PathParam("username") String username){
		
		User user = new User();
		logger.info("Load history for " + user.getName() + " from " + username);
		
		return chatService.getHistory(username, user.getName());
	}
	
	@RequestMapping(value = "/history/save", method = RequestMethod.GET)
	public void history(
			@PathParam("from") String from,
			@PathParam("to") String to,
			@PathParam("data") String data){
		
		Message message = new Message();
		message.setData(data);
		message.setTo(to);
		message.setFrom(from);
		
		chatService.saveHistory(message);
	}
	
}