package com.hcp.objective.component.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcp.objective.component.jobexcutor.JobExcutor;

@DisallowConcurrentExecution
@Service
public class SingleQuartzJobFactory implements Job {
	private static final Logger log = LoggerFactory.getLogger(SingleQuartzJobFactory.class);

	@Autowired
	JobExcutor excutor;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if (excutor != null) {
				excutor.execute(context);
			}
		} catch (Exception ex) {
			log.error("====================Scheduler-error-begin====================");
			log.error(ex.toString());
			StackTraceElement[] element = ex.getStackTrace();
			for (int i = 0; i < element.length; i++) {
				log.error("" + element[i]);
			}
			log.error("====================Scheduler-error-end====================");
		}

	}

}
