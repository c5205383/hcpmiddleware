package com.hcp.objective.component.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcp.objective.component.jobexecutor.IExecutor;
import com.hcp.objective.persistence.bean.BatchJob;

public class ClusterQuartzJobFactory implements Job {

	private static final Logger log = LoggerFactory.getLogger(ClusterQuartzJobFactory.class);

	IExecutor executor;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);

		if (batchJob.getName()
				.equals(AbstractQuartzManager.SCHEDULER_CHECK_JOB) /* || clusterQuartzManager.isRun() */) { // ����Ǽ�������÷�����������
			try {
				executor.execute();
			} catch (Exception ex) {
				log.error("====================Scheduler-error-begin====================");
				log.error(ex.toString());
				StackTraceElement[] element = ex.getStackTrace();
				for (int i = 0; i < element.length; i++) {
					log.error("error:" + element[i]);
				}
				log.error("====================Scheduler-error-end====================");
			}
		}
	}

}
