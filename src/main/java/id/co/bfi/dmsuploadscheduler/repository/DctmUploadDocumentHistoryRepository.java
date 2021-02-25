package id.co.bfi.dmsuploadscheduler.repository;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import id.co.bfi.dmsuploadscheduler.config.yaml.YamlPropertySourceFactory;
import id.co.bfi.dmsuploadscheduler.model.DctmUploadDocumentHistory;

@Configuration
@PropertySource(value = "file:conf/query.yml", factory = YamlPropertySourceFactory.class)
public interface DctmUploadDocumentHistoryRepository extends JpaRepository<DctmUploadDocumentHistory, String>{
	DctmUploadDocumentHistory findByDctmUploadDocumentHistoryId(String dctmUploadDocumentHistoryId);
	
	List<DctmUploadDocumentHistory> findByDctmDocumentsId(String dctmDocumentsId);
	
	static final String query = "select u from DctmUploadDocumentHistory u where (u.statusUpload = 1 or u.statusUpload = 3) and "
			+ "(u.dctmId = '' or u.dctmId is Null) and u.dctmDocumentsId = :dctmDocumentsId order by u.createdDate asc";
	
	@Query(query)
	List<DctmUploadDocumentHistory> listUploadDocHistoryByDocId(@Param("dctmDocumentsId") String dctmDocumentsId);
}
