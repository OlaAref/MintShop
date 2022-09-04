package com.olaaref.mintshop.common.entity.order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.olaaref.mintshop.common.entity.Category;
import com.olaaref.mintshop.common.entity.IdBasedEntity;
import com.olaaref.mintshop.common.entity.product.Product;

@Entity
@Table(name = "order_details")
public class OrderDetail extends IdBasedEntity {

	@Column(name = "quantity", nullable = false, length = 11)
	private int quantity;
	
	@Column(name = "shipping_cost", nullable = false)
	private float shippingCost;
	
	@Column(name = "product_cost", nullable = false)
	private float productCost;
	
	@Column(name = "unit_price", nullable = false)
	private float unitPrice;
	
	@Column(name = "subtotal", nullable = false)
	private float subTotal;
	
	@ManyToOne()
	@JoinColumn(name = "product_id")
	private Product product;
	
	@ManyToOne()
	@JoinColumn(name = "order_id")
	private Order order;

	public OrderDetail() {
		super();
	}

	public OrderDetail(Integer id) {
		super();
		this.id = id;
	}

	public Product getProduct() {
		return product;
	}

	public OrderDetail(String categoryName, int quantity, float shippingCost, float productCost, float subTotal) {
		this.product = new Product();
		this.product.setCategory(new Category(categoryName));
		this.quantity = quantity;
		this.shippingCost = shippingCost;
		this.productCost = productCost * quantity;
		this.subTotal = subTotal;
	}
	
	public OrderDetail(int quantity, String productName, float shippingCost, float productCost, float subTotal) {
		this.product = new Product(productName);
		this.quantity = quantity;
		this.shippingCost = shippingCost;
		this.productCost = productCost * quantity;
		this.subTotal = subTotal;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public float getShippingCost() {
		return shippingCost;
	}

	public void setShippingCost(float shippingCost) {
		this.shippingCost = shippingCost;
	}

	public float getProductCost() {
		return productCost;
	}

	public void setProductCost(float productCost) {
		this.productCost = productCost;
	}

	public float getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(float unitPrice) {
		this.unitPrice = unitPrice;
	}

	public float getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(float subTotal) {
		this.subTotal = subTotal;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "\nOrderDetail [id=" + id + ", product=" + product.getName() + ", quantity=" + quantity + ", shippingCost="
				+ shippingCost + ", productCost=" + productCost + ", unitPrice=" + unitPrice + ", subTotal=" + subTotal
				+ ", order=" + order.getOrderTime() + "]";
	}
	
	
}
