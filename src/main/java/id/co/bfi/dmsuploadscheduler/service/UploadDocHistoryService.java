package id.co.bfi.dmsuploadscheduler.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import id.co.bfi.dmsuploadscheduler.config.yaml.DctmRestConfig;
import id.co.bfi.dmsuploadscheduler.config.yaml.YamlPropertySourceFactory;
import id.co.bfi.dmsuploadscheduler.model.DctmDocuments;
import id.co.bfi.dmsuploadscheduler.model.DctmUploadDocumentHistory;
import id.co.bfi.dmsuploadscheduler.model.DmsDocType;
import id.co.bfi.dmsuploadscheduler.repository.DctmDocumentsRepository;
import id.co.bfi.dmsuploadscheduler.repository.DctmUploadDocumentHistoryRepository;
import id.co.bfi.dmsuploadscheduler.repository.DmsDocTypeRepository;
import id.co.bfi.dmsuploadscheduler.service.share_folder.ShareFolderService;
import id.co.bfi.dmsuploadscheduler.service.dctm.DctmDocumentService;
import id.co.bfi.dmsuploadscheduler.service.dctm.DctmFolderService;
import id.co.bfi.dmsuploadscheduler.service.dctm.DctmRestService;

@Component
@Configuration
@PropertySource(value = "file:conf/application.yml", factory = YamlPropertySourceFactory.class)
public class UploadDocHistoryService {
	
	@Autowired
	private DctmDocumentsRepository documentsRepository;
	
	@Autowired
	private DctmUploadDocumentHistoryRepository uploadDocHistoryRepository;
	
	@Autowired
	private DmsDocTypeRepository dmsDocTypeRepository;
	
	@Autowired
	private ShareFolderService shareFolderService;
	
	@Autowired
	private DctmRestService dctmRestService;
	
	@Autowired
	private DctmRestConfig dctmRestConfig;
	
	@Autowired
	private DctmDocumentService dctmDocumentService;
	
