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

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.hcp.objective.util.ODataExecutor;

@RestController
public class HomeController {
	public static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	@Autowired
	public ODataExecutor odataExecutor;
	
	@Autowired  
	private  HttpServletRequest request;
	
	
	
}

