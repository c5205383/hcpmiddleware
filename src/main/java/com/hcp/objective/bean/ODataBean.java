package com.hcp.objective.bean;

public class ODataBean {
	private String url;
	private String authorizationType;
	private String authorization;
	private String proxyName;
	private int proxyPort;
	private String queryUser;
	private String queryPwd;

	
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

}
