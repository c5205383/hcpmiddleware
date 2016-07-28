package com.hcp.objective.component.jobexecutor;

import com.hcp.objective.service.IODataService;

public interface IExecutor {
	public void execute();

	public void execute(IODataService service);
}
