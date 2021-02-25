package com.msi.upload.model.ms;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="dms_branch")
public class DmsBranch {
	@Id
	@Column(name = "dms_branch_id", unique = true, nullable = false)
	private String dmsBranchId;
	
	@Column(name = "dms_branch_name")
	private String dmsBranchName;
	
	@Column(name = "dms_region")
	private String dmsRegion;
	
	@Column(name = "dms_area")
	private String dmsArea;
	
	@Column(name = "dms_centro")
	private String dmsCentro;
	
	@Column(name = "dms_custodian")
	private String dmsCustodian;
	
	@Column(name = "dms_pic_centro")
	private String dmsPicCentro;
	
	@Column(name = "dms_email")
	private String dmsEmail;
	
	@Column(name = "dms_telp")
	private String dmsTelp;
	
	@Column(name = "dms_alamat_centro")
	private String dmsAlamatCentro;

	public String getDmsBranchId() {
		return dmsBranchId;
	}

	public void setDmsBranchId(String dmsBranchId) {
		this.dmsBranchId = dmsBranchId;
	}

	public String getDmsBranchName() {
		return dmsBranchName;
	}

	public void setDmsBranchName(String dmsBranchName) {
		this.dmsBranchName = dmsBranchName;
	}

	public String getDmsRegion() {
		return dmsRegion;
	}

	public void setDmsRegion(String dmsRegion) {
		this.dmsRegion = dmsRegion;
	}

	public String getDmsArea() {
		return dmsArea;
	}

	public void setDmsArea(String dmsArea) {
		this.dmsArea = dmsArea;
	}

	public String getDmsCentro() {
		return dmsCentro;
	}

	public void setDmsCentro(String dmsCentro) {
		this.dmsCentro = dmsCentro;
	}

	public String getDmsCustodian() {
		return dmsCustodian;
	}

	public void setDmsCustodian(String dmsCustodian) {
		this.dmsCustodian = dmsCustodian;
	}

	public String getDmsPicCentro() {
		return dmsPicCentro;
	}

	public void setDmsPicCentro(String dmsPicCentro) {
		this.dmsPicCentro = dmsPicCentro;
	}

	public String getDmsEmail() {
		return dmsEmail;
	}

	public void setDmsEmail(String dmsEmail) {
		this.dmsEmail = dmsEmail;
	}

	public String getDmsTelp() {
		return dmsTelp;
	}

	public void setDmsTelp(String dmsTelp) {
		this.dmsTelp = dmsTelp;
	}

	public String getDmsAlamatCentro() {
		return dmsAlamatCentro;
	}

	public void setDmsAlamatCentro(String dmsAlamatCentro) {
		this.dmsAlamatCentro = dmsAlamatCentro;
	}
}
