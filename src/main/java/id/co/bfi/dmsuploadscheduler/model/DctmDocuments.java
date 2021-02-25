package id.co.bfi.dmsuploadscheduler.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="dctm_documents")
public class DctmDocuments {
	@Id
	@Column(name = "dctm_documents_id", unique = true, nullable = false)
	private String dctmDocumentsId;

	@Column(name = "dctm_id")
	private String dctmId;
	
	@Column(name = "original_file_name")
	private String originalFileName;
	
	@Column(name = "path_file_name")
	private String pathFileName;
	
	@Column(name = "created_by")
	private String createdBy;
	
	@Column(name = "created_date")
	private Date createdDate;
	
	@Column(name = "last_modified_by")
	private String lastModifiedBy;
	
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
	
	@Column(name = "source")
	private String source;
	
	@Column(name = "doc_type")
	private String docType;
	
	@Column(name = "doc_key")
	private String docKey;
	
	@Column(name = "doc_type_id")
	private String docTypeId;

	public String getDctmDocumentsId() {
		return dctmDocumentsId;
	}

	public void setDctmDocumentsId(String dctmDocumentsId) {
		this.dctmDocumentsId = dctmDocumentsId;
	}

	public String getDctmId() {
		return dctmId;
	}

	public void setDctmId(String dctmId) {
		this.dctmId = dctmId;
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

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreateDate() {
		return createdDate;
	}

	public void setCreateDate(Date createDate) {
		this.createdDate = createDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
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
