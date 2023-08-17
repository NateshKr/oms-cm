package com.moglix.omscm.Utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.moglix.omscm.Model.HttpResponse;

@Component 
public class HttpConnectionHelper {

private static final Logger logger = LoggerFactory.getLogger(HttpConnectionHelper.class);
	
	public HttpResponse sendGetWithHead(String urlParams,  JSONObject requesthead, String contentType) throws Exception {
		
		logger.debug("SendGet : Url [" +urlParams + "], head["+requesthead+"]");
		
		HttpResponse response = new HttpResponse();

		URL obj = new URL(urlParams);

		if (logger.isDebugEnabled()) {
			logger.debug("\nSending 'GET' request to URL : " + urlParams);
		}

		HttpURLConnection con = (HttpURLConnection) obj.openConnection();

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestMethod("GET");
//		con.setRequestProperty("Accept", "application/json");

		
		con.connect();

		InputStream is = null;
		int responseCode = con.getResponseCode();
		String responseMessage = con.getResponseMessage();
		if (responseCode >= 400) {
		    is = con.getErrorStream();
		} else {
		    is = con.getInputStream();
		}

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();

		response.setContent(result.toString());
		response.setResponseCode(responseCode);
		response.setResponseMesage(responseMessage);
		if(logger.isDebugEnabled()){
			logger.debug("SendPostRequest : response [" +response + "]");
		}

		return response;
	}
	
	public HttpResponse sendPostRequestwithHead(String argUrl, JSONObject requesthead, JSONObject requestdata, String contentType) throws Exception {
		
		if(logger.isDebugEnabled()){
			logger.debug("Sending PostRequest :: Request is :: Url [" +argUrl + "], paramString[ Head ["+requesthead+"], Body ["+requestdata+"]], contentType["+contentType+"]");
		}
		
		HttpResponse response = new HttpResponse();
		URL url = new URL(argUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false);
		con.setRequestMethod("POST");

		
		
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
	
		con.connect();
		
		OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
		writer.write(requestdata.toString());
		writer.flush();
		writer.close();

		InputStream is = null;
		int responseCode = con.getResponseCode();
		String responseMessage = con.getResponseMessage();
		if (responseCode >= 400) {
		    is = con.getErrorStream();
		}
		else {
		    is = con.getInputStream();
		}

		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
		rd.close();

	   // System.out.println(result.toString());

		response.setContent(result.toString());
		response.setResponseCode(responseCode);
		response.setResponseMesage(responseMessage);
		if(logger.isDebugEnabled()){
			logger.debug("Response Recieved :: Response [" +response + "]");
		}
		return response;
	}

}
