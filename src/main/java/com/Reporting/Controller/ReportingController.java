package com.Reporting.Controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Reporting.service.ReportingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

@RestController
public class ReportingController 
{
	@Autowired
	private ReportingService reportingService;
	
	@GetMapping("/GenerateReport")
	public void GenerateReport() throws JsonMappingException, JsonProcessingException
	{
		reportingService.GenerateDepartmentReport();
	}
}
