package com.hcp.objective.schedule;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJob implements Job {
	public static final Logger logger = LoggerFactory.getLogger(TestJob.class);
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.error(new Date().toString());
	}

}
