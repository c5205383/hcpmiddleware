package com.hcp.objective.bean;

public class GoalPlanTemplate {
	private Long id;
	private String name;
	private String description;
	private String startDate;
	private String dueDate;
	private Long displayOrder;
	private Long parentPlanId;
	
	public GoalPlanTemplate(){}
	
	public GoalPlanTemplate(Long id, String name, String description, String startDate, String dueDate,
			Long displayOrder, Long parentPlanId) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.startDate = startDate;
		this.dueDate = dueDate;
		this.displayOrder = displayOrder;
		this.parentPlanId = parentPlanId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getDueDate() {
		return dueDate;
	}

	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}

	public Long getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Long displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Long getParentPlanId() {
		return parentPlanId;
	}

	public void setParentPlanId(Long parentPlanId) {
		this.parentPlanId = parentPlanId;
	}
	
	
}
