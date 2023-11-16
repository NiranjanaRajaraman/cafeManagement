package com.inn.cafe.serviceimpl;

import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.JWT.Category;
import com.inn.cafe.JWT.JWTFilter;
import com.inn.cafe.JWT.JWTUtil;
import com.inn.cafe.data.CategoryRepository;
import com.inn.cafe.data.ProductRepository;
import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.domain.Product;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.cafeUtils;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	JWTFilter jwtFitlter;

	private static Logger LOGGER = LoggerFactory.getLogger(ProductServiceImpl.class);
	Category category = null;

	@Override
	public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
		try {
			
			if (jwtFitlter.isAdmin()) {
				if (validateProductMap(requestMap, false)) {
					Category categoryFromDb = categoryRepository.findByCategoryCode(requestMap.get("categoryId"));
					if (Objects.nonNull(categoryFromDb)) {
						category = new Category(categoryFromDb);
					} else {
						return cafeUtils.getResponseEntity("Category code doesn't exist", HttpStatus.OK);
					}

					productRepository.save(getProductFromMap(requestMap, true));
					return cafeUtils.getResponseEntity("Product Added Successfully!", HttpStatus.OK);
				}
				return cafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			} else {
				return cafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {

		Product product = new Product();
		if (isAdd) {
			String ProductCode = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
			while (productRepository.findByProductCode(ProductCode) != null) {
				ProductCode = RandomStringUtils.randomAlphanumeric(8).toUpperCase();
			}
			product.setProductCode(ProductCode);
			product.setStatus("true");
		}
		if (!isAdd) {
			product.setStatus(requestMap.get("status"));
		}

		product.setCategory(category);
		product.setName(requestMap.get("name"));
		product.setDescription(requestMap.get("description"));
		product.setPrice(Float.parseFloat(requestMap.get("price")));
		return product;
	}

	private Boolean validateProductMap(Map<String, String> requestMap, Boolean isAdd) {
		if (requestMap.containsKey("name") && requestMap.containsKey("categoryId")) {
			if (isAdd) {
				return true;
			} else if (!isAdd) {
				return true;
			}
		}
		return false;

	}

}
