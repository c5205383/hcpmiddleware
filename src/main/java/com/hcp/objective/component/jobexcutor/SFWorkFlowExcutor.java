package com.hcp.objective.component.jobexcutor;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Job of querying workflow requests associated with employees.
 * 
 * @author Zero Yu
 */
public class SFWorkFlowExcutor {
	public static final Logger logger = LoggerFactory.getLogger(SFWorkFlowExcutor.class);

	public static void execute() {
		logger.error(new Date().toString());
	}

}
