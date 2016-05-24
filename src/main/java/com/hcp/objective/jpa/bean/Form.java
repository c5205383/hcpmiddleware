package com.hcp.objective.jpa.bean;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "FORM")
public class Form implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 2949217745622079056L;

	@Id
	@Column(name = "FORM_ID", nullable = false)
	private Long id;
	
	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "SCORE")
	private Double score;
	
	@Column(name = "COMPELETE_DATE")
	private Timestamp compeleteDate;
	
	@Column(name = "STATUS")
	private String status;
	
	@Column(name = "ROUTE_MAP_ID")
	private Integer routeMapId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	public Timestamp getCompeleteDate() {
		return compeleteDate;
	}

	public void setCompeleteDate(Timestamp compeleteDate) {
		this.compeleteDate = compeleteDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getRouteMapId() {
		return routeMapId;
	}

	public void setRouteMapId(Integer routeMapId) {
		this.routeMapId = routeMapId;
	}
	
}
