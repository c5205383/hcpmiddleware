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
 * @DisallowConcurrentExecution 禁止并发执行多个相同定义的JobDetail, 这个注解是加在Job类上的, 但意思并不是不能同时执行多个Job, 而是不能并发执行同一个Job
 * Definition(由JobDetail定义), 但是可以同时执行多个不同的JobDetail, 举例说明,我们有一个Job类,叫做SayHelloJob, 并在这个Job上加了这个注解,
 * 然后在这个Job上定义了很多个JobDetail, 如sayHelloToJoeJobDetail, sayHelloToMikeJobDetail, 那么当scheduler启动时,
 * 不会并发执行多个sayHelloToJoeJobDetail或者sayHelloToMikeJobDetail, 但可以同时执行sayHelloToJoeJobDetail跟sayHelloToMikeJobDetail
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
				log.error("位置:" + element[i]);
			}
			log.error("====================Scheduler-error-end====================");
		}

	}

}
