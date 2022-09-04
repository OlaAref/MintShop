package com.olaaref.mintshop.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.olaaref.mintshop.common.entity.order.Order;
import com.olaaref.mintshop.common.entity.order.OrderDetail;
import com.olaaref.mintshop.common.entity.order.OrderStatus;
import com.olaaref.mintshop.common.entity.order.OrderTrack;
import com.olaaref.mintshop.common.entity.product.Product;
import com.olaaref.mintshop.common.entity.setting.Setting;
import com.olaaref.mintshop.common.exception.OrderNotFoundException;
import com.olaaref.mintshop.paging.PagingAndSortingHelper;
import com.olaaref.mintshop.paging.PagingAndSortingParam;
import com.olaaref.mintshop.security.MintshopUserDetails;
import com.olaaref.mintshop.service.OrderService;
import com.olaaref.mintshop.service.SettingService;

@Controller
@RequestMapping({ "/order" })
public class OrderController {
	private String redirectToListUrl = "redirect:/order/page/1?sortField=orderTime&sortDir=desc";

	@Autowired
	private OrderService orderService;

	@Autowired
	private SettingService settingService;

	@GetMapping({ "/list" })
	public String listAll() {
		return this.redirectToListUrl;
	}

	@GetMapping({ "/page/{pageNum}" })
	public String listByPage(
			@PagingAndSortingParam(listName = "orders", moduleUrl = "/order") PagingAndSortingHelper helper,
			@PathVariable("pageNum") int pageNum, HttpServletRequest request,
			@AuthenticationPrincipal MintshopUserDetails loggedUser) {
		this.orderService.listByPage(pageNum, helper);
		loadCurrencySeting(request);
		if (loggedUser.hasRole("Shipper") && !loggedUser.hasRole("Admin") && !loggedUser.hasRole("Sales"))
			return "orders/list-orders-shipper";
		return "orders/list-orders";
	}

	private void loadCurrencySeting(HttpServletRequest request) {
		List<Setting> currencySettings = this.settingService.getCurrencySettings();
		for (Setting setting : currencySettings)
			request.setAttribute(setting.getKey(), setting.getValue());
	}

	@PostMapping({ "/save" })
	public String saveOrder(@ModelAttribute("order") Order order, HttpServletRequest request,
			RedirectAttributes redirectAttributes) {
		String countryName = request.getParameter("countryName");
		order.setCountry(countryName);
		updateProductDetails(order, request);
		updateOrderTracks(order, request);
		this.orderService.save(order);
		redirectAttributes.addFlashAttribute("message", order.getId());
		return this.redirectToListUrl;
	}

	private void updateOrderTracks(Order order, HttpServletRequest request) {
		String[] trackIds = request.getParameterValues("trackId");
		String[] trackDates = request.getParameterValues("trackDate");
		String[] trackStatuses = request.getParameterValues("trackStatus");
		String[] trackNotes = request.getParameterValues("trackNotes");
		List<OrderTrack> orderTracks = order.getOrderTracks();
		for (int i = 0; i < trackIds.length; i++) {
			System.out.println("trackIds " + trackIds[i]);
			System.out.println("\t trackDates " + trackDates[i]);
			System.out.println("\t trackStatuses " + trackStatuses[i]);
			System.out.println("\t trackNotes " + trackNotes[i]);
			Integer trackId = Integer.valueOf(Integer.parseInt(trackIds[i]));
			OrderTrack track = new OrderTrack();
			if (trackId.intValue() > 0)
				track.setId(trackId);
			track.setOrder(order);
			track.setNotes(trackNotes[i]);
			track.setStatus(OrderStatus.valueOf(trackStatuses[i]));
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
			LocalDateTime trackDate = LocalDateTime.parse(trackDates[i], formatter);
			track.setUpdatedTime(trackDate);
			System.out.println("\t track Date after format " + trackDate);
			orderTracks.add(track);
		}
	}

	private void updateProductDetails(Order order, HttpServletRequest request) {
		String[] detailIds = request.getParameterValues("detailId");
		String[] productIds = request.getParameterValues("productId");
		String[] productDetailCosts = request.getParameterValues("productDetailCost");
		String[] quantities = request.getParameterValues("quantity");
		String[] productPrices = request.getParameterValues("productPrice");
		String[] productSubtotals = request.getParameterValues("productSubtotal");
		String[] productShipCosts = request.getParameterValues("productShipCost");
		Set<OrderDetail> orderDetails = order.getOrderDetails();
		for (int i = 0; i < detailIds.length; i++) {
			System.out.println("detailIds " + detailIds[i]);
			System.out.println("\t productIds " + productIds[i]);
			System.out.println("\t productCosts " + productDetailCosts[i]);
			System.out.println("\t quantities " + quantities[i]);
			System.out.println("\t productPrices " + productPrices[i]);
			System.out.println("\t productSubtotals " + productSubtotals[i]);
			System.out.println("\t productShipCosts " + productShipCosts[i]);
			Integer detailId = Integer.valueOf(Integer.parseInt(detailIds[i]));
			Integer productId = Integer.valueOf(Integer.parseInt(productIds[i]));
			OrderDetail detail = new OrderDetail();
			if (detailId.intValue() > 0)
				detail.setId(detailId);
			detail.setOrder(order);
			detail.setProduct(new Product(productId));
			detail.setProductCost(Float.parseFloat(productDetailCosts[i]));
			detail.setQuantity(Integer.parseInt(quantities[i]));
			detail.setShippingCost(Float.parseFloat(productShipCosts[i]));
			detail.setSubTotal(Float.parseFloat(productSubtotals[i]));
			detail.setUnitPrice(Float.parseFloat(productPrices[i]));
			orderDetails.add(detail);
		}
	}

	@GetMapping({ "/detail/{id}" })
	public String viewOrderDetails(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes,
			HttpServletRequest request, @AuthenticationPrincipal MintshopUserDetails loggedUser) {
		try {
			Order order = this.orderService.getById(id);
			loadCurrencySeting(request);
			boolean isAdminOrSales = false;
			if (loggedUser.hasRole("Admin") || loggedUser.hasRole("Sales"))
				isAdminOrSales = true;
			model.addAttribute("isAdminOrSales", Boolean.valueOf(isAdminOrSales));
			model.addAttribute("order", order);
			return "orders/order-detail-modal";
		} catch (OrderNotFoundException e) {
			redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
			return this.redirectToListUrl;
		}
	}

	@GetMapping({ "/edit/{id}" })
	public String loadOrderDetails(@PathVariable("id") Integer id, Model model) {
		try {
			Order order = this.orderService.getById(id);
			model.addAttribute("order", order);
			model.addAttribute("pageTitle", "Edit");
			model.addAttribute("countries", this.orderService.listAllCountries());
			return "orders/order-form";
		} catch (OrderNotFoundException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return this.redirectToListUrl;
		}
	}

	@GetMapping({ "/delete/{id}" })
	public String deleteOrder(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
		try {
			this.orderService.deleteOrder(id);
			redirectAttributes.addFlashAttribute("deleteMessag", "The Order has been deleted successfully.");
		} catch (OrderNotFoundException e) {
			redirectAttributes.addFlashAttribute("deleteErrorMessag", e.getMessage());
		}
		return this.redirectToListUrl;
	}
}
