package com.msi.upload.service.dctm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Splitter;
import com.google.gson.JsonArray;
import com.msi.upload.config.ConfigProperties;

@Service
public class DctmUploadService {
	
	@Autowired
	ConfigProperties configProperties;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	public String getFullFileSystemPath(String objectId) {
		Properties dctmConfigProp = configProperties.getConfigProperties("dctm-rest_config");
		String fullFileSystemPath = null;
		Query q = entityManager.createNativeQuery("select all c.data_ticket, f.dos_extension, l.file_system_path, "
		+ "dc.r_docbase_id from dmr_content_sp  c, dm_format_sp  f, dm_filestore_sp  fs, dm_location_sp  l, "
		+ "dm_docbase_config_sp  dc where (c.r_object_id in (select r_object_id from dmr_content_r "
		+ "where parent_id=N'"+objectId+"') and (f.r_object_id=c.format) and (fs.r_object_id=c.storage_id) "
		+ "and (l.object_name=fs.root)) and (l.i_has_folder = 1 and l.i_is_deleted = 0) and "
		+ "(dc.i_has_folder = 1 and dc.i_is_deleted = 0)");
		List<Object[]> list = q.getResultList();
		if (list.size() > 0) {
			JSONArray jsArray = new JSONArray(list).getJSONArray(0);
			if (jsArray != null && jsArray.length() > 0) {
				int dataTicket = jsArray.getInt(0);
				String dosExtension = jsArray.getString(1);
				String fileSystemPath = jsArray.getString(2);
//				int docbaseId = jsArray.getInt(3);
//				String dbId = new String().valueOf(docbaseId);
//				while (dbId.length() < 8) {
//					dbId = "0"+dbId;
//				}
//				StringBuilder sb = new StringBuilder(fileSystemPath+"\\"+dbId);
				String dbId = dctmConfigProp.getProperty("specific_docbase_id");
				StringBuilder sb = new StringBuilder(fileSystemPath+"\\"+dbId);
				double dataTicketTemp = dataTicket + Math.pow(2.00, 32.00);
				String hexStr = Long.toHexString((long)dataTicketTemp);
				Iterable<String> hexStrArr = Splitter.fixedLength(2).split(hexStr);
				List<String> result = new ArrayList<String>();
				hexStrArr.forEach(result::add);
				if (result.size() > 0) {
					int count = 1;
					for(int i=0; i<result.size(); i++) {
						if (count < result.size()) {
							sb.append("\\"+result.get(i));
						} else {
							sb.append(result.get(i));
						}
					}
					sb.append("."+dosExtension);
					fullFileSystemPath = sb.toString();
				}
			}
		}
		return fullFileSystemPath.toString();
	}
	
	public JSONObject makeRequest(String url, String user, String pass) {
		JSONObject json = null;
		String strResponse = "";
		byte[] decodedBytes = Base64.getDecoder().decode(pass);
		String decodedPass = new String(decodedBytes);
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		BufferedReader rd = null;
		CloseableHttpResponse cls = null;
		HttpGet request = new HttpGet(url);
		Properties prop = configProperties.getConfigProperties("dctm-rest_config");
	    int socketTimeout = Integer.parseInt(prop.getProperty("socket_timeout"));
	    int connectTimeout = Integer.parseInt(prop.getProperty("connect_timeout"));
	    int connectionRequestTimeout = Integer.parseInt(prop.getProperty("connection_request_timeout"));
		RequestConfig config = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).
				setConnectionRequestTimeout(connectionRequestTimeout).build();
		request.setConfig(config);
		request.addHeader("Accept", "application/vnd.emc.documentum+json");
		request.addHeader("Authorization", "Basic " + com.documentum.xmlconfig.util.Base64.encode(user + ":" + decodedPass));
		try {
			cls = httpClient.execute((HttpUriRequest)request);
			HttpEntity entity = cls.getEntity();
			rd = new BufferedReader(new InputStreamReader(entity.getContent()));
			String line = "";
			while (line != null) {
				line = rd.readLine();
				strResponse = strResponse + line;
			} 
			strResponse = strResponse.trim().replace("\n", "");
			String statusline = cls.getStatusLine().toString();
			if (!statusline.contains("200") && !statusline.contains("201")) {
				System.out.println(strResponse);
		    } else {
		    	json = new JSONObject(strResponse);
		    } 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return json;
	}
	
