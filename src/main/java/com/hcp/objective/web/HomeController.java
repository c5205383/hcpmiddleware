/*package com.hcp.objective.web;

import java.util.List;

import org.odata4j.consumer.ODataConsumer;
import org.odata4j.consumer.ODataConsumers;
import org.odata4j.consumer.behaviors.OClientBehaviors;
import org.odata4j.core.OEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.Shop;

@RestController
public class HomeController {
	private static final String LOCAL_URL="http://localhost:8080/odata/v2/";
	private static final String LOCAL_USER="admin@LMSUOCompany1";
	private static final String LOCAL_PWD="pwd";
	
	private static final String URL="https://sfapiqacand.sflab.ondemand.com/odata/v2";
	private static final String USER="cgrant@PLTzcompany";
	private static final String PWD="pwd";
/*	@RequestMapping("/system/state")
	public String getShopInJSON(String jsonpName) {
		Shop shop = new Shop();
		shop.setName("kevin");
		return  jsonpName + "(" + "{'name':'kevin','score':123}" + ")";

	}
	 
	@RequestMapping(value = "/saveUser")  
    public @ResponseBody List<Shop> saveUser(@RequestBody List<Shop> shops) { 
         System.out.println(shops.get(1).getName());
         Shop shop = new Shop();
         shop.setName("Cathy");
         shops.add(shop);
         return shops;
    } 
	
	@RequestMapping(value = "/getUserJson")  
    public @ResponseBody Shop getUserJson(String userId) { 
         System.out.println(userId);
         Shop shop = new Shop();
        
         StringBuffer categoryBuf = new StringBuffer();
         ODataConsumer.Builder builder = ODataConsumers.newBuilder(URL);
 		 ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth(USER,
 				PWD)).build();
 		// list category names
 		for (OEntity category : consumer.getEntities("User").execute()) {
 		  String categoryName = category.getProperty("userId", String.class).getValue();
 		 categoryBuf.append(" Category name: " + categoryName);
 		}
 		 shop.setName(categoryBuf.toString());
         return shop;
    } 

}*/

package com.hcp.objective.web;

import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.schedule.TestJob;
import com.hcp.objective.service.QuartzManager;
import com.hcp.objective.util.ODataExecutor;
import com.hcp.objective.util.Util;

@RestController
public class HomeController {
	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	public ODataExecutor odataExecutor;

	@Autowired
	private HttpServletRequest request;

	@RequestMapping(value = "/get", method = RequestMethod.GET)
	public @ResponseBody String get(@RequestParam String entitySetName, @RequestParam(required = false) String eid,
			@RequestParam(required = false) String expand, @RequestParam(required = false) String query) {
		ODataBean bean;
		try {
			bean = odataExecutor.getInitializeBean(request);
			String authType = bean.getAuthorizationType();
			String auth = bean.getAuthorization();
			String serviceUrl = bean.getUrl();
			// String entitySetName = "WfRequest";
			// String query = "?$format=json";
			String authorizationHeader = authType + " ";
			authorizationHeader += new String(Base64.encodeBase64((auth).getBytes()));

			//StringBuilder result = new StringBuilder();
			String absolutUri = odataExecutor.createUri(serviceUrl, entitySetName, eid, expand, query);
			URL url = new URL(absolutUri);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", authorizationHeader);

//			BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//			String line;
//			while ((line = rd.readLine()) != null) {
//				result.append(line);
//			}
			InputStream in = conn.getInputStream();
			String encoding = conn.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = IOUtils.toString(in, encoding);
			conn.disconnect();
			in.close();
			//rd.close();

			return body;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}

	}

	@RequestMapping(value = "/post", method = RequestMethod.POST)
	public @ResponseBody String post() {
		try {
			ODataBean bean = odataExecutor.getInitializeBean(request);
			String authType = bean.getAuthorizationType();
			String auth = bean.getAuthorization();
			String serviceUrl = bean.getUrl();
			String authorizationHeader = authType + " ";
			authorizationHeader += new String(Base64.encodeBase64((auth).getBytes()));

			URL url = new URL(serviceUrl + "/upsert");
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Authorization", authorizationHeader);
			conn.setDoOutput(true);
			conn.setDoInput(true);
			OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
			// String requestBody = "{\"__metadata\": {\"uri\":
			// \"PerPerson('testzero')\"},\"personIdExternal\":
			// \"i326962x\",\"userId\": \"testzero\"}";
			// String requestBody = "{\"__metadata\": {\"uri\":
			// \"User('testzero01')\"},\"username\": \"tzero\",\"status\":
			// \"Active\",\"userId\": \"testzero01\"}";
			String requestBody = "";
			out.write(requestBody);
			out.flush();
			out.close();

			InputStream in = conn.getInputStream();
			String encoding = conn.getContentEncoding();
			encoding = encoding == null ? "UTF-8" : encoding;
			String body = IOUtils.toString(in, encoding);

			conn.disconnect();

			return body;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return "";
		}
	}

	@RequestMapping(value = "/testlogin", method = RequestMethod.GET)
	public String testlogin() {
		return Util.getLoginUser(request).toString();
	}
	
	@RequestMapping(value = "/testadd", method = RequestMethod.GET)
	public void testadd() {
		QuartzManager.addJob("testjob1", "trigger1", TestJob.class, "0/3 * * * * ?"); 
	}
	
	@RequestMapping(value = "/testpause", method = RequestMethod.GET)
	public void testPause() {
		QuartzManager.pauseJob("testjob1", "trigger1"); 
	}
	
	@RequestMapping(value = "/testresume", method = RequestMethod.GET)
	public void testresume() {
		QuartzManager.resumeJob("testjob1", "trigger1"); 
	}
}
