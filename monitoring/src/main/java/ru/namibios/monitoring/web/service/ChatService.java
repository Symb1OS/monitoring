package ru.namibios.monitoring.web.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import ru.namibios.monitoring.model.Children;
import ru.namibios.monitoring.model.User;
import ru.namibios.monitoring.websocket.chat.Message;

@Service
public class ChatService {

	private static final String SQL_SELECT_USERS = 
			"select username from fishing.users";
	
	private static final String SQL_SELECT_ADM_USERS = 
			"select user.username from fishing.users user "
		  + "inner join fishing.user_roles role on user.username = role.username "
		  + "where role.role = 'ROLE_ADMIN' ";
	
	private static final String SQL_SELECT_HISTORY = 
			"select time, fromuser, touser, data from fishing.chat "
		  + "where fromuser = ? and touser = ?"; 
	
	private static final String SQL_SAVE_HISTORY = 
			"insert into fishing.chat(fromuser, touser, data) values (?, ?, ?)";
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Children> getUsers(Collection<? extends GrantedAuthority> auth) {
				
		final String SQL = auth.contains(new SimpleGrantedAuthority(User.ROLE_ADMIN)) 
				? SQL_SELECT_USERS 
				: SQL_SELECT_ADM_USERS;
		
		return jdbc.query(SQL, new ResultSetExtractor<List<Children>>() {

			List<Children> childrens = new ArrayList<>();
			
			@Override
			public List<Children> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				while(rs.next()) {
					Children children = new Children(rs.getString("username"));
					childrens.add(children);
				}
				
				return childrens;
			}
			
		});
	}
	
	public List<Map<String, Object>> getHistory(String from, String to ){
	
		return jdbc.query(SQL_SELECT_HISTORY, new Object[] {from , to}, new ResultSetExtractor<List<Map<String, Object>>>(){

			List<Map<String, Object>> list = new ArrayList<>();
			@Override
			public List<Map<String, Object>> extractData(ResultSet rs) throws SQLException, DataAccessException {
				
				while(rs.next()) {
					Map<String, Object> map = new HashMap<>();
					map.put("time", rs.getTimestamp("time"));
					map.put("from", rs.getString("fromuser"));
					map.put("to", rs.getString("touser"));
					map.put("data", rs.getString("data"));
					
					list.add(map);
				}
				
				return list;
			}
			
		});
		
	}
	
	public void saveHistory(Message message) {
		jdbc.update(SQL_SAVE_HISTORY, message.getFrom(), message.getTo(), message.getData());
		
	}
}