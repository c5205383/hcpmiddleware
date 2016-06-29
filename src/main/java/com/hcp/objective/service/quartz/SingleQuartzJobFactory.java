package com.hcp.objective.service.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcp.objective.service.IBatchJobExcutor;

/*
 * @DisallowConcurrentExecution ��ֹ����ִ�ж����ͬ�����JobDetail, ���ע���Ǽ���Job���ϵ�, ����˼�����ǲ���ͬʱִ�ж��Job, ���ǲ��ܲ���ִ��ͬһ��Job
 * Definition(��JobDetail����), ���ǿ���ͬʱִ�ж����ͬ��JobDetail, ����˵��,������һ��Job��,����SayHelloJob, �������Job�ϼ������ע��,
 * Ȼ�������Job�϶����˺ܶ��JobDetail, ��sayHelloToJoeJobDetail, sayHelloToMikeJobDetail, ��ô��scheduler����ʱ,
 * ���Ტ��ִ�ж��sayHelloToJoeJobDetail����sayHelloToMikeJobDetail, ������ͬʱִ��sayHelloToJoeJobDetail��sayHelloToMikeJobDetail
 */
@DisallowConcurrentExecution
@Service
public class SingleQuartzJobFactory implements Job {
	private static final Logger log = LoggerFactory.getLogger(SingleQuartzJobFactory.class);

	@Autowired
	IBatchJobExcutor excutor;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			if (excutor != null) {
				System.out.println("123");
				excutor.execute(context);
			}
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
