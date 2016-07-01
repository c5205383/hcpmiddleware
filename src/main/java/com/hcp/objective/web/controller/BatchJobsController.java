package com.hcp.objective.web.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.configuration.ExcludeForTest;
import com.hcp.objective.persistence.bean.BatchJob;
import com.hcp.objective.service.BatchJobService;
import com.hcp.objective.service.IContextService;
import com.hcp.objective.web.model.request.BatchJobMergeRequest;
import com.hcp.objective.web.model.response.BatchJobResponse;

/**
 * The {@link}RestController for BatchJob entity.
 * 
 * @author Zero Yu
 */
@RestController
@ExcludeForTest
public class BatchJobsController {

	@Autowired
	private BatchJobService batchJobService;

	@Autowired
	private IContextService contextService;

	private Transformer<BatchJob, BatchJobResponse> SuccessTransformer = new Transformer<BatchJob, BatchJobResponse>() {

		@Override
		public BatchJobResponse transform(BatchJob job) {
			return new BatchJobResponse(job);
		}
	};

	/**
	 * 
	 * @param batchJobMergeRequest
	 *            the {@link}BatchJobMergeRequest object.
	 * @return a {@link}BatchJobResponse object.
	 */
	@RequestMapping(value = "/batchJob", method = RequestMethod.POST)
	public BatchJobResponse createOne(@NotNull @RequestBody BatchJobMergeRequest batchJobMergeRequest) {
		return new BatchJobResponse(batchJobService.createOne(batchJobMergeRequest));
	}

	/**
	 * 
	 * @param id
	 *            the batch job's id.
	 * @return the status message string.
	 */
	@RequestMapping(value = "/batchJob/{id}", method = RequestMethod.DELETE)
	public BatchJobResponse deleteOne(@PathVariable("id") Long id) {
		return new BatchJobResponse(batchJobService.deleteOneById(id), null, null);
	}

	/**
	 * 
	 * @param id
	 *            the user id.
	 * @param batchJobMergeRequest
	 *            the {@link}BatchJobMergeRequest object.
	 * @return a {@link}BatchJobResponse object.
	 */
	@RequestMapping(value = "/batchJob/{id}", method = RequestMethod.PUT)
	public BatchJobResponse updateOne(@PathVariable("id") Long id,
			@NotNull @RequestBody BatchJobMergeRequest batchJobMergeRequest) {
		return new BatchJobResponse(batchJobService.updateOne(id, batchJobMergeRequest));
	}

	/**
	 * 
	 * @param owner
	 *            the owner's name
	 * @return the {@link}Collection of {@link}BatchJobResponse.
	 */
	@RequestMapping(value = "/mybatchjob", method = RequestMethod.GET)
	public Collection<BatchJobResponse> findMyJobs(@RequestParam(value = "owner", required = false) String owner) {
		if (owner != null && owner.equals("") != true) {
		} else {
			owner = contextService.getLoginUserName();
		}

		if (owner == null || owner.equals("") == true) {
			List<BatchJobResponse> list = new ArrayList<BatchJobResponse>();
			list.add(new BatchJobResponse(BatchJobService.FAILED, null, null));
			return list;
		}
		return CollectionUtils.collect(batchJobService.findByOwner(owner), SuccessTransformer);
	}

	/**
	 * 
	 * @param owner
	 *            the owner's name
	 * @return the {@link}Collection of {@link}BatchJobResponse.
	 */
	@RequestMapping(value = "/batchJob", method = RequestMethod.GET)
	public Collection<BatchJobResponse> findAllJobs() {
		return CollectionUtils.collect(batchJobService.findAll(), SuccessTransformer);
	}
}
