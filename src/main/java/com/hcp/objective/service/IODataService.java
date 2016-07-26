package com.hcp.objective.service;

import com.hcp.objective.web.model.request.EmpInfoRequest;

public interface IODataService {

	/**
	 * Service for Controller to request User's direct reports by SFSF ODATA API
	 * @param loginUserId
	 * @return
	 */
	String getEmpDirectReports(String loginUserId);

	/**
	 *  Change SFSF employee job location
	 * @param empInfos
	 * @return
	 */
	String transferEmployee(EmpInfoRequest[] empInfos);

	/**
	 * Get SFSF Work flow by event reason
	 * @param eventReason
	 * @return
	 */
	String getEmpWorkflow(String eventReason);

	/**
	 * Create Employee
	 * @param empInfos
	 * @return
	 */
	String createEmployee(EmpInfoRequest[] empInfos);

	/**
	 * Get SFSF Companies
	 * @return
	 */
	String getFOCompany();

	/**
	 * Get SFSF Countries
	 * @return
	 */
	String getCountry();

	/**
	 * Get SFSF FOEventReason Data
	 * @return
	 */
	String getFOEventReason();

	/**
	 * Get SFSF Locations
	 * @return
	 */
	String getFOLocation();

	/**
	 * Get SFSF Job Code
	 * @return
	 */
	String getFOJobCode();

	/**
	 * Get SFSF Business Unit
	 * @return
	 */
	String getFOBusinessUnit();

	/**
	 * Get SFSF Goal Plan
	 * @return
	 */
	String getGoalPlanTemplate();

	/**
	 * Get SFSF Goals by Goal Plan template
	 * @param goalPlanId
	 * @return
	 */
	String getGoalsByTemplate(String goalPlanId);
	
	/**
	 * Get SFSF Form Folders. 
	 * @param userid
	 * @return
	 */
	String getFormFolder(String userid);

}
