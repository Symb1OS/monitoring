package ru.namibios.monitoring.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.namibios.monitoring.utils.Const;
import ru.namibios.monitoring.web.service.Service;

@Component
public class Sheduler {

	private static final Logger logger = Logger.getLogger(Sheduler.class);
	
	@Autowired
	private Service service;
	
	public void validDate() {
		logger.info("Start checking expired licence key.. ");
		int updatedRow = service.disableExpiredLicence();
		
		logger.info(String.format(Const.UPDATED_ROW, updatedRow));
	}
	
}
