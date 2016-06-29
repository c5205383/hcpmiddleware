package com.hcp.objective.service;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcp.objective.bean.ApplicationPropertyBean;
import com.hcp.objective.util.ODataConstants;
import com.hcp.objective.web.model.request.EmpInfoRequest;
import com.sun.istack.NotNull;

public class EmpEmploymentService {
	public static final Logger logger = LoggerFactory.getLogger(EmpEmploymentService.class);
	
	private String authType;
	private String auth;
	private String serviceUrl;
	private String authorizationHeader;
	private URL url;
	
	public EmpEmploymentService(ApplicationPropertyBean bean) throws Exception{
		authType = bean.getAuthorizationType();
		auth = bean.getAuthorization();
		serviceUrl = bean.getUrl();
		authorizationHeader = authType + " " + new String(Base64.encodeBase64((auth).getBytes()));
		url = new URL(serviceUrl + "/upsert");
	}
	
	private HttpURLConnection openConnection() throws IOException{
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("POST");
		conn.setRequestProperty("Content-Type", ODataConstants.APPLICATION_JSON);
		conn.setRequestProperty("Authorization", authorizationHeader);
		conn.setDoOutput(true);
		conn.setDoInput(true);
		
		return conn;
	}
	
	public void createUser(@NotNull EmpInfoRequest empInfoRequest) throws IOException{
		HttpURLConnection conn = openConnection();
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		String requestBody = createUserRequestBody(empInfoRequest);
		out.write(requestBody);
		out.flush();
		out.close();
		
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		conn.disconnect();
		
		logger.info("========create user========");
		logger.info(body);
		
	}
	
	public void createPerPerson(EmpInfoRequest empInfoRequest) throws IOException {
		HttpURLConnection conn = openConnection();
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		String requestBody = createPerPersonRequestBody(empInfoRequest);
		out.write(requestBody);
		out.flush();
		out.close();
		
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		conn.disconnect();
		
		logger.info("========create perperson========");
		logger.info(body);
	}

	public void createPerEmail(EmpInfoRequest empInfoRequest) throws IOException {
		HttpURLConnection conn = openConnection();
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		String requestBody = createEmailRequestBody(empInfoRequest);
		out.write(requestBody);
		out.flush();
		out.close();
		
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		conn.disconnect();
		
		logger.info("========create peremail========");
		logger.info(body);
	}

	public void createEmpEmployment(EmpInfoRequest empInfoRequest) throws IOException {
		HttpURLConnection conn = openConnection();
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		String requestBody = createEmpRequestBody(empInfoRequest);
		out.write(requestBody);
		out.flush();
		out.close();
		
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		conn.disconnect();
		
		logger.info("========create emp========");
		logger.info(body);
	}

	public void createEmpJob(EmpInfoRequest empInfoRequest) throws IOException {
		HttpURLConnection conn = openConnection();
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		String requestBody = createEmpJobRequestBody(empInfoRequest);
		out.write(requestBody);
		out.flush();
		out.close();
		conn.disconnect();
		
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		
		logger.info("========create emp job========");
		logger.info(body);
	}

	public void createPerPersonal(EmpInfoRequest empInfoRequest) throws IOException {
		HttpURLConnection conn = openConnection();
		OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
		String requestBody = createPerPersonalRequestBody(empInfoRequest);
		out.write(requestBody);
		out.flush();
		out.close();
		conn.disconnect();
		
		InputStream in = conn.getInputStream();
		String encoding = conn.getContentEncoding();
		encoding = encoding == null ? "UTF-8" : encoding;
		String body = IOUtils.toString(in, encoding);
		
		logger.info("========create perpersonal========");
		logger.info(body);
	}

	private String createUserRequestBody(EmpInfoRequest empInfoRequest) {
		Map<String,Object> propMap = new HashMap<String, Object>();
		Map<String,Object> uriMap = new HashMap<String, Object>();
		uriMap.put("uri", "User('"+empInfoRequest.getUserId()+"')");
		propMap.put("__metadata", uriMap);
		propMap.put("username", empInfoRequest.getUsername());
		propMap.put("status", empInfoRequest.getStatus());
		propMap.put("userId", empInfoRequest.getUserId());
		JSONObject jsonobj = new JSONObject(propMap);
		return jsonobj.toString();
	}

