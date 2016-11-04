package com.hcp.objective.component.jobexecutor;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hcp.SpringConfig.SpringAppConfig;
import com.hcp.objective.bean.ApplicationPropertyBean;
import com.hcp.objective.service.IODataService;

public abstract class LocalSpringContext {

	AnnotationConfigApplicationContext ctx = null;
	
	ApplicationPropertyBean app = (ApplicationPropertyBean) getBean(ApplicationPropertyBean.class);
	
	IODataService oDataService = (IODataService) getBean(IODataService.class);

	public Object getBean(Class<?> clz) {
		if (ctx == null) {
			ctx = new AnnotationConfigApplicationContext();
			ctx.register(SpringAppConfig.class);
			ctx.refresh();
		}
		Object r = null;
		try {
			r = ctx.getBean(clz);
		} catch (BeansException be) {
			System.out.println(be.getMessage());
		}
		return r;
	}

	public void closeContext() {
		if (ctx != null)
			ctx.close();
	}
}
