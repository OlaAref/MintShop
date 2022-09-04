package com.olaaref.mintshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.olaaref.mintshop.checkout.CheckoutInfo;
import com.olaaref.mintshop.common.entity.CartItem;
import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.common.entity.product.Product;

@Service
public class CheckoutService {
	
	private static final int DIM_DIVISOR = 5000;
	
	public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
		CheckoutInfo checkoutInfo = new CheckoutInfo();
		
		float productCost = calculateProductCost(cartItems);
		float productCostTotal = calculateProductCostTotal(cartItems);
		float shippingCostTotal = calculateShippingCost(cartItems, shippingRate);
		float paymentTotal = productCostTotal + shippingCostTotal;
		
		checkoutInfo.setProductCost(productCost);
		checkoutInfo.setProductTotal(productCostTotal);
		checkoutInfo.setDeliverDays(shippingRate.getDays());
		checkoutInfo.setCodSupported(shippingRate.isCodSupported());
		checkoutInfo.setShippingCostTotal(shippingCostTotal);
		checkoutInfo.setPaymentTotal(paymentTotal);
		
		return checkoutInfo;
	}

	private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
		float shippingCostTotal = 0.0f;
		
		for (CartItem item : cartItems) {
			Product product = item.getProduct();
			//DIM Weight = (length * width * height) / DIM DIVISOR
			float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
			//shipping weight
			float finalWeight = product.getWeight() > dimWeight ? product.getWeight() : dimWeight;
			//calculate shipping cost for each item
			float shippingCost = finalWeight * item.getQuantity() * shippingRate.getRate();
			
			//set the shipping cost to the item
			item.setShippingCost(shippingCost);
			
			shippingCostTotal += shippingCost;
					
		}
		
		return shippingCostTotal;
	}

	private float calculateProductCostTotal(List<CartItem> cartItems) {
		float total = 0.0f;
		
		for (CartItem item : cartItems) {
			total += item.getSubTotal();
		}
		
		return total;
	}

	private float calculateProductCost(List<CartItem> cartItems) {
		
		float cost = 0.0f;
		
		for (CartItem item : cartItems) {
			cost += item.getQuantity() * item.getProduct().getCost();
		}
		
		return cost;
	}

}
