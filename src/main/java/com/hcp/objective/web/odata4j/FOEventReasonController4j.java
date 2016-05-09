package com.hcp.objective.web.odata4j;

import javax.servlet.http.HttpServletRequest;

import org.core4j.Enumerable;
import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.odata4j.core.OProperty;
import org.odata4j.edm.EdmSimpleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.util.ODataExecutor;

@RestController("FOEventReasonController4j")
public class FOEventReasonController4j {
	public static final Logger logger = LoggerFactory.getLogger(FOEventReasonController4j.class);
	@Autowired
	public ODataExecutor odataUtils;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/getFOEventReason4j")
	public @ResponseBody String getFOEventReason() {
		long requestStartTime = System.currentTimeMillis();
		String FOEventReasonUri = "FOEventReason";
		String serviceUrl = null;
		String user = null;
		String pwd = null;
		try {
			ODataBean bean = odataUtils.getInitializeBean(request);
			serviceUrl = "https://apisalesdemo8.successfactors.com/odata/v2";
			user = "admin@HCPDISCC";
			pwd = "hcp";

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}

		ODataConsumer.Builder builder = ODataConsumers.newBuilder(serviceUrl);
		ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth(user, pwd)).build();
		Enumerable<OEntity> enumerable = consumer.getEntities(FOEventReasonUri).execute();
		StringBuffer sb = new StringBuffer();
		sb.append("{\"dataObj\":[");
		for (OEntity e : enumerable) {
			sb.append("{");
			for (OProperty<?> p : e.getProperties()) {
				Object v = p.getValue();
				if (p.getType().equals(EdmSimpleType.BINARY) && v != null) {
					v = org.odata4j.repack.org.apache.commons.codec.binary.Base64.encodeBase64String((byte[]) v).trim();
				}
				sb.append(String.format("\"%s\":\"%s\",", p.getName(), v));
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append("},");
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append("]}");
		long requestEndTime = System.currentTimeMillis();
		logger.error("========4j===========" + (requestEndTime - requestStartTime) / 1000);
		return sb.toString();
	}
}
