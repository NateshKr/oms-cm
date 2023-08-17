package com.moglix.omscm;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DataSourceConfig {

	@Bean
	@Qualifier("omsDataSource")
	@Primary
	@ConfigurationProperties(prefix="spring.datasource")
	DataSource omsDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@Qualifier("omsJdbcTemplate")
	JdbcTemplate omsJdbcTemplate(@Qualifier("omsDataSource")DataSource omsDataSource) {
		return new JdbcTemplate(omsDataSource);
	}
	
	@Bean
	@Qualifier("omsSlaveDataSource")
	@ConfigurationProperties(prefix="omsslave.datasource")
	DataSource omsSlaveDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@Qualifier("omsSlaveJdbcTemplate")
	JdbcTemplate omsSlaveJdbcTemplate(@Qualifier("omsSlaveDataSource")DataSource omsSlaveDataSource) {
		return new JdbcTemplate(omsSlaveDataSource);
	}
	
	@Bean
	@Qualifier("supplierDataSource")
	@ConfigurationProperties(prefix="supplier.datasource")
	DataSource supplierDataSource() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	@Qualifier("supplierJdbcTemplate")
	JdbcTemplate supplierJdbcTemplate(@Qualifier("supplierDataSource")DataSource supplierDataSource) {
		return new JdbcTemplate(supplierDataSource);
	}
}
