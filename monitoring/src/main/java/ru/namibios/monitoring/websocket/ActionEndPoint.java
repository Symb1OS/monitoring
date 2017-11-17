package ru.namibios.monitoring.websocket;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.EncodeException;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ru.namibios.monitoring.model.Action;
import ru.namibios.monitoring.model.User;

@Controller
@ServerEndpoint(value = "/action/{hash}",
				encoders = ActionEncoder.class,
				decoders = ActionDecoder.class)
public class ActionEndPoint {
	
	private static final Logger logger =  Logger.getLogger(ActionEndPoint.class);
	
	private static final Set<ActionEndPoint> fishClients = new CopyOnWriteArraySet<>();
	
	private String hash;
	
	private Session session;
    
    public String getHash() {
		return hash;
	}

	public Session getSession() {
		return session;
	}

	@OnOpen
    public void open(Session session, @PathParam("hash") String hash) {
    	logger.info("Open connection for " + hash);
    	
    	this.session = session;
    	this.hash = hash;
    	
    	fishClients.add(this);
    	
    }
    
    @OnClose
    public void close(Session session) {
    	logger.info("Close session for [" + hash + "]");
    	fishClients.remove(this);
    	
    }
    
    @OnMessage
    public void message(Session session, String message){
    	logger.info("Message from [" + hash + "] " + message);
    	
    }
    
    @OnError
    public void error(Session session, Throwable t) {
    	logger.error("Exception " + t);
    }
    
    @RequestMapping("/action")
    public @ResponseBody boolean send(@PathParam("command") String command,	
						    		  @PathParam("username") String username,
						    		  @PathParam("message") String message) {
    	
    	User user = new User();
    	Action action = new Action(command, username, message);
    	
    	fishClients.forEach( point -> {
    		synchronized (point) {
	    		try {
	    			if(point.getHash().equals(user.getHash())) {
	    				point.getSession().getBasicRemote().sendObject(action);
	    			}
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				} 
    		}
    	});
    	
    	return true;
    }
}