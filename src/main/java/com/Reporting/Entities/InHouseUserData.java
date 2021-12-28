package com.Reporting.Entities;

import java.util.Map;


public class InHouseUserData {
   
    private Long id;
    private String department;
    private Map<String, String> departmentData;
  
	public InHouseUserData(Long id, String department, Map<String, String> departmentData) {
		super();
		this.id = id;
		this.department = department;
		this.departmentData = departmentData;
	}
	public InHouseUserData() 
	{
		super();
	}
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

  public Map<String, String> getDepartmentData() {
		return departmentData;
	}
	public void setDepartmentData(Map<String, String> departmentData) {
		this.departmentData = departmentData;
	}
	
}