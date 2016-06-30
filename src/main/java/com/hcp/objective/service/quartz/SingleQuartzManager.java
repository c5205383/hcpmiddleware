package com.hcp.objective.service.quartz;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hcp.objective.persistence.bean.BatchJob;
import com.hcp.objective.service.BatchJobService;
import com.hcp.objective.service.quartz.AbstractQuartzManager;

public class SingleQuartzManager extends AbstractQuartzManager {
	private static final Logger log = LoggerFactory.getLogger(SingleQuartzManager.class);
	public static final int STATE_RUN = 1; // 运行状态
	public static final int STATE_WAIT = 0; // 等待状态

	@Autowired
	private BatchJobService batchJobService;

	public void startAll() throws SchedulerException {

		if (getState().equalsIgnoreCase(AbstractQuartzManager.STATE_SINGLE)) {

			List<BatchJob> jobs = batchJobService.findAll();
			for (BatchJob job : jobs) {
				if (BatchJob.STATUS_USED == job.getStatus()) {
					create(job);
				}
			}
			log.debug("=====定时任务启动完成=====");
		}
	}
}
