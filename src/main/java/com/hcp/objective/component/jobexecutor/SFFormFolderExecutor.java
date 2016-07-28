package com.hcp.objective.component.jobexecutor;

import org.springframework.beans.BeansException;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.hcp.SpringConfig.SpringAppConfig;
import com.hcp.objective.service.FormFolderService;
import com.hcp.objective.service.IODataService;

public class SFFormFolderExecutor implements IExecutor {

	@Override
	public void execute(IODataService service) {
		String result = service.getFormFolder(null);
		System.out.println(result);

	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
		ctx.register(SpringAppConfig.class);
		ctx.refresh();
		try {
			FormFolderService service = ctx.getBean(FormFolderService.class);
			System.out.println("Addition:" + service.getClass().toString());
			System.out.println(service.findAll().size());
		} catch (BeansException be) {
			System.out.println(be.getMessage());
		}
		ctx.close();
	}

}
