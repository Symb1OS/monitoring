package ru.namibios.monitoring.web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {
	
	private static final String SQL_GET_USER = "select * from fishing.users";
	
	private static final String SQL_UPDATE_USER = "update fishing.users set enabled = :enabled, date_valid = :date_valid where username = :username";
	
	private static final String SQL_ADD_USER = "insert into fishing.users (username, password, enabled, licence_key, date_create, date_valid) values(:username, :password, :enabled, :licence_key, :date_create, :date_valid )";
	
	private static final String SQL_DELETE_USER = "delete from fishing.users where username = :username";

	private static final String SQL_ADD_USER_ROLE = "insert into fishing.user_roles ( username, role) VALUES (:username, 'ROLE_USER')"; 

	@Autowired
	private JdbcTemplate jdbc;
	
	@Autowired
	private NamedParameterJdbcTemplate namedJdbc;
	
	public List<Map<String, Object>> get(){
		return jdbc.queryForList(SQL_GET_USER);
	}
	
	public void add(List<Map<String, Object>> list) {
		
		HashMap<String, Object>[] params = list.toArray(new HashMap[list.size()]);
		
		namedJdbc.batchUpdate(SQL_ADD_USER, params);
		namedJdbc.batchUpdate(SQL_ADD_USER_ROLE, params);
	}
	
	public void update(List<Map<String, Object>> list) {
		namedJdbc.batchUpdate(SQL_UPDATE_USER, list.toArray(new HashMap[list.size()]));
	}
	
	public void delete(List<Map<String, Object>> list) {
		namedJdbc.batchUpdate(SQL_DELETE_USER, list.toArray(new HashMap[list.size()]));
	}
	
}