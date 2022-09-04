package com.olaaref.mintshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.olaaref.mintshop.dao.PersistentTokenDaoImpl;
import com.olaaref.mintshop.security.MintshopUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private MintshopUserDetailsService userDetailsService;
	
	@Autowired
	private PersistentTokenDaoImpl persistentTokenRepository;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/states/list/**", "/cities/list/**").hasAnyAuthority("Admin", "Sales")
				.antMatchers("/css/**", "/js/**", "/img/**", "/fonts/**", "/webfonts/**", "/webjars/**").permitAll()
				.antMatchers("/user/**", "/setting/**", "/countries/**", "/states/**", "/cities/**").hasAuthority("Admin")
				.antMatchers("/brand/**", "/category/**").hasAnyAuthority("Admin", "Editor")
				
				.antMatchers("/product/list", "/product/page/**").hasAnyAuthority("Admin", "Editor", "Sales", "Shipper")
				.antMatchers("/product/details/**", "/customer/detail/**").hasAnyAuthority("Admin", "Editor", "Sales","Assistant")
				.antMatchers("/product/toAdd", "/product/delete/**", "/product/**/enabled/**", "/article/**", "/menu/**", "/section/**").hasAnyAuthority("Admin", "Editor")
				.antMatchers("/product/load/**", "/product/add", "/products/checkUnique").hasAnyAuthority("Admin", "Editor", "Sales")
				.antMatchers("/order/list/**", "/order/page/**", "/order/detail/**").hasAnyAuthority("Admin", "Sales", "Shipper")
				.antMatchers("/shippingRate/**", "/order/**", "/shippingRates/**", "/customer/**", "/reports/**", "/report/**").hasAnyAuthority("Admin", "Sales")
				.antMatchers("/orders/shipper/update/**").hasAnyAuthority("Shipper")
				.antMatchers("/review/**", "/question/**").hasAnyAuthority("Admin", "Assistant")
				.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.permitAll()
			.and()
			.logout()
				.permitAll()
			.and()
			.rememberMe()
				.key("rem-me-key")//to make remember-me cookies survive after restarting server
				.tokenRepository(persistentTokenRepository)
				.tokenValiditySeconds(1*24*60*60)
				.userDetailsService(userDetailsService)
			.and()
			.headers()
				.frameOptions()
					.sameOrigin();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(this.passwordEncoder());
		
		return authenticationProvider;
	}

}
