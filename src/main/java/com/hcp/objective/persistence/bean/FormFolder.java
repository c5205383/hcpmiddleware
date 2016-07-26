package com.hcp.objective.persistence.bean;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "T_FormFolder")
public class FormFolder implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6664107049667533886L;

	@Id
	@Column(name = "FOLDER_ID", nullable = false)
	private Long folderId;

	public Long getFolderId() {
		return folderId;
	}

	public void setFolderId(Long folderId) {
		this.folderId = folderId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	@Column(name = "USER_ID", nullable = false)
	private String userId;

	@Column(name = "FOLDER_NAME", nullable = false)
	private String folderName;
}
