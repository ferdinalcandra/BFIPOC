package id.co.bfi.dmsuploadscheduler.config.feign;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import feign.Contract;
import feign.auth.BasicAuthRequestInterceptor;
import id.co.bfi.dmsuploadscheduler.config.yaml.DctmRestConfig;
import id.co.bfi.dmsuploadscheduler.config.yaml.JasyptConfig;

@Configuration
public class FeignConfig {
	
	@Autowired
	DctmRestConfig dctmRestConfig;
	
	@Autowired
	JasyptConfig jasyptConfig;
	
	@Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor(dctmRestConfig.getUser(), jasyptConfig.decryptPassword(dctmRestConfig.getPass()));
    }
    
}
