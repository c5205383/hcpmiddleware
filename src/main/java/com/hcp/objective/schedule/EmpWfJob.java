package com.hcp.objective.schedule;

import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Job of querying workflow requests associated with employees.
 * 
 * @author Zero Yu
 */
public class EmpWfJob implements Job {
	public static final Logger logger = LoggerFactory.getLogger(EmpWfJob.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		logger.error(new Date().toString());
	}

}
