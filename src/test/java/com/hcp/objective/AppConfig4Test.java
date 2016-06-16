package com.hcp.objective;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;

import com.hcp.objective.common.ExcludeForTest;
import com.hcp.objective.service.BatchJobService;

@Configuration
@ComponentScan(basePackages = { "com.hcp.objective.util" }, excludeFilters = @Filter(ExcludeForTest.class))
public class AppConfig4Test {
	public static void main(String[] args) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(AppConfig4Test.class);
		ctx.refresh();
		try {
			BatchJobService bs = ctx.getBean(BatchJobService.class);
			System.out.println("Addition:" + bs.getClass().toString());
		} catch (BeansException be) {
			System.out.println(be.getMessage());
		}
		ctx.close();
	}

}
