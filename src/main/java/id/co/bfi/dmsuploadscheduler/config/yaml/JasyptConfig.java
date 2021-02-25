package id.co.bfi.dmsuploadscheduler.config.yaml;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "jasypt.encryptor")
@PropertySource(value = "file:conf/application.yml", factory = YamlPropertySourceFactory.class)
public class JasyptConfig {
	
	private String algorithm;
	
	private String password;
	
	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String decryptPassword(String encryptedPass) {
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setPassword(getPassword());
		encryptor.setAlgorithm(getAlgorithm());
		return encryptor.decrypt(encryptedPass);
	}
}
