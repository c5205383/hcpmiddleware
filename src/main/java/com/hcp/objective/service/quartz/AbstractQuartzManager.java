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
	private static String schedulerState = ""; // ������״̬��single����״̬��cluster��Ⱥ״̬��nullδ����״̬��
	public static final String STATE_SINGLE = "single"; // ����״̬
	public static final String STATE_CLUSTER = "cluster"; // ��Ⱥ״̬
	public static final String STATE_NULL = "null"; // δ����״̬

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
		// ��������
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
		// ���ʽ���ȹ�����
		CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(batchJob.getCronExpression());
		// ���µ�cronExpression���ʽ����һ���µ�trigger
		CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerName, BatchJob.JOB_GROUP_NAME)
				.withSchedule(scheduleBuilder).build();
		scheduler.scheduleJob(jobDetail, trigger);
		log.debug("=====��ʱ����[" + batchJob.getJobId() + "/" + batchJob.getName() + "]����ɹ�=====");
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
		log.debug("=====��ʱ����[" + batchJob.getJobId() + "/" + batchJob.getName() + "]ע���ɹ�=====");
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
			// ��ȡ��������ʶ
			TriggerKey triggerKey = TriggerKey.triggerKey(oldJob.getJobId(), BatchJob.JOB_GROUP_NAME);
			// ��ȡ������trigger
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null != trigger) {// ����Ѵ�������
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
			// ��ȡ��������ʶ
			TriggerKey triggerKey = TriggerKey.triggerKey(job.getJobId(), BatchJob.JOB_GROUP_NAME);
			// ��ȡ������trigger
			CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);
			if (null != trigger) {// ����Ѵ�������
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
		// ��ȡ������trigger
		CronTrigger trigger = (CronTrigger) scheduler.getTrigger(triggerKey);

		if (trigger != null) {
			// Trigger�Ѵ��ڣ���ô������Ӧ�Ķ�ʱ����
			// ���ʽ���ȹ�����
			CronScheduleBuilder scheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
			// ���µ�cronExpression���ʽ���¹���trigger
			trigger = trigger.getTriggerBuilder().withIdentity(triggerKey).withSchedule(scheduleBuilder).build();
			// ���µ�trigger��������jobִ��
			scheduler.rescheduleJob(triggerKey, trigger);
			log.debug("=====��ʱ����[" + job.getJobId() + "/" + job.getName() + "]���³ɹ�=====");
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
		log.debug("=====��ʱ����[" + job.getJobId() + "/" + job.getName() + "]��ͣ�ɹ�=====");
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
		log.debug("=====��ʱ����[" + job.getJobId() + "/" + job.getName() + "]�ָ��ɹ�=====");
	}

}
