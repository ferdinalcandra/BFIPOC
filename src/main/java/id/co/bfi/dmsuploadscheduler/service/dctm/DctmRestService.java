package id.co.bfi.dmsuploadscheduler.service.dctm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DctmRestService {
	
	@Autowired
	private DctmRestClient dctmRestClient;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	// test
	public JsonNode makeRequest(String dql) throws JsonMappingException, JsonProcessingException {
        return objectMapper.readTree(dctmRestClient.getDataFromDql(dql).getBody().toString());
	}
	
	// test
	public String getAttributeFromDql(String dql) throws JsonMappingException, JsonProcessingException {
        JsonNode json = objectMapper.readTree(dctmRestClient.getDataFromDql(dql).getBody().toString());
        if (json.has("entries"))
        	return json.get("entries").get(0).get("title").asText();
        else
        	return null;
	}
}
