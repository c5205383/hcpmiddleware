package com.hcp.objective.web.odata4j;

import javax.servlet.http.HttpServletRequest;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmSimpleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.util.ODataUtils;

@RestController("advancedGoalController")
public class GoalController {
	@Autowired
	public ODataUtils odataUtils;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping(value = "/getGoalsByTemplateId4j")
	public @ResponseBody String getGoalsByTemplate(String templateId) {
		ODataBean bean = odataUtils.getInitializeBean(request);
		String serviceUrl = bean.getUrl();
		String user = bean.getQueryUser();
		String pwd = bean.getQueryPwd();
		ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
		ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth(user, pwd)).build();
		String goalUri = "Goal" + "_" + templateId;
		StringBuffer sb=new StringBuffer();
		sb.append("{\"goals\":[");
		for (OEntity e : consumer.getEntities(goalUri).execute()) {
			sb.append("{");
			for (OProperty<?> p : e.getProperties()) {
			      Object v = p.getValue();
			      if (p.getType().equals(EdmSimpleType.BINARY) && v != null){
			    	  v = org.odata4j.repack.org.apache.commons.codec.binary.Base64.encodeBase64String((byte[]) v).trim();
			      } 
			      sb.append(String.format("\"%s\":\"%s\",",p.getName(), v));
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("},");
		}
		sb.deleteCharAt(sb.length()-1);
		sb.append("]}");

		return sb.toString();
	}

}
