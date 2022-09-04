package com.olaaref.mintshop.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.olaaref.mintshop.common.entity.ArticleType;
import com.olaaref.mintshop.common.entity.Currency;
import com.olaaref.mintshop.common.entity.MenuType;
import com.olaaref.mintshop.common.entity.order.OrderStatus;
import com.olaaref.mintshop.dao.ArticleRepository;
import com.olaaref.mintshop.dao.BrandRepository;
import com.olaaref.mintshop.dao.CategoryRepository;
import com.olaaref.mintshop.dao.CountryRepository;
import com.olaaref.mintshop.dao.CurrencyRepository;
import com.olaaref.mintshop.dao.CustomerRepository;
import com.olaaref.mintshop.dao.MenuRepository;
import com.olaaref.mintshop.dao.OrderRepository;
import com.olaaref.mintshop.dao.ProductRepository;
import com.olaaref.mintshop.dao.QuestionRepository;
import com.olaaref.mintshop.dao.ReviewRepository;
import com.olaaref.mintshop.dao.SectionRepository;
import com.olaaref.mintshop.dao.SettingRepository;
import com.olaaref.mintshop.dao.ShippingRateRepository;
import com.olaaref.mintshop.dao.StateRepository;
import com.olaaref.mintshop.user.UserRepositoy;

@Service
public class DashboardService {

	@Autowired
	private UserRepositoy userRepositoy;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Autowired
	private CurrencyRepository currencyRepository;
	
	@Autowired
	private CountryRepository countryRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private SettingRepository settingRepository;
	
	@Autowired
	private BrandRepository brandRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private MenuRepository menuRepository;
	
	@Autowired
	private SectionRepository sectionRepository;
	
	@Autowired
	private ArticleRepository articleRepository;
	
	@Autowired
	private ShippingRateRepository shippingRepository;
	
	@Autowired
	private QuestionRepository questionRepository;
	
	@Autowired
	private ReviewRepository reviewRepository;
	
	public long getTotal(String entity) {
		
		long total = 0;
		
		switch (entity) {
		case "User":
			total = userRepositoy.countUsers();
			break;
		case "Category":
			total = categoryRepository.countCategories();
			break;
			
		case "Customer":
			total = customerRepository.countCusomers();
			break;
		
		case "Country":
			total = countryRepository.count();
			break;
			
		case "State":
			total = stateRepository.count();
			break;
			
		case "Brand":
			total = brandRepository.count();
			break;
			
		case "Product":
			total = productRepository.count();
			break;
			
		case "Order":
			total = orderRepository.count();
			break;
			
		case "Menu":
			total = menuRepository.count();
			break;
			
		case "Section":
			total = sectionRepository.count();
			break;
			
		case "Article":
			total = articleRepository.count();
			break;
			
		case "Shipping":
			total = shippingRepository.count();
			break;
			
		case "Question":
			total = questionRepository.count();
			break;
			
		case "Review":
			total = reviewRepository.count();
			break;

		default:
			break;
		}
		
		return total;
	}

	public long getEnabled(String entity) {
		long count = 0;
		
		switch (entity) {
		case "User":
			count = userRepositoy.countEnabledUsers();
			break;
			
		case "Category":
			count = categoryRepository.countEnabledCategories();
			break;
			
		case "Customer":
			count = customerRepository.countEnabledCusomers();
			break;
			
		case "Product":
			count = productRepository.countEnabledProducts();
			break;
			
		case "Menu":
			count = menuRepository.countEnabledMenus();
			break;
			
		case "Section":
			count = sectionRepository.countEnabledSections();
			break;

		default:
			break;
		}
		
		return count;
	}
	
	public long getDisabled(String entity) {
		long count = 0;
		
		switch (entity) {
		case "User":
			count = userRepositoy.countDisabledUsers();
			break;
			
		case "Category":
			count = categoryRepository.countDisabledCategories();
			break;
			
		case "Customer":
			count = customerRepository.countDisabledCustomers();
			break;
			
		case "Product":
			count = productRepository.countDisabledProducts();
			break;
			
		case "Menu":
			count = menuRepository.countDisabledMenus();
			break;
			
		case "Section":
			count = sectionRepository.countDisabledSections();
			break;

		default:
			break;
		}
		
		return count;
	}

	public long getRootCategories() {
		return categoryRepository.countRootCategories();
	}

	public Currency getCurrency(Integer currencyId) {
		Optional<Currency> optionalCurrency = currencyRepository.findById(currencyId);
		if(optionalCurrency.isPresent()) {
			Currency currency = optionalCurrency.get();
			return currency;
		}
		return null;
		
	}
	
	public String getMailServer() {
		return settingRepository.findByKey("MAIL_HOST").getValue();
	}

	public long getInStockProductsCount() {
		return productRepository.countInStockProducts();
	}

	public long getOutOfStockProductsCount() {
		return productRepository.countOutOfStockProducts();
	}
	
	public long countOrdersByOrderStatus(String status) {
		OrderStatus orderStatus = OrderStatus.valueOf(status);
		return orderRepository.countByOrderStatus(orderStatus);
	}
	
	public long countMenusByMenuType(String type) {
		MenuType menuType = MenuType.valueOf(type);
		return menuRepository.countByMenuType(menuType);
	}

	public long countArticlesByArticleType(String articleType) {
		ArticleType type = ArticleType.valueOf(articleType);
		return articleRepository.countByArticleType(type);
	}

	public long countArticlesByPublishedStatus(boolean status) {
		return articleRepository.countByPublished(status);
	}

	public long countCodSupportedShippingRates() {
		return shippingRepository.countByCodSupported(true);
	}

	public long countQuestionsByApprovedStatus(boolean approved) {
		return questionRepository.countByApprovalStatus(approved);
	}

	public long countAnsweredQuestions() {
		return questionRepository.countAnsweredQuestions();
	}
	
	public long countNotAnsweredQuestions() {
		return questionRepository.countNotAnsweredQuestions();
	}

	public long countReviewedProducts() {
		return reviewRepository.countReviewedProducts();
	}

}
