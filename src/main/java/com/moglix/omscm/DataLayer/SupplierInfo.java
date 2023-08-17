package com.moglix.omscm.DataLayer;

import lombok.Data;

@Data
public class SupplierInfo {

	public SupplierInfo(int supplierId, String supplierUniqueId, int shipmentMode, String supplierPincode) {
		// TODO Auto-generated constructor stub
		this.supplierId = supplierId;
		this.supplierUniqueId = supplierUniqueId;
		this.shipmentMode = shipmentMode;
		this.supplierPincode = supplierPincode;
	}
	private Integer supplierId;
	private String supplierUniqueId;
	private Integer shipmentMode;
	private String supplierPincode;
	private Double transferPrice;
	
}
