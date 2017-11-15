package ru.namibios.monitoring.websocket;

import java.io.IOException;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import ru.namibios.monitoring.model.Action;
import ru.namibios.monitoring.utils.JSON;

public class ActionEncoder implements Encoder.Text<Action>{

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String encode(Action action) throws EncodeException {
		try {
			return JSON.getInstance().writeValueAsString(action);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}


}
