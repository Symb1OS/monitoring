package ru.namibios.monitoring.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import ru.namibios.monitoring.utils.Const;
import ru.namibios.monitoring.utils.JSON;


@Controller
@RequestMapping("/version")
public class VersionController {
	
	@RequestMapping("")
	public ModelAndView getFace() {
		
		ModelAndView mav = new ModelAndView();
		mav.setViewName("version");
		
		return mav;
	}
	
	@RequestMapping("/files")
	public @ResponseBody String getVersionList() throws JsonGenerationException, JsonMappingException, IOException{

		List<Map<String, Object>> list = new ArrayList<>();
		
		File dir = new File(Const.VERSION_DIR);
		for (File file : dir.listFiles()) {
			if(file.isFile()) {
				Map<String, Object>  map = new HashMap<>();
				map.put("name", file.getName());
				map.put("size", file.length());
				list.add(map);
			}
		}
		
		return JSON.getInstance().writeValueAsString(list);
	}
	
	@RequestMapping("/download")
	public void download(HttpServletRequest request, HttpServletResponse response) {
		
		String name = request.getParameter("name");
		
		File dir = new File(Const.VERSION_DIR);
		
		for (File file : dir.listFiles()) {
			if(file.getName().equals(name)){
				ServletOutputStream out = null;
				try {
					
					byte[] bytes = Files.readAllBytes(file.toPath());
						
					response.setHeader("Content-Disposition", "attachment;filename=" + name);
					response.setContentType("application/zip");
					response.setContentLength(bytes.length);
					
					out = response.getOutputStream();
					out.write(bytes);
					out.close();
					
				}catch (IOException ioe) {
					ioe.printStackTrace();
				}finally {
					IOUtils.closeQuietly(out);
				}
			}
		}
	}
	
}