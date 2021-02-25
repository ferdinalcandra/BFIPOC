package id.co.bfi.dmsuploadscheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.co.bfi.dmsuploadscheduler.model.DmsBranch;

@Repository
public interface DmsBranchRepository extends JpaRepository<DmsBranch, String>{
	DmsBranch findByBranchId(String branchId);
}
