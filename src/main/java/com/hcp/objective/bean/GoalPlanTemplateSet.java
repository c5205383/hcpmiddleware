package com.hcp.objective.bean;

import java.util.ArrayList;
import java.util.List;

public class GoalPlanTemplateSet {
	List<GoalPlanTemplate> templateSet;
	
	public GoalPlanTemplateSet() {
		templateSet=new ArrayList<GoalPlanTemplate>();
	}
	
	public void addGoalPlanTemplate(GoalPlanTemplate gpt) {
		templateSet.add(gpt);
	}

	public List<GoalPlanTemplate> getList() {
		return templateSet;
	}

	public void setList(List<GoalPlanTemplate> templateSet) {
		this.templateSet = templateSet;
	}
	
	
}
