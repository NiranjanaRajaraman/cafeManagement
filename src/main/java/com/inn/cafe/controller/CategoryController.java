package com.inn.cafe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.JWT.Category;
import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.service.CategoryService;
import com.inn.cafe.utils.cafeUtils;

@RestController
@RequestMapping(path="/category")
public class CategoryController {
	
	@Autowired 
	CategoryService categoryService;
	
	@PostMapping(path = "/add")
	ResponseEntity<String> addCategory(@RequestBody (required= true) Map<String,String> requestMap){
		try {
			return categoryService.addCategory(requestMap);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@GetMapping(path = "/get")
	ResponseEntity<List<Category>> getAllCategory(@RequestParam (required= false) String filterValue){
		try {
			return categoryService.getAllCategory(filterValue);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	
	@PostMapping(path = "/update")
	ResponseEntity<String> updateCategory(@RequestBody (required= true) Map<String,String> requestMap){
		try {
			return categoryService.updateCategory(requestMap);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		
	}
	

}
