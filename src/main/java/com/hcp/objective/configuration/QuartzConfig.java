package com.hcp.objective.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.hcp.objective.component.quartz.SpringJobFactory;

@Configuration
public class QuartzConfig {

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SchedulerFactoryBean schedulerFactoryBean() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		// http://www.tuicool.com/articles/Qjyamu
		schedulerFactoryBean.setJobFactory(springJobFactory());
		schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
		return schedulerFactoryBean;
	}

	@Bean
	// @Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SpringJobFactory springJobFactory() {
		return new SpringJobFactory();

	}
}
