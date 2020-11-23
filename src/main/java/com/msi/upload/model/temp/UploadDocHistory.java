package com.msi.upload.model.temp;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="UploadDocHistory")
public class UploadDocHistory {
	@Id
	@Column(name = "UploadId", unique = true, nullable = false)
	private String uploadId;

	@Column(name = "PathFilename")
	private String pathFileName;
	
	@Column(name = "SatatusUpload")
	private int statusUpload;
	
	@Column(name = "MetaDataDctm")
	private String metaDataDctm;
	
	@Column(name = "CreateDate")
	private Date createDate;
	
	@Column(name = "ModifiedDate")
	private Date modifiedDate;
	
	@Column(name = "dms_mime_type")
	private String dmsMimeType;
	
	@Column(name = "msg")
	private String msg;
	
	@Column(name = "dctmId")
	private String dctmId;
	
	@Column(name = "DocId")
	private String docId;

	public String getUploadId() {
		return uploadId;
	}

	public void setUploadId(String uploadId) {
		this.uploadId = uploadId;
	}

	public String getPathFileName() {
		return pathFileName;
	}

	public void setPathFileName(String pathFileName) {
		this.pathFileName = pathFileName;
	}

	public int getStatusUpload() {
		return statusUpload;
	}

	public void setStatusUpload(int statusUpload) {
		this.statusUpload = statusUpload;
	}

	public String getMetaDataDctm() {
		return metaDataDctm;
	}

	public void setMetaDataDctm(String metaDataDctm) {
		this.metaDataDctm = metaDataDctm;
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

	public String getDmsMimeType() {
		return dmsMimeType;
	}

	public void setDmsMimeType(String dmsMimeType) {
		this.dmsMimeType = dmsMimeType;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getDctmId() {
		return dctmId;
	}

	public void setDctmId(String dctmId) {
		this.dctmId = dctmId;
	}

	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}
}
