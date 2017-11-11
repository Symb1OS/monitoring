package ru.namibios.monitoring.web;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import ru.namibios.monitoring.model.User;
import ru.namibios.monitoring.utils.Const;
import ru.namibios.monitoring.utils.ImageUtils;


@Controller
public class ApplicationController {
	
	private static final Logger logger = Logger.getLogger(ApplicationController.class);
	
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
	
	@RequestMapping(value="/upload")
	public void uploadImage(@RequestParam("HASH") String hash,
							@RequestParam("SCREEN") MultipartFile multipartFile) throws JsonParseException, JsonMappingException, IOException {
		
		logger.info("Start upload file for [" + hash + "]");
		
	    int status = service.checkAuthorization(hash);
		if(status != Const.AUTH_OK) {
			logger.info("[" + hash + "] - Authentification bad");
			return;
		}
		
		InputStream in = new ByteArrayInputStream(multipartFile.getBytes());
		BufferedImage image = ImageIO.read(in);
		
		File file = new File(Const.UPLOAD_DIR + hash + ".jpg");
		ImageIO.write(image, "jpg", file);
	}

}