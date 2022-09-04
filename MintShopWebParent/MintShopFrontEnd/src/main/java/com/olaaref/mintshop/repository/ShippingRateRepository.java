package com.olaaref.mintshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.ShippingRate;

@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {

	public ShippingRate findByCountryAndState(Country country, String state);
}
