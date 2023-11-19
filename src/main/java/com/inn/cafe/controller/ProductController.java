package com.inn.cafe.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.cafeUtils;

@RestController
@RequestMapping(path = "/product")
public class ProductController {

	@Autowired
	ProductService productService;

	@PostMapping(path = "/add")
	ResponseEntity<String> addNewProduct(@RequestBody Map<String, String> requestMap) {
		try {
			return productService.addNewProduct(requestMap);

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(path = "/get")
	List<Map<String, String>> getAllCategory() {
		try {
			return productService.getAllProduct();

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ArrayList<>();

	}

	@PostMapping(path = "/update")
	ResponseEntity<String> updateProduct(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return productService.updateProduct(requestMap);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@PostMapping(path = "/delete/{id}")
	ResponseEntity<String> deleteProduct(@PathVariable(required = true) String  id) {
		try {
			return productService.deleteProduct(id);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@PostMapping(path = "/updateStatus")
	ResponseEntity<String> updateProductStatus(@RequestBody(required = true) Map<String, String> requestMap) {
		try {
			return productService.updateProductStatus(requestMap);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}
	
	@GetMapping(path = "/getByCategory")
	List<Map<String, String>> getByCategory(@RequestParam String categoryCode) {
		try {
			return productService.getByCategory(categoryCode);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new ArrayList<>();

	}
	
	@GetMapping(path = "/getProductByCode/{productCode}")
	Map<String, String> getByProductCode(@PathVariable String productCode) {
		try {
			return productService.getByProductCode(productCode);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return new HashMap<>();
	}
	
}
