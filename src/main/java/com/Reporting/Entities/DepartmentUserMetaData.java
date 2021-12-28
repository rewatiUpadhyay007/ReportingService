package com.Reporting.Entities;

import java.util.List;


public class DepartmentUserMetaData 
{
	
	private String id;
	private List<String> metaDataColumns;
	
	public DepartmentUserMetaData(String id, List<String> metaDataColumns) 
	{
		super();
		this.id = id;
		this.metaDataColumns = metaDataColumns;
	}
	public DepartmentUserMetaData() 
	{
		super();
	}
	public String getId() 
	{
		return id;
	}
	public void setId(String id) 
	{
		this.id = id;
	}
	public List<String> getMetaDataColumns() 
	{
		return metaDataColumns;
	}
	public void setMetaDataColumns(List<String> metaDataColumns) 
	{
		this.metaDataColumns = metaDataColumns;
	}

}
