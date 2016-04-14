package com.hcp.objective.util;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.olingo.client.api.ODataClient;
import org.apache.olingo.client.api.communication.request.retrieve.ODataEntitySetIteratorRequest;
import org.apache.olingo.client.api.communication.response.ODataRetrieveResponse;
import org.apache.olingo.client.api.domain.ClientEntity;
import org.apache.olingo.client.api.domain.ClientEntitySet;
import org.apache.olingo.client.api.domain.ClientEntitySetIterator;
import org.apache.olingo.client.api.http.HttpClientFactory;
import org.apache.olingo.client.api.uri.URIBuilder;
import org.apache.olingo.client.core.ODataClientFactory;
import org.apache.olingo.client.core.http.DefaultHttpClientFactory;
import org.apache.olingo.client.core.http.ProxyWrappingHttpClientFactory;
import org.apache.olingo.commons.api.format.ContentType;
import org.apache.olingo.commons.api.http.HttpStatusCode;

public class ODataTaskExecutor {
	 private static final String HTTP = "http://";
	 private static final String HTTPS = "https://";
	 private static final String HTTP_PROXY_HOST = "http.proxyHost";
	 private static final String HTTP_PROXY_PORT = "http.proxyPort";
	 private static final String HTTPS_PROXY_HOST = "https.proxyHost";
	 private static final String HTTPS_PROXY_PORT = "https.proxyPort";
	 private Map<String, Object> outputMap;
	 private String proxy;
	 public void execute(String url) {

		    ODataClient client = ODataClientFactory.getClient();
		    HttpClientFactory httpClientFactory;

		    if (StringUtils.isBlank(proxy)) {
		      String proxyHost = "proxy.sha.sap.corp";
		      int proxyPort = 0;

		      if (url.startsWith(HTTP)) {
		        proxyHost = System.getProperty(HTTP_PROXY_HOST);

		        if (!StringUtils.isBlank(proxyHost)) {
		          proxyPort = 8080;
		        }

		      } else {
		        if (!StringUtils.isBlank(proxyHost)) {
		          proxyPort = 8080;
		        }
		      }

		      if (!StringUtils.isBlank(proxyHost)) {
		        proxy = proxyHost + ":" + proxyPort;
		      }
		    }

		    if (!StringUtils.isBlank(proxy)) {
		      if (!proxy.startsWith(HTTP) && !proxy.startsWith(HTTPS)) {
		        proxy = HTTP + proxy;
		      }
		      URI proxyUri = client.newURIBuilder(proxy).build();
		      httpClientFactory = new ProxyWrappingHttpClientFactory(proxyUri,"cgrant@PLTzcompany","pwd");
		    } else {
		      httpClientFactory = new DefaultHttpClientFactory();
		    }

		    client.getConfiguration().setHttpClientFactory(httpClientFactory);
		        /*new ODataOAuth2HttpClientFactory(httpClientFactory,
		            oauth2GrantServiceUri, oauth2TokenServiceUri, clientId,
		            clientSecret, userId, companyId, userType, resourceType)*/

		    //if (HttpMethod.GET.name().equals(method)) {
		      readEntitySet(client,url);

		    /*} else if (HttpMethod.POST.name().equals(method)) {
		      createEntity(client, null);

		    } else {
		      logger.error("HTTP method " + method + " is not supported");
		    }*/
		  }

	 private void readEntitySet(ODataClient client,String url) {
		    URIBuilder uriBuilder = client.newURIBuilder(url);
		    URI serviceUri = uriBuilder.build();

		    //logger.debug("Start to read entity set from " + url);

		    ODataEntitySetIteratorRequest<ClientEntitySet, ClientEntity> entitySetIteratorRequest = client
		        .getRetrieveRequestFactory().getEntitySetIteratorRequest(serviceUri);

		    entitySetIteratorRequest.setFormat(ContentType.APPLICATION_JSON);

		    ODataRetrieveResponse<ClientEntitySetIterator<ClientEntitySet, ClientEntity>> retrieveResponse = entitySetIteratorRequest
		        .execute();

		    if (retrieveResponse.getStatusCode() == HttpStatusCode.OK.getStatusCode()) {
		     // logger.info("Read entity set successfully from " + url);

		      ClientEntitySetIterator<ClientEntitySet, ClientEntity> entitySetIterator = retrieveResponse.getBody();
		      outputMap = new LinkedHashMap<String, Object>();
		      boolean loadingAll = outputMap.isEmpty();
		      int index = 0;

		      while (entitySetIterator.hasNext()) {
		        ClientEntity entity = entitySetIterator.next();
		        String entityKey = url + "#" + index++;

		        if (loadingAll || outputMap.containsKey(entityKey)) {
		          outputMap.put(entityKey, entity);
		        }
		      }

		    } else {
		     // logger.error("Failed to read entity set", retrieveResponse);
		    }
		  }
}
