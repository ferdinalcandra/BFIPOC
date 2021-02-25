package id.co.bfi.dmsuploadscheduler.config.yaml;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "dctm-rest")
@PropertySource(value = "file:conf/dctm-rest.yml", factory = YamlPropertySourceFactory.class)
public class DctmRestConfig {
	private String user;
	private String pass;
	private String url;
	private String repositoryName;
	private String socketTimeout;
	private String connectTimeout;
	private String readTimeout;
	private String connectionRequestTimeout;
	private String specificDocbaseId;
	private String contentType;
	private String consumerDocCabinetName;
	private String internalDocCabinetName;
	private String dmsActiveDocFolderName;
	private String dmsInactiveDocFolderName;
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getRepositoryName() {
		return repositoryName;
	}
	public void setRepositoryName(String repositoryName) {
		this.repositoryName = repositoryName;
	}
	public String getSocketTimeout() {
		return socketTimeout;
	}
	public void setSocketTimeout(String socketTimeout) {
		this.socketTimeout = socketTimeout;
	}
	public String getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(String connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public String getReadTimeout() {
		return readTimeout;
	}
	public void setReadTimeout(String readTimeout) {
		this.readTimeout = readTimeout;
	}
	public String getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}
	public void setConnectionRequestTimeout(String connectionRequestTimeout) {
		this.connectionRequestTimeout = connectionRequestTimeout;
	}
	public String getSpecificDocbaseId() {
		return specificDocbaseId;
	}
	public void setSpecificDocbaseId(String specificDocbaseId) {
		this.specificDocbaseId = specificDocbaseId;
	}
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getConsumerDocCabinetName() {
		return consumerDocCabinetName;
	}
	public void setConsumerDocCabinetName(String consumerDocCabinetName) {
		this.consumerDocCabinetName = consumerDocCabinetName;
	}
	public String getInternalDocCabinetName() {
		return internalDocCabinetName;
	}
	public void setInternalDocCabinetName(String internalDocCabinetName) {
		this.internalDocCabinetName = internalDocCabinetName;
	}
	public String getDmsActiveDocFolderName() {
		return dmsActiveDocFolderName;
	}
	public void setDmsActiveDocFolderName(String dmsActiveDocFolderName) {
		this.dmsActiveDocFolderName = dmsActiveDocFolderName;
	}
	public String getDmsInactiveDocFolderName() {
		return dmsInactiveDocFolderName;
	}
	public void setDmsInactiveDocFolderName(String dmsInactiveDocFolderName) {
		this.dmsInactiveDocFolderName = dmsInactiveDocFolderName;
	}
}
