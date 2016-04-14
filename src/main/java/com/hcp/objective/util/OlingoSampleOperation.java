package com.hcp.objective.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.olingo.odata2.api.ODataCallback;
import org.apache.olingo.odata2.api.commons.HttpStatusCodes;
import org.apache.olingo.odata2.api.edm.Edm;
import org.apache.olingo.odata2.api.edm.EdmEntityContainer;
import org.apache.olingo.odata2.api.edm.EdmEntitySet;
import org.apache.olingo.odata2.api.edm.EdmException;
import org.apache.olingo.odata2.api.ep.EntityProvider;
import org.apache.olingo.odata2.api.ep.EntityProviderException;
import org.apache.olingo.odata2.api.ep.EntityProviderReadProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties;
import org.apache.olingo.odata2.api.ep.EntityProviderWriteProperties.ODataEntityProviderPropertiesBuilder;
import org.apache.olingo.odata2.api.ep.entry.EntryMetadata;
import org.apache.olingo.odata2.api.ep.entry.ODataEntry;
import org.apache.olingo.odata2.api.ep.feed.ODataFeed;
import org.apache.olingo.odata2.api.exception.ODataException;
import org.apache.olingo.odata2.api.processor.ODataResponse;
import org.apache.olingo.odata2.api.uri.ExpandSelectTreeNode;

public class OlingoSampleOperation {
	public static final String HTTP_METHOD_PUT = "PUT";
	public static final String HTTP_METHOD_POST = "POST";
	public static final String HTTP_METHOD_GET = "GET";
	private static final String HTTP_METHOD_DELETE = "DELETE";

	public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	public static final String HTTP_HEADER_ACCEPT = "Accept";

	public static final String APPLICATION_JSON = "application/json";
	public static final String APPLICATION_ATOM_XML = "application/atom+xml";
	public static final String APPLICATION_ATOM = "application/atom";
	public static final String APPLICATION_XML = "application/xml";

	public static final String FORMAT_JSON = "json";
	public static final String FORMAT_ATOM = "atom";

	public static final String METADATA = "$metadata";

	public static final String SEPARATOR = "/";

	public static final boolean PRINT_RAW_CONTENT = false;

	private String authorizationType;

	private String authorization;

	private String proxyName;

	private int proxyPort;
	
	/**
	 * 
	 * @param serviceUri: sample: https://sfapiqacand.sflab.ondemand.com/odata/v2
	 * @param authorizationType: Basic or Bear
	 * @param authorizatoin: username@company:pwd
	 * @return
	 * @throws IOException
	 * @throws ODataException
	 */
	public Edm readEdmAndNotValidate(String serviceUri,
			String authorizationType, String authorizatoin) throws IOException,
			ODataException {
		InputStream content = execute(serviceUri,
				APPLICATION_XML, HTTP_METHOD_GET, authorizationType,
				authorizatoin);
		//false of the second parameter indicate that no need for validation
		return EntityProvider.readMetadata(content, false);
	}

	/**
	 * 
	 * @param serviceUri: sample: https://sfapiqacand.sflab.ondemand.com/odata/v2
	 * @param authorizationType: Basic or Bear
	 * @param authorizatoin: username@company:pwd
	 * @return
	 * @throws IOException
	 * @throws ODataException
	 */
	public Edm readEdmAndValidate(String serviceUri, String authorizationType,
			String authorizatoin) throws IOException, ODataException {
		InputStream content = execute(serviceUri + SEPARATOR + METADATA,
				APPLICATION_XML, HTTP_METHOD_GET, authorizationType,
				authorizatoin);
		//true of the second parameter indicate that validation is need
		return EntityProvider.readMetadata(content, true);
	}

	/**
	 * 
	 * @param edm: metadata object
	 * @param serviceUri: sample: https://sfapiqacand.sflab.ondemand.com/odata/v2
	 * @param contentType: application/json or application/xml+atom
	 * @param entitySetName: entity name
	 * @param keyValue: key of entity, say 'admin' or userId='admin'
	 * @return
	 * @throws IOException
	 * @throws ODataException
	 */
	public ODataEntry readEntry(Edm edm, String serviceUri, String contentType,
			String entitySetName, String keyValue) throws IOException,
			ODataException {
		return readEntry(edm, serviceUri, contentType, entitySetName, keyValue,
				null);
	}

