package id.co.bfi.dmsuploadscheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="document_type_mst")
public class DmsDocType {
	@Id
	@Column(name = "sq_document_type_mst", unique = true, nullable = false)
	private String sqDocumentTypeMst;
	
	@Column(name = "doc_type")
	private String docType;
	
	@Column(name = "doc_type_desc")
	private String docTypeDesc;
	
	@Column(name = "max_size_id_kb")
	private String maxSizeInKb;
	
	@Column(name = "is_allow_versioning")
	private Integer isAllowVersioning;
	
	@Column(name = "is_allow_multiple")
	private Integer isAllowMultiple;
	
	@Column(name = "jenis_document_type")
	private String jenisDocumentType;

	public String getSqDocumentTypeMst() {
		return sqDocumentTypeMst;
	}

	public void setSqDocumentTypeMst(String sqDocumentTypeMst) {
		this.sqDocumentTypeMst = sqDocumentTypeMst;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getDocTypeDesc() {
		return docTypeDesc;
	}

	public void setDocTypeDesc(String docTypeDesc) {
		this.docTypeDesc = docTypeDesc;
	}

	public String getMaxSizeInKb() {
		return maxSizeInKb;
	}

	public void setMaxSizeInKb(String maxSizeInKb) {
		this.maxSizeInKb = maxSizeInKb;
	}

	public Integer getIsAllowVersioning() {
		return isAllowVersioning;
	}

	public void setIsAllowVersioning(Integer isAllowVersioning) {
		this.isAllowVersioning = isAllowVersioning;
	}

	public Integer getIsAllowMultiple() {
		return isAllowMultiple;
	}

	public void setIsAllowMultiple(Integer isAllowMultiple) {
		this.isAllowMultiple = isAllowMultiple;
	}

	public String getJenisDocumentType() {
		return jenisDocumentType;
	}

	public void setJenisDocumentType(String jenisDocumentType) {
		this.jenisDocumentType = jenisDocumentType;
	}
}
