package com.olaaref.mintshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.olaaref.mintshop.oauth.CustomerOAuth2UserService;
import com.olaaref.mintshop.oauth.DatabaseLoginSuccessHandler;
import com.olaaref.mintshop.oauth.OAuth2LoginSuccessHandler;
import com.olaaref.mintshop.repository.PersistentTokenDaoImpl;
import com.olaaref.mintshop.security.CustomerUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private CustomerUserDetailsService customerUserDetailsService;
	
	@Autowired
	private PersistentTokenDaoImpl persistentTokenDaoImpl;
	
	@Autowired
	private CustomerOAuth2UserService oAuth2UserService;
	
	@Autowired
	private OAuth2LoginSuccessHandler auth2LoginSuccessHandler;
	
	@Autowired
	private DatabaseLoginSuccessHandler databaseLoginSuccessHandler;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(this.authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/customer/details", "/customer/updateAccountDetails", "/address/**", "/checkout/**", "/order/**").authenticated()
				.antMatchers("/review/ratings/**", "/question/all/**").permitAll()
				.antMatchers("/review/**", "/question/**").authenticated()
				
				.anyRequest().permitAll()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.successHandler(databaseLoginSuccessHandler)
				.permitAll()
			.and()
			.oauth2Login()
				.loginPage("/login")
				.userInfoEndpoint()
				.userService(oAuth2UserService)
			.and()
				.successHandler(auth2LoginSuccessHandler)
			.and()
			.logout()
				.permitAll()
			.and()
			.rememberMe()
				.key("remember-me-key")
				.tokenRepository(persistentTokenDaoImpl)
				.tokenValiditySeconds(1*24*60*60)
				.userDetailsService(customerUserDetailsService)
			.and()
			.sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS);
			
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/css/**", "/js/**", "/fonts/**", "/img/**");
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		
		authenticationProvider.setUserDetailsService(customerUserDetailsService);
		authenticationProvider.setPasswordEncoder(this.passwordEncoder());
		
		return authenticationProvider;
	}
	
}
