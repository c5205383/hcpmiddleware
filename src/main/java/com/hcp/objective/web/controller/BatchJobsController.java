package com.hcp.objective.web.controller;

import java.util.ArrayList;
import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.hcp.objective.service.quartz.SingleQuartzManagerService;
//import com.hcp.objective.web.model.request.BatchJobMergeRequest;
import com.hcp.objective.web.model.response.BaseResponse;

/**
 * The {@link}RestController for BatchJob entity.
 * 
 * @author Zero Yu
 */
@RestController
// @ExcludeForTest
public class BatchJobsController {

	public static final Logger logger = LoggerFactory.getLogger(BatchJobsController.class);

	@Autowired
	private BatchJobService batchJobService;

	@Autowired
	private IContextService contextService;

	@Autowired
	SingleQuartzManagerService singleQuartzManagerService;

	private Transformer<BatchJob, BaseResponse<BatchJob>> SuccessTransformer = new Transformer<BatchJob, BaseResponse<BatchJob>>() {

		@Override
		public BaseResponse<BatchJob> transform(BatchJob job) {
			return new BaseResponse<BatchJob>(job);
		}
	};

	/**
	 * 
	 * @param batchJobMergeRequest
	 *            the {@link}BatchJobMergeRequest object.
	 * @return a {@link}BatchJobResponse object.
	 */
	@RequestMapping(value = "/batchJob", method = RequestMethod.POST)
	public @ResponseBody String createOne(@NotNull @RequestBody BatchJob batchJobMergeRequest) {

		// Create job storage
		BatchJob job = batchJobService.createOne(batchJobMergeRequest);

		// If job status is active, start Job, else delete job.
		//singleQuartzManagerService.delete(job);
		//if (job != null && job.getStatus()) {
		//	singleQuartzManagerService.create(job);
		//}

		BaseResponse<BatchJob> response = new BaseResponse<BatchJob>(job);
		return new JSONObject(response).toString();
	}

	/**
	 * 
	 * @param id
	 *            the batch job's id.
	 * @return the status message string.
	 */
	@RequestMapping(value = "/batchJob/{id}", method = RequestMethod.DELETE)
	public BaseResponse<BatchJob> deleteOne(@PathVariable("id") Long id) {
		return new BaseResponse<BatchJob>(batchJobService.deleteOneById(id), null, null);
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
	public BaseResponse<BatchJob> updateOne(@PathVariable("id") Long id,
			@NotNull @RequestBody BatchJob batchJobMergeRequest) {
		boolean success = false;

		// Save batch job change to DB
		BatchJob job = batchJobService.updateOne(id, batchJobMergeRequest);
		// If job status is active, start Job, else delete job.
		singleQuartzManagerService.delete(job);
		if (job.getStatus()) {
			success = singleQuartzManagerService.create(job);
		}

		return new BaseResponse<BatchJob>(success ? BatchJobService.SUCCESS : BatchJobService.FAILED, "", job);
	}

	/**
	 * 
	 * @param owner
	 *            the owner's name
	 * @return the {@link}Collection of {@link}BatchJobResponse.
	 */
	@RequestMapping(value = "/mybatchjob", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String findMyJobs(@RequestParam(value = "owner", required = false) String owner) {
		Collection<BaseResponse<BatchJob>> list = null;
		if (owner != null && owner.equals("") != true) {
		} else {
			owner = contextService.getLoginUserName();
		}
		if (owner == null || owner.equals("") == true) {
			list = new ArrayList<BaseResponse<BatchJob>>();
			list.add(new BaseResponse<BatchJob>(BatchJobService.FAILED, null, null));
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
		Collection<BaseResponse<BatchJob>> list = CollectionUtils.collect(batchJobService.findAll(), SuccessTransformer);
		return new JSONArray(list).toString();
	}
}
