package com.hcp.objective.web.model.request;

import java.io.Serializable;

import org.hibernate.validator.constraints.NotBlank;

/**
 * The BatchJobMergeRequest is a request object which used to store batch job
 * data before the data merged into a {@link}BatchJob object
 * 
 * @author Zero Yu
 */
public class BatchJobMergeRequest implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = -8937869606246065718L;

	@NotBlank
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String type;
	@NotBlank
	private Double interval;
	private String info;
	@NotBlank
	private Boolean status;

	public BatchJobMergeRequest() {
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
}
