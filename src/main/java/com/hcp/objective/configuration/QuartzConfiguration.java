package com.hcp.objective.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.hcp.objective.service.jobexcutor.JobExcutor;
import com.hcp.objective.service.quartz.ClusterQuartzManager;
import com.hcp.objective.service.quartz.SingleQuartzManager;
import com.hcp.objective.service.quartz.SpringJobFactory;

@Configuration
public class QuartzConfiguration {
	@Bean
	public MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean() {
		MethodInvokingJobDetailFactoryBean obj = new MethodInvokingJobDetailFactoryBean();
		obj.setTargetBeanName("jobone");
		obj.setTargetMethod("myTask");
		return obj;
	}

	// Job is scheduled for 3+1 times with the interval of 30 seconds
	@Bean
	public SimpleTriggerFactoryBean simpleTriggerFactoryBean() {
		SimpleTriggerFactoryBean stFactory = new SimpleTriggerFactoryBean();
		stFactory.setJobDetail(methodInvokingJobDetailFactoryBean().getObject());
		stFactory.setStartDelay(3000);
		stFactory.setRepeatInterval(30000);
		stFactory.setRepeatCount(3);
		return stFactory;
	}


	// Job is scheduled after every 1 minute

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		// Job�����ʵ������������Quartz�н��е�, job��Springע��Ķ����޷�����������
		// ����취��дSchedulerFactoryBean ����job�� JobFactory���Ѹ�д��JobFactory���ó�SchedulerFactoryBean��������
		// org.springframework.scheduling.quartz.SchedulerFactoryBean����ࡣԴ�����£��ο�http://www.tuicool.com/articles/Qjyamu
		schedulerFactoryBean.setJobFactory(springJobFactory());

		// scheduler.setTriggers(simpleTriggerFactoryBean().getObject(),cronTriggerFactoryBean().getObject());
		return schedulerFactoryBean;
	}

	/*
	 * Spring��Job��ע�빦�ܣ���ʵ�ܼ򵥣�ԭ��������������չJobFactory����job�ķ������ڴ�����Job�Ժ��������ע��
	 */
	@Bean
	// @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SpringJobFactory springJobFactory() {
		return new SpringJobFactory();

	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SingleQuartzManager singleQuartzManager() {
		return new SingleQuartzManager();
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public ClusterQuartzManager clusterQuartzManager() {
		return new ClusterQuartzManager();
	}

	@Bean(name = "excutor")
	public JobExcutor jobExcutor() {
		JobExcutor excutor = new JobExcutor();
		// scheduler.setTriggers(simpleTriggerFactoryBean().getObject(),cronTriggerFactoryBean().getObject());
		return excutor;
	}
}