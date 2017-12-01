package ru.namibios.monitoring.websocket.chat;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import ru.namibios.monitoring.utils.JSON;

public class MessageEncoder implements Encoder.Text<Message>{

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(Message message) throws EncodeException {
		String json = null;
		try {
			json  = JSON.getInstance().writeValueAsString(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return json;
	}

}
