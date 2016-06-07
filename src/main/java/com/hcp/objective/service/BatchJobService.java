package com.hcp.objective.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.jpa.bean.BatchJob;
import com.hcp.objective.persistence.repositories.BatchJobRepository;
import com.hcp.objective.web.model.request.BatchJobMergeRequest;

/**
 * The {@link}BatchJobService includes operations about the {@link}BatchJob object
 * 
 * @author Zero Yu
 */
@Service
@Transactional
public class BatchJobService {

	@Autowired
	private BatchJobRepository batchJobRepository;
	
	public List<BatchJob> findAll() {
		return batchJobRepository.findAll();
	}
	
	public BatchJob createOne(@NotNull BatchJobMergeRequest batchJobMergeRequest) {
		BatchJob batchJob = new BatchJob();
		mergeScalarProperties(batchJobMergeRequest, batchJob);
		QuartzManager.addBatchJob(batchJob);
		return batchJobRepository.saveAndFlush(batchJob);
	}

	public String deleteOneById(@NotNull Long id) {
		try {
			BatchJob batchJob = batchJobRepository.findOne(id);
			QuartzManager.deleteBatchJob(batchJob);
			batchJobRepository.delete(id);
			return "Delete session successfully";
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		}
	}
	
	public BatchJob updateOne(@NotNull Long id, @NotNull BatchJobMergeRequest batchJobMergeRequest) {
		BatchJob batchJob = batchJobRepository.findOne(id);
		if (id == null) {
			throw new IllegalArgumentException("id");
		}
		mergeScalarProperties(batchJobMergeRequest, batchJob);
		QuartzManager.changeBatchJob(batchJob);
		return batchJobRepository.saveAndFlush(batchJob);
	}

	public List<BatchJob> findByOwner(@NotNull String owner) {
		return batchJobRepository.findByOwner(owner);
	}
	
	private void mergeScalarProperties(BatchJobMergeRequest batchJobMergeRequest, BatchJob batchJob) {
		//batchJob.setId(batchJobMergeRequest.getId());
		batchJob.setName(batchJobMergeRequest.getName());
		batchJob.setType(batchJobMergeRequest.getType());
		batchJob.setInterval(batchJobMergeRequest.getInterval());
		batchJob.setInfo(batchJobMergeRequest.getInfo());
		batchJob.setStatus(batchJobMergeRequest.getStatus());
		batchJob.setOwner(batchJobMergeRequest.getOwner());
	}
}
