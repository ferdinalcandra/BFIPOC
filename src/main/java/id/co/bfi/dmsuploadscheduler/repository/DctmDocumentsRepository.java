package id.co.bfi.dmsuploadscheduler.repository;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import id.co.bfi.dmsuploadscheduler.config.yaml.YamlPropertySourceFactory;
import id.co.bfi.dmsuploadscheduler.model.DctmDocuments;

@Configuration
@PropertySource(value = "file:conf/query.yml", factory = YamlPropertySourceFactory.class)
public interface DctmDocumentsRepository extends JpaRepository<DctmDocuments, String>{
	DctmDocuments findByDctmDocumentsId(String dctmDocumentsId);
	
	static final String query = "select c from DctmDocuments c where c.dctmId = '' or c.dctmId is Null order by c.createdDate asc";
	
	@Query(query)
	List<DctmDocuments> listDocuments();
}
