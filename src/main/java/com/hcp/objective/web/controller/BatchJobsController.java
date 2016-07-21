package com.hcp.objective.web.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
// @ExcludeForTest
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
	public @ResponseBody String createOne(@NotNull @RequestBody BatchJobMergeRequest batchJobMergeRequest) {
		BatchJobResponse response = new BatchJobResponse(batchJobService.createOne(batchJobMergeRequest));
		return new JSONObject(response).toString();
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

		if (batchJobMergeRequest.getStatus()) {
			// TODO: Start Job
		} else {
			// TODO: Stop Job
		}

		BatchJob result = batchJobService.updateOne(id, batchJobMergeRequest);

		return new BatchJobResponse(batchJobService.updateOne(id, batchJobMergeRequest));
	}

	/**
	 * 
	 * @param owner
	 *            the owner's name
	 * @return the {@link}Collection of {@link}BatchJobResponse.
	 */
	@RequestMapping(value = "/mybatchjob", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String findMyJobs(@RequestParam(value = "owner", required = false) String owner) {
		Collection<BatchJobResponse> list = null;
		if (owner != null && owner.equals("") != true) {
		} else {
			owner = contextService.getLoginUserName();
		}
		if (owner == null || owner.equals("") == true) {
			list = new ArrayList<BatchJobResponse>();
			list.add(new BatchJobResponse(BatchJobService.FAILED, null, null));
		} else {
			list = CollectionUtils.collect(batchJobService.findByOwner(owner), SuccessTransformer);
		}
		return new JSONArray(list).toString();
	}

	/**
	 * Get All jobs
	 * 
	 * @return
	 */
	@RequestMapping(value = "/batchJob", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String findAllJobs() {
		Collection<BatchJobResponse> list = CollectionUtils.collect(batchJobService.findAll(), SuccessTransformer);
		return new JSONArray(list).toString();
	}
}
