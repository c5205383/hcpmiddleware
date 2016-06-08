package com.hcp.objective.service;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.hcp.objective.jpa.bean.BatchJob;
import com.hcp.objective.schedule.EmpWfJob;
import com.hcp.objective.schedule.TestJob;

/**
 * The Quartz Manager
 * 
 * @author Zero Yu
 */
public class QuartzManager {
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static String JOB_GROUP_NAME = "DEFAULT_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "DEFAULT_TRIGGERGROUP_NAME";
	
	public static void addJob(String jobName, String triggerName, Class<TestJob> cls, String time) {
		Scheduler sched;
		try {
			sched = gSchedulerFactory.getScheduler();
			JobDetail job = newJob(cls).withIdentity(jobName, JOB_GROUP_NAME).build();
			CronTrigger trigger = newTrigger()
				    .withIdentity(triggerName, TRIGGER_GROUP_NAME)
				    .withSchedule(cronSchedule(time))
				    .build();
			sched.scheduleJob(job, trigger);
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}
	}
	
	public static void pauseJob(String jobName, String triggerName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.pauseTrigger(triggerKey(triggerName, TRIGGER_GROUP_NAME));
			sched.pauseJob(jobKey(jobName, JOB_GROUP_NAME));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public static void resumeJob(String jobName, String triggerName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.resumeTrigger(triggerKey(triggerName, TRIGGER_GROUP_NAME));
			sched.resumeJob(jobKey(jobName, JOB_GROUP_NAME));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteJob(String jobName, String triggerName) {
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.deleteJob(jobKey(jobName, JOB_GROUP_NAME));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Add a batch job and schedule it
	 * 
	 * @param batchJob
	 * 				the {@link}BatchJob object
	 */
	public static void addBatchJob(BatchJob batchJob) {
		Scheduler sched;
		JobDetail job = null;
		String triggerName = "trigger_" + batchJob.getName();
		String time = null;
		try {
			sched = gSchedulerFactory.getScheduler();
			switch(batchJob.getType()) {
				case "workflow":
					job = newJob(EmpWfJob.class)
					.withIdentity(batchJob.getName(), JOB_GROUP_NAME)
					.usingJobData("eventReason", "HIRNEW")
					.build();
					break;
				case "user":
					break;
			}
			double interval = batchJob.getInterval();
			if (interval == 1) {
				time = "0 0/1 * * * ?";
			} else if (interval == 0.5) {
				time = "0 0/30 * * * ?";
			}
			CronTrigger trigger = newTrigger()
				    .withIdentity(triggerName, TRIGGER_GROUP_NAME)
				    .withSchedule(cronSchedule(time))
				    .build();
			sched.scheduleJob(job, trigger);
			if (!sched.isShutdown()) {
				sched.start();
			}
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}
	}
	
	/**
	 * Change a batch job's status
	 * 
	 * @param batchJob
	 * 				the {@link}BatchJob object
	 */
	public static void changeBatchJob(BatchJob batchJob) {
		String triggerName = "trigger_" + batchJob.getName();
		String jobName = batchJob.getName();
		boolean status = batchJob.getStatus();
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			if (status == false) {
				sched.pauseTrigger(triggerKey(triggerName, TRIGGER_GROUP_NAME));
				sched.pauseJob(jobKey(jobName, JOB_GROUP_NAME));
			} else {
				sched.resumeTrigger(triggerKey(triggerName, TRIGGER_GROUP_NAME));
				sched.resumeJob(jobKey(jobName, JOB_GROUP_NAME));
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Delete a batch job
	 * 
	 * @param batchJob
	 * 				the {@link}BatchJob object
	 */
	public static void deleteBatchJob(BatchJob batchJob) {
		String jobName = batchJob.getName();
		try {
			Scheduler sched = gSchedulerFactory.getScheduler();
			sched.deleteJob(jobKey(jobName, JOB_GROUP_NAME));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}
	
}
