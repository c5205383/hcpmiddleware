package com.hcp.objective.bean;

public class ApplicationPropertyBean {
	private String url;
	private String authorizationType;
	private String authorization;
	private String proxyName;
	private int proxyPort;
	private String queryUser;
	private String queryPwd;
	
	//Added by Bruce 2016-5-30
	private String company;
	private String contentType;
	private boolean isProxy;
	private String charset;
	
	//for quartz scheduler
	private String quartzState;

	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getAuthorizationType() {
		return authorizationType;
	}
	public void setAuthorizationType(String authorizationType) {
		this.authorizationType = authorizationType;
	}
	public String getAuthorization() {
		return authorization;
	}
	public void setAuthorization(String authorization) {
		this.authorization = authorization;
	}
	public String getProxyName() {
		return proxyName;
	}
	public void setProxyName(String proxyName) {
		this.proxyName = proxyName;
	}
	public int getProxyPort() {
		return proxyPort;
	}
	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}
	
	public String getQueryUser() {
		return queryUser;
	}
	
	public void setQueryUser(String queryUser) {
		this.queryUser = queryUser;
	}
	
	public String getQueryPwd() {
		return queryPwd;
	}
	
	public void setQueryPwd(String queryPwd) {
		this.queryPwd = queryPwd;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public boolean isProxy() {
		return isProxy;
	}
	public void setProxy(boolean isProxy) {
		this.isProxy = isProxy;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getQuartzState() {
		return quartzState;
	}
	public void setQuartzState(String quartzState) {
		this.quartzState = quartzState;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}

}
