package ru.namibios.monitoring.web.controller.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import ru.namibios.monitoring.model.LoginStatus;
import ru.namibios.monitoring.utils.JSON;

public class AuthentificationSuccess implements AuthenticationSuccessHandler{

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication auth)
			throws IOException, ServletException {

		LoginStatus loginStatus = new LoginStatus(true, auth.isAuthenticated(), auth.getName(), null);
		
		ServletOutputStream out = response.getOutputStream();
		JSON.getInstance().writeValue(out, loginStatus);
		
	}

}
