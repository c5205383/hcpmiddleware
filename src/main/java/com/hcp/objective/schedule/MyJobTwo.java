package com.hcp.objective.schedule;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public class MyJobTwo extends QuartzJobBean {
	public static final Logger logger = LoggerFactory.getLogger(MyJobTwo.class);
    protected void executeInternal(JobExecutionContext ctx) throws JobExecutionException {
    	   logger.error("======"+System.currentTimeMillis());
      }
} 
