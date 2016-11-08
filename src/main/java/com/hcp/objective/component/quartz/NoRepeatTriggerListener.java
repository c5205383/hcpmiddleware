package com.hcp.objective.component.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcp.objective.persistence.bean.BatchJob;

import org.quartz.TriggerListener;

public class NoRepeatTriggerListener implements TriggerListener {

	private static final Logger log = LoggerFactory.getLogger(NoRepeatTriggerListener.class);
	private static final String TRIGGER_LISTENER_NAME = "NoRepeatTriggerListener";

	@Override
	public String getName() {
		return TRIGGER_LISTENER_NAME;
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		// TODO Auto-generated method stub

	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);
		if (batchJob != null) {
			log.info(getName() + " trigger: " + trigger.getKey() + " completed at " + trigger.getStartTime());
			log.info("Job Id:{}, Job Name:{}, Job Type:{}", batchJob.getId(), batchJob.getName(), batchJob.getType());

			/*
			 * // update stored job status BatchJobService batchJobService = (BatchJobService)
			 * getBean(BatchJobService.class); BatchJob newJob = batchJobService.findOne(batchJob.getId());
			 * newJob.setStatus(false); newJob.setRunningStatus(AbstractQuartzManager.JOB_STOP);
			 * batchJobService.updateOne(batchJob.getId(), newJob); // remove job from scheduler
			 * SingleQuartzManagerService singleQuartzManagerService = (SingleQuartzManagerService) getBean(
			 * SingleQuartzManagerService.class); singleQuartzManagerService.delete(batchJob);
			 */
		}
	}

}
