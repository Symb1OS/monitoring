package ru.namibios.monitoring.websocket.chat;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.server.standard.SpringConfigurator;

import ru.namibios.monitoring.web.service.SocketService;

@ServerEndpoint(value="/chat/{user}",
				decoders=MessageDecoder.class,
				encoders=MessageEncoder.class,
				configurator=SpringConfigurator.class)
public class ChatEndPoint {
	
	private static final Logger logger = Logger.getLogger(ChatEndPoint.class); 
	
	private static final Set<Session> clients = new CopyOnWriteArraySet<>();
	
	private SocketService socketService;
	
	@Autowired
	public ChatEndPoint(SocketService socketService) {
		this.socketService = socketService;
	}

	@OnOpen
	public void onOpen(@PathParam("user") String user, Session session) {

		session.getUserProperties().put("user", user);
		
		logger.info("Connetion open for " + user);
		
		clients.add(session);
		logger.info("Sessions " + clients.size());
	}
	
	@OnMessage
	public void onMessage(Message message, Session session) {
		logger.info("Message from [" + session.getUserProperties().get("user") + "] message: " + message );
		
		broadcast(message);
		saveHistory(message);
	}
	
	private void saveHistory(Message message) {
		socketService.saveHistory(message);
	}

	@OnClose
	public void onClose(Session session) {
		logger.info("Close connection for " + session.getUserProperties().get("user"));
		
		clients.remove(session);
	}
	
	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
		logger.error("Exception " + t);
	}
	
	public void broadcast(Message message) {
		
		clients.forEach((session) ->{
			String user = (String) session.getUserProperties().get("user");
			if(user.equals(message.getTo())) {
				
				try {
					logger.info("Sended message " + message);
					
					session.getBasicRemote().sendObject(message);
					
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
				
			}
		});
			
	}
}