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

@ServerEndpoint(value="/chat/{user}",
				decoders=MessageDecoder.class,
				encoders=MessageEncoder.class)
public class ChatEndPoint {
	
	private static final Logger logger = Logger.getLogger(ChatEndPoint.class); 
	
	private static final Set<ChatEndPoint> clients = new CopyOnWriteArraySet<>();
	
	private String user;
	
	private Session session;
	
	public String getUser() {
		return user;
	}

	public Session getSession() {
		return session;
	}

	@OnOpen
	public void onOpen(@PathParam("user") String user, Session session) {
		
		this.user = user;
		this.session = session;
		
		logger.info("Connetion open for " + user);
		
		clients.add(this);
		
	}
	
	@OnMessage
	public void onMessage(Message message, Session session) {
		logger.info("Message from [" + user + "] message: " + message );
		broadcast(message);
	}
	
	@OnClose
	public void onClose(Session session) {
		logger.info("Close connection for " + user);
		
		clients.remove(this);
	}
	
	@OnError
	public void onError(Throwable t) {
		t.printStackTrace();
		logger.error("Exception " + t);
	}
	
	public void broadcast(Message message) {
		
		clients.forEach(point -> {
			String user = point.getUser();
			if (user.equals(message.getTo())) {
				try {
					
					point.getSession().getBasicRemote().sendObject(message);
					
				} catch (IOException | EncodeException e) {
					e.printStackTrace();
				}
			}
			
		});
			
	}

}