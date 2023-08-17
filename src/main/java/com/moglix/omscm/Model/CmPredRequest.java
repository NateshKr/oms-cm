package com.moglix.omscm.Model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@ToString
public class CmPredRequest {

	private String productMsn;
	
	private Integer quantity;
	
	private String shippingPincode;
	
	private Double itemPrice;
	
	private Double taxRate;
	
	private Double totalDiscount;
	
	private Integer paymentMode;
	
	private Double shippingTotal;
	
	private boolean isGstInvoice;
	
	private Double cmValue;
	
	
}
