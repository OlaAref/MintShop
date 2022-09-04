package com.olaaref.mintshop.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.City;
import com.olaaref.mintshop.common.entity.CityDTO;
import com.olaaref.mintshop.common.entity.State;
import com.olaaref.mintshop.repository.CityRepository;

@RestController
@RequestMapping("/settings")
public class CityRestController {

	@Autowired
	private CityRepository cityRepository;
	
	@GetMapping("/list/{id}")
	public List<CityDTO> listByState(@PathVariable("id") Integer id){
		List<City> cities = cityRepository.findAllByStateOrderByNameAsc(new State(id));
		List<CityDTO> dtoCities = new ArrayList<CityDTO>();
		
		for (City city : cities) {
			dtoCities.add(new CityDTO(city.getId(), city.getName()));
		}
		
		return dtoCities;
	}
	
	@GetMapping("/listCities/{state}")
	public List<CityDTO> listByState(@PathVariable("state") String stateName){
		List<City> cities = cityRepository.findAllByStateNameOrderByNameAsc(stateName);
		List<CityDTO> dtoCities = new ArrayList<CityDTO>();
		
		for (City city : cities) {
			dtoCities.add(new CityDTO(city.getId(), city.getName()));
		}
		
		return dtoCities;
	}
	
}
