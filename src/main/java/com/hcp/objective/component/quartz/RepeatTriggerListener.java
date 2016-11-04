package com.hcp.objective.component.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;

public class RepeatTriggerListener implements TriggerListener {
	
	private static final String TRIGGER_LISTENER_NAME = "RepeatTriggerListener";
	@Override
	public String getName() {
		return TRIGGER_LISTENER_NAME;
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		// TODO Auto-generated method stub
		
	}

}
