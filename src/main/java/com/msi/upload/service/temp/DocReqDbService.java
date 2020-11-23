package com.msi.upload.service.temp;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.msi.upload.config.ConfigProperties;
import com.msi.upload.model.temp.Documents;
import com.msi.upload.model.temp.UploadDocHistory;
import com.msi.upload.repository.ms.DmsDocTypeRepository;
import com.msi.upload.repository.temp.DocumentsRepository;
import com.msi.upload.repository.temp.UploadDocHistoryRepository;
import com.msi.upload.service.ShareFolderService;
import com.msi.upload.service.dctm.DctmUploadService;

@Component
public class DocReqDbService {
	@Autowired
	DocumentsRepository documentsRepository;
	
	@Autowired
	UploadDocHistoryRepository uploadDocHistoryRepository;
	
	@Autowired
	DmsDocTypeRepository dmsDocTypeRepository;
	
	@Autowired
	DctmUploadService dctmUploadService;
	
	@Autowired
	ConfigProperties configProperties;
	
	@Autowired
	ShareFolderService shareFolderService;
	
	@Scheduled(cron = "${cron.expression}", zone = "Asia/Jakarta")
	public void processDmsReqUpload() throws SQLException, IOException {
		List<Documents> documentsList = documentsRepository.listDocuments();
		if (documentsList != null) {
			if (documentsList.size() > 0) {
				System.out.println("processing documentsList : "+documentsList.size()+" row(s)");
				for(int i=0; i<documentsList.size(); i++) {
					Properties prop = configProperties.getConfigProperties("application");
					String docId = documentsList.get(i).getDocId();
					System.out.println("processing docId : "+docId);
					List<UploadDocHistory> uploadDocHistoryList = uploadDocHistoryRepository.listUploadDocHistoryByDocId(docId);
					if (uploadDocHistoryList != null) {
						if (uploadDocHistoryList.size() > 0) {
							boolean success = true;
							for (int j=0; j<uploadDocHistoryList.size(); j++) {
								if (success == true) {
									String pathFileName = uploadDocHistoryList.get(j).getPathFileName();
									final String userName = prop.getProperty("share_folder_user");
									final String password = prop.getProperty("share_folder_pass");
									final String domain = prop.getProperty("share_folder_domain");
									final String shareFolderName = prop.getProperty("share_folder_name_upload");
									pathFileName = "\\\\"+domain+"\\"+shareFolderName+"\\"+pathFileName;
									if (pathFileName.contains("\\\\")) {
										pathFileName = pathFileName.replace("\\\\", "//");
									}
									if (pathFileName.contains("\\")) {
										pathFileName = pathFileName.replace("\\", "/");
									}
									if (!pathFileName.startsWith("//")) {
										pathFileName = "//"+pathFileName;
									}
									if (!pathFileName.startsWith("smb:")) {
										pathFileName = "smb:"+pathFileName;
									}
									byte[] decodedBytes = Base64.getDecoder().decode(password);
						    		String decodedPass = new String(decodedBytes);
									ByteArrayOutputStream baos = shareFolderService.getFileOverSharedFolder(domain, userName, decodedPass, pathFileName);
									String documentByte = Base64.getEncoder().encodeToString(baos.toByteArray());
									String mimeType = uploadDocHistoryList.get(j).getDmsMimeType();
									String properties = uploadDocHistoryList.get(j).getMetaDataDctm();
									JSONObject json = new JSONObject(properties);
									String dctmDocNumber = null;
									String objectType = null;
									String chronicleId = null;
									String queryDql = null;
									if (json != null) {
										if (json.has("dctm_doc_number")) {
											if (json.get("dctm_doc_number") != null) {
												if (!json.get("dctm_doc_number").toString().isEmpty()) {
													dctmDocNumber = json.get("dctm_doc_number").toString().trim();
												}
											}
										}
										if (json.has("r_object_type")) {
											if (json.get("r_object_type") != null) {
												if (!json.get("r_object_type").toString().isEmpty()) {
													objectType = json.get("r_object_type").toString().trim();
												}
											}
										}
									}
									if (objectType != null && dctmDocNumber != null) {
										queryDql = "select i_chronicle_id from "+objectType+" where lower(dctm_doc_number) = '"+dctmDocNumber.toLowerCase()+"'";
										Properties restProp = configProperties.getConfigProperties("dctm-rest_config");
									    String repositoryName = restProp.getProperty("repository_name");
									    String user = restProp.getProperty("user");
									    String pass = restProp.getProperty("pass");
									    String url = restProp.getProperty("url");
									    chronicleId = dctmUploadService.getAttributeFromDql(url, repositoryName, queryDql, user, pass);
									}
									String objectId = null;
									CloseableHttpResponse response = null;
									String statusline = null;
									String reasonPhrase = null;
									if (chronicleId != null) {
										// versioning
										// checkout
										CloseableHttpResponse checkoutResponse = dctmUploadService.checkoutDoc(chronicleId);
										String statuslineCheckout = checkoutResponse.getStatusLine().toString();
										if (statuslineCheckout.contains("200") || statuslineCheckout.contains("201")) {
											System.out.println("success checkout doc : "+chronicleId);
											response = dctmUploadService.uploadVersionDocument(chronicleId, json.toString(), documentByte, mimeType);
										    statusline = response.getStatusLine().toString();
											reasonPhrase = response.getStatusLine().getReasonPhrase();
										    if (statusline.contains("200") || statusline.contains("201")) {
												HttpEntity uploadEntity = response.getEntity();
												String uploadResponseString = EntityUtils.toString(uploadEntity, "UTF-8");
												JSONObject objUploadResponse = new JSONObject(uploadResponseString);
												if (objUploadResponse.has("properties")) {
													System.out.println("upload version response : "+objUploadResponse);
													System.out.println("versioning...");
													objectId = objUploadResponse.getJSONObject("properties").getString("r_object_id");
												}
										    }
										}
									} else {
										// new doc
										response = dctmUploadService.uploadToDocumentum(properties, documentByte, mimeType);
										statusline = response.getStatusLine().toString();
									    reasonPhrase = response.getStatusLine().getReasonPhrase();
										if (statusline.contains("200") || statusline.contains("201")) {
											HttpEntity entity = response.getEntity();
											String responseString = EntityUtils.toString(entity, "UTF-8");
											JSONObject objResponse = new JSONObject(responseString);
											if (objResponse.has("properties")) {
												objectId = objResponse.getJSONObject("properties").getString("r_object_id");
												System.out.println("upload response : "+objResponse);
												System.out.println("new document...");
											}
									    }
									}
									Date now = new Date();
									String fullSystemPath = null;
									if (objectId != null) {
										System.out.println("success uploading docId : "+docId);
										System.out.println("objectId : "+objectId);
										uploadDocHistoryList.get(j).setDctmId(objectId);
										uploadDocHistoryList.get(j).setStatusUpload(2);
										uploadDocHistoryList.get(j).setMsg(reasonPhrase);
										fullSystemPath = dctmUploadService.getFullFileSystemPath(objectId);
										uploadDocHistoryList.get(j).setPathFileName(fullSystemPath);
									} else {
										success = false;
										uploadDocHistoryList.get(j).setStatusUpload(3);
										uploadDocHistoryList.get(j).setMsg(reasonPhrase);
									}
									uploadDocHistoryList.get(j).setModifiedDate(now);
									uploadDocHistoryRepository.save(uploadDocHistoryList.get(j));
									System.out.println("success updating UploadDocHistory : "+uploadDocHistoryList.get(j).getUploadId());
									
									if (objectId != null && fullSystemPath != null) {
										documentsList.get(i).setPathFileName(fullSystemPath);
										documentsList.get(i).setDctmId(objectId);
										documentsList.get(i).setModifiedDate(now);
										documentsRepository.save(documentsList.get(i));
										System.out.println("success updating Documents : "+docId);
									}
								}
							}
						}
					}
				}
			}
		}
	}
}
