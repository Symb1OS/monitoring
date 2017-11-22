package ru.namibios.monitoring.websocket.chat;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import ru.namibios.monitoring.utils.JSON;

public class MessageDecoder implements Decoder.Text<Message>{

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Message decode(String s) throws DecodeException {
		try {
			return JSON.getInstance().readValue(s, Message.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public boolean willDecode(String s) {
		return s != null;
	}

}
