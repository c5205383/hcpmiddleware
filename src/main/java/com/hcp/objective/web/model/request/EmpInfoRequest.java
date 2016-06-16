package com.hcp.objective.web.model.request;

import java.io.Serializable;

public class EmpInfoRequest implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 365531236664116691L;

	private String username;
	private String status;
	private String userId;
	
	private String personIdExternal;
	
	private String startDate;

	private String jobCode;
	private String eventReason;
	private String company;
	private String businessUnit;
	private String managerId;
	
	private String gender;
	private String initials;
	private String firstName;
	private String lastName;
	private String email;
	
	private String location;
	private String businessPhone;
	private String empId;
	private String city;
	private String defaultFullName;
	private String function;
	private String type;
	private String hireDate;
	private String country;
	private String nationalId;
	private String nationalIdType;
	
	//Added by Bruce 2016-5-30
	private String transferCity;
	private String transferLocation;
	private String effectiveDate;
	private String transferCompany;
	
	public EmpInfoRequest(){
		
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPersonIdExternal() {
		return personIdExternal;
	}

	public void setPersonIdExternal(String personIdExternal) {
		this.personIdExternal = personIdExternal;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getJobCode() {
		return jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	public String getEventReason() {
		return eventReason;
	}

	public void setEventReason(String eventReason) {
		this.eventReason = eventReason;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getBusinessUnit() {
		return businessUnit;
	}

	public void setBusinessUnit(String businessUnit) {
		this.businessUnit = businessUnit;
	}

	public String getManagerId() {
		return managerId;
	}

	public void setManagerId(String managerId) {
		this.managerId = managerId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTransferCity() {
		return transferCity;
	}

	public void setTransferCity(String transferCity) {
		this.transferCity = transferCity;
	}

	public String getTransferLocation() {
		return transferLocation;
	}

	public void setTransferLocation(String transferLocation) {
		this.transferLocation = transferLocation;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getTransferCompany() {
		return transferCompany;
	}

	public void setTransferCompany(String transferCompany) {
		this.transferCompany = transferCompany;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getBusinessPhone() {
		return businessPhone;
	}

	public void setBusinessPhone(String businessPhone) {
		this.businessPhone = businessPhone;
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getDefaultFullName() {
		return defaultFullName;
	}

	public void setDefaultFullName(String defaultFullName) {
		this.defaultFullName = defaultFullName;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getFunction() {
		return function;
	}

	public void setFunction(String function) {
		this.function = function;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getHireDate() {
		return hireDate;
	}

	public void setHireDate(String hireDate) {
		this.hireDate = hireDate;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getNationalIdType() {
		return nationalIdType;
	}

	public void setNationalIdType(String nationalIdType) {
		this.nationalIdType = nationalIdType;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}
	
}
