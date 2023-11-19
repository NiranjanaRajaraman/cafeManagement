package com.inn.cafe.service;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

public interface ProductService {

	ResponseEntity<String> addNewProduct(Map<String, String> requestMap);

	List<Map<String, String>> getAllProduct();

	ResponseEntity<String> updateProduct(Map<String, String> requestMap);

	ResponseEntity<String> deleteProduct(String id);

	ResponseEntity<String> updateProductStatus(Map<String, String> requestMap);

	List<Map<String, String>> getByCategory(String categoryCode);

	Map<String, String> getByProductCode(String productCode);

}
