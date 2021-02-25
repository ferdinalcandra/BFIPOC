package id.co.bfi.dmsuploadscheduler.service.dctm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Service
public class DctmFolderService {
	
	@Autowired
	private DctmRestClient dctmRestClient;
	
	@Autowired
	private DctmRestService dctmRestService;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	// test
	public String createFolderByPath(String path) throws JsonMappingException, JsonProcessingException {
		String folderId = null;
		if (path.substring(0, 1).equals("/")) {
			String[] folders = path.split("/");
			String checkedPath = "/" + folders[1];
			String cabinetId = null;
			String dqlCabinetCheck = "select r_object_id from dm_cabinet where object_name = '"+folders[1]+"' and "
					+ "any r_folder_path = '"+checkedPath+"'";
			String existingCabinetId = dctmRestService.getAttributeFromDql(dqlCabinetCheck);
			if (existingCabinetId == null)
				cabinetId = createObject(folders[1], "dm_cabinet" , "/cabinets");
			else
				cabinetId = existingCabinetId;
			for (int i=2; i<folders.length; ++i) {
				checkedPath = checkedPath + "/" + folders[i];
				String dqlFolderCheck = "select r_object_id from dm_folder where object_name = '"+folders[i]+"' and "
						+ "any r_folder_path = '"+checkedPath+"'";
				String existingFolderId = dctmRestService.getAttributeFromDql(dqlFolderCheck);
				if (existingFolderId == null) {
					folderId = createObject(folders[i], "dm_folder", "/folders/"+cabinetId+"/objects");
					if (folderId != null)
						cabinetId = folderId;
				} else
					folderId = existingFolderId;
			}
		}
		return folderId;
	}
	
	// test
	public String createObject(String objectName, String objectType, String url) throws JsonMappingException, JsonProcessingException {
		JsonNode propertiesValue = objectMapper.createObjectNode().put("object_name", objectName).put("r_object_type", objectType);
		ObjectNode properties = objectMapper.createObjectNode().set("properties", propertiesValue);
		JsonNode json = objectMapper.readTree(dctmRestClient.createObject(url, objectMapper.writeValueAsString(properties)).getBody().toString());
		if (json.has("properties"))
        	return json.get("properties").get("r_object_id").asText();
        else
        	return null;
	}
}
