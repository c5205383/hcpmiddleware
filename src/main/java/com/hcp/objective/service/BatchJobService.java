package com.hcp.objective.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.BatchJob;
import com.hcp.objective.persistence.repositories.BatchJobRepository;
import com.hcp.objective.web.model.request.BatchJobMergeRequest;

/**
 * The {@link}BatchJobService includes operations about the {@link}BatchJob object
 * 
 * @author Zero Yu
 */
@Service
@Transactional
//@ExcludeForTest
public class BatchJobService {

	public static int SUCCESS = 0; //
	public static int FAILED = -1;

	@Autowired
	private BatchJobRepository batchJobRepository;

	/**
	 * Find all exist batch jobs
	 * 
	 * @return List - a list of {@link}BatchJob
	 */
	public List<BatchJob> findAll() {
		return batchJobRepository.findAll();
	}

	/**
	 * Create a batch job
	 * 
	 * @param batchJobMergeRequest
	 *            the request include a batch job information
	 * @return A {@link}BatchJob object
	 */
	public BatchJob createOne(@NotNull BatchJobMergeRequest batchJobMergeRequest) {
		BatchJob batchJob = new BatchJob();
		mergeScalarProperties(batchJobMergeRequest, batchJob);
		// QuartzManager.addBatchJob(batchJob);
		return batchJobRepository.saveAndFlush(batchJob);
	}

	/**
	 * Delete one batch job by id
	 * 
	 * @param id
	 *            {@link}BatchJob's id
	 * @return state code
	 */
	public int deleteOneById(@NotNull Long id) {
		try {
			batchJobRepository.delete(id);
			return SUCCESS;
		} catch (IllegalArgumentException e) {
			return FAILED;
		}
	}

	/**
	 * Update one batch job by id
	 * 
	 * @param id
	 *            {@link}BatchJob's id
	 * @param batchJobMergeRequest
	 *            the request include a batch job information
	 * @return A {@link}BatchJob object
	 */
	public BatchJob updateOne(@NotNull Long id, @NotNull BatchJobMergeRequest batchJobMergeRequest) {
		BatchJob batchJob = batchJobRepository.findOne(id);
		if (id == null) {
			throw new IllegalArgumentException("id");
		}
		mergeScalarProperties(batchJobMergeRequest, batchJob);
		// QuartzManager.changeBatchJob(batchJob);
		return batchJobRepository.saveAndFlush(batchJob);
	}

	/**
	 * Find a list of batch jobs by owner
	 * 
	 * @param owner
	 *            {@link}BatchJob's owner
	 * @return List - a list of {@link}BatchJob
	 */
	public List<BatchJob> findByOwner(@NotNull String owner) {
		return batchJobRepository.findByOwner(owner);
	}

	/**
	 * Merge batch job's data from a {@link}BatchJobMergeRequest to a {@link}BatchJob object
	 * 
	 * @param batchJobMergeRequest
	 *            the request include a batch job information
	 * @param batchJob
	 *            the {@link}BatchJob object
	 */
	private void mergeScalarProperties(BatchJobMergeRequest batchJobMergeRequest, BatchJob batchJob) {
		// batchJob.setId(batchJobMergeRequest.getId());
		batchJob.setName(batchJobMergeRequest.getName());
		batchJob.setType(batchJobMergeRequest.getType());
		batchJob.setInterval(batchJobMergeRequest.getInterval());
		batchJob.setInfo(batchJobMergeRequest.getInfo());
		batchJob.setStatus(batchJobMergeRequest.getStatus());
		batchJob.setOwner(batchJobMergeRequest.getOwner());
	}
}
