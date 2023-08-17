package com.moglix.omscm.Service;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.moglix.omscm.BusinessLayer.CmApiInterface;
import com.moglix.omscm.Model.BaseResponse;
import com.moglix.omscm.Model.CalculateCMRequest;
import com.moglix.omscm.Model.CmPredRequest;
import com.moglix.omscm.MongoDbModel.ProductLogisticCost;
import com.moglix.omscm.Repository.ProductLCMongoDbRepository;
import com.moglix.omscm.Utils.CalculatedCM;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

import lombok.Data;

@Service
public class CmPredictionService implements CmApiInterface{
	
	Logger logger = LoggerFactory.getLogger(CmPredictionService.class);
	
	@Autowired
	private ProductLCMongoDbRepository productLCMongoDbRepository;
	
	@Autowired
	private LogisticCostEstimationService logisticCostEstimationService;

	@Override
	public BaseResponse getCmPred(CmPredRequest request) {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse();
		
		// validate request data
		String validateRes = this.validateRequest(request);
		if(validateRes != null) {
			response.setMessage(validateRes);
			return response;
		}
		
		// check mongodb for Data on msn-pincode-qty
		long cnt = productLCMongoDbRepository.count();
		
		
		// request data for calculating final cm value		
		CalculateCMRequest calCmRequest = new CalculateCMRequest();
		calCmRequest.setItemPrice(request.getItemPrice());
		calCmRequest.setQuantity(request.getQuantity());
		calCmRequest.setTaxRate(request.getTaxRate());
		
		// optional fields
		calCmRequest.setTotalDiscount((request.getTotalDiscount() == null || request.getTotalDiscount().toString().isEmpty()) ? 0.0: request.getTotalDiscount());
		calCmRequest.setShippingTotal((request.getShippingTotal() == null || request.getShippingTotal().toString().isEmpty()) ? 0.0: request.getShippingTotal() );
		calCmRequest.setPaymentMode((request.getPaymentMode() == null || request.getPaymentMode().toString().isEmpty()) ? 0 : request.getPaymentMode() );
		
		calCmRequest.setOrderTypeFlag( request.isGstInvoice() );
		
		// stored data
		response = getLspCost(request);
		
		if(response.getSuccess() && response.getData() != null  ) {
			ProductLogisticCost productCost = (ProductLogisticCost)response.getData();
			
			calCmRequest.setTransferPrice(productCost.getTransferPrice());
			calCmRequest.setFwdCost(productCost.getFwdCost());
			calCmRequest.setCodCost(productCost.getCodCost());
			calCmRequest.setRtoCost(productCost.getRtoProb());
		}
		else {
			calCmRequest.setTransferPrice(100.0);
			calCmRequest.setFwdCost(10.0);
			calCmRequest.setCodCost(20.0);
			calCmRequest.setRtoCost(0.0);
			
//			return response;
		}
		
		Double cmValue = CalculatedCM.getCmValue(calCmRequest);
				
		request.setCmValue(cmValue);
		
		response.setMessage("success");
		response.setData(request);
		response.setSuccess(true);
		return response;
		
	}
	
	public String validateRequest(CmPredRequest request) {
		if(request.getProductMsn() == null || request.getProductMsn().toString().isEmpty()) {
			return "Product Msn is invalid";
		}
		else if(request.getQuantity() == null || request.getQuantity().toString().isEmpty()){
			return "Invalid Product quantity";
		}
		else if(request.getShippingPincode() == null || request.getShippingPincode().toString().isEmpty()){
			return "Invalid Shipping Pincode";
		}
		else if(request.getItemPrice() == null || request.getItemPrice().toString().isEmpty()){
			return "Invalid Product Price";
		}
		else {		
			return null;
		}
		
	}
	
	public BaseResponse getLspCost(CmPredRequest request) {
		BaseResponse response = new BaseResponse();
		
		ProductLogisticCost productLogisticCostResult = getProductLogisticCostMDB(request);
		if(productLogisticCostResult != null) {
			response.setCode(200);
			response.setSuccess(true);
			response.setData(productLogisticCostResult);
			
		}
		else {
			response = logisticCostEstimationService.populateLogisticCost(request);
		}
		
		return response;		
	}
	
	public ProductLogisticCost getProductLogisticCostMDB(CmPredRequest request) {
		
		ProductLogisticCost productLogisticCostResult = productLCMongoDbRepository.findByProductMsnAndShippingPincodeAndQuantity(request.getProductMsn(), request.getShippingPincode(), request.getQuantity());
		
		return productLogisticCostResult;
		
		// with shard
//		MongoClient mongoClient = MongoClients.create();
//		MongoDatabase database = mongoClient.getDatabase("product_cm_prediction");
//		return null;
	}
	

}
