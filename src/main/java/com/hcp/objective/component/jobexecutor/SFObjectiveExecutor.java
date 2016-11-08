package com.hcp.objective.component.jobexecutor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hcp.objective.bean.ApplicationPropertyBean;
import com.hcp.objective.persistence.bean.GoalPlanTemplate;
import com.hcp.objective.service.GoalPlanTemplateService;
import com.hcp.objective.service.IODataService;

@Component("OBJECTIVE_EXECUTOR")
public class SFObjectiveExecutor implements IExecutor {

	private String key_d = "d";
	private String key_result = "results";
	private String key_goalplan_id = "id";
	private String key_startDate = "startDate";
	private String key_description = "description";
	private String key_name = "name";
	private String key_dueDate = "dueDate";

	public static final Logger logger = LoggerFactory.getLogger(SFObjectiveExecutor.class);
	@Autowired
	ApplicationPropertyBean app;
	@Autowired
	IODataService oDataService;
	@Autowired
	GoalPlanTemplateService goalPlanTemplateService;

	@Override
	public void execute() {

		// TODO: delete all goal plan
		goalPlanTemplateService.deleteAll();

		// TODO: update new goal plan
		if (oDataService != null) {
			String sData = oDataService.getGoalPlanTemplate();
			if (sData != null && !sData.isEmpty()) {
				System.out.println(sData);
				JSONObject object = new JSONObject(sData);
				JSONArray array = object.getJSONObject(key_d).getJSONArray(key_result);
				System.out.println(array.length());

				if (array != null) {
					List<GoalPlanTemplate> goalPlans = new ArrayList<GoalPlanTemplate>();
					for (Iterator<Object> iterator = array.iterator(); iterator.hasNext();) {
						JSONObject one = (JSONObject) iterator.next();
						GoalPlanTemplate bean = new GoalPlanTemplate();
						long id = one.getLong(key_goalplan_id);
						bean.setGoalPlanId(id);
						bean.setName(one.getString(key_name));
						bean.setDesc(one.getString(key_description));
						bean.setStartDate(one.getString(key_startDate));
						bean.setDueDate(one.getString(key_dueDate));
						bean.setUserId(app.getQueryUser());

						goalPlans.add(bean);
					}
					goalPlanTemplateService.createMore(goalPlans);
				}
			}
		}

	}

}
