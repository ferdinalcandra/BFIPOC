package com.msi.upload.model.ms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dms_doc_type")
public class DmsDocType {
	@Id
	@Column(name = "dms_doc_type_id", unique = true, nullable = false)
	private String dmsDocTypeId;
	
	@Column(name = "dms_process_name")
	private String dmsProcessName;
	
	@Column(name = "dms_doc_type")
	private String dmsDocType;
	
	@Column(name = "dms_doc_type_name")
	private String dmsDocTypeName;
	
	@Column(name = "dms_max_size")
	private String dmsMaxSize;
	
	@Column(name = "dms_version")
	private Integer dmsVersion;
	
	@Column(name = "dms_group_doc_id")
	private String dmsGroupDocId;
	
	@Column(name = "dms_group_scheme_product")
	private String dmsGroupSchemeProduct;

	public String getDmsDocTypeName() {
		return dmsDocTypeName;
	}

	public void setDmsDocTypeName(String dmsDocTypeName) {
		this.dmsDocTypeName = dmsDocTypeName;
	}

	public String getDmsDocTypeId() {
		return dmsDocTypeId;
	}

	public void setDmsDocTypeId(String dmsDocTypeId) {
		this.dmsDocTypeId = dmsDocTypeId;
	}

	public String getDmsProcessName() {
		return dmsProcessName;
	}

	public void setDmsProcessName(String dmsProcessName) {
		this.dmsProcessName = dmsProcessName;
	}

	public String getDmsDocType() {
		return dmsDocType;
	}

	public void setDmsDocType(String dmsDocType) {
		this.dmsDocType = dmsDocType;
	}

	public String getDmsMaxSize() {
		return dmsMaxSize;
	}

	public void setDmsMaxSize(String dmsMaxSize) {
		this.dmsMaxSize = dmsMaxSize;
	}

	public Integer getDmsVersion() {
		return dmsVersion;
	}

	public void setDmsVersion(Integer dmsVersion) {
		this.dmsVersion = dmsVersion;
	}

	public String getDmsGroupDocId() {
		return dmsGroupDocId;
	}

	public void setDmsGroupDocId(String dmsGroupDocId) {
		this.dmsGroupDocId = dmsGroupDocId;
	}

	public String getDmsGroupSchemeProduct() {
		return dmsGroupSchemeProduct;
	}

	public void setDmsGroupSchemeProduct(String dmsGroupSchemeProduct) {
		this.dmsGroupSchemeProduct = dmsGroupSchemeProduct;
	}
}
