package com.hcp.objective.component.jobexecutor;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hcp.SpringConfig.SpringAppConfig;

public abstract class HcpExecutor {
	public Object getBean(Class<?> clz) {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(SpringAppConfig.class);
		ctx.refresh();
		Object r = null;
		try {
			r = ctx.getBean(clz);
		} catch (BeansException be) {
			System.out.println(be.getMessage());
		}
		ctx.close();
		return r;
	}
}
