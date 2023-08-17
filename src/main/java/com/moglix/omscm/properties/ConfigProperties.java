package com.moglix.omscm.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component 
@PropertySource("classpath:config.properties") 
@ConfigurationProperties 
public class ConfigProperties {

	private String supplierListApi;
	private String analyticsApi;
	private String omsFeApi;

	public String getSupplierListApi() {
		return supplierListApi;
	}

	public void setSupplierListApi(String supplierListApi) {
		this.supplierListApi = supplierListApi;
	}

	public String getAnalyticsApi() {
		return analyticsApi;
	}

	public void setAnalyticsApi(String analyticsApi) {
		this.analyticsApi = analyticsApi;
	}

	public String getOmsFeApi() {
		return omsFeApi;
	}

	public void setOmsFeApi(String omsFeApi) {
		this.omsFeApi = omsFeApi;
	}
}
