package com.hcp.objective.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.hcp.objective.bean.ODataBean;
import com.sap.security.um.UMException;
import com.sap.security.um.user.User;
import com.sap.security.um.user.UserProvider;
@Component
public class ODataExecutor {
	public static final Logger logger = LoggerFactory.getLogger(ODataExecutor.class);
	@Autowired
	public Environment env;
	private ODataBean odataBean = null;
	public ODataBean getInitializeBean(HttpServletRequest request)throws Exception{
		if(odataBean==null){
			odataBean = new ODataBean();
			InitialContext ctx;
			String userName="";
			String sfUserName=null;
			String sfPassword=null;
			try {
			    ctx = new InitialContext();
			    UserProvider userProvider = (UserProvider) ctx.lookup("java:comp/env/user/Provider");
			    User user = null;
			    if (request.getUserPrincipal() != null) {
				  user = userProvider.getUser(request.getUserPrincipal().getName());
				  userName = user.getName().toLowerCase();
			    }
			} catch (NamingException | UMException e) {
			    logger.error(e.getMessage(),e);
			    throw e;
			}
			sfUserName=env.getProperty("service.username."+userName);
			sfPassword=env.getProperty("service.password."+userName);
			if(StringUtils.isEmpty(sfUserName)){
				sfUserName = env.getProperty("service.username.default");
				sfPassword = env.getProperty("service.password.default");
			}
			odataBean.setAuthorization(sfUserName+":"+sfPassword);
			odataBean.setAuthorizationType(env.getProperty("service.authorizationType"));
			odataBean.setProxyName(env.getProperty("service.proxy.hostname"));
			odataBean.setProxyPort(Integer.parseInt(env.getProperty("service.proxy.port")));
			odataBean.setUrl(env.getProperty("service.url"));
			odataBean.setQueryUser(sfUserName);
			odataBean.setQueryPwd(sfPassword);
		}
		return odataBean;
	}

	public Edm readEdmAndNotValidate(String serviceUri,
			String authorizationType, String authorizatoin) throws IOException,
			ODataException {
		InputStream content = execute(serviceUri,
				ODataConstants.APPLICATION_ATOM_XML, ODataConstants.HTTP_METHOD_GET, authorizationType,
				authorizatoin);
		 Edm edm = EntityProvider.readMetadata(content, false);
		 return edm;
	}
	/**
	 * 
	 * There is no filter and query string of this method. The query is like https://.../odata/v2/User
     * @param edm metadata object
	 * @param serviceUri: sample: https://sfapiqacand.sflab.ondemand.com/odata/v2
	 * @param contentType: application/json or application/xml+atom
	 * @param entitySetName: entity name
	 * @param expand: expand string, say "manager, proxy"
	 * @param queryString: say "$select=username,status&$filter=status eq 't'"
	 * @return
	 * @throws IOException
	 * @throws ODataException
	 */
	public ODataFeed readFeed(Edm edm, String serviceUri, String contentType,
			String entitySetName, String expand, String queryString)
			throws IOException, ODataException {
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		String absolutUri = createUri(serviceUri, entitySetName, null, expand,
				queryString);
		InputStream content = execute(absolutUri, contentType, ODataConstants.HTTP_METHOD_GET);
		return EntityProvider.readFeed(contentType,
				entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}
	
	public ODataFeed readFeed(Edm edm, String serviceUri, String contentType,
			String entitySetName, String id,String expand, String queryString)
			throws IOException, ODataException {
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		String absolutUri = createUri(serviceUri, entitySetName, id, expand,
				queryString);
		InputStream content = execute(absolutUri, contentType, ODataConstants.HTTP_METHOD_GET);
		return EntityProvider.readFeed(contentType,
				entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}
	
	public ODataEntry readEntry(Edm edm, String serviceUri, String contentType, String entitySetName, String keyValue)
		      throws IOException, ODataException {
		    // working with the default entity container
		    EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		    // create absolute uri based on service uri, entity set name and key property value
		    String absolutUri = createUri(serviceUri, entitySetName, keyValue);

		    InputStream content = execute(absolutUri, contentType, ODataConstants.HTTP_METHOD_GET);

		    return EntityProvider.readEntry(contentType,
		        entityContainer.getEntitySet(entitySetName),
		        content,
		        EntityProviderReadProperties.init().build());
	}
	
	public ODataEntry readEntry(Edm edm, String serviceUri, String contentType, 
		      String entitySetName, String keyValue, String expandRelationName)
		      throws IOException, ODataException {
		    // working with the default entity container
		    EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		    // create absolute uri based on service uri, entity set name with its key property value and optional expanded relation name
		    String absolutUri = createUri(serviceUri, entitySetName, keyValue, expandRelationName);

		    InputStream content = execute(absolutUri, contentType, ODataConstants.HTTP_METHOD_GET);

		    return EntityProvider.readEntry(contentType,
		        entityContainer.getEntitySet(entitySetName),
		        content,
		        EntityProviderReadProperties.init().build());
	}
	
	private InputStream execute(String relativeUri, String contentType,
			String httpMethod, String authorizationType, String authorization)
			throws IOException {
		Boolean isProxy=new Boolean(env.getProperty("service.isProxy"));
		HttpURLConnection connection = null;
		if(isProxy){
			 connection = initializeConnectionWithProxy(relativeUri,
						contentType, httpMethod, authorizationType, authorization);
		}else{
			 connection = initializeConnectionWithoutProxy(relativeUri,
						contentType, httpMethod, authorizationType, authorization);
		}
		connection.connect();
		checkStatus(connection);
		InputStream content = connection.getInputStream();
		return content;
	}
	
	private InputStream execute(String relativeUri, String contentType,
			String httpMethod) throws IOException {
		String authorizationType = odataBean.getAuthorizationType();
		String authorization = odataBean.getAuthorization();
		return execute(relativeUri, contentType, httpMethod, authorizationType,
				authorization);
	}
	
	private HttpURLConnection initializeConnectionWithProxy(String absolutUri,
			String contentType, String httpMethod, String authorizationType,
			String authorization) throws MalformedURLException, IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
				"proxy.sha.sap.corp", 8080));
		Properties systemProperties = System.getProperties();
		systemProperties.setProperty("http.proxyHost","proxy.sha.sap.corp");
		systemProperties.setProperty("http.proxyPort","8080");
		String authorizationHeader = authorizationType + " ";
		authorizationHeader += new String(Base64.encodeBase64((authorization).getBytes()));
		//only for local test
		HttpURLConnection connection = (HttpURLConnection) new URL(absolutUri).openConnection(proxy);
		 connection.setRequestProperty("Authorization", authorizationHeader);
		 connection.setRequestProperty("ContentType","text/xml;charset=utf-8");
		 connection.setRequestMethod(httpMethod);
		 
