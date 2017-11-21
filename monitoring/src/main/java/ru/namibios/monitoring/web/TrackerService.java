package ru.namibios.monitoring.web;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import ru.namibios.monitoring.model.User;

public class TrackerService {

	private static final String SQL_SELECT_ALL_ISSUE = "select * from fishing.issues";
	
	private static final String SQL_WHERE_USER = " where creator = ?";
	
	private static final String SQL_INSERT_ISSUE = "insert into fishing.issues(name, type, creator, description )"
												 + "values (:name, :type, :creator, :description )";
	
	private static final String SQL_SELECT_ISSUE = "select * from fishing.issues where id = ?";
	
	private static final String SQL_UPDATE_ISSUE = "update fishing.issues set name = :name, type = :type, status = :status, description = :description where id = :id";
	
	private static final String SQL_REF_STATUS = "select * from fishing.issue_status";

	private static final String SQL_REF_TYPE = "select * from fishing.issue_type";
	
	@Autowired
	private NamedParameterJdbcTemplate njdbc;
	
	@Autowired
	private JdbcTemplate jdbc;
	
	public List<Map<String, Object>> getIssues() {
		
		User user = new User();
		
		return user.getAuthority().contains(new SimpleGrantedAuthority(User.ROLE_ADMIN))
				? jdbc.queryForList(SQL_SELECT_ALL_ISSUE)
				: jdbc.queryForList(SQL_SELECT_ALL_ISSUE + SQL_WHERE_USER, user.getName());
		
	}
	
	public void addIssue(MapSqlParameterSource param) {
		njdbc.update(SQL_INSERT_ISSUE, param);
		
	}
	
	public Map<String, Object> getIssue(String id) {
		return jdbc.queryForMap(SQL_SELECT_ISSUE, id);
	}

	public List<Map<String, Object>> getAllStatus() {
		return jdbc.queryForList(SQL_REF_STATUS);
	}

	public List<Map<String, Object>> getAllType() {
		return jdbc.queryForList(SQL_REF_TYPE);
	}

	public void updateIssue(MapSqlParameterSource param) {
		njdbc.update(SQL_UPDATE_ISSUE, param);
	}
	
}