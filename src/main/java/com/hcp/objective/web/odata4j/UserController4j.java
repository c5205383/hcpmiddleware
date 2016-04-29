package com.hcp.objective.web.odata4j;

import javax.servlet.http.HttpServletRequest;

import org.core4j.Enumerable;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperty;
import org.odata4j.core.ORelatedEntitiesLink;
import org.odata4j.core.ORelatedEntityLink;
import org.odata4j.edm.EdmSimpleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.util.ODataExecutor;

@RestController("userController4j")
public class UserController4j {
	
	public static final Logger logger = LoggerFactory.getLogger(GoalController4j.class);
	@Autowired
	public ODataExecutor odataUtils;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping(value = "/getUserDirectReports4j")
	public @ResponseBody String getUserDirectReports4j() {
		//String directReportsUri = "User('cgrant1')";
		String serviceUrl = null;
		String user = null;
		String pwd = null;
		try{
			ODataBean bean = odataUtils.getInitializeBean(request);
			serviceUrl = bean.getUrl();
			user = bean.getQueryUser();
			pwd = bean.getQueryPwd();
		
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
		ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth(user, pwd)).build();
		OEntity e1 = consumer.getEntity("User", "cgrant1").execute();
		ORelatedEntitiesLink link = e1.getLink("directReports", ORelatedEntitiesLink.class);
		Enumerable<OEntity> enumerable = consumer.getEntities(link).execute();
		StringBuffer sb=new StringBuffer();
		sb.append("{\"dataObj\":[");
		for (OEntity e : enumerable) {
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
