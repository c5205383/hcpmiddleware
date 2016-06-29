package com.hcp.objective.web.model.response;

import java.io.Serializable;

import com.hcp.objective.persistence.bean.BatchJob;


public class BatchJobResponse implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = -7569276776498425612L;

	private Long id;
	private String name;
	private String type;
	private Double interval;
	private String info;
	private Boolean status;
	private String owner;

	public BatchJobResponse() {
	}
	
	public BatchJobResponse(BatchJob batchJob) {
		this.setId(batchJob.getId());
		this.setName(batchJob.getName());
		this.setType(batchJob.getType());
		this.setInterval(batchJob.getInterval());
		this.setInfo(batchJob.getInfo());
		this.setStatus(batchJob.getStatus());
		this.setOwner(batchJob.getOwner());
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Double getInterval() {
		return interval;
	}

	public void setInterval(Double interval) {
		this.interval = interval;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
	
}
