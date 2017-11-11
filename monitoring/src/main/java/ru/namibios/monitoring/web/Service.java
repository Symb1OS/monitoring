package ru.namibios.monitoring.web;

import java.io.File;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import ru.namibios.monitoring.model.ShaEncoder;
import ru.namibios.monitoring.utils.Const;

public class Service {

	@Autowired
	private JdbcTemplate jdbc;
	
	private static final String SQL_SELECT_CHECK_AUTHORIZATION = "SELECT COUNT(*) FROM fishing.users WHERE licence_key = ?";
	
	private static final String SQL_CURRENT_PASSWORD = "select COUNT(*) from fishing.users where username = ? and password = ?";
	
	private static final String SQL_CHECK_VALID_LICENCE = "select enabled from fishing.users where licence_key = ?"; 
	
	private static final String SQL_UPDATE_PASSWORD = "update fishing.users SET password = ? where username = ?";

	private static final String SQL_SELECT_SETTINGS = "select licence_key, date_valid, url_monitoring from fishing.users where username = ?";

	private static final String SQL_UPDATE_URL_MONITORING = "update fishing.users set url_monitoring = ? where username = ?";
	
	public int checkAuthorization(String hash){
		int status = jdbc.queryForObject(SQL_SELECT_CHECK_AUTHORIZATION, Integer.class, hash); 
		return status == 1 ? Const.AUTH_OK : Const.AUTH_BAD; 
	}
	
	public int checkValid(String licenceKey) {
		int status = jdbc.queryForObject(SQL_CHECK_VALID_LICENCE, Integer.class, licenceKey);
		return status == 1 ? Const.AUTH_OK : Const.AUTH_BAD;
	}
	
	public boolean checkOldPassword(String username, String oldPassword) {
		String hash = ShaEncoder.encode(oldPassword);
		int status = jdbc.queryForObject(SQL_CURRENT_PASSWORD, Integer.class , username, hash);
		return status == 1 ? true : false;
	}
	
	public int changePassword(String username, String password) {
		String hash = ShaEncoder.encode(password);
		return jdbc.update(SQL_UPDATE_PASSWORD, hash, username);
	}

	public Map<String, Object> getSettings(String username) {
		return jdbc.queryForMap(SQL_SELECT_SETTINGS, username);
	}

	public String getSnapshotName(String hash) {
		String path = "";
		String name = "";
		File files = new File(Const.UPLOAD_DIR);
		for(File file : files.listFiles()){
			if(file.isFile()) {
				name = file.getName();
				if(name.startsWith(hash)) 
					path = file.getAbsolutePath();
			}
		}
		return path;
		
	}

	public void changeUrlMonitoring(String url, String name) {
		jdbc.update(SQL_UPDATE_URL_MONITORING, url, name);
	}
}