	@Autowired
	private DctmFolderService dctmFolderService;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Scheduled(cron = "${scheduler.cronExpression}", zone = "Asia/Jakarta")
	public void processDmsReqUpload() throws SQLException, IOException {
		List<DctmDocuments> documentsList = documentsRepository.listDocuments();
		if (documentsList != null) {
			if (documentsList.size() > 0) {
				for(int i=0; i<documentsList.size(); i++) {
					String docId = documentsList.get(i).getDctmDocumentsId();
					List<DctmUploadDocumentHistory> uploadDocHistoryList = uploadDocHistoryRepository.listUploadDocHistoryByDocId(docId);
					if (uploadDocHistoryList != null) {
						if (uploadDocHistoryList.size() > 0) {
							boolean success = true;
							for (int j=0; j<uploadDocHistoryList.size(); j++) {
								if (success) {
									String pathFileName = uploadDocHistoryList.get(j).getPathFileName();
									ByteArrayOutputStream baos = shareFolderService.getFileOverSharedFolder(pathFileName);
									String documentByte = Base64.getEncoder().encodeToString(baos.toByteArray());
									String mimeType = uploadDocHistoryList.get(j).getDmsMimeType();
									String properties = uploadDocHistoryList.get(j).getMetadataDctm();
									JsonNode json = objectMapper.readTree(properties);
									String dctmDocNumber = null;
									String objectType = null;
									String chronicleId = null;
									String docTypeId = null;
									if (json != null) {
										if (json.has("dctm_doc_number")) {
											if (json.get("dctm_doc_number") != null) {
												if (!json.get("dctm_doc_number").toString().isEmpty())
													dctmDocNumber = json.get("dctm_doc_number").asText().trim();
											}
										}
										if (json.has("r_object_type")) {
											if (json.get("r_object_type") != null) {
												if (!json.get("r_object_type").toString().isEmpty())
													objectType = json.get("r_object_type").asText().trim();
											}
										}
										if (json.has("dctm_doc_type_id")) {
											if (json.get("dctm_doc_type_id") != null) {
												if (!json.get("dctm_doc_type_id").toString().isEmpty())
													docTypeId = json.get("dctm_doc_type_id").asText().trim();
											}
										}
									}
									if (objectType != null && dctmDocNumber != null) {
										String queryDql = "select i_chronicle_id from "+objectType+" where lower(dctm_doc_number) = '"+dctmDocNumber.toLowerCase()+"'";
									    chronicleId = dctmRestService.getAttributeFromDql(queryDql);
									}
									String objectId = null;
									JsonNode uploadDocResponse = null;
									DmsDocType dmsDocType = dmsDocTypeRepository.findBySqDocumentTypeMst(docTypeId);
									String folderId = dctmFolderService.createFolderByPath(generateFolderPath(docTypeId, json, dmsDocType));
									if (chronicleId != null) {
										if (dmsDocType.getIsAllowVersioning() == 1) {
											// versioning
											uploadDocResponse = dctmDocumentService.uploadVersionDocument(chronicleId, properties, documentByte, mimeType);
											objectId = uploadDocResponse.get("properties").get("r_object_id").asText();
										} else if (dmsDocType.getIsAllowMultiple() == 1) {
											// multiple
											uploadDocResponse = dctmDocumentService.uploadDocument(properties, documentByte, mimeType, folderId);
											objectId = uploadDocResponse.get("properties").get("r_object_id").asText();
										}
									} else {
										// new doc
										uploadDocResponse = dctmDocumentService.uploadDocument(properties, documentByte, mimeType, folderId);
										objectId = uploadDocResponse.get("properties").get("r_object_id").asText();
									}
									Date now = new Date();
									String fullSystemPath = null;
									if (objectId != null) {
										uploadDocHistoryList.get(j).setDctmId(objectId);
										uploadDocHistoryList.get(j).setStatusUpload(2);
										uploadDocHistoryList.get(j).setMsg("OK");
										fullSystemPath = dctmDocumentService.getFullFileSystemPath(objectId);
										uploadDocHistoryList.get(j).setPathFileName(Base64.getEncoder().encodeToString(fullSystemPath.getBytes()));
										
										documentsList.get(i).setPathFileName(Base64.getEncoder().encodeToString(fullSystemPath.getBytes()));
										documentsList.get(i).setDctmId(objectId);
										documentsList.get(i).setLastModifiedDate(now);
										documentsList.get(i).setLastModifiedBy("dms scheduler");
										documentsRepository.save(documentsList.get(i));
									} else {
										success = false;
										uploadDocHistoryList.get(j).setStatusUpload(3);
										uploadDocHistoryList.get(j).setMsg("Failed");
									}
									uploadDocHistoryList.get(j).setLastModifiedDate(now);
									uploadDocHistoryList.get(j).setLastModifiedBy("dms scheduler");
									uploadDocHistoryRepository.save(uploadDocHistoryList.get(j));
								}
							}
						}
					}
				}
			}
		}
	}
	
	// test
	public String generateFolderPath(String docTypeId, JsonNode json, DmsDocType dmsDocType) {
		String folderPath = null;
		String customerId = null;
		if (json.has("dctm_customer_id")) {
			if (json.get("dctm_customer_id") != null) {
				if (!json.get("dctm_customer_id").asText().isEmpty())
					customerId = json.get("dctm_customer_id").asText().trim();
			}
		}
		if (dmsDocType.getJenisDocumentType().equalsIgnoreCase("KONSUMEN")) {
			folderPath = "/"+dctmRestConfig.getConsumerDocCabinetName()+"/"+customerId+"/"+dmsDocType.getDocType();
		} else if (dmsDocType.getJenisDocumentType().equalsIgnoreCase("INTERNAL")) {
			folderPath = "/"+dctmRestConfig.getInternalDocCabinetName()+"/"+
					dmsDocType.getDocTypeDesc()+"/"+dctmRestConfig.getDmsActiveDocFolderName();
		} else {
			String branchId = null;
			if (json.has("dctm_branch_id")) {
				if (json.get("dctm_branch_id") != null) {
					if (!json.get("dctm_branch_id").asText().isEmpty())
						branchId = json.get("dctm_branch_id").asText().trim();
				}
			}
			String leadsId = null;
			if (json.has("dctm_leads_id")) {
				if (json.get("dctm_leads_id") != null) {
					if (!json.get("dctm_leads_id").asText().isEmpty())
						leadsId = json.get("dctm_leads_id").asText().trim();
				}
			}
			folderPath = "/"+dctmRestConfig.getConsumerDocCabinetName()+"/"+customerId+"/"+branchId
			+"/"+leadsId+"/"+dmsDocType.getDocType();
		}
		return folderPath;
	}
	
}
