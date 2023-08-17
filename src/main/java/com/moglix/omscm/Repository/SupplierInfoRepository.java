package com.moglix.omscm.Repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.moglix.omscm.DataLayer.SupplierInfo;

@Repository
public class SupplierInfoRepository {

	Logger logger = LoggerFactory.getLogger(SupplierInfoRepository.class);
	
	@Autowired
    @Qualifier ("supplierJdbcTemplate")
    JdbcTemplate supplierJdbcTemplate;
	
	public SupplierInfo getSupplierInfoByUniqueId(String supplierUniqueId) {
		
		String sql = "select s.id as supplierId, s.unique_supplier_id as supplierUniqueId, sbt.shipment_mode as shipmentMode, sa.pincode from suppliers s join supplier_business_types sbt on s.id = sbt.supplier_id and sbt.business_type = 2 "
				+ " supplier_address sa on s.id = sa.supplier_id and sa.is_deleted = 0 and sa.is_default = 1 and sa.type = 4 ";
		
		return (SupplierInfo) supplierJdbcTemplate.query(sql,
				(rs, rowNum) -> new SupplierInfo(Integer.parseInt(rs.getString("supplierId")), rs.getString("supplierUniqueId"), Integer.parseInt(rs.getString("shipmentMode")), rs.getString("pincode")));
		
	}
}
