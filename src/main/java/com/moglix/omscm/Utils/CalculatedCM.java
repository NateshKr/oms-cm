package com.moglix.omscm.Utils;

import com.moglix.omscm.Model.CalculateCMRequest;

public class CalculatedCM {

	public static Double getCmValue(CalculateCMRequest request) {
		Double taxComponent = 0.0;
//		Double taxPercent = request.getTaxRate()/100.0;
		
		if(request.isOrderTypeFlag()) {
			taxComponent =  request.getTransferPrice() * request.getQuantity() * request.getTaxRate()/100.0 ; 
		}
		else {
			taxComponent = (request.getItemPrice() * request.getQuantity()) * ( 1 - ( 1 / (1+(request.getTaxRate()/100.0)) ) );
		}
		
		taxComponent = Math.round(taxComponent*100.0)/100.0;
		
		Double itemSp = (request.getItemPrice() * request.getQuantity()) + request.getShippingTotal() - (taxComponent + request.getTotalDiscount() );

		Double itemGm = itemSp - (request.getTransferPrice() * request.getQuantity());

		
		Double cmValue = 0.0;
		if(request.getPaymentMode() == 2) { // cod cases
			cmValue = itemGm - (request.getCodCost() + request.getFwdCost() );    
		}
		else {
			Double pgCharge = ((request.getItemPrice() * request.getQuantity()) + request.getShippingTotal() - request.getTotalDiscount() ) 
					* (getPGRates(request.getPaymentMode())/100.0) ;
			
			cmValue = itemGm - (request.getFwdCost() + pgCharge) ;
		}
		
		return cmValue;
	}
	
	
	public static double getPGRates(Integer paymentMode) {
		double rate = 0.0;
		switch(paymentMode) {
			case 0: 
				rate = 2.4 ;
				break;
			case 1:
				rate = 2;
				break;
			case 2:
				rate = 0;
				break;
			case 3:
				rate = 0;
				break;
			case 4:
				rate = 2;
				break;
			case 5:
				rate = 2;
				break;
			case 6:
				rate = 2;
				break;
			default:
				rate = 0;
		}
		
		return rate;
	}
}