	private String createPerPersonRequestBody(EmpInfoRequest empInfoRequest) {
		Map<String,Object> propMap = new HashMap<String, Object>();
		Map<String,Object> uriMap = new HashMap<String, Object>();
		uriMap.put("uri", "PerPerson('"+empInfoRequest.getUserId()+"')");
		propMap.put("__metadata", uriMap);
		propMap.put("personIdExternal", empInfoRequest.getPersonIdExternal());
		propMap.put("userId", empInfoRequest.getUserId());
		JSONObject jsonobj = new JSONObject(propMap);
		return jsonobj.toString();
	}

	private String createEmailRequestBody(EmpInfoRequest empInfoRequest) {
		Map<String,Object> propMap = new HashMap<String, Object>();
		Map<String,Object> uriMap = new HashMap<String, Object>();
		uriMap.put("uri", "PerEmail(personIdExternal='"+empInfoRequest.getPersonIdExternal()+"',emailType='17161')");
		propMap.put("__metadata", uriMap);
		propMap.put("personIdExternal", empInfoRequest.getPersonIdExternal());
		propMap.put("userId", empInfoRequest.getUserId());
		JSONObject jsonobj = new JSONObject(propMap);
		return jsonobj.toString();
	}
	
	private String createEmpRequestBody(EmpInfoRequest empInfoRequest) {
		Map<String,Object> propMap = new HashMap<String, Object>();
		Map<String,Object> uriMap = new HashMap<String, Object>();
		uriMap.put("uri", "EmpEmployment(personIdExternal='"+empInfoRequest.getPersonIdExternal()+"',userId='"+empInfoRequest.getUserId()+"')");
		propMap.put("__metadata", uriMap);
		propMap.put("startDate", "/Date("+empInfoRequest.getStartDate()+")/");
		propMap.put("personIdExternal", empInfoRequest.getPersonIdExternal());
		JSONObject jsonobj = new JSONObject(propMap);
		return jsonobj.toString();
	}
	
	private String createEmpJobRequestBody(EmpInfoRequest empInfoRequest) {
		Map<String,Object> propMap = new HashMap<String, Object>();
		Map<String,Object> uriMap = new HashMap<String, Object>();
		uriMap.put("uri", "EmpJob");
		propMap.put("__metadata", uriMap);
		propMap.put("jobCode", empInfoRequest.getJobCode());
		propMap.put("userId", empInfoRequest.getUserId());
		propMap.put("startDate", "/Date("+empInfoRequest.getStartDate()+")/");
		propMap.put("eventReason", empInfoRequest.getEventReason());
		propMap.put("company", empInfoRequest.getCompany());
		propMap.put("businessUnit", empInfoRequest.getBusinessUnit());
		propMap.put("managerId", empInfoRequest.getManagerId());
		JSONObject jsonobj = new JSONObject(propMap);
		return jsonobj.toString();
	}

	private String createPerPersonalRequestBody(EmpInfoRequest empInfoRequest) {
		SimpleDateFormat dateFormat =   new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ss" );
		String time = dateFormat.format(empInfoRequest.getStartDate());
		Map<String,Object> propMap = new HashMap<String, Object>();
		Map<String,Object> uriMap = new HashMap<String, Object>();
		uriMap.put("uri", "PerPersonal(personIdExternal='"+empInfoRequest.getPersonIdExternal()+"',startDate=datetime'"+time+"')");
		propMap.put("__metadata", uriMap);
		propMap.put("personIdExternal", empInfoRequest.getPersonIdExternal());
		propMap.put("gender", empInfoRequest.getGender());
		propMap.put("initials", empInfoRequest.getInitials());
		propMap.put("firstName", empInfoRequest.getFirstName());
		propMap.put("lastName", empInfoRequest.getLastName());
		JSONObject jsonobj = new JSONObject(propMap);
		return jsonobj.toString();
	}
}

