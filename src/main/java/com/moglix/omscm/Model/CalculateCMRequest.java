package com.moglix.omscm.Model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CalculateCMRequest {
	
	private Integer quantity;
	private Double itemPrice;
	private Double taxRate;
	private Double totalDiscount;
	private Double shippingTotal;
	private Double transferPrice;
	private Double fwdCost;
	private Double codCost;
	private Double rtoCost;
	private Integer paymentMode;
	private boolean orderTypeFlag;
	
}
