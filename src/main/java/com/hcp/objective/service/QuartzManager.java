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

public class QuartzManager {
	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
	private static String JOB_GROUP_NAME = "DEFAULT_JOBGROUP_NAME";
	private static String TRIGGER_GROUP_NAME = "DEFAULT_TRIGGERGROUP_NAME";
	
	public static void addJob(String jobName, String triggerName, Class cls, String time) {
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
}
