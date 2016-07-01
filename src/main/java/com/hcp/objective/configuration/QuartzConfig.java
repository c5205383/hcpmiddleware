package com.hcp.objective.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.hcp.objective.component.jobexcutor.JobExcutor;
import com.hcp.objective.component.quartz.SpringJobFactory;

@Configuration
public class QuartzConfig {

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
	 * Spring��Job��ע�빦�ܣ���ʵ�ܼ򵥣�ԭ�������������չJobFactory����job�ķ������ڴ�����Job�Ժ��������ע��
	 */
	@Bean
	// @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SpringJobFactory springJobFactory() {
		return new SpringJobFactory();

	}

	@Bean(name = "excutor")
	public JobExcutor jobExcutor() {
		JobExcutor excutor = new JobExcutor();
		return excutor;
	}
}
