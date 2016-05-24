package com.hcp.objective.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
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

	@Autowired  
	private  HttpServletRequest request;
	
	public ODataBean getInitializeBean(HttpServletRequest request) throws Exception {
		odataBean = new ODataBean();
		String sfUserName = null;
		String sfPassword = null;
			
		sfUserName = env.getProperty("service.username.default");
		sfPassword = env.getProperty("service.password.default");
		odataBean.setAuthorization(sfUserName + ":" + sfPassword);
		odataBean.setAuthorizationType(env.getProperty("service.authorizationType"));
		odataBean.setProxyName(env.getProperty("service.proxy.hostname"));
		odataBean.setProxyPort(Integer.parseInt(env.getProperty("service.proxy.port")));
		odataBean.setUrl(env.getProperty("service.url"));
		odataBean.setQueryUser(sfUserName);
		odataBean.setQueryPwd(sfPassword);
		
		return odataBean;
	}

	private User getLoginUser(){
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
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	public Edm readEdmAndNotValidate(String serviceUri, String authorizationType, String authorizatoin)
			throws IOException, ODataException {
		InputStream content = execute(serviceUri, ODataConstants.APPLICATION_ATOM_XML, ODataConstants.HTTP_METHOD_GET,
				authorizationType, authorizatoin);
		Edm edm = EntityProvider.readMetadata(content, false);
		return edm;
	}

	/**
	 * 
	 * There is no filter and query string of this method. The query is like
	 * https://.../odata/v2/User
	 * 
	 * @param edm
	 *            metadata object
	 * @param serviceUri:
	 *            sample: https://sfapiqacand.sflab.ondemand.com/odata/v2
	 * @param contentType:
	 *            application/json or application/xml+atom
	 * @param entitySetName:
	 *            entity name
	 * @param expand:
	 *            expand string, say "manager, proxy"
	 * @param queryString:
	 *            say "$select=username,status&$filter=status eq 't'"
	 * @return
	 * @throws IOException
	 * @throws ODataException
	 */
	public ODataFeed readFeed(Edm edm, String serviceUri, String contentType, String entitySetName, String expand,
			String queryString) throws IOException, ODataException {
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		String absolutUri = createUri(serviceUri, entitySetName, null, expand, queryString);
		InputStream content = execute(absolutUri, contentType, ODataConstants.HTTP_METHOD_GET);
		return EntityProvider.readFeed(contentType, entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}

	public ODataFeed readFeed(Edm edm, String serviceUri, String contentType, String entitySetName, String id,
			String expand, String queryString) throws IOException, ODataException {
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		String absolutUri = createUri(serviceUri, entitySetName, id, expand, queryString);
		InputStream content = execute(absolutUri, contentType, ODataConstants.HTTP_METHOD_GET);
		return EntityProvider.readFeed(contentType, entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}

	public ODataEntry readEntry(Edm edm, String serviceUri, String contentType, String entitySetName, String keyValue)
			throws IOException, ODataException {
		// working with the default entity container
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		// create absolute uri based on service uri, entity set name and key
		// property value
		String absolutUri = createUri(serviceUri, entitySetName, keyValue);

		InputStream content = execute(absolutUri, contentType, ODataConstants.HTTP_METHOD_GET);

		return EntityProvider.readEntry(contentType, entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}

	public ODataEntry readEntry(Edm edm, String serviceUri, String contentType, String entitySetName, String keyValue,
			String expandRelationName, String queryString) throws IOException, ODataException {
		// working with the default entity container
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		// create absolute uri based on service uri, entity set name with its
		// key property value and optional expanded relation name
		String absolutUri = createUri(serviceUri, entitySetName, keyValue, expandRelationName, queryString);

		InputStream content = execute(absolutUri, contentType, ODataConstants.HTTP_METHOD_GET);

		return EntityProvider.readEntry(contentType, entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}

	private InputStream execute(String relativeUri, String contentType, String httpMethod, String authorizationType,
			String authorization) throws IOException {
		Boolean isProxy = new Boolean(env.getProperty("service.isProxy"));
		HttpURLConnection connection = null;
		if (isProxy) {
			connection = initializeConnectionWithProxy(relativeUri, contentType, httpMethod, authorizationType,
					authorization);
		} else {
			connection = initializeConnectionWithoutProxy(relativeUri, contentType, httpMethod, authorizationType,
					authorization);
		}
		connection.connect();
		checkStatus(connection);
		InputStream content = connection.getInputStream();
		return content;
	}

	private InputStream execute(String relativeUri, String contentType, String httpMethod) throws IOException {
		String authorizationType = odataBean.getAuthorizationType();
		String authorization = odataBean.getAuthorization();
		return execute(relativeUri, contentType, httpMethod, authorizationType, authorization);
	}

	private HttpURLConnection initializeConnectionWithProxy(String absolutUri, String contentType, String httpMethod,
			String authorizationType, String authorization) throws MalformedURLException, IOException {
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("proxy.sha.sap.corp", 8080));
		Properties systemProperties = System.getProperties();
		systemProperties.setProperty("http.proxyHost", "proxy.sha.sap.corp");
		systemProperties.setProperty("http.proxyPort", "8080");
		String authorizationHeader = authorizationType + " ";
		authorizationHeader += new String(Base64.encodeBase64((authorization).getBytes()));
		// only for local test
		HttpURLConnection connection = (HttpURLConnection) new URL(absolutUri).openConnection(proxy);
		connection.setRequestProperty("Authorization", authorizationHeader);
		connection.setRequestProperty("ContentType", "text/xml;charset=utf-8");
		connection.setRequestMethod(httpMethod);

		return connection;
	}

	public HttpURLConnection initializeConnectionWithoutProxy(String absolutUri, String contentType, String httpMethod,
			String authorizationType, String authorization) throws MalformedURLException, IOException {
		String authorizationHeader = authorizationType + " ";
		authorizationHeader += new String(Base64.encodeBase64((authorization).getBytes()));
		HttpURLConnection connection = (HttpURLConnection) new URL(absolutUri).openConnection();
		connection.setRequestProperty("Authorization", authorizationHeader);
		connection.setRequestProperty("ContentType", "text/xml;charset=utf-8");
		connection.setRequestMethod(httpMethod);

		return connection;
	}

	private HttpStatusCodes checkStatus(HttpURLConnection connection) throws IOException {
		HttpStatusCodes httpStatusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
		if (400 <= httpStatusCode.getStatusCode() && httpStatusCode.getStatusCode() <= 599) {
			throw new RuntimeException("Http Connection failed with status " + httpStatusCode.getStatusCode() + " "
					+ httpStatusCode.toString());
		}
		return httpStatusCode;
	}

	private String createUri(String serviceUri, String entitySetName, String id) {
		return createUri(serviceUri, entitySetName, id, null);
	}

	private String createUri(String serviceUri, String entitySetName, String id, String expand) {
		final StringBuilder absolutUri = new StringBuilder(serviceUri).append(ODataConstants.SEPARATOR)
				.append(entitySetName);
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

	public ODataBean getOdataBean() {
		return odataBean;
	}

	public void setOdataBean(ODataBean odataBean) {
		this.odataBean = odataBean;
	}
	
	/*public ODataEntry createEntry(Edm edm, String serviceUri, String contentType, String entitySetName,
			Map<String, Object> data, String authorizationType, String authorization) throws Exception {
		String absolutUri = createUri(serviceUri, entitySetName, null);
		return writeEntity(edm, absolutUri, entitySetName, data, contentType, ODataConstants.HTTP_METHOD_POST, 
				authorizationType,authorization);
	}

	private ODataEntry writeEntity(Edm edm, String absolutUri, String entitySetName, Map<String, Object> data,
			String contentType, String httpMethod, String authorizationType, String authorization)
			throws EdmException, MalformedURLException, IOException, EntityProviderException, URISyntaxException {

		HttpURLConnection connection = initializeConnectionWithoutProxy(absolutUri, contentType, httpMethod,
				authorizationType, authorization);
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setRequestProperty(ODataConstants.HTTP_HEADER_ACCEPT, contentType);
		connection.setRequestProperty(ODataConstants.HTTP_HEADER_CONTENT_TYPE, contentType);
		connection.setRequestMethod("POST");
		connection.setUseCaches(false);
		connection.setRequestProperty("Connection", "Keep-Alive");
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		EdmEntitySet entitySet = entityContainer.getEntitySet(entitySetName);
		//EntityProviderReadProperties properties = EntityProviderReadProperties.init().mergeSemantic(false).build();
		//ODataEntry entry = EntityProvider.readEntry(requestContentType, uriInfo.getStartEntitySet(), content, properties);
		
		//URI rootUri = new URI(entitySetName);
		//UriInfo uriInfo = new UriInfoImpl();
		URI rootUri = new URI("http://localhost:8181/odata/v2");
       data.put("key", "id");
       //Map<String, Map<String, Object>> linkdata = new HashMap();
       //linkdata.put("d", null);
		EntityProviderWriteProperties properties = EntityProviderWriteProperties.serviceRoot(rootUri).includeSimplePropertyType(true).contentOnly(true).omitJsonWrapper(true).omitETag(false).build();
        List<Map<String, Object>> dataList =  new ArrayList();
         dataList.add(data);
		ODataResponse response = EntityProvider.writeEntry(contentType, entitySet, data, properties);
		//ODataResponse response = EntityProvider.writeFeed(contentType, entitySet, dataList, properties);
		// get (http) entity which is for default Olingo implementation an
		// InputStream
		Object entity = response.getEntity();
		if (entity instanceof InputStream) {
			byte[] buffer = streamToArray((InputStream) entity);
			// just for logging
			String content = new String(buffer);
			connection.getOutputStream().write(buffer);
		}

		// if a entity is created (via POST request) the response body contains
		// the new created entity
		ODataEntry entry = null;
		HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection.getResponseCode());
		if (statusCode == HttpStatusCodes.CREATED) {
			InputStream content = connection.getInputStream();
			content = logRawContent(httpMethod + " response:\n  ", content, "\n");
			entry = EntityProvider.readEntry(contentType, entitySet, content,
					EntityProviderReadProperties.init().build());
		}
		connection.disconnect();

		return entry;
	}

	private InputStream logRawContent(String prefix, InputStream content, String postfix) throws IOException {
		if (true) {
			byte[] buffer = streamToArray(content);
			content.close();

			// print(prefix + new String(buffer) + postfix);

			return new ByteArrayInputStream(buffer);
		}
		return content;
	}

	private byte[] streamToArray(InputStream stream) throws IOException {
		byte[] result = new byte[0];
		byte[] tmp = new byte[8192];
		int readCount = stream.read(tmp);
		while (readCount >= 0) {
			byte[] innerTmp = new byte[result.length + readCount];
			System.arraycopy(result, 0, innerTmp, 0, result.length);
			System.arraycopy(tmp, 0, innerTmp, result.length, readCount);
			result = innerTmp;
			readCount = stream.read(tmp);
		}
		return result;
	}*/

}
