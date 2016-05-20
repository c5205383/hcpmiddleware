package com.hcp.objective.web.olingo;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.service.EmpEmploymentService;
import com.hcp.objective.util.ODataExecutor;
import com.hcp.objective.web.model.request.EmpInfoRequest;

@RestController
public class EmpEmploymentController {
	public static final Logger logger = LoggerFactory.getLogger(EmpEmploymentController.class);
	@Autowired
	public ODataExecutor odataExecutor;

	@Autowired
	private HttpServletRequest request;
	
	private EmpEmploymentService empService = null;

	@RequestMapping(value = "/createEmpEmployment", method = RequestMethod.POST)
	public @ResponseBody String createEmpEmployment(@RequestBody EmpInfoRequest empInfoRequest) {
		try {
			ODataBean bean = odataExecutor.getInitializeBean(request);
			empService = new EmpEmploymentService(bean);
			
			empService.createUser(empInfoRequest);
			empService.createPerPerson(empInfoRequest);
			empService.createPerEmail(empInfoRequest);
			empService.createEmpEmployment(empInfoRequest);
			empService.createEmpJob(empInfoRequest);
			empService.createPerPersonal(empInfoRequest);

			return "done";
		} catch (IOException e1) {
			logger.error(e1.getMessage(),e1);
			return "error";
		} catch (Exception e2) {
			logger.error(e2.getMessage(),e2);
			return "error";
		}
		
	}

}
