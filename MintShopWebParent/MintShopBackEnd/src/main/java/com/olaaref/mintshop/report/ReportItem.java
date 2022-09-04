package com.olaaref.mintshop.report;

import java.util.Objects;

public class ReportItem {
	private String identifier;
	private float grossSales;//total of sales
	private float netSales;//profit
	private int orderCounts;
	private int productCounts;
	
	public ReportItem() {
		
	}

	public ReportItem(String identifier) {
		super();
		this.identifier = identifier;
	}

	public ReportItem(String identifier, float grossSales, float netSales) {
		super();
		this.identifier = identifier;
		this.grossSales = grossSales;
		this.netSales = netSales;
	}

	public ReportItem(String identifier, float grossSales, float netSales, int productCounts) {
		super();
		this.identifier = identifier;
		this.grossSales = grossSales;
		this.netSales = netSales;
		this.productCounts = productCounts;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public float getGrossSales() {
		return grossSales;
	}

	public void setGrossSales(float grossSales) {
		this.grossSales = grossSales;
	}

	public float getNetSales() {
		return netSales;
	}

	public void setNetSales(float netSales) {
		this.netSales = netSales;
	}

	public int getOrderCounts() {
		return orderCounts;
	}

	public void setOrderCounts(int orderCounts) {
		this.orderCounts = orderCounts;
	}

	public int getProductCounts() {
		return productCounts;
	}

	public void setProductCounts(int productCounts) {
		this.productCounts = productCounts;
	}

	@Override
	public int hashCode() {
		return Objects.hash(identifier);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ReportItem other = (ReportItem) obj;
		return Objects.equals(identifier, other.identifier);
	}

	@Override
	public String toString() {
		return "ReportItem [identifier=" + identifier + ", grossSales=" + grossSales + ", netSales=" + netSales
				+ ", orderCounts=" + orderCounts + "]";
	}

	public void addGrossSales(float amount) {
		this.grossSales += amount;
	}
	
	public void addNetSales(float amount) {
		this.netSales += amount;
	}
	
	public void increaseOrdersCount() {
		this.orderCounts++;
	}
	
	public void increaseProductCounts(int productCount) {
		this.productCounts += productCount;
	}
	
	
}
