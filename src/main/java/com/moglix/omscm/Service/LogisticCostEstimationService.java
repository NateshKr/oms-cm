package com.moglix.omscm.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;

import com.moglix.omscm.BusinessLayer.LogisticCostEstimationInterface;
import com.moglix.omscm.DataLayer.SupplierInfo;
import com.moglix.omscm.Model.BaseResponse;
import com.moglix.omscm.Model.CmPredRequest;
import com.moglix.omscm.Model.HttpResponse;
import com.moglix.omscm.Model.LogisticCostEstimationRequest;
import com.moglix.omscm.MongoDbModel.ProductLogisticCost;
import com.moglix.omscm.Repository.ProductLCMongoDbRepository;
import com.moglix.omscm.Repository.SupplierInfoRepository;
import com.moglix.omscm.Utils.HttpConnectionHelper;
import com.moglix.omscm.properties.ConfigProperties;

@Service
public class LogisticCostEstimationService implements LogisticCostEstimationInterface {
	
	Logger logger = LoggerFactory.getLogger(LogisticCostEstimationService.class);
	
	@Autowired
	private ConfigProperties configProperties;
	
	@Autowired
	private HttpConnectionHelper httpConnectionHelper;
	
	@Autowired
	private SupplierInfoRepository supplierInfoRepository;
	
	@Autowired
	private ProductLCMongoDbRepository productLCMongoDbRepository;

