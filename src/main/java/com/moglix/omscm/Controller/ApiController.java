package com.moglix.omscm.Controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.moglix.omscm.BusinessLayer.CmApiInterface;
import com.moglix.omscm.BusinessLayer.LogisticCostEstimationInterface;

import com.moglix.omscm.Model.BaseResponse;
import com.moglix.omscm.Model.CmPredRequest;

@RestController
@RequestMapping("oms")
@CrossOrigin(origins = "*")
public class ApiController {

	Logger logger = LoggerFactory.getLogger(ApiController.class);
	
	@Autowired
	public CmApiInterface cmApiInterface;
	
	@Autowired
	public LogisticCostEstimationInterface logisticCostEstimationInterface;
	
	@RequestMapping(value="predict/cm", method = RequestMethod.POST)
	public BaseResponse getCmPrediction(@RequestBody CmPredRequest request) {
		return cmApiInterface.getCmPred(request);
	}
	
	
	@RequestMapping(value="product/logistic/cost", method = RequestMethod.POST)
	public BaseResponse getLogisticCost(@RequestBody CmPredRequest request) {
		return logisticCostEstimationInterface.populateLogisticCost(request);
	}
	
}
