package com.Reporting.Entities;

public class PartnerDataUser 
{
 private Long userId;
 private String name;
 private String phone;
 private String department;
 
 public PartnerDataUser()
 {}
public PartnerDataUser(Long userId, String name, String phone, String department) {
	super();
	this.userId = userId;
	this.name = name;
	this.phone = phone;
	this.department = department;
}
public Long getUserId() {
	return userId;
}
public void setUserId(Long userId) {
	this.userId = userId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getPhone() {
	return phone;
}
public void setPhone(String phone) {
	this.phone = phone;
}
public String getDepartment() {
	return department;
}
public void setDepartment(String department) {
	this.department = department;
}
}
