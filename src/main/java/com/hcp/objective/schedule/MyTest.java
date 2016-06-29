package com.hcp.objective.schedule;

import org.quartz.JobExecutionContext;

import com.hcp.objective.service.IBatchJobExcutor;

public class MyTest implements IBatchJobExcutor {

	@Override
	public void execute(JobExecutionContext context) {
		// TODO Auto-generated method stub
		System.out.println("123123123");
	}

}
