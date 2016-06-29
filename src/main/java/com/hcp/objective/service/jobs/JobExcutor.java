package com.hcp.objective.service.jobs;

import org.quartz.JobExecutionContext;

import com.hcp.objective.persistence.bean.BatchJob;
import com.hcp.objective.service.IBatchJobExcutor;
import com.hcp.objective.service.quartz.AbstractQuartzManager;

public class JobExcutor implements IBatchJobExcutor {

	@Override
	public void execute(JobExecutionContext context) {
		// TODO Auto-generated method stub
		BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);

		System.out.println("Key:" + context.getJobDetail().getKey());
		if (batchJob != null) {
			System.out.println(batchJob.getId());
			System.out.println(batchJob.getName());
			System.out.println(batchJob.getType());
		}

	}

}
