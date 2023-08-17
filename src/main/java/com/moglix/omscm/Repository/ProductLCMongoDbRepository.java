package com.moglix.omscm.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.moglix.omscm.MongoDbModel.ProductLogisticCost;

public interface ProductLCMongoDbRepository extends MongoRepository<ProductLogisticCost, String>{

	public long count();
	
	public ProductLogisticCost findByProductMsnAndShippingPincodeAndQuantity(String productMsn, String pincode, Integer quantity);
}
