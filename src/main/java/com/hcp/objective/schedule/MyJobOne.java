package com.hcp.objective.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service("jobone")
public class MyJobOne {
	public static final Logger logger = LoggerFactory.getLogger(MyJobOne.class);
	 protected void myTask() {
	    	logger.error("This is my task"+"======="+System.currentTimeMillis());
	 }
}
