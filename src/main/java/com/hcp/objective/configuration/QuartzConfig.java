package com.hcp.objective.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import com.hcp.objective.component.quartz.NoRepeatTriggerListener;
import com.hcp.objective.component.quartz.SpringJobFactory;

@Configuration
public class QuartzConfig {

	protected SchedulerFactoryBean schedulerFactoryBean;
	protected NoRepeatTriggerListener noRepeatTriggerListener;

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SchedulerFactoryBean schedulerFactoryBean() {
		if (this.schedulerFactoryBean == null) {
			this.schedulerFactoryBean = new SchedulerFactoryBean();
			// http://www.tuicool.com/articles/Qjyamu
			this.schedulerFactoryBean.setJobFactory(springJobFactory());
			this.schedulerFactoryBean.setWaitForJobsToCompleteOnShutdown(true);
		}
		return this.schedulerFactoryBean;
	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public SpringJobFactory springJobFactory() {
		return new SpringJobFactory();

	}

	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public NoRepeatTriggerListener noRepeatTriggerListener() {
		if (this.noRepeatTriggerListener == null) {
			this.noRepeatTriggerListener = new NoRepeatTriggerListener();
		}
		return this.noRepeatTriggerListener;
	}
}
