package id.co.bfi.dmsuploadscheduler.service.dctm;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.bfi.dmsuploadscheduler.config.yaml.DctmRestConfig;
import id.co.bfi.dmsuploadscheduler.config.yaml.QueryConfig;

@Service
public class DctmDocumentService {
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private DctmRestConfig dctmRestConfig; 
	
	@Autowired
	private QueryConfig queryConfig;
	
	@Autowired
	private DctmRestService dctmRestService;
	
	@Autowired
	private DctmRestClient dctmRestClient;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	// test
	public JsonNode uploadDocument(String properties, String documentByte, String mimeType, String folderId) throws JsonMappingException, JsonProcessingException {
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("metadata", objectMapper.createObjectNode().set("properties", objectMapper.readTree(properties)).toString());
		parameters.add("content", Base64.getDecoder().decode(documentByte.replaceAll(" ", "+")));
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		String contentType = dctmRestService.getAttributeFromDql(queryConfig.getContentTypeDql()+"'"+mimeType+"'");
		String result = restTemplate.postForObject(dctmRestConfig.getUrl()+"/repositories/"+dctmRestConfig.getRepositoryName()+
				"/folders/"+folderId+"/documents?format="+contentType, 
		    new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
	    return objectMapper.readTree(result);
	}
	
	// test
	public JsonNode uploadVersionDocument(String chronicleId, String properties, String documentByte, String mimeType) throws JsonMappingException, JsonProcessingException, UnsupportedEncodingException {
		String isDocumentCheckoutDql = "select r_lock_owner from "+objectMapper.readTree(properties).get("r_object_type").asText()+"(all) where r_object_id = '"+chronicleId+"'";
		if (!dctmRestService.getAttributeFromDql(isDocumentCheckoutDql).equals("")) {
			dctmRestClient.cancelCheckoutDocument("/objects/"+chronicleId+"/lock");
		}
		dctmRestClient.checkoutDocument("/objects/"+chronicleId+"/lock");
		MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<String, Object>();
		parameters.add("content", Base64.getDecoder().decode(documentByte.replaceAll(" ", "+")));
		parameters.add("metadata", objectMapper.createObjectNode().set("properties", objectMapper.readTree(properties)).toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		String contentType = dctmRestService.getAttributeFromDql(queryConfig.getContentTypeDql()+"'"+mimeType+"'");
		String result = restTemplate.postForObject(dctmRestConfig.getUrl()+"/repositories/"+dctmRestConfig.getRepositoryName()+"/objects/"+chronicleId+
				"/versions?object-id="+chronicleId+"&version-policy=next-major&format="+contentType, 
		    new HttpEntity<MultiValueMap<String, Object>>(parameters, headers), String.class);
		return objectMapper.readTree(result);
	}
	
	// test
	public String getFullFileSystemPath(String objectId) throws JsonMappingException, JsonProcessingException {
		String fullFileSystemPath = null;
		String docbaseParamDql = queryConfig.getDocbaseParamDql();
		JsonNode resultObject = dctmRestService.makeRequest(docbaseParamDql.replace("parentId", objectId));
		JsonNode paramObject = null;
		if (resultObject != null) {
	    	if (resultObject.has("entries")) {
	    		JsonNode jsonArray = objectMapper.readTree(resultObject.get("entries").toString());
	    		paramObject = objectMapper.readTree(jsonArray.get(0).get("content").get("properties").toString());
	    	}
	    }
		if (paramObject != null) {
			int dataTicket = paramObject.get("data_ticket").asInt();
			String dosExtension = paramObject.get("dos_extension").asText();
			String fileSystemPath = paramObject.get("file_system_path").asText();
			int docbaseId = paramObject.get("r_docbase_id").asInt();
			String dbId = new String().valueOf(docbaseId);
			while (dbId.length() < 8) {
				dbId = "0"+dbId;
			}
			StringBuilder sb = new StringBuilder(fileSystemPath+"\\"+dbId);
//			String dbId = dctmConfigProp.getProperty("specific_docbase_id");
//			StringBuilder sb = new StringBuilder(fileSystemPath+"\\"+dbId);
			double dataTicketTemp = dataTicket + Math.pow(2.00, 32.00);
			String hexStr = Long.toHexString((long)dataTicketTemp);
			String[] hexStrArr = hexStr.replaceAll("..(?!$)", "$0 ").split(" ");
			if (hexStrArr.length > 0) {
				int count = 1;
				for(int i=0; i<hexStrArr.length; i++) {
					if (count < hexStrArr.length)
						sb.append("\\"+hexStrArr[i]);
					else
						sb.append(hexStrArr[i]);
				}
				sb.append("."+dosExtension);
				fullFileSystemPath = sb.toString();
			}
		}
		return fullFileSystemPath.toString();
	}
	
}
