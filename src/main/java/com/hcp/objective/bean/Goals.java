package com.hcp.objective.bean;

import java.util.ArrayList;
import java.util.List;

public class Goals {
	private List<Goal> goals;
	
	public Goals(){goals=new ArrayList<Goal>();}
	
	public void addGoal(Goal goal){
		goals.add(goal);
	}

	public List<Goal> getGoals() {
		return goals;
	}

	public void setGoals(List<Goal> goals) {
		this.goals = goals;
	}
}
