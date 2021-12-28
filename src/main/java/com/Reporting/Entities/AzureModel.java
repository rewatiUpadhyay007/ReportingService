package com.Reporting.Entities;

public class AzureModel {
	
	private String containerName;
	private String fileName;
	private String data;
	
	public AzureModel() {
	}
	
	public AzureModel(String containerName, String fileName, String data) {
		this.containerName = containerName;
		this.fileName = fileName;
		this.data = data;
	}

	public String getContainerName() {
		return containerName;
	}
	public void setContainerName(String containerName) {
		this.containerName = containerName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	
	
	

}
