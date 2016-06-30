package com.hcp.objective.persistence.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: BatchJob
 * 
 * @author Zero Yu
 */
@Entity
@Table(name = "T_BatchJob")
public class BatchJob implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = 5433969825096666920L;
	public static final boolean STATUS_USED = true;
	public static String JOB_GROUP_NAME = "DEFAULT_JOBGROUP_NAME";

	@Id
	@GeneratedValue
	@Column(name = "ID", nullable = false)
	private Long id;

	@Column(name = "NAME", nullable = false)
	private String name;

	@Column(name = "TYPE", nullable = false)
	private String type;

	@Column(name = "INTERVAL", nullable = false)
	private Double interval;

	@Column(name = "INFO", nullable = false)
	private String info;

	@Column(name = "STATUS", nullable = false)
	private Boolean status;

	@Column(name = "OWNER", nullable = false)
	private String owner;

	public Long getId() {
		return id;
	}

	public String getJobId() {
		return Long.toString(id);
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

	public String getCronExpression() {
		// TODO Auto-generated method stub
		double interval = this.getInterval();
		String time = null;
		if (interval == 1) {
			time = "0 0/1 * * * ?";
		} else if (interval == 0.5) {
			time = "0 0/30 * * * ?";
		}
		return time == null ? "0/2 * * * * ?" : time;
	}

}
