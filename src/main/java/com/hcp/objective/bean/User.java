package com.hcp.objective.bean;

public class User {
	private String userId;
	private String userName;
	private String city;
	private String gender;

	public User() {
	}

	public User(String userId, String userName, String city, String gender) {
		this.userId = userId;
		this.setUserName(userName);
		this.city = city;
		this.gender = gender;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", city=" + city + ", gender=" + gender + "]";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
