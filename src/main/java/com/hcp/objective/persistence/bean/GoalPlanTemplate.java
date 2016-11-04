package com.hcp.objective.persistence.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="T_GOAL_PLAN_TEMPLATE")
public class GoalPlanTemplate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -667754709083444009L;

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "GOAL_PLAN_ID", nullable = false)
	private Long goalPlanId;
	
	public Long getGoalPlanId() {
		return goalPlanId;
	}

	public void setGoalPlanId(Long goalPlanId) {
		this.goalPlanId = goalPlanId;
	}

	@Column(name = "USER_ID")
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "NAME", nullable = false)
	private String name;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCRIPTION", nullable = false)
	private String desc;
	
	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Column(name = "STARTDATE", nullable = false)
	private String startDate;
	
	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	@Column(name = "DUEDATE", nullable = false)
	private String dueDate;

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	
}
