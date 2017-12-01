package ru.namibios.monitoring.web.service;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import ru.namibios.monitoring.websocket.chat.Message;

@Service
public class SocketService {

	private static final String SQL_SAVE_HISTORY = 
			"insert into fishing.chat(fromuser, touser, data) values (?, ?, ?)";
	
	private JdbcTemplate jdbc;

	public void setJdbc(DataSource dataSource ) {
		this.jdbc = new JdbcTemplate(dataSource);
	}
	
	public void saveHistory(Message message) {
		jdbc.update(SQL_SAVE_HISTORY, message.getFrom(), message.getTo(), message.getData());
		
	}
	
}
