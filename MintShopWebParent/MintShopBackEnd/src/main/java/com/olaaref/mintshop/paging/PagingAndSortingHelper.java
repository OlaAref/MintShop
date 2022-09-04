package com.olaaref.mintshop.paging;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.ModelAndViewContainer;

public class PagingAndSortingHelper {

	private ModelAndViewContainer model;
	private String listName;
	private String sortField;
	private String sortDir;
	private String keyword;
	
	
	
	public PagingAndSortingHelper(ModelAndViewContainer model, String listName, String sortField,
			String sortDir, String keyword) {
		super();
		this.model = model;
		this.listName = listName;
		this.sortField = sortField;
		this.sortDir = sortDir;
		this.keyword = keyword;
	}

	 public void listEntities(int pageNum, int pageSize, EntityPagingRepository<?, Integer> repository) {
		
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		Pageable pageable = PageRequest.of(pageNum -1, pageSize, sort);
		
		Page<?> page = null;
		
		if(keyword != null) {
			page = repository.findAll(keyword, pageable);
		}
		else {
			page = repository.findAll(pageable);
		}
		
		updateModelAttributes(pageNum, page); 
	}

	public void updateModelAttributes(int pageNum, Page<?> page) {
		
		int pageSize = page.getSize();
		
		long start = (pageNum - 1) * pageSize + 1;
		long end = start + pageSize - 1;
		if(end > page.getTotalElements()) end = page.getTotalElements();

		model.addAttribute(listName, page.getContent());
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("startCount", start);
		model.addAttribute("endCount", end);
		model.addAttribute("totalElements", page.getTotalElements());
		model.addAttribute("totalPages", page.getTotalPages());
		
		
	}
	
	public Pageable createPageable(int pageNum, int productPerPage) {
		Sort sort = Sort.by(sortField);
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		return PageRequest.of(pageNum - 1, productPerPage, sort);
	}


	public String getSortField() {
		return sortField;
	}



	public String getSortDir() {
		return sortDir;
	}



	public String getKeyword() {
		return keyword;
	}



	
	
	
}
