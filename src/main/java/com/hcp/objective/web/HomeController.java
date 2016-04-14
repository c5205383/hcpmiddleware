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

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.List;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.EntryMetadata;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.Goal;
import com.hcp.objective.bean.GoalPlanTemplate;
import com.hcp.objective.bean.GoalPlanTemplateSet;
import com.hcp.objective.bean.Goals;
import com.hcp.objective.bean.SimpleGoals;
import com.hcp.objective.bean.SimpleGoal;
import com.hcp.objective.bean.User;
import com.hcp.objective.bean.Users;
import com.hcp.objective.util.JSONSerializationUtils;
import com.hcp.objective.util.ODataTaskExecutor;
import com.hcp.objective.util.OlingoSampleOperation;
import com.hcp.objective.util.Util;

@RestController
public class HomeController {
	private static final String LOCAL_URL = "http://10.56.205.93:8181/odata/v2/";
	private static final String LOCAL_USER = "admin@LMSUOCompany1";
	private static final String LOCAL_PWD = "pwd";

	private static final String URL = "https://sfapiqacand.sflab.ondemand.com/odata/v2";
	private static final String USER = "cgrant@PLTzcompany";
	private static final String PWD = "pwd";

	@RequestMapping(value = "/getUserJson")
	public @ResponseBody String getUserJson(String json) {
		
		OlingoSampleOperation api = new OlingoSampleOperation();
		api.setAuthorization("cgrant@PLTzcompany:pwd");
		api.setAuthorizationType("Basic");
		api.setProxyName("proxy.sha.sap.corp");
		api.setProxyPort(8080);
		String serviceUri = "https://sfapiqacand.sflab.ondemand.com/odata/v2/User";
		String authType = api.getAuthorizationType();
		String auth = api.getAuthorization();
		
		try{
		Edm edm = api.readEdmAndNotValidate(serviceUri, authType, auth);
		ODataEntry entry = api.readEntry(edm, serviceUri, "application/json",
				"User", "format=json");
		EntryMetadata em = entry.getMetadata();
		return em.toString();
		}catch(Exception e){
			return e.getMessage();
		}

		/*ODataTaskExecutor executer = new ODataTaskExecutor();
		executer.execute("https://sfapiqacand.sflab.ondemand.com/odata/v2/User");
		return null;*/
		/* String resource = "";
		
		 try {
		      HttpClient httpClient = new DefaultHttpClient();

		      if (System.getProperties().containsKey("http.proxyHost") && System.getProperties().containsKey("http.proxyPort")) {
		        // support proxy settings
		        String hostName = "proxy.sha.sap.corp";
		        String hostPort = "8080";

		        HttpHost proxy = new HttpHost(hostName, Integer.parseInt(hostPort));
		        httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		      }

		      HttpUriRequest httpRequest = new HttpGet("https://sfapiqacand.sflab.ondemand.com/odata/v2/User");
		
		      if (headers != null) {
		          for (String key : headers.keySet()) {
		            String value = (String) headers.get(key).toString();
		            Header header = new BasicHeader(key, value);
		            httpRequest.addHeader(header);
		          }
		        }
		        if (mediaType != null) {
		          if (content != null) {
		            httpRequest.addHeader(HttpHeaders.CONTENT_TYPE, mediaType.toString());
		          }
		          if (method == ODataHttpMethod.GET)
		          {
		            httpRequest.addHeader(HttpHeaders.ACCEPT, mediaType.toString());
		          }
		        }

		        // Execute the request
		        HttpResponse response = httpClient.execute(httpRequest);
		        // Examine the response status
		 
		        // Get hold of the response entity
		        HttpEntity entity = response.getEntity();
		        // If the response does not enclose an entity, there is no need
		        // to worry about connection release
		        if (entity != null) {
		          resource = EntityUtils.toString(entity);
		        }
		        return resource;
		        //return new ResponseData(response.getStatusLine().getStatusCode(), resource);
		      } catch (Exception e) {
		        //throw Throwables.propagate(e);
		      }*/
		
		
		
		
		
		
		
		
		/*ODataConsumer.Builder builder = ODataConsumers.newBuilder(URL);
		builder.setFormatType(FormatType.ATOM);
		ODataConsumer consumer = builder.setClientBehaviors(new BasicAuthenticationBehavior(USER, PWD)).build();
		Enumerable<OEntity> userss = consumer.getEntities("User").execute();
		for (OEntity category : userss) {
			String uId = category.getProperty("userId", String.class).getValue();
			String uName = category.getProperty("username", String.class).getValue();
			String city = category.getProperty("city", String.class).getValue();
			String gender = category.getProperty("gender", String.class).getValue();
			User user = new User(uId, uName, city, gender);
			users.addUser(user);
		}
		String userJsonString = JSONSerializationUtils.toJSON(users);
		return json + "(" + userJsonString + ")";*/
		
	}
/*	public Edm readEdm(String serviceUrl) throws IOException, ODataException {
	    InputStream content = execute(serviceUrl + "/" + "$metadata", "application/json", "GET");
	    
	    
	    return EntityProvider.readMetadata(content, false);
	  }
	private InputStream execute(String relativeUri, String contentType, String httpMethod) throws IOException {
	    HttpURLConnection connection = initializeConnection(relativeUri, contentType, httpMethod);

	    connection.connect();
	    checkStatus(connection);

	    InputStream content = connection.getInputStream();
	    content = logRawContent(httpMethod + " request on uri '" + relativeUri + "' with content:\n  ", content, "\n");
	    return content;
	  }
	
	private HttpURLConnection initializeConnection(String absolutUri, String contentType, String httpMethod)
		      throws MalformedURLException, IOException {
		    URL url = new URL(absolutUri);
		    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	
		    
		    
		    String userpass = username + ":" + password;
		    String basicAuth = "Basic " + new String(new Base64().encode(userpass.getBytes()));
		    uc.setRequestProperty ("Authorization", basicAuth);
		    InputStream in = uc.getInputStream();
		    
		    
		    connection.setRequestMethod(httpMethod);
		    connection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);
		    if(HTTP_METHOD_POST.equals(httpMethod) || HTTP_METHOD_PUT.equals(httpMethod)) {
		      connection.setDoOutput(true);
		      connection.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
		    }

		    return connection;
		  }*/

	
	
	
	
	/*@RequestMapping(value = "/getGoalJson")
	public @ResponseBody String getGoalJson(String json) {
		Goals goals = new Goals();
		ODataConsumer.Builder builder = ODataConsumers.newBuilder(URL);
		ODataConsumer consumer = builder.setClientBehaviors(OClientBehaviors.basicAuth(USER, PWD)).build();
		for (OEntity category : consumer.getEntities("Goal_5").filter("userId eq 'cgrant1'").execute()) {
			long Id = category.getProperty("id", Long.class).getValue();
			String name = category.getProperty("name", String.class).getValue();
			String userId = category.getProperty("userId", String.class).getValue();
			String state = category.getProperty("state", String.class).getValue();
			String lm = category.getProperty("lastModified").getValue().toString();
			Goal goal = new Goal(Id, name, userId, state, lm);
			goals.addGoal(goal);
		}
		String goalJsonString = JSON.toJSONString(goals);
		System.out.println(json + "(" + goalJsonString + ")");
		return json + "(" + goalJsonString + ")";
	}*/

	

	
	
}

