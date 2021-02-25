package id.co.bfi.dmsuploadscheduler.config.yaml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "query")
@PropertySource(value = "file:conf/query.yml", factory = YamlPropertySourceFactory.class)
public class QueryConfig {
	private String docbaseParamDql;
	private String folderIdDql;
	private String contentTypeDql;
	
	public String getDocbaseParamDql() {
		return docbaseParamDql;
	}

	public void setDocbaseParamDql(String docbaseParamDql) {
		this.docbaseParamDql = docbaseParamDql;
	}

	public String getFolderIdDql() {
		return folderIdDql;
	}

	public void setFolderIdDql(String folderIdDql) {
		this.folderIdDql = folderIdDql;
	}

	public String getContentTypeDql() {
		return contentTypeDql;
	}

	public void setContentTypeDql(String contentTypeDql) {
		this.contentTypeDql = contentTypeDql;
	}
}
