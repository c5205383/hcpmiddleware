package com.hcp.objective.service.quartz;

import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcp.objective.component.quartz.AbstractQuartzManager;
import com.hcp.objective.persistence.bean.BatchJob;
import com.hcp.objective.service.BatchJobService;

@Service
public class SingleQuartzManagerService extends AbstractQuartzManager {
	private static final Logger log = LoggerFactory.getLogger(SingleQuartzManagerService.class);
	public static final int STATE_RUN = 1; 
	public static final int STATE_WAIT = 0; 

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
			log.debug("Start All");
		}
	}

	public void startMyJob(String owner) throws SchedulerException {

		List<BatchJob> jobs = batchJobService.findByOwner(owner);
		for (BatchJob job : jobs) {
			if (BatchJob.STATUS_USED == job.getStatus()) {
				create(job);
			}
		}
		log.debug("=====start my job=====");  
	}
}
