package com.hcp.objective.component.jobexcutor;

import org.quartz.JobExecutionContext;

import com.hcp.objective.component.quartz.AbstractQuartzManager;
import com.hcp.objective.persistence.bean.BatchJob;

public class JobExcutor {

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
		// TODO Auto-generated method stub
		BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);
		System.out.println("Key:" + context.getJobDetail().getKey());
		if (batchJob != null) {
			if (batchJob.getType().equalsIgnoreCase(ExcutorEnum.WORK_FLOW.getName())) {
				System.out.print("Job Id:" + batchJob.getJobId() + ",");
				System.out.print("Job Name:" + batchJob.getName() + ", ");
				System.out.print("Job Type:" + batchJob.getType() + "\n");

				SFWorkFlowExcutor.execute(); 
			}
		}

	}

}
