package id.co.bfi.dmsuploadscheduler.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import id.co.bfi.dmsuploadscheduler.model.DmsDocType;

@Repository
public interface DmsDocTypeRepository extends JpaRepository<DmsDocType, String>{
	DmsDocType findBySqDocumentTypeMst(String sqDocumentTypeMst);
}
