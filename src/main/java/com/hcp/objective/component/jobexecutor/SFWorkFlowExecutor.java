package com.hcp.objective.component.jobexecutor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Job of querying workflow requests associated with employees.
 * 
 * @author Zero Yu
 */
public class SFWorkFlowExecutor implements IExecutor {
	public static final Logger logger = LoggerFactory.getLogger(SFWorkFlowExecutor.class);

	public void execute() {
		logger.info(new Date().toString());
		System.out.println(new Date().toString());
	}

}
