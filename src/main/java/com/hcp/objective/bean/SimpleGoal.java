package com.hcp.objective.bean;

public class SimpleGoal {
	private long id;
	private String type;
	private String start;
	private String due;
	private String name;
	private String userId;
	
	public SimpleGoal(){}
	
	public SimpleGoal(long id, String type, String start, String due, String name, String userId) {
		super();
		this.id = id;
		this.type = type;
		this.start = start;
		this.due = due;
		this.name = name;
		this.userId = userId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getDue() {
		return due;
	}

	public void setDue(String due) {
		this.due = due;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
