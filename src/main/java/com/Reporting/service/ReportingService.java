package com.Reporting.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public interface ReportingService 
{
	public void GenerateDepartmentReport() throws JsonMappingException, JsonProcessingException;	
}
