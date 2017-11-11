package ru.namibios.monitoring.model;

import java.util.Map;

import ru.namibios.monitoring.utils.Http;


public class TelegramNotification extends Notification {

	public TelegramNotification(String message) {
		super(message);
	}
	
	public TelegramNotification(Map<String, Object> map) {
		super(String.format(Notification.REG_DATA, map.get("username"), map.get("password"), map.get("licence_key")));
	}

	@Override
	public void send() {
		try {
			
			Http http = new Http();
			http.sendTelegram(Notification.KEY_TELEGRAM, message);
		
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}