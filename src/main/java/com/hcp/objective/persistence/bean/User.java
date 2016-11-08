package com.hcp.objective.persistence.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_USER")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2451883887257865748L;

	@Id
	@Column(name = "USERID", nullable = false)
	private String userId;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMarried() {
		return married;
	}

	public void setMarried(String married) {
		this.married = married;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAddressLine1() {
		return addressLine1;
	}

	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}

	public String getAddressLine2() {
		return addressLine2;
	}

	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}

	public String getKeyPosition() {
		return keyPosition;
	}

	public void setKeyPosition(String keyPosition) {
		this.keyPosition = keyPosition;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getDefaultFullName() {
		return defaultFullName;
	}

	public void setDefaultFullName(String defaultFullName) {
		this.defaultFullName = defaultFullName;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDivision() {
		return division;
	}

	public void setDivision(String division) {
		this.division = division;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	@Column(name = "EMPID")
	private String empId;
	
	@Column(name = "CITY")
	private String city;
	
	@Column(name = "USERNAME", nullable = false)
	private String username;
	
	@Column(name = "MARRIED")
	private String married;
	
	@Column(name = "NICKNAME")
	private String nickname;
	
	@Column(name = "ADDRESSLINE1")
	private String addressLine1;
	
	@Column(name = "ADDRESSLINE2")
	private String addressLine2;
	
	@Column(name = "KEYPOSITION")
	private String keyPosition;
	
	@Column(name = "JOBCODE")
	private String jobCode;
	
	@Column(name = "DEFAULTFULLNAME")
	private String defaultFullName;
	
	@Column(name = "TITLE")
	private String title;
	
	@Column(name = "DIVISION")
	private String division;
	
	@Column(name = "COUNTRY")
	private String country;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "DEPARTMENT")
	private String department;
	
	@Column(name = "STATE")
	private String state;
	
	@Column(name = "GENDER")
	private String gender;
	
	@Column(name = "BUSINESSPHONE")
	private String businessPhone;
	
	
}
