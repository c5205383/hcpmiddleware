package com.hcp.objective.service.quartz;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcp.objective.service.quartz.AbstractQuartzManager;

public class SingleQuartzManager extends AbstractQuartzManager {
	private static final Logger log = LoggerFactory.getLogger(SingleQuartzManager.class);
	public static final int STATE_RUN = 1; // 运行状态
	public static final int STATE_WAIT = 0; // 等待状态

	public void startAll() throws SchedulerException {

		if (getState().equalsIgnoreCase(AbstractQuartzManager.STATE_SINGLE)) {
			/*
			 * SchedulerJobService schedulerJobService = ContextHolder.getBean(SchedulerJobService.class);
			 * List<SchedulerJob> jobs = schedulerJobService.findAll(); for (SchedulerJob job : jobs) { if
			 * (SchedulerJob.STATUS_USED == job.getJobStatus()) { create(job); } }
			 */
			log.debug("=====定时任务启动完成=====");
		}
	}
}
