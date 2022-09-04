package com.olaaref.mintshop.checkout;

import java.text.DecimalFormat;
import java.time.LocalDate;

public class CheckoutInfo {
	
	private float productCost;
	private float productTotal;
	private float shippingCostTotal;
	private float paymentTotal;
	private int deliverDays;
	private LocalDate deliverDate;
	private boolean codSupported;
	
	public CheckoutInfo() {
		super();
	}
	
	
	public float getProductCost() {
		return productCost;
	}
	
	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}
	
	public float getProductTotal() {
		return productTotal;
	}
	
	public void setProductTotal(float productTotal) {
		this.productTotal = productTotal;
	}
	
	public float getShippingCostTotal() {
		return shippingCostTotal;
	}
	
	public void setShippingCostTotal(float shippingCostTotal) {
		this.shippingCostTotal = shippingCostTotal;
	}
	
	public float getPaymentTotal() {
		return paymentTotal;
	}
	
	public void setPaymentTotal(float paymentTotal) {
		this.paymentTotal = paymentTotal;
	}
	
	public int getDeliverDays() {
		return deliverDays;
	}
	
	public void setDeliverDays(int deliverDays) {
		this.deliverDays = deliverDays;
	}
	
	public LocalDate getDeliverDate() {
		LocalDate date = LocalDate.now().plusDays(deliverDays);
		return date;
	}
	
	public void setDeliverDate(LocalDate deliverDate) {
		this.deliverDate = deliverDate;
	}
	
	public boolean isCodSupported() {
		return codSupported;
	}
	
	public void setCodSupported(boolean codSupported) {
		this.codSupported = codSupported;
	}
	
	public String getPaypalPaymentTotal() {
		DecimalFormat formatter = new DecimalFormat("##.##");
		return formatter.format(paymentTotal);
	}
	

}
