package id.co.bfi.dmsuploadscheduler.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="branch_mst")
public class DmsBranch {
	@Id
	@Column(name = "branch_id", unique = true, nullable = false)
	private String branchId;
	
	@Column(name = "branch_full_name")
	private String branchFullName;

	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}

	public String getBranchFullName() {
		return branchFullName;
	}

	public void setBranchFullName(String branchFullName) {
		this.branchFullName = branchFullName;
	}
}