	/**
	 * 
	 * @param edm metadata object
	 * @param serviceUri: sample: https://sfapiqacand.sflab.ondemand.com/odata/v2
	 * @param contentType: application/json or application/xml+atom
	 * @param entitySetName: entity name
	 * @param keyValue: key of entity, say "'admin'" or "userId='admin'"
	 * @param expandRelationName, property names that need to be expanded, say "manager,proxy"
	 * @return
	 * @throws IOException
	 * @throws ODataException
	 */
	public ODataEntry readEntry(Edm edm, String serviceUri, String contentType,
			String entitySetName, String keyValue, String expandRelationName)
			throws IOException, ODataException {
		// get the default entity container
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		// create absolute uri based on service uri, entity set name with its
		// key property value and optional expanded relation name
		String absolutUri = createUri(serviceUri, entitySetName, keyValue,
				expandRelationName);

		System.out.println(absolutUri);

		InputStream content = execute(absolutUri, contentType, HTTP_METHOD_GET);

		return EntityProvider.readEntry(contentType,
				entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}

	/**
	 * There is no filter and query string of this method. The query is like https://.../odata/v2/User
     * @param edm metadata object
	 * @param serviceUri: sample: https://sfapiqacand.sflab.ondemand.com/odata/v2
	 * @param contentType: application/json or application/xml+atom
	 * @param entitySetName: entity name
	 * @return
	 * @throws IOException
	 * @throws ODataException
	 */
	public ODataFeed readFeed(Edm edm, String serviceUri, String contentType,
			String entitySetName) throws IOException, ODataException {
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		String absolutUri = createUri(serviceUri, entitySetName, null);

		System.out.println(absolutUri);

		InputStream content = execute(absolutUri, contentType, HTTP_METHOD_GET);
		return EntityProvider.readFeed(contentType,
				entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
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

		System.out.println(absolutUri);

		InputStream content = execute(absolutUri, contentType, HTTP_METHOD_GET);
		return EntityProvider.readFeed(contentType,
				entityContainer.getEntitySet(entitySetName), content,
				EntityProviderReadProperties.init().build());
	}

	/**
	 * 
	 * @param edm
	 * @param serviceUri
	 * @param entitySetName
	 * @param data: property name and value pairs 
	 * @param selectProperties: list of normal property names that will be included in request payload
	 * @param map: navigation property name and value pair
	 * @param selectLinks: list of navigation property names that will be included in request payload
	 * @param contentType: application/json or application/xml+atom
	 * @param authorizationType: Basic or Bear
	 * @param authorization: username@company:pwd
	 * @return
	 * @throws Exception
	 */
	public ODataEntry createEntry(Edm edm, String serviceUri,
			String entitySetName, Map<String, Object> data,
			List<String> selectProperties,
			Map<String, Map<String, Object>> map, List<String> selectLinks,
			String contentType, String authorizationType, String authorization)
			throws Exception {
		String absolutUri = createUri(serviceUri, entitySetName, null);
		return writeEntity(edm, absolutUri, entitySetName, data,
				selectProperties, map, selectLinks, contentType,
				HTTP_METHOD_POST, authorizationType, authorization, null);
	}

	/**
	 * 
	 * @param edm
	 * @param serviceUri
	 * @param contentType
	 * @param entitySetName
	 * @param id: id of the entry to be updated
	 * @param data
	 * @param selectProperties
	 * @param map
	 * @param selectLinks
	 * @param authorizationType
	 * @param authorization
	 * @throws Exception
	 */
	public void updateEntry(Edm edm, String serviceUri, String contentType,
			String entitySetName, String id, Map<String, Object> data,
			List<String> selectProperties,
			Map<String, Map<String, Object>> map, List<String> selectLinks,
			String authorizationType, String authorization) throws Exception {
		String absolutUri = createUri(serviceUri, entitySetName, id);
		writeEntity(edm, absolutUri, entitySetName, data, selectProperties,
				map, selectLinks, contentType, HTTP_METHOD_PUT,
				authorizationType, authorization, null);
	}

	/**
	 * 
	 * @param edm
	 * @param serviceUri
	 * @param contentType
	 * @param entitySetName
	 * @param id: id of the entry to be merged
	 * @param data
	 * @param selectProperties
	 * @param map
	 * @param selectLinks
	 * @param authorizationType
	 * @param authorization
	 * @throws Exception
	 */
	public void mergeEntry(Edm edm, String serviceUri, String contentType,
			String entitySetName, String id, Map<String, Object> data,
			List<String> selectProperties,
			Map<String, Map<String, Object>> map, List<String> selectLinks,
			String authorizationType, String authorization) throws Exception {
		String absolutUri = createUri(serviceUri, entitySetName, id);
		Map<String, String> mergeHeader = new HashMap<String, String>();
		mergeHeader.put("X-HTTP-Method", "MERGE");
		writeEntity(edm, absolutUri, entitySetName, data, selectProperties,
				map, selectLinks, contentType, HTTP_METHOD_POST,
				authorizationType, authorization, mergeHeader);
	}

	/**
	 * 
	 * @param serviceUri
	 * @param entityName
	 * @param id: id of the entry to be deleted
	 * @param authorizationType
	 * @param authorization
	 * @return
	 * @throws IOException
	 */
	public HttpStatusCodes deleteEntry(String serviceUri, String entityName,
			String id, String authorizationType, String authorization)
			throws IOException {
		String absolutUri = createUri(serviceUri, entityName, id);
		HttpURLConnection connection = connect(absolutUri, APPLICATION_XML,
				HTTP_METHOD_DELETE, authorizationType, authorization);
		return HttpStatusCodes.fromStatusCode(connection.getResponseCode());
	}

	/**
	 * 
	 * @param edm: edm of specific Odata API
	 * @param absolutUri
	 * @param entitySetName
	 * @param data: all of the key-value pairs, include inline property data
	 * @param selectProperties: list of property names of outer entity
	 * @param linkData: map of linked navigation property name and values
	 * @param selectLinks: list of linked navigation property names
	 * @param contentType
	 * @param httpMethod
	 * @param authorizationType
	 * @param authorization
	 * @param additionalParams: map of additional headers
	 * @return
	 * @throws EdmException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws EntityProviderException
	 * @throws URISyntaxException
	 */
	private ODataEntry writeEntity(Edm edm, String absoluteUri,
			String entitySetName, Map<String, Object> data,
			List<String> selectProperties,
			Map<String, Map<String, Object>> linkData,
			List<String> selectLinks, String contentType, String httpMethod,
			String authorizationType, String authorization,
			Map<String, String> additionalParams) throws EdmException,
			MalformedURLException, IOException, EntityProviderException,
			URISyntaxException {

		//initialize a HTTP connection
		HttpURLConnection connection = initializeConnection(absoluteUri,
				contentType, httpMethod, authorizationType, authorization);

		//add addtional headers for the connection if there is any
		if (additionalParams != null && additionalParams.size() > 0) {
			for (String key : additionalParams.keySet()) {
				connection.setRequestProperty(key, additionalParams.get(key));
			}
		}

		//get default entity container
		EdmEntityContainer entityContainer = edm.getDefaultEntityContainer();
		EdmEntitySet entitySet = entityContainer.getEntitySet(entitySetName);
		int lastIndex = absoluteUri.lastIndexOf("/");
		String serviceUri = absoluteUri.substring(0, lastIndex + 1);
		URI rootUri = new URI(serviceUri);

		//prepare properties
		ExpandSelectTreeNode expandSelectTree = null;
		EntityProviderWriteProperties properties = null;

		if (selectProperties == null && selectLinks == null) {
			properties = EntityProviderWriteProperties.serviceRoot(rootUri)
					.omitJsonWrapper(true).build();
		} else if (selectLinks == null) {
			expandSelectTree = ExpandSelectTreeNode.entitySet(entitySet)
					.selectedProperties(selectProperties).build();
			properties = EntityProviderWriteProperties.serviceRoot(rootUri)
					.omitJsonWrapper(true).expandSelectTree(expandSelectTree)
					.build();
		} else if (selectProperties == null) {
			expandSelectTree = ExpandSelectTreeNode.entitySet(entitySet)
					.selectedLinks(selectLinks).build();
			properties = EntityProviderWriteProperties.serviceRoot(rootUri)
					.additionalLinks(linkData).omitJsonWrapper(true)
					.expandSelectTree(expandSelectTree).build();
		} else {

			expandSelectTree = ExpandSelectTreeNode.entitySet(entitySet)
					.selectedProperties(selectProperties)
					.selectedLinks(selectLinks).build();
			properties = EntityProviderWriteProperties.serviceRoot(rootUri)
					.additionalLinks(linkData).omitJsonWrapper(true)
					.expandSelectTree(expandSelectTree).build();
		}
		// serialize data into ODataResponse object
		ODataResponse response = EntityProvider.writeEntry(contentType,
				entitySet, data, properties);
		// get (http) entity which is for default Olingo implementation an
		// InputStream
		Object entity = response.getEntity();
		if (entity instanceof InputStream) {
			byte[] buffer = streamToArray((InputStream) entity);
			// just for logging
			String content = new String(buffer);
			System.out.println(httpMethod + " request on uri '" + absoluteUri
					+ "' with content:\n  " + content + "\n");
			connection.getOutputStream().write(buffer);
		}

		// if a entity is created (via POST request) the response body contains
		// the new created entity
		ODataEntry entry = null;
		HttpStatusCodes statusCode = HttpStatusCodes.fromStatusCode(connection
				.getResponseCode());
		if (statusCode == HttpStatusCodes.CREATED) {
			// get the content as InputStream and de-serialize it into an
			// ODataEntry object
			InputStream content = connection.getInputStream();
			content = logRawContent(httpMethod + " request on uri '"
					+ absoluteUri + "' with content:\n  ", content, "\n");
			entry = EntityProvider.readEntry(contentType, entitySet, content,
					EntityProviderReadProperties.init().build());
		}

		connection.disconnect();

		return entry;
	}

	private String createUri(String serviceUri, String entitySetName, String id) {
		return createUri(serviceUri, entitySetName, id, null);
	}

	private String createUri(String serviceUri, String entitySetName,
			String id, String expand) {
		final StringBuilder absolutUri = new StringBuilder(serviceUri).append(
				SEPARATOR).append(entitySetName);
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
		final StringBuilder absolutUri = new StringBuilder(serviceUri).append(
				SEPARATOR).append(entitySetName);
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

	/**
	 * This method is to initialize the connection with request url, method,
	 * content type and authorization.
	 * 
	 * @param absolutUri
	 * @param contentType
	 * @param httpMethod
	 * @param authorizationType
	 * @param authorization
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	private HttpURLConnection initializeConnection(String absolutUri,
			String contentType, String httpMethod, String authorizationType,
			String authorization) throws MalformedURLException, IOException {

		URL url = new URL(absolutUri);
		HttpURLConnection connection;
		if (getProxyName() != null && getProxyName().length() > 0
				&& getProxyPort() > 0) {
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
					getProxyName(), getProxyPort()));
			connection = (HttpURLConnection) url.openConnection(proxy);
		} else {
			connection = (HttpURLConnection) url.openConnection();
		}

		String authorizationHeader = authorizationType + " ";
		if (authorizationType == "Basic") {
			authorizationHeader += new String(Base64.encodeBase64((authorization)
				.getBytes()));
		} else {
			authorizationHeader += authorization;
		}
		
		connection.setRequestProperty("Authorization", authorizationHeader);

		connection.setRequestMethod(httpMethod);
		connection.setRequestProperty(HTTP_HEADER_ACCEPT, contentType);

		if (HTTP_METHOD_POST.equals(httpMethod)
				|| HTTP_METHOD_PUT.equals(httpMethod)) {
			connection.setDoOutput(true);
			connection
					.setRequestProperty(HTTP_HEADER_CONTENT_TYPE, contentType);
		}

		return connection;
	}

	private HttpURLConnection connect(String relativeUri, String contentType,
			String httpMethod, String authorizationType, String authorization)
			throws IOException {
		HttpURLConnection connection = initializeConnection(relativeUri,
				contentType, httpMethod, authorizationType, authorization);
		connection.connect();
		checkStatus(connection);
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

	private InputStream execute(String relativeUri, String contentType,
			String httpMethod) throws IOException {
		String authorizationType = this.getAuthorizationType();
		String authorization = this.getAuthorization();
		return execute(relativeUri, contentType, httpMethod, authorizationType,
				authorization);
	}

	private InputStream execute(String relativeUri, String contentType,
			String httpMethod, String authorizationType, String authorization)
			throws IOException {
		HttpURLConnection connection = initializeConnection(relativeUri,
				contentType, httpMethod, authorizationType, authorization);
		connection.connect();
		checkStatus(connection);

		InputStream content = connection.getInputStream();

		content = logRawContent(httpMethod + " request on uri '" + relativeUri
				+ "' with content:\n", content, "\n");
		return content;
	}

	private InputStream logRawContent(String prefix, InputStream content,
			String postfix) throws IOException {
		if (PRINT_RAW_CONTENT) {
			byte[] buffer = streamToArray(content);
			System.out.println(prefix + new String(buffer) + postfix);
			return new ByteArrayInputStream(buffer);
		}
		return content;
	}

	public static byte[] streamToArray(InputStream stream) throws IOException {
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
		stream.close();
		return result;
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

}
