package com.hcp.objective.component.jobexecutor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component("EMPLOYEE_EXECUTOR")
public class SFEmployeeExecutor implements IExecutor {
	public static final Logger logger = LoggerFactory.getLogger(SFEmployeeExecutor.class);

	public void execute() {
		logger.info(new Date().toString());
		System.out.println(new Date().toString());
	}

}
