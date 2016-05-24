package com.hcp.objective.web.model.response;

import java.io.Serializable;
import java.sql.Timestamp;

import com.hcp.objective.jpa.bean.Form;

public class FormDetailResponse  implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 342743430021959138L;

	private Integer id;
	private String userId;
	private Double score;
	private Timestamp compeleteDate;
	private String status;
	private Integer routeMapId;
	
	public FormDetailResponse() {
	}
	
	public FormDetailResponse(Form form) {
		this.setId(form.getId());
		this.setUserId(form.getUserId());
		this.setScore(form.getScore());
		this.setCompeleteDate(form.getCompeleteDate());
		this.setStatus(form.getStatus());
		this.setRouteMapId(form.getRouteMapId());
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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
