package com.inn.cafe.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.inn.cafe.JWT.Category;
import com.inn.cafe.JWT.JWTFilter;
import com.inn.cafe.JWT.JWTUtil;
import com.inn.cafe.data.CategoryRepository;
import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.service.CategoryService;
import com.inn.cafe.utils.cafeUtils;

@Service
public class CategoryServiceImpl implements  CategoryService{
	
	@Autowired
	CategoryRepository categoryRepository;
	
	@Autowired
	JWTUtil jwtUtil;

	@Autowired
	JWTFilter jwtFitlter;

	private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);


	@Override
	public ResponseEntity<String> addCategory(Map<String, String> requestMap) {
		try {
			if(jwtFitlter.isAdmin()) {
				if(validateCategoryMap(requestMap,false)) {
					categoryRepository.save(getCategoryFromMap(requestMap,true));
					return cafeUtils.getResponseEntity("cetegory Added Successfully!", HttpStatus.OK);
				}
			}
			else {
				return cafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
				
			}
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}


	private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
		if(requestMap.containsKey("name")) {
			if(validateId) {
				return true;
			}else if(!validateId) {
				return true;
			}
			
		}
		return false;
	}
		private Category getCategoryFromMap(Map<String,String> requestMap, Boolean isAdd) {
			Category category = new Category();
			if(isAdd) {
				String categoryCode= RandomStringUtils.randomAlphanumeric(8).toUpperCase();
				while (categoryRepository.findByCategoryCode(categoryCode) != null) {
					categoryCode= RandomStringUtils.randomAlphanumeric(8).toUpperCase();
				}
				category.setCategoryCode(categoryCode);
			}
			category.setName(requestMap.get("name"));
			
			return category;
			
		}


		@Override
		public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
			try {
				if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")) {
					return new ResponseEntity<List<Category>>(categoryRepository.findAll(),HttpStatus.OK);
				}
				return new ResponseEntity<List<Category>>(categoryRepository.findAll(),HttpStatus.OK);
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
		}


		@Override
		public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
			try {
				if(jwtFitlter.isAdmin()) {
					if(validateCategoryMap(requestMap, false)) {
					Category categoryObj=	categoryRepository.findByCategoryCode(requestMap.get("code"));
					if(Objects.nonNull(categoryObj)) {
						categoryObj.setName(requestMap.get("name"));
						categoryRepository.save(categoryObj);
						return cafeUtils.getResponseEntity("cetegory updated Successfully!", HttpStatus.OK);
					}
					else {
						return cafeUtils.getResponseEntity("Category code doesn't exist", HttpStatus.OK);
					}
					}
					cafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}
				else {
					return cafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
				}
				
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		}
}
