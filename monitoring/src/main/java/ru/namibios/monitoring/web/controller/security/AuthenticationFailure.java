package ru.namibios.monitoring.web.controller.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import ru.namibios.monitoring.model.LoginStatus;
import ru.namibios.monitoring.utils.JSON;

public class AuthenticationFailure implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException auth)
			throws IOException, ServletException {
		
		LoginStatus loginStatus = new LoginStatus(false, false, null, auth.getLocalizedMessage());
		
		JSON.getInstance().writeValue(response.getOutputStream(), loginStatus);
		
	}

}
