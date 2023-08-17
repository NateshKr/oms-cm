package com.moglix.omscm.MongoDbModel;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("product_logistic_cost")
public class ProductLogisticCost {

	@Id
    private String id;
	
	private String productMsn;
	private String shippingPincode;
	private double itemPrice;
	private Integer quantity;
	private double transferPrice;
	private double fwdCost;
	private double codCost;
	private double rtoCost;
	
	public ProductLogisticCost(String id, String productMsn, String shippingPincode, double itemPrice, Integer quantity,
			double transferPrice, double fwdCost, double codCost, double rtoCost, double rtoProb) {
		super();
		this.id = id;
		this.productMsn = productMsn;
		this.shippingPincode = shippingPincode;
		this.itemPrice = itemPrice;
		this.quantity = quantity;
		this.transferPrice = transferPrice;
		this.fwdCost = fwdCost;
		this.codCost = codCost;
		this.rtoCost = rtoCost;
		this.rtoProb = rtoProb;
	}
	
	public ProductLogisticCost() {
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductMsn() {
		return productMsn;
	}
	public void setProductMsn(String productMsn) {
		this.productMsn = productMsn;
	}
	public String getShippingPincode() {
		return shippingPincode;
	}
	public void setShippingPincode(String shippingPincode) {
		this.shippingPincode = shippingPincode;
	}
	public double getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public double getTransferPrice() {
		return transferPrice;
	}
	public void setTransferPrice(double transferPrice) {
		this.transferPrice = transferPrice;
	}
	public double getFwdCost() {
		return fwdCost;
	}
	public void setFwdCost(double fwdCost) {
		this.fwdCost = fwdCost;
	}
	public double getCodCost() {
		return codCost;
	}
	public void setCodCost(double codCost) {
		this.codCost = codCost;
	}
	public double getRtoCost() {
		return rtoCost;
	}
	public void setRtoCost(double rtoCost) {
		this.rtoCost = rtoCost;
	}
	public double getRtoProb() {
		return rtoProb;
	}
	public void setRtoProb(double rtoProb) {
		this.rtoProb = rtoProb;
	}
	private double rtoProb;
	
}
