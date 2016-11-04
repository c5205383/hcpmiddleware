package com.hcp.objective.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.hcp.objective.persistence.bean.GoalPlanTemplate;

@Repository
@Transactional
public interface GoalPlanTemplateRepository extends JpaRepository<GoalPlanTemplate, Long>{
	
	List<GoalPlanTemplate> findByUserId(String userId);

}
