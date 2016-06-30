package com.hcp.objective.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.hcp.objective.service.jobexcutor.JobExcutor;
import com.hcp.objective.service.quartz.ClusterQuartzManager;
import com.hcp.objective.service.quartz.SingleQuartzManager;
import com.hcp.objective.service.quartz.SpringJobFactory;

@Configuration
public class QuartzConfiguration {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		// Job对象的实例化过程是在Quartz中进行的, job内Spring注入的对象无法关联起来。
		// 解决办法复写SchedulerFactoryBean 生成job的 JobFactory，把复写的JobFactory设置成SchedulerFactoryBean的新属性
		// org.springframework.scheduling.quartz.SchedulerFactoryBean这个类。源码如下，参考http://www.tuicool.com/articles/Qjyamu
		schedulerFactoryBean.setJobFactory(springJobFactory());

		// scheduler.setTriggers(simpleTriggerFactoryBean().getObject(),cronTriggerFactoryBean().getObject());
		return schedulerFactoryBean;
	}

	/*
	 * Spring对Job的注入功能，其实很简单，原理就是在我们扩展JobFactory创建job的方法，在创建完Job以后进行属性注入
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
