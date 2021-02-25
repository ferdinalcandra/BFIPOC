package com.msi.upload.model.ms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dms_group_doc")
public class DmsGroupDoc {
	@Id
	@Column(name = "dms_group_doc_id", unique = true, nullable = false)
	private String dmsGroupDocId;
	
	@Column(name = "dms_group_doc_name")
	private String dmsGroupDocName;
	
	@Column(name = "dms_file_naming_format")
	private String dmsFileNamingFormat;

	public String getDmsGroupDocId() {
		return dmsGroupDocId;
	}

	public void setDmsGroupDocId(String dmsGroupDocId) {
		this.dmsGroupDocId = dmsGroupDocId;
	}

	public String getDmsGroupDocName() {
		return dmsGroupDocName;
	}

	public void setDmsGroupDocName(String dmsGroupDocName) {
		this.dmsGroupDocName = dmsGroupDocName;
	}

	public String getDmsFileNamingFormat() {
		return dmsFileNamingFormat;
	}

	public void setDmsFileNamingFormat(String dmsFileNamingFormat) {
		this.dmsFileNamingFormat = dmsFileNamingFormat;
	}
}
