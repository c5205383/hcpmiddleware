package com.hcp.objective.service;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcp.objective.persistence.bean.BatchJob;


@DisallowConcurrentExecution
/*
 * @DisallowConcurrentExecution ��ֹ����ִ�ж����ͬ�����JobDetail, ���ע���Ǽ���Job���ϵ�, ����˼�����ǲ���ͬʱִ�ж��Job, ���ǲ��ܲ���ִ��ͬһ��Job
 * Definition(��JobDetail����), ���ǿ���ͬʱִ�ж����ͬ��JobDetail, ����˵��,������һ��Job��,����SayHelloJob, �������Job�ϼ������ע��,
 * Ȼ�������Job�϶����˺ܶ��JobDetail, ��sayHelloToJoeJobDetail, sayHelloToMikeJobDetail, ��ô��scheduler����ʱ,
 * ���Ტ��ִ�ж��sayHelloToJoeJobDetail����sayHelloToMikeJobDetail, ������ͬʱִ��sayHelloToJoeJobDetail��sayHelloToMikeJobDetail
 */
public class SingleQuartzJobFactory implements Job {
	private static final Logger log = LoggerFactory.getLogger(SingleQuartzJobFactory.class);

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		BatchJob batchJob = (BatchJob) context.getMergedJobDataMap().get(AbstractQuartzManager.JOB_OBJECT_NAME);

		System.out.println("Key:" + context.getJobDetail().getKey());
		System.out.println(batchJob.getId());
		System.out.println(batchJob.getName());
		System.out.println(batchJob.getType());
		try {
			// IBatchJobExcutor jobExcutor = (IBatchJobExcutor) ctx.getBean(IBatchJobExcutor.class);
			// jobExcutor.execute(context);
		} catch (Exception ex) {
			log.error("====================Scheduler-error-begin====================");
			log.error(ex.toString());
			StackTraceElement[] element = ex.getStackTrace();
			for (int i = 0; i < element.length; i++) {
				log.error("λ��:" + element[i]);
			}
			log.error("====================Scheduler-error-end====================");
		}

	}

}
