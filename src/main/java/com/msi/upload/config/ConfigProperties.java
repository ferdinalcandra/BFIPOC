package com.msi.upload.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class ConfigProperties {
	public Properties getConfigProperties(String propName) {
		Properties prop = new Properties();
	    InputStream inputStream = ConfigProperties.class.getClassLoader().getResourceAsStream("/"+propName+".properties");
	    try {
	    	prop.load(inputStream);
	    } catch (IOException e) {
	    	e.printStackTrace();
	    } 
	    return prop;
	}
}
