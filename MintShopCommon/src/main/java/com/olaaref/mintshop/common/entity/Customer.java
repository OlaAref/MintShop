package com.olaaref.mintshop.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name = "customers")
public class Customer extends AbstractAddress {
	
	@Column(name = "email", nullable = false, length = 45, unique = true)
	private String email;
	
	@Column(name = "password", nullable = false, length = 64)
	private String password;

	@ManyToOne
	@JoinColumn(name = "country_id")
	private Country country;
	
	@Column(name = "enabled", nullable = false)
	private boolean enabled;
	
	@Column(name = "verification_code", nullable = true, length = 64)
	private String verificationCode;
	
	@Column(name = "reset_password_token", length = 30)
	private String resetPasswordToken;
	
	@CreationTimestamp
	@Column(name = "created_time", nullable = false)
	private LocalDateTime createdTime;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "authentication_type", length = 10)
	private AuthenticationType authenticationType;

	public Customer() {
		super();
	}

	public Customer(Integer id) {
		super();
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public String getResetPasswordToken() {
		return resetPasswordToken;
	}

	public void setResetPasswordToken(String resetPasswordToken) {
		this.resetPasswordToken = resetPasswordToken;
	}

	public LocalDateTime getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(LocalDateTime createdTime) {
		this.createdTime = createdTime;
	}

	public AuthenticationType getAuthenticationType() {
		return authenticationType;
	}

	public void setAuthenticationType(AuthenticationType authenticationType) {
		this.authenticationType = authenticationType;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
	@Transient
	public String getFullName() {
		return this.firstName + " " + this.lastName;
	}
	
	@Transient
	public String getAddress() {
		String address = firstName;
		if(lastName != null && !lastName.isEmpty()) address += " " + lastName;
		if(!addressLine1.isEmpty()) address += ", " + addressLine1;
		if(addressLine2 != null && !addressLine2.isEmpty()) address += ", " + addressLine2;
		if(!city.isEmpty()) address += ", " + city;
		if(state != null && !state.isEmpty()) address += ", " + state;
		address += ", " + country.getName();
		if(!postalCode.isEmpty()) address += ".\n Postal Code: " + postalCode;
		if(!phoneNumber.isEmpty()) address += ".\n Phone Number: " + phoneNumber;
		
		return address;
	}
}