	@Override
	public BaseResponse populateLogisticCost(CmPredRequest request) {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse();
		
		// get least tp seller
		BaseResponse supplierInfoResponse = getSuppliersForMsn(request.getProductMsn());		
		if(supplierInfoResponse.getCode() != 200 || !supplierInfoResponse.getSuccess()) {
			return supplierInfoResponse;
		}
		SupplierInfo supplierInfo = (SupplierInfo)supplierInfoResponse.getData();
		
		// get verified product dimension
		BaseResponse dimensionResponse = getProductDimension(request);
		if(!dimensionResponse.getSuccess()) {
			return dimensionResponse;
		}
		JSONObject dimensionObj = new JSONObject(dimensionResponse.getData());
		
		// get lsp reco shipper
		BaseResponse lspRecoResponse = getLspRecomendation(request, dimensionObj, new JSONObject(supplierInfoResponse.getData()) );
		if(!lspRecoResponse.getSuccess()) {
			return lspRecoResponse;
		}
		JSONObject lspRecoObj = new JSONObject(lspRecoResponse.getData());
		
		// Store data in mongodb
		ProductLogisticCost productCostData = new ProductLogisticCost();
		productCostData.setProductMsn(request.getProductMsn());
		productCostData.setQuantity(request.getQuantity());
		productCostData.setShippingPincode(request.getShippingPincode());
		productCostData.setTransferPrice(supplierInfo.getTransferPrice());
		productCostData.setItemPrice(request.getItemPrice());
		
		try {
			productCostData.setFwdCost(lspRecoObj.getDouble("fwd_cost"));
			productCostData.setCodCost(lspRecoObj.getDouble("cod_cost"));
			productCostData.setRtoCost(lspRecoObj.getDouble("rto_cost"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			response.setMessage(e.getMessage());
			return response;
		}
		
		productLCMongoDbRepository.save(productCostData);
		
		response.setCode(200);
		response.setSuccess(true);
		response.setData(productCostData);
		return response;
	}
	

	public BaseResponse getLspRecomendation(CmPredRequest request, JSONObject dimensionObj, JSONObject supplierInfo) {
		BaseResponse response = new BaseResponse();
		response.setCode(400);	
		
		JSONObject requestObj = new JSONObject();
		try { 
			double invoiceValue = (request.getItemPrice() *request.getQuantity() ) - request.getTotalDiscount();
			requestObj.put("order_id", 123); // dummy val
			requestObj.put("item_id", 123); // dummy val
			requestObj.put("length", dimensionObj.getDouble("length")); 
			requestObj.put("width", dimensionObj.getDouble("width")); 
			requestObj.put("height", dimensionObj.getDouble("height")); 
			requestObj.put("dead_weight", dimensionObj.getDouble("weight")); 
			requestObj.put("source_pincode", (supplierInfo.getInt("shipmentMode") == 1) ? supplierInfo.getInt("shippingPincode") : "110091" ); 
			requestObj.put("destination_pincode", request.getShippingPincode()); 
			requestObj.put("COD", (request.getPaymentMode() == 2)?1:0 ); 
			requestObj.put("invoice_value", invoiceValue); 
			requestObj.put("direction", "FWD"); 
			requestObj.put("is_dropship", (supplierInfo.getInt("shipmentMode") == 1)?1:0 ); 
			requestObj.put("vmi_type", 0); // dummy val 
			
		}
		catch(JSONException e) {
			e.printStackTrace();
			response.setMessage(e.getMessage());
			return response;
		}
		
		String url = configProperties.getSupplierListApi()+"lspapi/get_lsp_chunk";
		
		try {
			HttpResponse httpResponse = httpConnectionHelper.sendPostRequestwithHead(url, null, requestObj, "");
			
			String json = httpResponse.getContent();
			JSONObject obj = new JSONObject(json);
			if(obj.get("status").toString().equalsIgnoreCase("success") && obj.get("best_lsp") != null ) {
				response.setCode(200);
				response.setSuccess(true);
				response.setData(obj.get("best_lsp"));
				return response;
			}
			else {
				response.setMessage(obj.get("message").toString());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return response;		
	}
	
	
	public BaseResponse getProductDimension(CmPredRequest request) {
		BaseResponse response = new BaseResponse();
		
		JSONObject requestObj = new JSONObject();
		try {
			requestObj.put("product_msn", request.getProductMsn());
			requestObj.put("quantity", request.getQuantity());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setMessage(e.getMessage());
			return response;
		}
		
		String url = configProperties.getOmsFeApi()+"api/getProductDimension";
		
		try {
			HttpResponse httpResponse = httpConnectionHelper.sendPostRequestwithHead(url, null, requestObj, "");
			
			String json = httpResponse.getContent();
			JSONObject obj = new JSONObject(json);
			if(obj.getBoolean("status")) {
				response.setCode(200);
				response.setSuccess(true);
				response.setData(obj.get("msnDimension"));
				return response;
			}
			else {
				response.setMessage(obj.get("message").toString());
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}
	
	
	public BaseResponse getSuppliersForMsn(String msn) {
		// TODO Auto-generated method stub
		BaseResponse response = new BaseResponse();
		response.setCode(400);		
		
		JSONArray msnlist = new JSONArray();
		msnlist.put(msn);
		
		JSONObject requestObj = new JSONObject();
		try {
			requestObj.put("msnList", msnlist);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			response.setMessage(e.getMessage());
			return response;
		}
		
		String url = configProperties.getSupplierListApi()+"supplier/product/fetchSupplierList";
		
		try {
			HttpResponse httpResponse = httpConnectionHelper.sendPostRequestwithHead(url, null, requestObj, "");
			
			String json = httpResponse.getContent();
			JSONObject obj = new JSONObject(json);
			
			JSONArray supplierData = obj.getJSONArray("data") ;
			JSONObject supplierList = supplierData.getJSONObject(0).getJSONObject("priceData").getJSONObject("supplierPricing");
			
			logger.debug(supplierList.toString());
			
			Iterator<String> keys = supplierList.keys();
			
			String leastTpSupplier = "";
			double leastTp = 0.0;
			while(keys.hasNext()) {
				String supUniqueId = keys.next();
				
				// check active b2c supplier 
				if( (supplierList.getJSONObject(supUniqueId).getInt("outOfStockFlag") == 0 && 
						supplierList.getJSONObject(supUniqueId).getBoolean("supplierActiveStatus") && 
						supplierList.getJSONObject(supUniqueId).getBoolean("supplierRetailFlag")  ) && 
						(leastTp == 0.0 || supplierList.getJSONObject(supUniqueId).getDouble("retailTp") < leastTp ) ) {
						
					leastTp = supplierList.getJSONObject(supUniqueId).getDouble("retailTp");
					leastTpSupplier = supUniqueId;
					
				}		
			}
			
			SupplierInfo supplierInfo = supplierInfoRepository.getSupplierInfoByUniqueId(leastTpSupplier);
			supplierInfo.setTransferPrice(leastTp);
			response.setMessage("Supplier List found for Msn "+msn);
			response.setData(supplierInfo);
			response.setSuccess(true);
			response.setCode(200);	
			return response;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			response.setMessage(e.getMessage());
			e.printStackTrace();
		}
		return response;
	}

}
