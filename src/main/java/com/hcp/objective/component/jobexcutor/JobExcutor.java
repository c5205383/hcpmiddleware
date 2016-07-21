package com.hcp.objective.component.jobexcutor;

import org.quartz.JobExecutionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcp.objective.component.quartz.AbstractQuartzManager;
import com.hcp.objective.persistence.bean.BatchJob;

public class JobExcutor {
	public static final Logger logger = LoggerFactory.getLogger(JobExcutor.class);

	public enum ExcutorEnum {
		WORK_FLOW("workflow");

		private String name;

		private ExcutorEnum(String name) {
			this.setName(name);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

	public void execute(JobExecutionContext context) throws InterruptedException {
		BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);
		System.out.println("Key:" + context.getJobDetail().getKey());
		if (batchJob != null) {
			if (batchJob.getType().equalsIgnoreCase(ExcutorEnum.WORK_FLOW.getName())) {

				logger.info("Job Id:{}, Job Name:{}, Job Type:{}", batchJob.getId(), batchJob.getName(),
						batchJob.getType());

				SFWorkFlowExcutor.execute();
			}

		}

	}

}
