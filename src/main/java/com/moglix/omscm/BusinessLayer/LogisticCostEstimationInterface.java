package com.moglix.omscm.BusinessLayer;

import com.moglix.omscm.Model.BaseResponse;
import com.moglix.omscm.Model.CmPredRequest;

public interface LogisticCostEstimationInterface {

	public BaseResponse populateLogisticCost(CmPredRequest request);
}
