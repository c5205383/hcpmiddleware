package com.hcp.objective.web.odata4j;

import javax.servlet.http.HttpServletRequest;

import org.core4j.Enumerable;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OLinks;
import org.odata4j.core.OProperties;
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
	
	public static final Logger logger = LoggerFactory.getLogger(UserController4j.class);
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
	
	@RequestMapping(value = "/createUser4j")
	public @ResponseBody String createUser4j() {
		long requestStartTime = System.currentTimeMillis();
		try{
			ODataConsumer.Builder builder = ODataConsumers.newBuilder("https://sfapiqacand.sflab.ondemand.com/odata/v2");
			ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth("cgrant@huayi", "123")).build();
			//OEntity msaban1 = consumer.getEntity("User", "admin").execute();
			OEntity admin = consumer.getEntity("User", "admin").execute();
			OEntity newUser = consumer.createEntity("User")
					.properties(OProperties.string("userId", "zero"))
					.properties(OProperties.string("gender", "M"))
					.properties(OProperties.string("username", "zero"))
					.properties(OProperties.string("firstName", "zero"))
					.properties(OProperties.string("lastName", "yu"))
					.properties(OProperties.string("email", "zero.yu@sap.com"))
					.properties(OProperties.string("timeZone", "EST"))
					.properties(OProperties.string("jobCode", "HR-MGR"))
					.properties(OProperties.string("location", "CD"))
					.properties(OProperties.string("division", "Industries"))
					.properties(OProperties.string("status", "t"))
					.properties(OProperties.string("country", "China"))
					.properties(OProperties.string("department", "Industries"))
					.link("manager", admin)
					.link("hr", admin)
					.execute();
			long requestEndTime = System.currentTimeMillis();
			logger.error("========4j==========="+(requestEndTime-requestStartTime)/1000);
		}catch(Exception e){
			logger.error(e.getMessage(),e);
		}
		
		return "create user doned";
	}
	
	
}
