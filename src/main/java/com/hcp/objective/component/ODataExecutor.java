package com.hcp.objective.component;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.stereotype.Component;

import com.hcp.objective.bean.ApplicationPropertyBean;
import com.hcp.objective.util.ODataConstants;
import com.sap.security.um.UMException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;

@Component
public class ODataExecutor {
	public static final Logger logger = LoggerFactory.getLogger(ODataExecutor.class);
	@Autowired
	public Environment env;

	@Autowired
	private ApplicationPropertyBean applicationPropertyBean;

	public ApplicationPropertyBean getInitializeBean() throws Exception {
		return applicationPropertyBean;
	}

	@SuppressWarnings("unused")
	private User getLoginUser(HttpServletRequest request) {
		InitialContext ctx;
		try {
			ctx = new InitialContext();
			UserProvider userProvider = (UserProvider) ctx.lookup("java:comp/env/user/Provider");
			User user = null;
			if (request.getUserPrincipal() != null) {
				user = userProvider.getUser(request.getUserPrincipal().getName());
			}
			return user;
		} catch (NamingException | UMException e) {
			logger.error(e.getMessage(), e);
			return null;
		}
	}

	public String createUri(String serviceUri, String entitySetName, String id, String expand, String queryString) {
		final StringBuilder absolutUri = new StringBuilder(serviceUri).append(ODataConstants.SEPARATOR)
				.append(entitySetName);
		if (id != null) {
			absolutUri.append("(").append(id).append(")");

		}
		if (expand != null && queryString != null) {
			absolutUri.append("?$expand=").append(expand).append("&").append(queryString);
		} else if (expand != null) {
			absolutUri.append("?$expand=").append(expand);
		} else if (queryString != null) {
			absolutUri.append("?").append(queryString);
		}
		return absolutUri.toString();

	}

	public ApplicationPropertyBean getOdataBean() {
		return applicationPropertyBean;
	}

	public void setOdataBean(ApplicationPropertyBean odataBean) {
		this.applicationPropertyBean = odataBean;
	}

	/**************************************************************************************************/
	// Add by Bruce 2016-05-26
	public String readData(/* HttpServletRequest request, */String entityName, String key, String query,
			String requestMethod) {
		String result = null;
		try {
			if (applicationPropertyBean == null)
				this.getInitializeBean();
			String charset = applicationPropertyBean.getCharset();
			String authorizationType = applicationPropertyBean.getAuthorizationType();
			String authorization = applicationPropertyBean.getAuthorization();
			// String contentType = odataBean.getContentType();

			String absolutUri = createAbsolutUri(entityName, key, query);
			HttpURLConnection conn = initializeConnection(absolutUri, requestMethod, authorizationType, authorization,
					null);

			InputStream in = conn.getInputStream();
			String encoding = conn.getContentEncoding();
			encoding = encoding == null ? charset : encoding;
			result = IOUtils.toString(in, encoding);
			logger.info(result);
			conn.disconnect();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public String postData(/* HttpServletRequest request, */ String entityName, String postData, String query,
			String requestMethod) {

		String result = null;
		try {
			if (applicationPropertyBean == null)
				this.getInitializeBean();
			String charset = applicationPropertyBean.getCharset();
			String authorizationType = applicationPropertyBean.getAuthorizationType();
			String authorization = applicationPropertyBean.getAuthorization();
			String contentType = applicationPropertyBean.getContentType();

			String absolutUri = createAbsolutUri(entityName, null, query);
			HttpURLConnection conn = initializeConnection(absolutUri, requestMethod, authorizationType, authorization,
					contentType);

			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(postData);
			writer.flush();
			writer.close();

			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {

			}
			InputStream in = conn.getInputStream();
			String encoding = conn.getContentEncoding();
			encoding = encoding == null ? charset : encoding;
			result = IOUtils.toString(in, encoding);
			logger.info(result);
			conn.disconnect();
			// rd.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	private String createAbsolutUri(String entityName, String key, String query) {
		String serviceUri = applicationPropertyBean.getUrl();
		final StringBuilder absolutUri = new StringBuilder(serviceUri).append(ODataConstants.SEPARATOR)
				.append(entityName);
		if (key != null) {
			absolutUri.append("(").append(key).append(")");
		}
		if (query != null) {
			absolutUri.append("?").append(query);
		}
		return absolutUri.toString();
	}

	private HttpURLConnection initializeConnection(String absolutUri, String httpMethod, String authorizationType,
			String authorization, String contentType) throws MalformedURLException, IOException {
		HttpURLConnection connection = null;

		if (applicationPropertyBean.isProxy()) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(applicationPropertyBean.getProxyName(),
					applicationPropertyBean.getProxyPort()));
			connection = (HttpURLConnection) new URL(absolutUri).openConnection(proxy);
		} else {
			connection = (HttpURLConnection) new URL(absolutUri).openConnection();
		}

		String authorizationHeader = authorizationType + " ";
		authorizationHeader += new String(Base64.encode((authorization).getBytes()));
		//authorizationHeader += new String(Base64Utils.encode(authorization.getBytes()));
		connection.setRequestProperty("Authorization", authorizationHeader);
		if (contentType != null)
			connection.setRequestProperty("content-type", "application/json");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestMethod(httpMethod);

		connection.setConnectTimeout(30000 * 2);

		return connection;
	}

}
