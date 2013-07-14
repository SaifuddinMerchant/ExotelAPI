package com.sam.exotel;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Java Template class to call any Exotel API
 * @author Saifuddin Merchant (Sam)
 *
 */
@SuppressWarnings("deprecation")
public class ExotelJavaApiTemplate {

	public static Logger sLogger = LoggerFactory.getLogger(ExotelJavaApiTemplate.class);
	/**
	 * Template method to call any Exotel API
	 * @param pExotelUserSID - The exotel user id
	 * @param pExotelToken - The exotel token
	 * @param pPostURL - The API URL to be called
	 * @param pParameters - The parameters to be passed to the API URL
	 * @return
	 * @throws Exception
	 */
	public String callAPI(String pExotelUserSID, String pExotelToken, String pPostURL, List<NameValuePair> pParameters)
			throws Exception {

		SSLContext sslContext = SSLContext.getInstance("SSL");

		// set up a TrustManager that trusts everything
		sslContext.init(null, new TrustManager[] { new X509TrustManager() {
			public X509Certificate[] getAcceptedIssuers() {
				
				sLogger.debug("getAcceptedIssuers =============");
				return null;
			}

			public void checkClientTrusted(X509Certificate[] certs, String authType) {
				sLogger.debug("checkClientTrusted =============");
			}

			public void checkServerTrusted(X509Certificate[] certs, String authType) {
				sLogger.debug("checkServerTrusted =============");
			}

			@SuppressWarnings("unused")
			public boolean isClientTrusted(X509Certificate[] arg0) {
				return false;
			}

			@SuppressWarnings("unused")
			public boolean isServerTrusted(X509Certificate[] arg0) {
				return false;
			}
		} }, new SecureRandom());

		SSLSocketFactory sf = new SSLSocketFactory(sslContext, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
		Scheme httpsScheme = new Scheme("https", sf, 443);
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(httpsScheme);

		HttpParams params = new BasicHttpParams();
		ClientConnectionManager cm = new SingleClientConnManager(params, schemeRegistry);

		DefaultHttpClient client = new DefaultHttpClient(cm, params);

		// Replace "Exotel SID" and "Exotel Token" with your SID and Token
		client.getCredentialsProvider().setCredentials(new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
				new UsernamePasswordCredentials(pExotelUserSID, pExotelToken));
		
		pPostURL = StringUtils.replace(pPostURL,"{sid}", pExotelUserSID);
		pPostURL = StringUtils.replace(pPostURL,"{token}", pExotelToken);
		
		sLogger.info("Request URL : {}", pPostURL);
		sLogger.info("Request Parameters : {}", pParameters);
		
		HttpPost post = new HttpPost(pPostURL);
		post.setEntity(new UrlEncodedFormEntity(pParameters));

		ResponseHandler<String> responseHandler = new BasicResponseHandler();
		String response = client.execute(post, responseHandler);

		sLogger.info("Response : {}", response);
		return response;
	}

}
