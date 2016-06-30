package com.hcp.objective.service;

import org.quartz.JobExecutionContext;

public interface IBatchJobExcutor {
	public void execute(JobExecutionContext context) throws InterruptedException;
}
