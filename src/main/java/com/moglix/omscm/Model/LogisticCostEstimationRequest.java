package com.moglix.omscm.Model;

import lombok.Data;

@Data
public class LogisticCostEstimationRequest {

	private String productMsn;
	private Integer quantity;
	private String shippingPincode;
	private Double itemPrice;
	private Double transferPrice;
}
