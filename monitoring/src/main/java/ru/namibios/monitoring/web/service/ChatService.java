package ru.namibios.monitoring.web.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import ru.namibios.monitoring.model.Children;
import ru.namibios.monitoring.model.User;

public class ChatService {

	private static final String SQL_SELECT_USERS = "select username from fishing.users";
	
	private static final String SQL_SELECT_ADM_USERS = 
			"select user.username from fishing.users user "
		  + "inner join fishing.user_roles role on user.username = role.username "
		  + "where role.role = 'ROLE_ADMIN' ";
	
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
}