package com.olaaref.mintshop.controller;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.olaaref.mintshop.common.entity.Currency;
import com.olaaref.mintshop.security.MintshopUserDetails;
import com.olaaref.mintshop.service.DashboardService;
import com.olaaref.mintshop.service.SettingService;
import com.olaaref.mintshop.setting.GeneralSettingBag;


@Controller
public class MainController {
	
	@Autowired
	private DashboardService dashService;
	
	@Autowired
	private SettingService settingService;
	
	@GetMapping("/")
	public String viewMainPage(Model model, @AuthenticationPrincipal MintshopUserDetails loggedUser) {
		sendDataToIndexPage(loggedUser, model);
		return "index";
	}

	@GetMapping({ "/login" })
	public String viewLoginPage() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null
				|| authentication instanceof org.springframework.security.authentication.AnonymousAuthenticationToken)
			return "login";
		return "redirect:/";
	}
	
	private void sendDataToIndexPage(MintshopUserDetails loggedUser, Model model) {
		model.addAttribute("date", LocalDate.now());
		model.addAttribute("time", LocalTime.now());
		
		boolean isAdmin = loggedUser.hasRole("Admin");
		boolean isSales = loggedUser.hasRole("Sales");
		boolean isEditor = loggedUser.hasRole("Editor");
		boolean isShipper = loggedUser.hasRole("Shipper");
		boolean isAssistant = loggedUser.hasRole("Assistant");
		
		if(isAdmin) {
			sendUsersDataToIndexPage(model);
			sendGeneralInformationsDataToIndexPage(model);
			sendSettingsDataToIndexPage(model);
		}
		if(isAdmin || isEditor) {
			sendCategoriesDataToIndexPage(model);
			sendBrandsDataToIndexPage(model);
			sendArticlesDataToIndexPage(model);
			sendMenusDataToIndexPage(model);
			sendSectionsDataToIndexPage(model);
		}
		if(isAdmin || isSales) {
			sendCustomersDataToIndexPage(model);
			sendShippingDataToIndexPage(model);
		}
		if(isAdmin || isAssistant) {
			sendQuestionsDataToIndexPage(model);
			sendReviewsDataToIndexPage(model);
		}
		if(isAdmin || isSales || isShipper) {
			sendOrdersDataToIndexPage(model);
		}
		if(isAdmin || isEditor || isSales || isShipper){
			sendProductsDataToIndexPage(model);
		}
		
	}

	private void sendReviewsDataToIndexPage(Model model) {
		model.addAttribute("totalReviews", dashService.getTotal("Review"));
		model.addAttribute("reviewedProducts", dashService.countReviewedProducts());
		
	}

	private void sendQuestionsDataToIndexPage(Model model) {
		model.addAttribute("totalQuestions", dashService.getTotal("Question"));
		model.addAttribute("approvedQuestions", dashService.countQuestionsByApprovedStatus(true));
		model.addAttribute("disapprovedQuestions", dashService.countQuestionsByApprovedStatus(false));
		model.addAttribute("answeredQuestions", dashService.countAnsweredQuestions());
		model.addAttribute("unansweredQuestions", dashService.countNotAnsweredQuestions());
		
	}

	private void sendShippingDataToIndexPage(Model model) {
		model.addAttribute("codSupported", dashService.countCodSupportedShippingRates());
		model.addAttribute("totalShippingRates", dashService.getTotal("Shipping"));
		
	}

	private void sendArticlesDataToIndexPage(Model model) {
		model.addAttribute("totalArticles", dashService.getTotal("Article"));
		model.addAttribute("menuBoundArticles", dashService.countArticlesByArticleType("MENU_BOUND"));
		model.addAttribute("freeArticles", dashService.countArticlesByArticleType("FREE"));
		model.addAttribute("publishedArticles", dashService.countArticlesByPublishedStatus(true));
		model.addAttribute("unpublishedArticles", dashService.countArticlesByPublishedStatus(false));
		
	}

	private void sendSectionsDataToIndexPage(Model model) {
		model.addAttribute("totalSections", dashService.getTotal("Section"));
		model.addAttribute("enabledSections", dashService.getEnabled("Section"));
		model.addAttribute("disabledSections", dashService.getDisabled("Section"));
		
	}

	private void sendMenusDataToIndexPage(Model model) {
		model.addAttribute("totalMenus", dashService.getTotal("Menu"));
		model.addAttribute("enabledMenus", dashService.getEnabled("Menu"));
		model.addAttribute("disabledMenus", dashService.getDisabled("Menu"));
		model.addAttribute("headerMenus", dashService.countMenusByMenuType("HEADER"));
		model.addAttribute("footerMenus", dashService.countMenusByMenuType("FOOTER"));
		
	}

	private void sendOrdersDataToIndexPage(Model model) {
		model.addAttribute("totalOrders", dashService.getTotal("Order"));
		model.addAttribute("newOrders", dashService.countOrdersByOrderStatus("NEW"));
		model.addAttribute("canceledOrders", dashService.countOrdersByOrderStatus("CANCELED"));
		model.addAttribute("processingOrders", dashService.countOrdersByOrderStatus("PROCESSING"));
		model.addAttribute("shippingOrders", dashService.countOrdersByOrderStatus("SHIPPING"));
		model.addAttribute("deliveredOrders", dashService.countOrdersByOrderStatus("DELIVERED"));
		
	}

	private void sendProductsDataToIndexPage(Model model) {
		model.addAttribute("totalProducts", dashService.getTotal("Product"));
		model.addAttribute("enabledProducts", dashService.getEnabled("Product"));
		model.addAttribute("disabledProducts", dashService.getDisabled("Product"));
		model.addAttribute("inStockProducts", dashService.getInStockProductsCount());
		model.addAttribute("outOfStockProducts", dashService.getOutOfStockProductsCount());
		
	}

	private void sendBrandsDataToIndexPage(Model model) {
		model.addAttribute("totalBrands", dashService.getTotal("Brand"));
		
	}

	private void sendSettingsDataToIndexPage(Model model) {
		model.addAttribute("totalCountries", dashService.getTotal("Country"));
		model.addAttribute("totalStates", dashService.getTotal("State"));
		model.addAttribute("mailServer", dashService.getMailServer());
		
	}

	private void sendGeneralInformationsDataToIndexPage(Model model) {
		GeneralSettingBag generalSettings = settingService.getGeneralSettings();
		String currencyId = generalSettings.get("CURRENCY_ID").getValue();
		Currency currency = dashService.getCurrency(Integer.valueOf(Integer.parseInt(currencyId)));
		model.addAttribute("currencyName", currency.getName());
		model.addAttribute("currencySymbol", currency.getSymbol());
		model.addAttribute("DecimalPointType", generalSettings.get("DECIMAL_POINT_TYPE").getValue());
		model.addAttribute("DecimalDigit", generalSettings.get("DECIMAL_DIGITS").getValue());
		model.addAttribute("ThousandsPointType", generalSettings.get("THOUSANDS_POINT_TYPE").getValue());
		
	}

	private void sendCustomersDataToIndexPage(Model model) {
		model.addAttribute("totalCustomers", dashService.getTotal("Customer"));
		model.addAttribute("enabledCustomers", dashService.getEnabled("Customer"));
		model.addAttribute("disabledCustomers", dashService.getDisabled("Customer"));
		
	}

	private void sendCategoriesDataToIndexPage(Model model) {
		model.addAttribute("totalCategories", dashService.getTotal("Category"));
		model.addAttribute("enabledCategories", dashService.getEnabled("Category"));
		model.addAttribute("disabledCategories", dashService.getDisabled("Category"));
		model.addAttribute("rootCategories", dashService.getRootCategories());
		
	}

	private void sendUsersDataToIndexPage(Model model) {
		model.addAttribute("totalUsers", dashService.getTotal("User"));
		model.addAttribute("enabledUsers", dashService.getEnabled("User"));
		model.addAttribute("disabledUsers", dashService.getDisabled("User"));
		
	}
}
