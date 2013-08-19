package com.kth.seds.utilities;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONStringer;


public class Communication {
	
	private final static int DEFAULT_TIMEOUT = 15000;
	
	private String serverAddress;
	private JSONStringer jsonParameter;
	private int timeOutTime;
	
	public Communication(String serverAddress,JSONStringer jsonParameter){
		this.serverAddress = serverAddress;
		this.jsonParameter = jsonParameter;
		this.timeOutTime = DEFAULT_TIMEOUT;
	}
	
	public Communication(String serverAddress,JSONStringer jsonParameter, int timeOutTime){
		this.serverAddress = serverAddress;
		this.jsonParameter = jsonParameter;
		this.timeOutTime = timeOutTime;
	}

	
	public byte[] connectToServer() throws ClientProtocolException, IOException{
		HttpPost request = new HttpPost(serverAddress);
		request.setHeader("Accept", "application/json");
		request.setHeader("Content-type", "application/json");
		
		if(jsonParameter!=null){
			StringEntity entity = new StringEntity(jsonParameter.toString(),"UTF-8");
			entity.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
			entity.setContentType("application/json");
			request.setEntity(entity);	
		}
		

		DefaultHttpClient httpClient = new DefaultHttpClient();

		HttpParams httpParams = new BasicHttpParams(); 
		HttpConnectionParams.setConnectionTimeout(httpParams, timeOutTime);
		HttpConnectionParams.setSoTimeout(httpParams, timeOutTime); 
		httpClient = new DefaultHttpClient(httpParams);
		
		try{
			HttpResponse response = httpClient.execute(request);
			
			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			response.getEntity().writeTo(outstream);
			
			return outstream.toByteArray();
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}		
	}
}
