package com.msi.upload.model.temp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="Documents")
public class Documents {
	@Id
	@Column(name = "DocId", unique = true, nullable = false)
	private String docId;

	@Column(name = "DctmId")
	private String dctmId;
	
	@Column(name = "UploadId")
	private String uploadId;
	
	@Column(name = "OriginalFileName")
	private String originalFileName;
	
	@Column(name = "PathFileName")
	private String pathFileName;
	
	@Column(name = "CreateDate")
	private Date createDate;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "Source")
	private String source;
	
	@Column(name = "DocType")
	private String docType;
	
	@Column(name = "DocKey")
	private String docKey;
	
	@Column(name = "DocTypeId")
	private String docTypeId;

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getDctmId() {
		return dctmId;
	}

	public void setDctmId(String dctmId) {
		this.dctmId = dctmId;
	}

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}

	public String getPathFileName() {
		return pathFileName;
	}

	public void setPathFileName(String pathFileName) {
		this.pathFileName = pathFileName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocKey() {
		return docKey;
	}

	public void setDocKey(String docKey) {
		this.docKey = docKey;
	}

	public String getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(String docTypeId) {
		this.docTypeId = docTypeId;
	}
}
