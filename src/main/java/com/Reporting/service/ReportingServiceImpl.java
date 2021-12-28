package com.Reporting.service;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.Reporting.Entities.AzureModel;
import com.Reporting.Entities.DepartmentUserMetaData;
import com.Reporting.Entities.InHouseUserData;
import com.Reporting.Entities.PartnerDataUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ReportingServiceImpl implements ReportingService
{
	@Value("${cosmosDBService}")
	private  String cosmosDBService;
	@Value("${DepartmentDataEndpoint}")
	private  String DepartmentDataEndpoint;
	@Value("${UserDataEndpoint}")
	private  String UserDataEndpoint;	
	@Value("${NoMatchFileName}")
	private  String NoMatchFileName;
	@Value("${MatchFileSuffix}")
	private  String MatchFileSuffix;

	@Value("${ServiceBus}")
	private  String ServiceBus;
	@Value("${ServiceBusReadEndpoint}")
	private  String ServiceBusReadEndpoint;

	@Value("${BLOBService}")
	private  String BLOBService;
	@Value("${BLOBWriteToFileURL}")
	private  String BLOBWriteToFileURL;
	@Value("${BLOBReportContainer}")
	private  String BLOBReportContainer;

	
	@Autowired
	private RestTemplate restTemplate;
	
	List<DepartmentUserMetaData> DepartmentInfo;
	private List<DepartmentUserMetaData> getMetadata()
	{
		if(DepartmentInfo ==null)
		{
			String requestUrl = cosmosDBService+DepartmentDataEndpoint;
			System.out.println(requestUrl);
			DepartmentUserMetaData[] metdata = this.restTemplate.getForObject(requestUrl, DepartmentUserMetaData[].class);
			//System.out.println("Fetched metadata items: "+metdata.size()+ "Firts item: " + metdata.get(0).getId());
			
			List<DepartmentUserMetaData> list = Arrays.asList(metdata);
			DepartmentInfo = list;		
		}			
		return DepartmentInfo;
	}
	
	private InHouseUserData getUserData(Long Id)
	{
		String requestUrl = cosmosDBService+UserDataEndpoint+"/"+Id;
		System.out.println("Requesting user details for ID: "+Id+"on "+requestUrl);
		InHouseUserData userdata = this.restTemplate.getForObject(requestUrl, InHouseUserData.class);
		System.out.println("Received Data for user: "+userdata);
		return userdata;
	}
	
	private List<String> GetTopMessages()
	{
		String url =ServiceBus+ServiceBusReadEndpoint;
		List<String>  messages = this.restTemplate.getForObject(url, List.class);		
		return messages;
	}
	
	private void AddDataToReport(String data,String fileName)
	{
		String blobUrl = BLOBService+BLOBWriteToFileURL;
		System.out.println("AddDataToReport with URL: "+ blobUrl);
		AzureModel azM = new AzureModel(BLOBReportContainer, fileName,data);
		var result = this.restTemplate.postForObject(blobUrl, azM, String.class);	
		System.out.println(result);
	}
	@Override
	public void GenerateDepartmentReport() throws JsonMappingException, JsonProcessingException 
	{
		//get document metadata details
		var metaData = getMetadata();
		System.out.println("Fetched Metadata. Department Item count: "+ metaData.size());
		//Read from topic 
		List<String> messages = GetTopMessages();
		 while(messages.size() >0)
		 {
			for(String message: messages)
			{
				System.out.println("Provessinng user: "+message);
				ObjectMapper objectMapper = new ObjectMapper();
				PartnerDataUser user= objectMapper.readValue(message, PartnerDataUser.class);
				
				System.out.println("Checking to see if the user data is available");
				//Get corresponding in-house user data from cosmos db 
				var inHouseData = getUserData(user.getUserId());
				System.out.println("In house user data" + inHouseData);
				var department = user.getDepartment();
				System.out.println("User Department: "+ department);
				String fileName= department+MatchFileSuffix;//"MatchReport.csv";	
				String content = user.getUserId()+","+user.getName()+","+user.getPhone()+","+user.getDepartment(); 
				System.out.println("Original Content: "+content);
				//Case no match found
				if(inHouseData == null)
				{
					//Write to No-match report
					content +=",Missing User"; 
					fileName = NoMatchFileName;
				}
				//case match found
				else
				{			
					//write to Department Report	
					System.out.println("Getting Deprtment metadata:");
					DepartmentUserMetaData dept = metaData.stream().filter(x -> x.getId().equals(department)).findAny().orElse(null);
					if(dept!=null)
					{
						if(dept.getId().equals(inHouseData.getDepartment()))
						{
							//get metadata
							List<String> departmentMetaData = dept.getMetaDataColumns();	
							var userDepData = inHouseData.getDepartmentData();
							for(String column:departmentMetaData)
							{
								System.out.println("Fetching data for column : "+column);
								content += ","+userDepData.get(column);
							}
						}
						else
						{
							 content +=",Department Metadata mismatch"; 
							 fileName = NoMatchFileName;
						}	
						
					}
					
					else
					{
						 content +=",Missing Department Metadata"; 
						 fileName = NoMatchFileName;
					}	
				}
				System.out.println("Adding content\nContent: "+content+"\nFile Name: "+fileName);
				AddDataToReport(content,fileName);
			}
			messages = GetTopMessages();
		 }		
	}
	
	

}
