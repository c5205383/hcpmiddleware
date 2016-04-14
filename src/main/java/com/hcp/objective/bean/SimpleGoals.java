package com.hcp.objective.bean;

import java.util.ArrayList;
import java.util.List;

public class SimpleGoals {
	private List<SimpleGoal> goals;
	
	public SimpleGoals(){goals=new ArrayList<SimpleGoal>();}
	
	public void addGoal(SimpleGoal goal){
		goals.add(goal);
	}

	public List<SimpleGoal> getGoals() {
		return goals;
	}

	public void setGoals(List<SimpleGoal> goals) {
		this.goals = goals;
	}
	
	
}
