package com.hcp.objective;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Configuration;

import com.hcp.objective.configuration.ExcludeForTest;
import com.hcp.objective.service.BatchJobService;

@Configuration
@ComponentScan(basePackages = {
		"com.hcp.objective" }, excludeFilters = @Filter(type = FilterType.ANNOTATION, value = ExcludeForTest.class))
public class SpringAppConfig4Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(SpringAppConfig4Test.class);
		ctx.refresh();
		try {
			BatchJobService util = ctx.getBean(BatchJobService.class);
			System.out.println("Addition:" + util.getClass().toString());
		} catch (BeansException be) {
			System.out.println(be.getMessage());
		}
		ctx.close();
	}

}
