package com.hcp.objective.service;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.GoalPlanTemplate;
import com.hcp.objective.persistence.repositories.GoalPlanTemplateRepository;

@Service
@Transactional
public class GoalPlanTemplateService {
	public static int SUCCESS = 0; //
	public static int FAILED = -1;

	@Autowired
	private GoalPlanTemplateRepository goalPlanTemplateRepository;

	public List<GoalPlanTemplate> findByUserId(String userId) {
		return goalPlanTemplateRepository.findByUserId(userId);
	}

	public void deleteMore(@NotNull List<GoalPlanTemplate> goalPlans) {
		goalPlanTemplateRepository.delete(goalPlans);
	}
	
	public void deleteAll(){
		goalPlanTemplateRepository.deleteAll();
	}
	
	public List<GoalPlanTemplate> findAll(){
		return goalPlanTemplateRepository.findAll();
	}
	
	public void createMore(@NotNull List<GoalPlanTemplate> goalPlans){
		goalPlanTemplateRepository.save(goalPlans);
		goalPlanTemplateRepository.flush();
	}
}
