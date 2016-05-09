package com.hcp.objective.web.odata4j;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.util.ODataExecutor;

@RestController("empEmploymentController4j")
public class EmpEmploymentController4j {
	public static final Logger logger = LoggerFactory.getLogger(EmpEmploymentController4j.class);

	@Autowired
	public ODataExecutor odataUtils;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/createEmpEmployment4j")
	public @ResponseBody String createEmpEmployment4j() {
		long requestStartTime = System.currentTimeMillis();
		try {
			ODataConsumer.Builder builder = ODataConsumers.newBuilder("https://apisalesdemo8.successfactors.com/odata/v2");
			ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth("admin@HCPDISCC", "hcp")).build();
//			OEntity user = createUser(consumer);
			OEntity perPerson = createPerPerson(consumer);
//			OEntity empEmployment = createEmpEmployment(consumer);
//			OEntity empJob = createEmpJob(consumer);
//			OEntity newPerPerson = updatePerPerson(consumer);
			long requestEndTime = System.currentTimeMillis();
			logger.error("========4j===========" + (requestEndTime - requestStartTime) / 1000);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		return "create emp doned";
	}

	private OEntity createUser(ODataConsumer consumer) {
		long requestStartTime = System.currentTimeMillis();
		OEntity newUser = consumer.createEntity("User")
				.properties(OProperties.string("username", "testzero"))
				.properties(OProperties.string("userId", "testzero"))
				.properties(OProperties.string("status", "Active"))
				.execute();
		long requestEndTime = System.currentTimeMillis();
		logger.error("create user done" + (requestEndTime-requestStartTime)/1000);
		return newUser;
	}
	
	private OEntity createPerPerson(ODataConsumer consumer){
		long requestStartTime = System.currentTimeMillis();
		OEntity newPerPerson = consumer.createEntity("PerPerson")
				.properties(OProperties.string("personIdExternal", "i007"))
				.properties(OProperties.string("userId", "testzero"))
				.execute();
		long requestEndTime = System.currentTimeMillis();
		logger.error("create perperson done" + (requestEndTime-requestStartTime)/1000);
		return newPerPerson;
	}
	
	private OEntity createEmpEmployment(ODataConsumer consumer){
		long requestStartTime = System.currentTimeMillis();
		OEntity newEmpEmployment = consumer.createEntity("EmpEmployment")
				.properties(OProperties.string("userId", "testzero"))
				.properties(OProperties.string("personIdExternal", "i007"))
				.properties(OProperties.datetime("startDate", new Date()))
				.execute();
		long requestEndTime = System.currentTimeMillis();
		logger.error("create emp done" + (requestEndTime-requestStartTime)/1000);
		return newEmpEmployment;
	}
	

	private OEntity createEmpJob(ODataConsumer consumer) {
		long requestStartTime = System.currentTimeMillis();
		OEntity newUser = consumer.createEntity("EmpJob")
				.properties(OProperties.string("userId", "testzero"))
				.properties(OProperties.datetime("startDate", new Date()))
				.properties(OProperties.string("jobCode", "ADMIN-1"))
				.properties(OProperties.string("eventReason", "HIRNEW"))
				.properties(OProperties.string("businessUnit", "ACE_CORP"))
				.properties(OProperties.string("managerId", "NO_MANAGER"))
				.execute();
		long requestEndTime = System.currentTimeMillis();
		logger.error("create empjob done" + (requestEndTime-requestStartTime)/1000);
		return newUser;
	}
	

	private OEntity updatePerPerson(ODataConsumer consumer) {
		long requestStartTime = System.currentTimeMillis();
		OEntity newPerPerson = consumer.createEntity("PerPerson")
				.properties(OProperties.string("personIdExternal", "i007"))
				.properties(OProperties.string("gender", "M"))
				.properties(OProperties.string("initials", "tz"))
				.properties(OProperties.string("firstName", "test"))
				.properties(OProperties.string("lastName", "zero"))
				.execute();
		long requestEndTime = System.currentTimeMillis();
		logger.error("create perperson done" + (requestEndTime-requestStartTime)/1000);
		return newPerPerson;
	}
	
	
}
