package ru.namibios.monitoring.web.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.server.PathParam;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ru.namibios.monitoring.model.User;
import ru.namibios.monitoring.utils.JSON;
import ru.namibios.monitoring.web.service.TrackerService;

@Controller
@RequestMapping("/tracker")
public class TrackerController {

	private static final Logger logger = Logger.getLogger(TrackerController.class);
	
	@Autowired
	private TrackerService trackerService;
	
	@RequestMapping(method=RequestMethod.GET)
	public ModelAndView getFace() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("tracker");
		
		return mav;
	}
	
	@RequestMapping(value="/status")
	public @ResponseBody List<Map<String, Object>> getRefStatus(){
		return trackerService.getAllStatus();
		
	}
	
	@RequestMapping(value="/type")
	public @ResponseBody List<Map<String, Object>> getRefType(){
		return trackerService.getAllType();
		
	}
	
	@RequestMapping(value="/issues")
	public @ResponseBody List<Map<String, Object>> getIssues(){
		
		return trackerService.getIssues();
		
	}
	
	@RequestMapping(value="/issues/get", method=RequestMethod.GET)
	public @ResponseBody Map<String, Object> getIssue(@PathParam("id") String id) throws JsonGenerationException, JsonMappingException, IOException{
		
		HashMap<String, Object> response = new HashMap<>();
		
		response.put("success", true);
		response.put("data", JSON.getInstance().writeValueAsString(trackerService.getIssue(id)));
		
		return response;
	}
	
	@RequestMapping(value="/issues/update", method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> updateIssue(
				@PathParam("id") String id,
				@PathParam("name") String name,
				@PathParam("type") String type,
				@PathParam("status") String status,
				@PathParam("description") String description){
		
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("id", id);
		param.addValue("name", name);
		param.addValue("type", type);
		param.addValue("status", status);
		param.addValue("description", description);
		
		trackerService.updateIssue(param);
		
		Map<String, Object> response = new HashMap<String, Object>();
		response.put("success", true);
		response.put("message", "Задача обновлена");
		
		return response;
				
	}
	
	@RequestMapping(value="/issues/add", method=RequestMethod.POST)
	public @ResponseBody Map<String, Object> addIssue(@PathParam("name") String name,
						@PathParam("type") String type,
						@PathParam("description") String description) {
		
		logger.info("[" + new User().getName() + "] Add issue");
		
		MapSqlParameterSource param = new MapSqlParameterSource();
		param.addValue("name", name);
		param.addValue("type", type);
		param.addValue("creator", new User().getName());
		param.addValue("description", description);
		
		trackerService.addIssue(param);
		
		HashMap<String, Object> response = new HashMap<>();
		response.put("success", true);
		
		return response;
		
	}
	
}