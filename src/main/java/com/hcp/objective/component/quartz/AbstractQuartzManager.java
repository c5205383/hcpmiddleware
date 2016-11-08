package com.hcp.objective.component.quartz;

import java.util.Date;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.DateBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.KeyMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import com.hcp.objective.bean.ApplicationPropertyBean;
import com.hcp.objective.component.jobexecutor.NoRepeatTriggerListener;
import com.hcp.objective.persistence.bean.BatchJob;

/**
 * The Abstract Quartz Manager
 * 
 * @author Zero Yu & Bruce Wang
 */

public abstract class AbstractQuartzManager {
	private static final Logger log = LoggerFactory.getLogger(AbstractQuartzManager.class);
	private static String schedulerState = ""; // single/cluster/null
	public static final String STATE_SINGLE = "single"; //
	public static final String STATE_CLUSTER = "cluster";
	public static final String STATE_NULL = "null";
	public static String JOB_TRIGGER_PREFIX = "TRIGGER_";
	public static String NO_REPEAT_JOB_TRIGGER_PREFIX = "NO_REPEAT_TRIGGER_";
	public static String JOB_OBJECT_NAME = "HCP_BATCH_JOB";
	public static final String SCHEDULER_CHECK_JOB = "SCHEDULER_CHECK_JOB";
	public static String JOB_RUNNING = "RUNNING";
	public static String JOB_STOP = "STOP";

	@Autowired
	ApplicationPropertyBean appBean;

	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	NoRepeatTriggerListener noRepeatTriggerListener;

	public String getState() {
		if (StringUtils.isEmpty(schedulerState)) {
			schedulerState = appBean.getQuartzState();
		}
		return schedulerState;
	}

	private String generateTriggerName(BatchJob job) {
		return JOB_TRIGGER_PREFIX + job.getJobId();
	}

	private String generateNoRepeatTriggerName(BatchJob job) {
		return NO_REPEAT_JOB_TRIGGER_PREFIX + job.getJobId();
	}

	/**
	 * Create batch job, and add it to scheduler.
	 * 
	 * @param batchJob
	 * @throws SchedulerException
	 */
	public boolean create(BatchJob batchJob) {
		boolean success = true;
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobDetail jobDetail = null;
			if (getState().equalsIgnoreCase(AbstractQuartzManager.STATE_SINGLE)) {
				jobDetail = JobBuilder.newJob(SingleQuartzJobFactory.class)
						.withIdentity(batchJob.getJobId(), BatchJob.JOB_GROUP_NAME).build();
			} else if (getState().equalsIgnoreCase(AbstractQuartzManager.STATE_CLUSTER)) {
				jobDetail = JobBuilder.newJob(ClusterQuartzJobFactory.class)
						.withIdentity(batchJob.getJobId(), BatchJob.JOB_GROUP_NAME).build();
			} else {
				return false;
			}
			jobDetail.getJobDataMap().put(JOB_OBJECT_NAME, batchJob);
			Trigger trigger = batchJobTrigger(batchJob);

			scheduler.scheduleJob(jobDetail, trigger);
			log.debug("=====Create[" + batchJob.getJobId() + "/" + batchJob.getName() + "]=====");
		} catch (SchedulerException e) {
			success = false;
			log.error(e.getLocalizedMessage());
		}
		return success;

	}

	private Trigger batchJobTrigger(BatchJob batchJob) {
		Trigger trigger = null;
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		if (batchJob.getCronExpression() == null || batchJob.getCronExpression().isEmpty()) {
			// Build a trigger for a specific moment in time, with no repeats:
			// 15 seconds later of current time
			Date startTime = DateBuilder.nextGivenSecondDate(null, 15);
			trigger = TriggerBuilder.newTrigger()
					.withIdentity(generateNoRepeatTriggerName(batchJob), BatchJob.JOB_GROUP_NAME).startAt(startTime)
					.build();
			// Registering A TriggerListener With The Scheduler To Listen To A Specific Trigger
			try {
				scheduler.getListenerManager().addTriggerListener(noRepeatTriggerListener,
						KeyMatcher.keyEquals(trigger.getKey()));
			} catch (SchedulerException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(batchJob.getCronExpression());
			trigger = TriggerBuilder.newTrigger().withIdentity(generateTriggerName(batchJob), BatchJob.JOB_GROUP_NAME)
					.withSchedule(scheduleBuilder).build();
		}

		return trigger;
	}

	/**
	 * Delete batch job from scheduler
	 * 
	 * @param batchJob
	 * @throws SchedulerException
	 */
	public void delete(BatchJob batchJob) {
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(batchJob.getJobId(), BatchJob.JOB_GROUP_NAME);
			scheduler.deleteJob(jobKey);
			log.debug("=====debug delete[" + batchJob.getJobId() + "/" + batchJob.getName() + "]=====");
		} catch (SchedulerException e) {
			log.error(e.getLocalizedMessage());
		}

	}

	public boolean findJob(BatchJob batchJob) {
		boolean find = false;
		try {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			JobKey jobKey = JobKey.jobKey(batchJob.getJobId(), BatchJob.JOB_GROUP_NAME);
			if (scheduler.getJobDetail(jobKey) != null) {
				find = true;
			}
		} catch (SchedulerException e) {
			log.error(e.getLocalizedMessage());
			find = false;
		} finally {
		}
		return find;

	}

	/**
	 * Update old batch job as new job, delete old one from scheduler and add new to scheduler
	 * 
	 * @param oldJob
	 * @param newJob
	 * @throws SchedulerException
	 */
	public void update(BatchJob oldJob, BatchJob newJob) throws SchedulerException {
		if (oldJob == null) {
			if (BatchJob.STATUS_USED == newJob.getStatus()) {
				create(newJob);
			}
		} else {
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(generateTriggerName(oldJob), BatchJob.JOB_GROUP_NAME);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null != trigger) {
				delete(oldJob);
			}
			if (BatchJob.STATUS_USED == newJob.getStatus()) {
				create(newJob);
			}
		}
	}

	/**
	 * Update job. Find the old one and delete it, add new one.
	 * 
	 * @param job
	 * @throws SchedulerException
	 */
	public void update(BatchJob job) throws SchedulerException {

		if (job != null) {

			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			TriggerKey triggerKey = TriggerKey.triggerKey(generateTriggerName(job), BatchJob.JOB_GROUP_NAME);
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null != trigger) {
				delete(job);
			}
			if (BatchJob.STATUS_USED == job.getStatus()) {
				create(job);
			}
		}
	}

	/**
	 * 
	 * @param job
	 * @throws SchedulerException
	 */
	public void modify(BatchJob job) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobId(), BatchJob.JOB_GROUP_NAME);
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		if (trigger != null) {
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			scheduler.rescheduleJob(triggerKey, trigger);
			log.debug("=====Modify[" + job.getJobId() + "/" + job.getName() + "]=====");
		}

	}

	/**
	 * 
	 * @param job
	 * @throws SchedulerException
	 */
	public void pause(BatchJob job) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(job.getJobId(), BatchJob.JOB_GROUP_NAME);
		scheduler.pauseJob(jobKey);
		log.debug("=====Pause[" + job.getJobId() + "/" + job.getName() + "]=====");
	}

	/**
	 * 
	 * @param scheduleJob
	 * @throws SchedulerException
	 */
	public void resume(BatchJob job) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(job.getJobId(), BatchJob.JOB_GROUP_NAME);
		scheduler.resumeJob(jobKey);
		log.debug("=====Resume[" + job.getJobId() + "/" + job.getName() + "]=====");
	}

}
