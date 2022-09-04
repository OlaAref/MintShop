package com.olaaref.mintshop.controller;

import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.olaaref.mintshop.checkout.CheckoutInfo;
import com.olaaref.mintshop.checkout.paypal.PaypalApiException;
import com.olaaref.mintshop.common.entity.Address;
import com.olaaref.mintshop.common.entity.CartItem;
import com.olaaref.mintshop.common.entity.Customer;
import com.olaaref.mintshop.common.entity.ShippingRate;
import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.common.entity.order.PaymentMethod;
import com.olaaref.mintshop.mail.Utility;
import com.olaaref.mintshop.service.AddressService;
import com.olaaref.mintshop.service.CheckoutService;
import com.olaaref.mintshop.service.OrderService;
import com.olaaref.mintshop.service.PaypalService;
import com.olaaref.mintshop.service.SettingService;
import com.olaaref.mintshop.service.ShippingRateService;
import com.olaaref.mintshop.service.ShoppingCartService;
import com.olaaref.mintshop.setting.EmailSettingBag;
import com.olaaref.mintshop.setting.PaymentSettingBag;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {
	
	@Autowired
	private CheckoutService checkoutService;
	@Autowired
	private HelperController helper;
	@Autowired
	private AddressService addressService;
	@Autowired
	private ShippingRateService shippingRateService;
	@Autowired
	private ShoppingCartService shoppingCartService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private SettingService settingService;
	@Autowired
	private PaypalService paypalService;
	
	@GetMapping("/checkoutForm")
	public String showCheckoutForm(HttpServletRequest request, Model model) {
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		
		if(defaultAddress != null) {
			shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
			model.addAttribute("shippingAddress", defaultAddress.toString());
		}
		else {
			shippingRate = shippingRateService.getShippingRateForCustomer(customer);
			model.addAttribute("shippingAddress", customer.getAddress());
		}
		
		if(shippingRate == null) {
			return "redirect:/cart/list";
		}
		
		List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
		CheckoutInfo checkout = checkoutService.prepareCheckout(cartItems, shippingRate);
		String currencyCode = settingService.getCurrencyCode();
		
		PaymentSettingBag paymentSettings = settingService.getPaymentSettings();
		String paypalClientId = paymentSettings.getClientID();
		
		model.addAttribute("checkout", checkout);
		model.addAttribute("cartItems", cartItems);
		model.addAttribute("currencyCode", currencyCode);
		model.addAttribute("customer", customer);
		model.addAttribute("paypalClientId", paypalClientId);
		
		return "checkout/checkout-form";
	}
	
	
	@PostMapping("/placeOrder")
	public String placeOrder(HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
		String paymentparam = request.getParameter("paymentMethod");
		PaymentMethod paymentMethod = PaymentMethod.valueOf(paymentparam);
		
		Customer customer = helper.getAuthenticatedCustomer(request);
		
		Address defaultAddress = addressService.getDefaultAddress(customer);
		ShippingRate shippingRate = null;
		
		if(defaultAddress != null) {
			shippingRate = shippingRateService.getShippingRateForAddress(defaultAddress);
		}
		else {
			shippingRate = shippingRateService.getShippingRateForCustomer(customer);
		}
		
		List<CartItem> cartItems = shoppingCartService.listCartItems(customer);
		CheckoutInfo checkout = checkoutService.prepareCheckout(cartItems, shippingRate);
		
		Order createdOrder = orderService.createOrder(customer, defaultAddress, cartItems, checkout, paymentMethod);
		
		//empty the cart
		shoppingCartService.deleteByCustomer(customer);
		
		//send confirmation email
		sendOrderConfirmationEmail(request, createdOrder);
		
		return "checkout/order-completed";
	}

	private void sendOrderConfirmationEmail(HttpServletRequest request, Order createdOrder) throws UnsupportedEncodingException, MessagingException {
		EmailSettingBag emailSettings = settingService.getEmailSettings();
		JavaMailSenderImpl mailSender = (JavaMailSenderImpl) Utility.prepareMailSender(emailSettings);
		mailSender.setDefaultEncoding("utf-8");
		
		String toAddress = createdOrder.getCustomer().getEmail();
		String subject = emailSettings.getOrderConfirmationSubject();
		String content = emailSettings.getOrderConfirmationContent();
		
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper msgHelper = new MimeMessageHelper(mimeMessage);
		
		msgHelper.setFrom(emailSettings.getFromAddress(), emailSettings.getSenderName());
		msgHelper.setTo(toAddress);
		
		//edit subject, set order id
		subject = subject.replace("[[orderId]]", String.valueOf(createdOrder.getId()));
		
		msgHelper.setSubject(subject);
		
		//edit content
		//name, orderId, orderTime, shippingAddress, total, paymentMethod
		content = content.replace("[[orderId]]", String.valueOf(createdOrder.getId()));
		String name = "<b>" + createdOrder.getCustomer().getFullName() + "</b>";
		content = content.replace("[[name]]", name);
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("HH:mm:ss E, dd MM yyyy");
		String orderDate = createdOrder.getOrderTime().format(dateFormatter);
		content = content.replace("[[orderTime]]", orderDate);
		String formattedTotal = Utility.formatCurrency(createdOrder.getTotal(), settingService.getCurrencySettings());
		content = content.replace("[[total]]", formattedTotal);
		content = content.replace("[[paymentMethod]]", createdOrder.getPaymentMethod().toString());
		content = content.replace("[[shippingAddress]]", createdOrder.getShippingAddress());
		
		msgHelper.setText(content, true);
		
		mailSender.send(mimeMessage);
	}
	
	@PostMapping("/processPaypalOrder")
	public String processPaypalOrder(HttpServletRequest request, Model model) throws UnsupportedEncodingException, MessagingException {
		
		String orderId = request.getParameter("orderId");
		
		String messageTitle = null;
		String message = null;
		
		try {
			
			if(paypalService.validateOrder(orderId)) {
				return placeOrder(request);
			}
			else {
				messageTitle = "Checkout Failure";
				message = "ERROR : Transaction could not be completed because order information is invalid.";
			}
			
		} 
		catch (PaypalApiException e) {
			messageTitle = "Checkout Failure";
			message = "ERROR : Transaction failed due to error : " + e.getMessage();
		}
		
		model.addAttribute("messageTitle", messageTitle);
		model.addAttribute("message", message);
		
		return "message";
	}

}



