	public String getAttributeFromDql(String url, String repositoryName, String dql, String user, String pass) throws JSONException {
	    JSONObject theObject = makeRequest(url + "/repositories/" + repositoryName + "?dql=" + dql.replace(" ", "%20"), user, pass);
	    String attribute = null;
	    if (theObject != null) {
	    	if (theObject.has("entries")) {
	    		JSONArray js = theObject.getJSONArray("entries");
		    	for (int j = 0; j < js.length(); j++) {
		    		JSONObject ja = (JSONObject)js.get(j);
		    		for (int i = 0; i < ja.length(); i++)
		    			attribute = (String)ja.get("title"); 
		    	} 
	    	}
	    } 
	    return attribute;
	}
	
	public CloseableHttpResponse uploadToDocumentum(String properties, 
			String documentByte, String mimeType) throws ClientProtocolException, IOException, JSONException {
		Properties prop = configProperties.getConfigProperties("dctm-rest_config");
	    String repositoryName = prop.getProperty("repository_name");
	    String user = prop.getProperty("user");
	    String pass = prop.getProperty("pass");
	    String url = prop.getProperty("url");
	    int socketTimeout = Integer.parseInt(prop.getProperty("socket_timeout"));
	    int connectTimeout = Integer.parseInt(prop.getProperty("connect_timeout"));
	    int connectionRequestTimeout = Integer.parseInt(prop.getProperty("connection_request_timeout"));
	    byte[] decodedBytes = Base64.getDecoder().decode(pass);
	    String decodedPass = new String(decodedBytes);
	    DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
	    String dqlFolderId = "select r_object_id from dm_folder where object_name='Temp'";
	    String folderId = getAttributeFromDql(url, repositoryName, dqlFolderId, user, pass);
	    HttpPost post = new HttpPost(url + "/repositories/" + repositoryName + "/folders/" + folderId + "/objects");
		RequestConfig config = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).
				setConnectionRequestTimeout(connectionRequestTimeout).build();
	    post.setConfig(config);
	    post.addHeader("Accept", "application/vnd.emc.documentum+json");
	    post.addHeader("Authorization", "Basic " + com.documentum.xmlconfig.util.Base64.encode(user + ":" + decodedPass));
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    String dqlContentType = "select name from dm_format where mime_type='"+mimeType+"'";
	    String contentType = getAttributeFromDql(url, repositoryName, dqlContentType, user, pass);
	    nameValuePairs.add(new BasicNameValuePair("format", contentType));
	    post.setEntity((HttpEntity)new UrlEncodedFormEntity(nameValuePairs));
	    String json = "{\"properties\" : "+properties+"}";
	    MultipartEntity reqEntity = new MultipartEntity();
	    reqEntity.addPart("data", (ContentBody)new StringBody(json, "application/vnd.emc.documentum+json", Charset.forName("UTF-8")));
	    String base64Image = documentByte.replaceAll(" ", "+");
	    byte[] decodedBytes1 = Base64.getDecoder().decode(base64Image);
	    reqEntity.addPart("file", (ContentBody)new ByteArrayBody(decodedBytes1, mimeType, ""));
	    post.setEntity((HttpEntity)reqEntity);
	    CloseableHttpResponse response = defaultHttpClient.execute((HttpUriRequest)post);
		return response;
	}
	
	public CloseableHttpResponse checkoutDoc(String objectId) throws ClientProtocolException, IOException {
		Properties prop = configProperties.getConfigProperties("dctm-rest_config");
	    String repositoryName = prop.getProperty("repository_name");
	    String user = prop.getProperty("user");
	    String pass = prop.getProperty("pass");
	    String url = prop.getProperty("url");
	    int socketTimeout = Integer.parseInt(prop.getProperty("socket_timeout"));
	    int connectTimeout = Integer.parseInt(prop.getProperty("connect_timeout"));
	    int connectionRequestTimeout = Integer.parseInt(prop.getProperty("connection_request_timeout"));
	    byte[] decodedBytes = Base64.getDecoder().decode(pass);
	    String decodedPass = new String(decodedBytes);
	    DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
	    HttpPut put = new HttpPut(url + "/repositories/" + repositoryName + "/objects/" + objectId + "/lock");
	    RequestConfig config = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).
				setConnectionRequestTimeout(connectionRequestTimeout).build();
	    put.setConfig(config);
	    put.addHeader("Authorization", "Basic " + com.documentum.xmlconfig.util.Base64.encode(user + ":" + decodedPass));
	    put.addHeader("Accept", "application/vnd.emc.documentum+json");
		CloseableHttpResponse response = defaultHttpClient.execute((HttpUriRequest)put);
		return response;
	}
	
	public CloseableHttpResponse uploadVersionDocument(String objectId, String properties, 
			String documentByte, String mimeType) throws ClientProtocolException, IOException, JSONException {
		Properties prop = configProperties.getConfigProperties("dctm-rest_config");
	    String repositoryName = prop.getProperty("repository_name");
	    String user = prop.getProperty("user");
	    String pass = prop.getProperty("pass");
	    String url = prop.getProperty("url");
	    int socketTimeout = Integer.parseInt(prop.getProperty("socket_timeout"));
	    int connectTimeout = Integer.parseInt(prop.getProperty("connect_timeout"));
	    int connectionRequestTimeout = Integer.parseInt(prop.getProperty("connection_request_timeout"));
	    byte[] decodedBytes = Base64.getDecoder().decode(pass);
	    String decodedPass = new String(decodedBytes);
	    DefaultHttpClient defaultHttpClient = new DefaultHttpClient();
	    HttpPost post = new HttpPost(url + "/repositories/" + repositoryName + "/objects/" + objectId + "/versions?object-id=" 
	    				+ objectId + "&version-policy=next-major");
	    RequestConfig config = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connectTimeout).
				setConnectionRequestTimeout(connectionRequestTimeout).build();
	    post.setConfig(config);
	    post.addHeader("Accept", "application/vnd.emc.documentum+json");
	    post.addHeader("Authorization", "Basic " + com.documentum.xmlconfig.util.Base64.encode(user + ":" + decodedPass));
	    List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
	    String dqlContentType = "select name from dm_format where mime_type='"+mimeType+"'";
	    String contentType = getAttributeFromDql(url, repositoryName, dqlContentType, user, pass);
	    nameValuePairs.add(new BasicNameValuePair("format", contentType));
	    post.setEntity((HttpEntity)new UrlEncodedFormEntity(nameValuePairs));
	    String json = "{\"properties\" : "+properties+"}";
	    MultipartEntity reqEntity = new MultipartEntity();
	    reqEntity.addPart("metadata", (ContentBody)new StringBody(json, "application/vnd.emc.documentum+json", Charset.forName("UTF-8")));
	    String base64Image = documentByte.replaceAll(" ", "+");
	    byte[] decodedBytes1 = Base64.getDecoder().decode(base64Image);
	    reqEntity.addPart("binary", (ContentBody)new ByteArrayBody(decodedBytes1, mimeType, ""));
	    post.setEntity((HttpEntity)reqEntity);
	    CloseableHttpResponse response = defaultHttpClient.execute((HttpUriRequest)post);
		return response;
	}
}
