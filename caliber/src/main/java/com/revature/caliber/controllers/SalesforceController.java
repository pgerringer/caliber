package com.revature.caliber.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.revature.caliber.security.models.SalesforceUser;
import com.revature.caliber.services.SalesforceService;

@RestController
public class SalesforceController {

	private static final Logger log = Logger.getLogger(SalesforceController.class);
	
	@Autowired
	private SalesforceService salesforceService;

	public void setSalesforceService(SalesforceService salesforceService) {
		this.salesforceService = salesforceService;
	}
	
	
	
	@RequestMapping(value="/salesforce/token", method=RequestMethod.GET)
	public String getSalesforceToken(){
		return ((SalesforceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.getSalesforceToken().getAccessToken();
	}
	
}
