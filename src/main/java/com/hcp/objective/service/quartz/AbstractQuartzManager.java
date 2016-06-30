package com.hcp.objective.service.quartz;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import com.hcp.objective.bean.ApplicationPropertyBean;
import com.hcp.objective.persistence.bean.BatchJob;

/**
 * The Quartz Manager
 * 
 * @author Zero Yu
 */

public abstract class AbstractQuartzManager {
	private static final Logger log = LoggerFactory.getLogger(AbstractQuartzManager.class);
	private static String schedulerState = ""; // 定任务状态（single单机状态、cluster集群状态、null未启动状态）
	public static final String STATE_SINGLE = "single"; // 单机状态
	public static final String STATE_CLUSTER = "cluster"; // 集群状态
	public static final String STATE_NULL = "null"; // 未启动状态

	public static String JOB_GROUP_NAME = "DEFAULT_JOBGROUP_NAME";
	public static String JOB_TRIGGER_PREFIX = "TRIGGER_";
	public static String JOB_OBJECT_NAME = "batchjob";

	@Autowired
	ApplicationPropertyBean appBean;

	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;

	@Autowired

	public String getState() {
		if (StringUtils.isEmpty(schedulerState)) {
			schedulerState = appBean.getQuartzState();
		}
		return schedulerState;
	}

	/**
	 * 
	 * @param batchJob
	 * @throws SchedulerException
	 */
	public void create(BatchJob batchJob) throws SchedulerException {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		String triggerName = JOB_TRIGGER_PREFIX + batchJob.getName();
		// 创建任务
		JobDetail jobDetail = null;
		if (getState().equalsIgnoreCase(AbstractQuartzManager.STATE_SINGLE)) {
			jobDetail = JobBuilder.newJob(SingleQuartzJobFactory.class).withIdentity(batchJob.getName(), JOB_GROUP_NAME)
					.build();
		} else if (getState().equalsIgnoreCase(AbstractQuartzManager.STATE_CLUSTER)) {
			jobDetail = JobBuilder.newJob(ClusterQuartzJobFactory.class)
					.withIdentity(batchJob.getName(), JOB_GROUP_NAME).build();
		} else {
			return;
		}
		jobDetail.getJobDataMap().put(JOB_OBJECT_NAME, batchJob);
		// 表达式调度构建器
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(batchJob.getCronExpression());
		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, JOB_GROUP_NAME)
				.withSchedule(scheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, trigger);
		log.debug("=====定时任务[" + batchJob.getId() + "/" + batchJob.getName() + "]载入成功=====");
	}

	public static void update(BatchJob oldJob, BatchJob newJob) throws SchedulerException {

	}

	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();

	private static String TRIGGER_GROUP_NAME = "DEFAULT_TRIGGERGROUP_NAME";


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
	 *            the {@link}BatchJob object
	 */
	public static void addBatchJob(BatchJob batchJob) {
		Scheduler sched;
		JobDetail job = null;
		String triggerName = "trigger_" + batchJob.getName();
		String time = null;
		try {
			sched = gSchedulerFactory.getScheduler();
			switch (batchJob.getType()) {
			case "workflow":
				job = newJob(SingleQuartzJobFactory.class).withIdentity(batchJob.getName(), JOB_GROUP_NAME)
						.usingJobData("eventReason", "HIRNEW").build();
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
			CronTrigger trigger = newTrigger().withIdentity(triggerName, TRIGGER_GROUP_NAME)
					.withSchedule(cronSchedule(time)).build();
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
	 *            the {@link}BatchJob object
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
	 *            the {@link}BatchJob object
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
