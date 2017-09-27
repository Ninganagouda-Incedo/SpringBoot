package com.boot.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@SuppressWarnings("deprecation")
@Component
public class RestClient {
	
	public static void main(String[] args) throws Exception {
		HashMap< String, String> map = new HashMap<>();
		map.put("Content-Type", "application/json");
		
		//System.out.println(get("http://cricapi.com/api/matches?apikey=WHZybJYEPzgyc67Hqvd4Y4Anpkq1",map));
		JSONObject obj = new JSONObject(get("http://cricapi.com/api/matches?apikey=WHZybJYEPzgyc67Hqvd4Y4Anpkq1",map));
		//System.out.println("-------------------------------------------------------------------------------------------");
		//System.out.println(obj);
		//System.out.println("-------------------------------------------------------------------------------------------");
		System.out.println("-------------------------------------------------------------------------------------------");
		JSONArray arr = new JSONArray();
		arr = obj.getJSONArray("matches");
		for(int i=0;i<arr.length();i++)
		{
			JSONObject ob = new JSONObject(arr.get(i).toString());
			if(ob.getString("team-1").equalsIgnoreCase("India"))
			{
				System.out.println(new JSONObject(arr.get(i).toString()));
				System.out.println( " -----------------------------------@@@@@@@@@@@@@@@@@@SSS@@@@@@@@@@@");
			}
			
		}
		System.out.println("-------------------------------------------------------------------------------------------");
		// get("https://www.google.co.in/search?q=google+api+call",map);
		// get("http://localhost:8083/nfv-orchestration/user/1",map);
	}//ASDDAS
	
	public final static String get(String uri, Map<String, String> map) throws Exception {
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpGet httpget = new HttpGet(uri);
            if (null != map) {
	            for (Map.Entry<String, String> entry: map.entrySet()) {
	            	httpget.setHeader(entry.getKey(), entry.getValue());
	            	//System.out.println("RestClient.get.header:" + entry.getKey() + ":" + entry.getValue());
	            }
            }
            return httpclient.execute(httpget, new BootResponseHandler());
        } finally {
            httpclient.close();
        }
    }


}

