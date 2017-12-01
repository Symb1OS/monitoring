package ru.namibios.monitoring.websocket.action;

import java.io.IOException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import ru.namibios.monitoring.model.Action;
import ru.namibios.monitoring.utils.JSON;

public class ActionDecoder implements Decoder.Text<Action>{

	@Override
	public void init(EndpointConfig config) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Action decode(String s) throws DecodeException {
		try {
			return JSON.getInstance().readValue(s, Action.class);
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
