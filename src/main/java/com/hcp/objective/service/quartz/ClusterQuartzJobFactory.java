package com.hcp.objective.service.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.persistence.bean.BatchJob;
import com.hcp.objective.service.IBatchJobExcutor;

public class ClusterQuartzJobFactory implements Job {

	private static final Logger log = LoggerFactory.getLogger(ClusterQuartzJobFactory.class);

	@Autowired
	IBatchJobExcutor excutor;

	@Autowired
	ClusterQuartzManager clusterQuartzManager;

	public void execute(JobExecutionContext context) throws JobExecutionException {
		BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);

		if (batchJob.getName().equals(ClusterQuartzManager.SCHEDULER_CHECK_JOB) || clusterQuartzManager.isRun()) { // 如果是检查任务或该服务正在运行
			try {
				excutor.execute(context);
			} catch (Exception ex) {
				log.error("====================Scheduler-error-begin====================");
				log.error(ex.toString());
				StackTraceElement[] element = ex.getStackTrace();
				for (int i = 0; i < element.length; i++) {
					log.error("位置:" + element[i]);
				}
				log.error("====================Scheduler-error-end====================");
			}
		}
	}

}
