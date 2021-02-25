package id.co.bfi.dmsuploadscheduler.service.dctm;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;

import id.co.bfi.dmsuploadscheduler.config.feign.FeignConfig;
import id.co.bfi.dmsuploadscheduler.config.yaml.YamlPropertySourceFactory;
import feign.Body;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

@PropertySource(value = "file:conf/dctm-rest.yml", factory = YamlPropertySourceFactory.class)
@FeignClient(name = "dctmRestClient", url = "${dctm-rest.url}/repositories/${dctm-rest.repositoryName}", configuration = FeignConfig.class)
public interface DctmRestClient {
	
	// test semua method
	@RequestLine("GET {url}")
	ResponseEntity<String> makeRequest(@Param(value="url") String url);
	
	@RequestLine("GET ?dql={dql}")
	ResponseEntity<String> getDataFromDql(@Param(value="dql") String dql);
	
	@RequestLine("PUT {url}")
	ResponseEntity<String> checkoutDocument(@Param(value="url") String url);
	
	@RequestLine("DELETE {url}")
	ResponseEntity<String> cancelCheckoutDocument(@Param(value="url") String url);
	
	@RequestLine("POST {url}")
	@Body("{properties}")
	@Headers("Content-Type: application/vnd.emc.documentum+json")
	ResponseEntity<String> createObject(@Param(value="url") String url, @Param(value="properties") String properties);
	
}
