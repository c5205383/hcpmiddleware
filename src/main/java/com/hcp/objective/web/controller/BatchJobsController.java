package com.hcp.objective.web.controller;

import java.util.Collection;

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

import com.hcp.objective.common.ExcludeForTest;
import com.hcp.objective.jpa.bean.BatchJob;
import com.hcp.objective.service.BatchJobService;
import com.hcp.objective.web.model.request.BatchJobMergeRequest;
import com.hcp.objective.web.model.response.BatchJobResponse;

/**
 * The {@link}RestController for BatchJob entity.
 * 
 * @author Zero Yu
 */
@RestController
@RequestMapping("/batchJob")
@ExcludeForTest
public class BatchJobsController {

	@Autowired
	private BatchJobService batchJobService;
	
	private Transformer<BatchJob, BatchJobResponse> DETAIL_RESPONSE_TRANSFORMER = new Transformer<BatchJob, BatchJobResponse>() {

		@Override
		public BatchJobResponse transform(BatchJob input) {
			return new BatchJobResponse(input);
		}
	};
	
	/**
	 * The API to <b>CREATE</b> a new batch job.<br>
	 * <br>
	 * API URL - <b>"/hcp/batchJob"</b><br>
	 * Method - <b>"POST"</b>
	 * 
	 * @param batchJobMergeRequest
	 *            the {@link}BatchJobMergeRequest object.
	 * @return a {@link}BatchJobResponse object.
	 */
	@RequestMapping(method = RequestMethod.POST)
	public BatchJobResponse createOne(@NotNull @RequestBody BatchJobMergeRequest batchJobMergeRequest) {
		return new BatchJobResponse(batchJobService.createOne(batchJobMergeRequest));
	}
	
	/**
	 * The API to DELETE a batch job by id.<br>
	 * <br>
	 * API URL - <b>"/hcp/batchJob/{id}"</b><br>
	 * Method - <b>"DELETE"</b>
	 * 
	 * @param id
	 *            the batch job's id.
	 * @return the status message string.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public String deleteOne(@PathVariable("id") Long id) {
		return batchJobService.deleteOneById(id);
	}
	
	/**
	 * The API to <b>UPDATE</b> a batch job's data by id.<br>
	 * <br>
	 * API URL - <b>"/hcp/batchJob/{id}"</b><br>
	 * Method - <b>"PUT"</b>
	 * 
	 * @param id
	 *            the user id.
	 * @param batchJobMergeRequest
	 *            the {@link}BatchJobMergeRequest object.
	 * @return a {@link}BatchJobResponse object.
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public BatchJobResponse updateOne(@PathVariable("id") Long id, 
			@NotNull @RequestBody BatchJobMergeRequest batchJobMergeRequest) {
		return new BatchJobResponse(batchJobService.updateOne(id, batchJobMergeRequest));
	}
	
	/**
	 * The API to <b>GET</b> batch jobs by some conditions.<br>
	 * <br>
	 * API URL - <b>"/hcp/batchJob"</b> or <b>"/hcp/batchJob?owner={owner}"</b><br>
	 * Method - <b>"GET"</b>
	 * 
	 * @param owner
	 *            the owner's name 
	 * @return the {@link}Collection of {@link}BatchJobResponse.
	 */
	@RequestMapping(method = RequestMethod.GET)
	public Collection<BatchJobResponse> findBySeveralConditions(@RequestParam(value = "owner", required = false) String owner) {
		if (owner != null && owner.equals("") != true) {
			return CollectionUtils.collect(batchJobService.findByOwner(owner), DETAIL_RESPONSE_TRANSFORMER);
		} else {
			return CollectionUtils.collect(batchJobService.findAll(), DETAIL_RESPONSE_TRANSFORMER);
		}
	}
}
