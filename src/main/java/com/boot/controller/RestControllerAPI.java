package com.boot.controller;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestControllerAPI {
	
	Logger log = Logger.getLogger(RestControllerAPI.class.getName());

	@RequestMapping(value = "restAPI", method = RequestMethod.GET)
	public String get() throws ClientProtocolException, IOException{
		
		System.out.println("####################################################################");
		String uri = "http://api.openweathermap.org/data/2.5/weather?q=Bangalore,In&appid=c1bb32ab5e3f3207378c548dc39eb581";
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try 
        {
            HttpGet httpget = new HttpGet(uri);
            httpget.setHeader("Content-Type", "application/json");
            // String response = httpclient.execute(httpget, new BootResponseHandler());
            CloseableHttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject obj = null;
            try {
				obj = new JSONObject(result);
				JSONArray arr = (JSONArray) obj.get("weather");
				JSONObject objOfWeather = arr.getJSONObject(0);
				System.out.println("Weather Response "+obj);
				result = objOfWeather.getString("description");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            log.info(result);
            return "Bangalore Weather Right Now .......... "+result;
        } finally {
            httpclient.close();
        }
		
	}
}
