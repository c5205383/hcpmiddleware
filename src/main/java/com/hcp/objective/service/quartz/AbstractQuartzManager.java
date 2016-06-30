package com.hcp.objective.service.quartz;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.util.StringUtils;

import com.hcp.objective.bean.ApplicationPropertyBean;
import com.hcp.objective.persistence.bean.BatchJob;

/**
 * The Abstract Quartz Manager
 * 
 * @author Zero Yu & Bruce Wang
 */

public abstract class AbstractQuartzManager {
	private static final Logger log = LoggerFactory.getLogger(AbstractQuartzManager.class);
	private static String schedulerState = ""; // 定任务状态（single单机状态、cluster集群状态、null未启动状态）
	public static final String STATE_SINGLE = "single"; // 单机状态
	public static final String STATE_CLUSTER = "cluster"; // 集群状态
	public static final String STATE_NULL = "null"; // 未启动状态

	public static String JOB_TRIGGER_PREFIX = "TRIGGER_";
	public static String JOB_OBJECT_NAME = "BATCH_JOB";

	@Autowired
	ApplicationPropertyBean appBean;

	@Autowired
	SchedulerFactoryBean schedulerFactoryBean;

	public String getState() {
		if (StringUtils.isEmpty(schedulerState)) {
			schedulerState = appBean.getQuartzState();
		}
		return schedulerState;
	}

	/**
	 * Create batch job, and add it to scheduler.
	 * 
	 * @param batchJob
	 * @throws SchedulerException
	 */
	public void create(BatchJob batchJob) throws SchedulerException {

		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		String triggerName = JOB_TRIGGER_PREFIX + batchJob.getJobId();
		// 创建任务
		JobDetail jobDetail = null;
		if (getState().equalsIgnoreCase(AbstractQuartzManager.STATE_SINGLE)) {
			jobDetail = JobBuilder.newJob(SingleQuartzJobFactory.class)
					.withIdentity(batchJob.getJobId(), BatchJob.JOB_GROUP_NAME).build();
		} else if (getState().equalsIgnoreCase(AbstractQuartzManager.STATE_CLUSTER)) {
			jobDetail = JobBuilder.newJob(ClusterQuartzJobFactory.class)
					.withIdentity(batchJob.getJobId(), BatchJob.JOB_GROUP_NAME).build();
		} else {
			return;
		}
		jobDetail.getJobDataMap().put(JOB_OBJECT_NAME, batchJob);
		// 表达式调度构建器
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(batchJob.getCronExpression());
		// 按新的cronExpression表达式构建一个新的trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, BatchJob.JOB_GROUP_NAME)
				.withSchedule(scheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, trigger);
		log.debug("=====定时任务[" + batchJob.getJobId() + "/" + batchJob.getName() + "]载入成功=====");
	}

	/**
	 * Delete batch job from scheduler
	 * 
	 * @param batchJob
	 * @throws SchedulerException
	 */
	public void delete(BatchJob batchJob) throws SchedulerException {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = JobKey.jobKey(batchJob.getJobId(), BatchJob.JOB_GROUP_NAME);
		scheduler.deleteJob(jobKey);
		log.debug("=====定时任务[" + batchJob.getJobId() + "/" + batchJob.getName() + "]注销成功=====");
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
			// 获取触发器标识
			TriggerKey triggerKey = TriggerKey.triggerKey(oldJob.getJobId(), BatchJob.JOB_GROUP_NAME);
			// 获取触发器trigger
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null != trigger) {// 如果已存在任务
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
			// 获取触发器标识
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobId(), BatchJob.JOB_GROUP_NAME);
			// 获取触发器trigger
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null != trigger) {// 如果已存在任务
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
		// 获取触发器trigger
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		if (trigger != null) {
			// Trigger已存在，那么更新相应的定时设置
			// 表达式调度构建器
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			// 按新的cronExpression表达式重新构建trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// 按新的trigger重新设置job执行
			scheduler.rescheduleJob(triggerKey, trigger);
			log.debug("=====定时任务[" + job.getJobId() + "/" + job.getName() + "]更新成功=====");
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
		log.debug("=====定时任务[" + job.getJobId() + "/" + job.getName() + "]暂停成功=====");
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
		log.debug("=====定时任务[" + job.getJobId() + "/" + job.getName() + "]恢复成功=====");
	}

}
