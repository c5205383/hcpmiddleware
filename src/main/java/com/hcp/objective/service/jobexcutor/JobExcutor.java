package com.hcp.objective.service.jobexcutor;

import org.quartz.JobExecutionContext;

import com.hcp.objective.persistence.bean.BatchJob;
import com.hcp.objective.service.IBatchJobExcutor;
import com.hcp.objective.service.quartz.AbstractQuartzManager;

public class JobExcutor implements IBatchJobExcutor {

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

	@Override
	public void execute(JobExecutionContext context) throws InterruptedException {
		// TODO Auto-generated method stub
		BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);

		System.out.println("Key:" + context.getJobDetail().getKey());
		if (batchJob != null) {
			System.out.print("Job Id:" + batchJob.getJobId() + ",");
			System.out.print("Job Name:" + batchJob.getName() + ", ");
			System.out.print("Job Type:" + batchJob.getType() + "\n");
			if (batchJob.getType().equalsIgnoreCase(ExcutorEnum.WORK_FLOW.getName())) {
				SFWorkFlowExcutor.execute();
				Thread.sleep(3000);
			}
		}

	}

}
