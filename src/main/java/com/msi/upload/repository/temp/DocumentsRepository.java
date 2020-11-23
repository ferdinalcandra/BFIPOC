package com.msi.upload.repository.temp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.msi.upload.model.temp.Documents;

public interface DocumentsRepository extends JpaRepository<Documents, String>{
	Documents findByDocId(String docId);
	
	String query = "select c from Documents c where c.dctmId = '' or c.dctmId is Null order by c.createDate asc";
	@Query(query)
	List<Documents> listDocuments();
}
