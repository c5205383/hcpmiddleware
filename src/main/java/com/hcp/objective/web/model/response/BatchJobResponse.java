package com.hcp.objective.web.model.response;

import java.io.Serializable;

import com.hcp.objective.persistence.bean.BatchJob;

public class BatchJobResponse extends BaseResponse implements Serializable {

	/**
	 * Generated serial version uid
	 */
	private static final long serialVersionUID = -7569276776498425612L;

	public BatchJobResponse() {
	}

	public BatchJobResponse(BatchJob batchJob) {
		this.setObject(batchJob);
	}

	public BatchJobResponse(int status, String message, BatchJob batchJob) {
		this.setStatus(status);
		if (message == null) {
			this.setMessage(status == 0 ? SUCCESS : FAILED);
		} else {
			this.setMessage(message);
		}
		this.setMessage(message);
		this.setObject(batchJob);
	}

}
