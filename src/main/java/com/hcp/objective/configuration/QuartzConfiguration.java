package com.hcp.objective.configuration;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import com.hcp.objective.schedule.MyJobTwo;
import com.hcp.objective.service.jobs.JobExcutor;
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

	@Bean
	public JobDetailFactoryBean jobDetailFactoryBean() {
		JobDetailFactoryBean factory = new JobDetailFactoryBean();
		factory.setJobClass(MyJobTwo.class);
		// Map<String,Object> map = new HashMap<String,Object>();
		// map.put("name", "RAM");
		// map.put(MyJobTwo.COUNT, 1);
		// factory.setJobDataAsMap(map);
		factory.setGroup("mygroup");
		factory.setName("myjob");
		return factory;
	}

	// Job is scheduled after every 1 minute
	@Bean
	public CronTriggerFactoryBean cronTriggerFactoryBean() {
		CronTriggerFactoryBean stFactory = new CronTriggerFactoryBean();
		stFactory.setJobDetail(jobDetailFactoryBean().getObject());
		stFactory.setStartDelay(1000);
		stFactory.setName("mytrigger");
		stFactory.setGroup("mygroup");
		stFactory.setCronExpression("0 0/1 * 1/1 * ? *");
		return stFactory;
	}

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
