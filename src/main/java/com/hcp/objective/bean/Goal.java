package com.hcp.objective.bean;

public class Goal {
	private long id;
	private double weight;
	//private double actualAchievement;
	private String lastModified;
	private double done;
	private String link;
	private String state;
	private String numbering;
	private String type;
	private String guid;
	private String modifier;
	private String category;
	private int flag;
	private String start;
	private String metric;
	private String name;
	private String userId;
	//private String actual;
	private String due;
	
	public Goal(){}
	
	public Goal(long id, double weight, /*double actualAchievement,*/ String lastModified, double done, String link,
			String state, String numbering, String type, String guid, String modifier, String category, int flag,
			String start, String metric, String name, String userId, /*String actual,*/ String due) {
		super();
		this.id = id;
		this.weight = weight;
		//this.actualAchievement = actualAchievement;
		this.lastModified = lastModified;
		this.done = done;
		this.link = link;
		this.state = state;
		this.numbering = numbering;
		this.type = type;
		this.guid = guid;
		this.modifier = modifier;
		this.category = category;
		this.flag = flag;
		this.start = start;
		this.metric = metric;
		this.name = name;
		this.userId = userId;
		//this.actual = actual;
		this.due = due;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	/*public double getActualAchievement() {
		return actualAchievement;
	}

	public void setActualAchievement(double actualAchievement) {
		this.actualAchievement = actualAchievement;
	}*/

	public String getLastModified() {
		return lastModified;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public double getDone() {
		return done;
	}

	public void setDone(double done) {
		this.done = done;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getNumbering() {
		return numbering;
	}

	public void setNumbering(String numbering) {
		this.numbering = numbering;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getMetric() {
		return metric;
	}

	public void setMetric(String metric) {
		this.metric = metric;
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

	/*public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}*/

	public String getDue() {
		return due;
	}

	public void setDue(String due) {
		this.due = due;
	}

	
	
}
