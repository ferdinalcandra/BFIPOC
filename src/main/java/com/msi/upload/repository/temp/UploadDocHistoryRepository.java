package com.msi.upload.repository.temp;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.msi.upload.model.temp.Documents;
import com.msi.upload.model.temp.UploadDocHistory;

public interface UploadDocHistoryRepository extends JpaRepository<UploadDocHistory, String>{
	UploadDocHistory findByUploadId(String uploadId);
	
	List<UploadDocHistory> findByDocId(String docId);
	
	String query = "select u from UploadDocHistory u where (u.statusUpload = 1 or u.statusUpload = 3) and "
			+ "(u.dctmId = '' or u.dctmId is Null) and u.docId = :docId order by u.createDate asc";
	@Query(query)
	List<UploadDocHistory> listUploadDocHistoryByDocId(@Param("docId") String docId);
}
