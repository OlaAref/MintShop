package com.olaaref.mintshop.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.olaaref.mintshop.common.entity.Country;
import com.olaaref.mintshop.common.entity.State;
import com.olaaref.mintshop.common.entity.StateDTO;
import com.olaaref.mintshop.repository.StateRepository;


@RestController
@RequestMapping("/settings")
public class StateRestController {
	
	@Autowired
	private StateRepository stateRepository;
	
	@GetMapping("/list-states/{id}")
	public List<StateDTO> listByCountry(@PathVariable("id") Integer id){
		List<State> states = stateRepository.findAllByCountryOrderByNameAsc(new Country(id));
		List<StateDTO> dtoStates = new ArrayList<StateDTO>();
		
		for (State state : states) {
			dtoStates.add(new StateDTO(state.getId(), state.getName()));
		}
		
		return dtoStates;
	}

}