		 return connection;
    }
	
	private HttpURLConnection initializeConnectionWithoutProxy(String absolutUri,
			String contentType, String httpMethod, String authorizationType,
			String authorization) throws MalformedURLException, IOException {
		String authorizationHeader = authorizationType + " ";
		authorizationHeader += new String(Base64.encodeBase64((authorization).getBytes()));
		 HttpURLConnection connection = (HttpURLConnection) new URL(absolutUri).openConnection();
		 connection.setRequestProperty("Authorization", authorizationHeader);
		 connection.setRequestProperty("ContentType","text/xml;charset=utf-8");
		 connection.setRequestMethod(httpMethod);
		 
		 return connection;
    }
	
	private HttpStatusCodes checkStatus(HttpURLConnection connection)
			throws IOException {
		HttpStatusCodes httpStatusCode = HttpStatusCodes
				.fromStatusCode(connection.getResponseCode());
		if (400 <= httpStatusCode.getStatusCode()
				&& httpStatusCode.getStatusCode() <= 599) {
			throw new RuntimeException("Http Connection failed with status "
					+ httpStatusCode.getStatusCode() + " "
					+ httpStatusCode.toString());
		}
		return httpStatusCode;
	}
	
	private String createUri(String serviceUri, String entitySetName, String id) {
		return createUri(serviceUri, entitySetName, id, null);
	}

	private String createUri(String serviceUri, String entitySetName,
			String id, String expand) {
		final StringBuilder absolutUri = new StringBuilder(serviceUri).append(
				ODataConstants.SEPARATOR).append(entitySetName);
		if (id != null) {
			absolutUri.append("(").append(id).append(")");
			if (expand != null) {
				absolutUri.append("?$expand=").append(expand);
			}
			return absolutUri.toString();
		} else {
			if (expand != null) {
				absolutUri.append("?$expand=").append(expand);
			}
			return absolutUri.toString();
		}
	}

	private String createUri(String serviceUri, String entitySetName,
			String id, String expand, String queryString) {
		final StringBuilder absolutUri = new StringBuilder(serviceUri).append(ODataConstants.SEPARATOR).append(entitySetName);
		if (id != null) {
			absolutUri.append("(").append(id).append(")");
			if (expand != null) {
				absolutUri.append("?$expand=").append(expand);
			}
			return absolutUri.toString();
		} else {
			if (expand != null && queryString != null) {
				absolutUri.append("?$expand=").append(expand).append("&")
						.append(queryString);
			} else if (expand != null) {
				absolutUri.append("?$expand=").append(expand);
			} else if (queryString != null) {
				absolutUri.append("?").append(queryString);
			}
			return absolutUri.toString();
		}
	}
	
	public ODataBean getOdataBean() {
		return odataBean;
	}

	public void setOdataBean(ODataBean odataBean) {
		this.odataBean = odataBean;
	}
}
