package com.inn.cafe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.cafeUtils;

@RestController
@RequestMapping(path = "/product")
public class ProductController {
	
	@Autowired
	ProductService productService;
	
	@PostMapping(path= "/add")
	ResponseEntity<String> addNewProduct(@RequestBody Map<String,String> requestMap){
		try {
			return productService.addNewProduct(requestMap);
			
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
