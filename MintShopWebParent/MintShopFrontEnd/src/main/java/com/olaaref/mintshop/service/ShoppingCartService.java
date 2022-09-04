package com.olaaref.mintshop.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.CartItem;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.exception.ShoppingCartException;
import com.olaaref.mintshop.repository.CartItemRepository;
import com.olaaref.mintshop.repository.ProductRepository;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	public Integer addProductToCart(Integer productId, Integer quantity, Customer customer) throws ShoppingCartException {
		Integer updatedQuantity = quantity;
		
		Product product = new Product(productId);
		CartItem cartItem = cartItemRepository.findByCustomerAndProduct(customer, product);
		//if the product is in the cart before
		if(cartItem != null) {
			updatedQuantity = cartItem.getQuantity() + quantity;
			if(updatedQuantity > 5) {
				throw new ShoppingCartException("Could not add more " + quantity + " item(s) because there is already " 
						+ cartItem.getQuantity() + " item(s) in your shopping cart. Maximum items allowed is 5.");
			}
		}
		//if the product is not in the cart before
		else {
			cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setCustomer(customer);
			
		}
		
		cartItem.setQuantity(updatedQuantity);
		
		cartItemRepository.save(cartItem);
		
		return updatedQuantity;
	}
	
	public List<CartItem> listCartItems(Customer customer){
		return cartItemRepository.findByCustomer(customer);
	}
	
	public float updateQuantity(Integer productId, Integer quantity, Customer customer) {
		cartItemRepository.updateQuantity(quantity, customer.getId(), productId);
		Product product = productRepository.findById(productId).get();
		float subTotal = product.getDiscountedPrice() * quantity;
		return subTotal;
	}
	
	public void removeProduct(Integer productId, Customer customer) {
		cartItemRepository.deleteByCustomerAndProduct(customer.getId(), productId);
	}
	
	public void deleteByCustomer(Customer customer) {
		cartItemRepository.deleteByCustomer(customer.getId());
	}
}
