package com.olaaref.mintshop.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.dao.CountryRepository;

@RestController
@RequestMapping("/countries")
public class CountryRestController {

	@Autowired
	private CountryRepository countryRepository;
	
	@GetMapping("/list")
	public List<Country> listAll(){
		return countryRepository.findAllByOrderByNameAsc();
	}
	
	@PostMapping("/save")
	public String svaeCountry(@RequestBody Country country) {
		Country savedCountry = countryRepository.save(country);
		return String.valueOf(savedCountry.getId());
	}
	
	@DeleteMapping("/delete/{id}")
	public void deleteCountry(@PathVariable("id") Integer id){
		countryRepository.deleteById(id);
	}
}
