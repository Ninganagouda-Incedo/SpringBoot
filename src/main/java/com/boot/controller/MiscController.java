package com.boot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiscController {

	@Value("${username.name1}")
	private String name;
	
	@RequestMapping(name="testApi",method=RequestMethod.GET)
	public ResponseEntity<String> response()
	{
		List list = new ArrayList<String>();
		list.add("Manu");
		list.add("tanu");
		list.add("venu");
		System.out.println("Response Entity Checking "+HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
		System.out.println();
		return new ResponseEntity<>(list.toString(),HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
	}
}
