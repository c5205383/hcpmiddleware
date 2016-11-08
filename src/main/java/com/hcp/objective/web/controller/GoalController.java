package com.hcp.objective.web.controller;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.Transformer;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.configuration.ExcludeForTest;
import com.hcp.objective.persistence.bean.GoalPlanTemplate;
import com.hcp.objective.service.GoalPlanTemplateService;
import com.hcp.objective.service.IContextService;
import com.hcp.objective.service.IODataService;
import com.hcp.objective.web.model.response.BaseResponse;

@RestController
@ExcludeForTest
public class GoalController {
	public static final Logger logger = LoggerFactory.getLogger(GoalController.class);
	@Autowired
	private IODataService oDataService;
	@Autowired
	private IContextService contextService;
	@Autowired
	private GoalPlanTemplateService goalPlanTemplateService;

	private Transformer<GoalPlanTemplate, BaseResponse<GoalPlanTemplate>> SuccessTransformer = new Transformer<GoalPlanTemplate, BaseResponse<GoalPlanTemplate>>() {

		@Override
		public BaseResponse<GoalPlanTemplate> transform(GoalPlanTemplate goalPlan) {
			return new BaseResponse<GoalPlanTemplate>(goalPlan);
		}
	};

	@RequestMapping(value = "/goalPlanTemplate", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalPlanTemplate() {
		return oDataService.getGoalPlanTemplate();
	}

	@RequestMapping(value = "/apigoals", produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalsByTemplate(String goalPlanId) {
		String userId = contextService.getLoginUserName();
		return oDataService.getGoalsByTemplate(goalPlanId,userId);
	}

	@RequestMapping(value = "/mygoalplan", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String getMyPlan(@RequestParam(value = "userId", required = false) String userId) {
		Collection<BaseResponse<GoalPlanTemplate>> list = null;
		if (userId != null && userId.equals("") != true) {
		} else {
			userId = contextService.getLoginUserName();
		}
		if (userId == null || userId.equals("") == true) {
			list = new ArrayList<BaseResponse<GoalPlanTemplate>>();
			list.add(new BaseResponse<GoalPlanTemplate>(BaseResponse.FAILED_VALUE, null, null));
		} else {
			list = CollectionUtils.collect(goalPlanTemplateService.findByUserId(userId), SuccessTransformer);
		}
		return new JSONArray(list).toString();
	}

	@RequestMapping(value = "/goalplan", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
	public @ResponseBody String getGoalPlan() {
		Collection<BaseResponse<GoalPlanTemplate>> list = null;
		list = CollectionUtils.collect(goalPlanTemplateService.findAll(), SuccessTransformer);

		return new JSONArray(list).toString();
	}
}
