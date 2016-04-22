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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.bean.ODataBean;
import com.hcp.objective.util.ODataUtils;

@RestController
public class HomeController {
	@Autowired
	public ODataUtils odataUtils;
	
	@Autowired  
	private  HttpServletRequest request;
	
	@RequestMapping(value = "/getUsersJsonHome")
	public @ResponseBody String getUserJson(String json) {
		ODataBean bean = odataUtils.getInitializeBean(request);
		String authType = bean.getAuthorizationType();
		String auth = bean.getAuthorization();
		String serviceUrl = bean.getUrl();
		ODataEntry dataEntry = null;
		try{	
		Edm edm = odataUtils.readEdmAndNotValidate(serviceUrl+"/User/$metadata", authType, auth);
		//$filter=username%20eq%20%27msaban%27 $select=username,userId&
		ODataFeed dataFeed = odataUtils.readFeed(edm, serviceUrl, "application/xml+atom", edm.getEntitySets().get(0).getName(), null,"$top=50");
		List<ODataEntry> dataEntryList = dataFeed.getEntries();
		StringBuffer buf = new StringBuffer();
		String dataKey ="";
		///buf.append(dataEntry.getMetadata());
		for(int i=0;i<dataEntryList.size();i++){
			dataEntry = dataEntryList.get(i);
			Map propMap = dataEntry.getProperties();
			for(Iterator iter = propMap.keySet().iterator();iter.hasNext();){
				dataKey = iter.next().toString();
				buf.append(dataKey+"======"+dataEntry.getProperties().get(dataKey)+"<br/>");
			}
			
			buf.append("<br/><br/><br/>");
		}
		   return json + "(" +  buf.toString() + ")";
		}catch(Exception e){
			return e.getMessage();
		}
		
	}
	
}

