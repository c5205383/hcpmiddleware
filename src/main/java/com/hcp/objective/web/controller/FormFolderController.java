package com.hcp.objective.web.controller;

import java.util.Collection;

import javax.validation.constraints.NotNull;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.persistence.bean.FormFolder;
import com.hcp.objective.service.FormFolderService;
import com.hcp.objective.service.IContextService;
import com.hcp.objective.web.model.response.BaseResponse;

/**
 * 
 * @author BRUCE WANG
 *
 */
@RestController
public class FormFolderController {

	public static final Logger logger = LoggerFactory.getLogger(FormFolderController.class);

	private Transformer<FormFolder, BaseResponse<FormFolder>> SuccessTransformer = new Transformer<FormFolder, BaseResponse<FormFolder>>() {
		@Override
		public BaseResponse<FormFolder> transform(FormFolder folder) {
			return new BaseResponse<FormFolder>(folder);
		}
	};
	@Autowired
	private FormFolderService formFolderService;

	private IContextService contextService;

	@RequestMapping(value = "/formFolder", method = RequestMethod.POST)
	public @ResponseBody String createOne(@NotNull @RequestBody FormFolder formFolder) {

		// Create job storage
		FormFolder folder = formFolderService.createOne(formFolder);

		BaseResponse<FormFolder> response = new BaseResponse<FormFolder>(folder);
		return new JSONObject(response).toString();
	}

	/**
	 * Get All jobs
	 * 
	 * @return
	 */
	@RequestMapping(value = "/formFolder", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String findAllJobs() {
		Collection<BaseResponse<FormFolder>> list = CollectionUtils.collect(formFolderService.findAll(),
				SuccessTransformer);
		return new JSONArray(list).toString();
	}
}
