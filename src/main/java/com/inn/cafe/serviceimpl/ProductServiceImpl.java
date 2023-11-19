package com.inn.cafe.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.JWT.Category;
import com.inn.cafe.JWT.JWTFilter;
import com.inn.cafe.JWT.JWTUtil;
import com.inn.cafe.data.CategoryRepository;
import com.inn.cafe.data.ProductRepository;
import com.inn.cafe.data.productCustomRepository;
import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.domain.Product;
import com.inn.cafe.service.ProductService;
import com.inn.cafe.utils.cafeUtils;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	ProductRepository productRepository;

	@Autowired
	productCustomRepository productCustom;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	MongoTemplate mongoTemplate;

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
				if (validateAddProductMap(requestMap, false)) {
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

	private Boolean validateAddProductMap(Map<String, String> requestMap, Boolean isAdd) {
		if (requestMap.containsKey("name") && requestMap.containsKey("categoryId")) {
			if (isAdd) {
				return true;
			} else if (!isAdd) {
				return true;
			}
		}
		return false;

	}

	private Boolean validateUpdateProductMap(Map<String, String> requestMap, Boolean isAdd) {
		if (requestMap.containsKey("productId")) {
			if (isAdd) {
				return true;
			} else if (!isAdd) {
				return true;
			}
		}
		return false;

	}

	@Override
	public List<Map<String, String>> getAllProduct() {
		try {
			List<Product> productList = productRepository.findAll();
			List<Map<String, String>> mapResponse = new ArrayList<>();
			productList.stream().forEach(obj -> {
				Map<String, String> map = new HashMap<>();
				map.put("productCode", obj.getProductCode());
				map.put("name", obj.getName());
				map.put("description", obj.getDescription());
				map.put("price", obj.getPrice().toString());
				map.put("status", obj.getStatus());
				map.put("categoryId", obj.getCategory().getCategoryCode());
				map.put("categoryName", obj.getCategory().getName());
				mapResponse.add(map);
			});
			return mapResponse;

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ArrayList<>();
	}

	@Override
	public ResponseEntity<String> updateProduct(Map<String, String> requestMap) {
		try {
			if (jwtFitlter.isAdmin()) {
				if (validateUpdateProductMap(requestMap, false)) {
					Product productObj = productRepository.findByProductCode(requestMap.get("productId"));
					if (Objects.nonNull(productObj)) {
						if (requestMap.containsKey("price")) {
							productObj.setPrice(Float.parseFloat(requestMap.get("price")));
						}
						if (requestMap.containsKey("name")) {
							productObj.setName(requestMap.get("name"));
						}
						if (requestMap.containsKey("description")) {
							productObj.setDescription(requestMap.get("description"));
						}
						if (requestMap.containsKey("status")) {
							productObj.setStatus(requestMap.get("status"));

						}
						if (requestMap.containsKey("categoryId")) {
							Category categoryFromDb = categoryRepository
									.findByCategoryCode(requestMap.get("categoryId"));
							if (Objects.nonNull(categoryFromDb)) {
								category = new Category(categoryFromDb);
							} else {
								return cafeUtils.getResponseEntity("Category code doesn't exist", HttpStatus.OK);
							}
							productObj.setCategory(category);
						}
						productRepository.save(productObj);
						return cafeUtils.getResponseEntity("Product updated Successfully", HttpStatus.OK);
					} else {
						return cafeUtils.getResponseEntity("Product code doesn't exist", HttpStatus.OK);
					}
				} else {
					return cafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}

			} else {
				return cafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> deleteProduct(String id) {
		try {
			if (jwtFitlter.isAdmin()) {
				Product productObj = productRepository.findByProductCode(id);
				if (Objects.nonNull(productObj)) {
					productRepository.deleteByProductCode(id);
					return cafeUtils.getResponseEntity("Product deleted Successfully", HttpStatus.OK);
				} else {
					return cafeUtils.getResponseEntity("Product code doesn't exist", HttpStatus.OK);
				}
			} else {
				return cafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> updateProductStatus(Map<String, String> requestMap) {
		try {
			if (jwtFitlter.isAdmin()) {
				if (requestMap.containsKey("status") && requestMap.containsKey("productCode")) {
					Product productObj = productRepository.findByProductCode(requestMap.get("productCode"));
					if (Objects.nonNull(productObj)) {
						productObj.setStatus(requestMap.get("status"));
						productRepository.save(productObj);
						return cafeUtils.getResponseEntity("Product status updated Successfully", HttpStatus.OK);
					} else {
						return cafeUtils.getResponseEntity("Product code doesn't exist", HttpStatus.OK);
					}
				} else {
					return cafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
				}

			} else {
				return cafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public List<Map<String, String>> getByCategory(String categoryCode) {
		try {
			List<Product> productFromDb = getProductByCategory(categoryCode);
			if (Objects.nonNull(productFromDb)) {

				List<Map<String, String>> mapResponse = new ArrayList<>();
				productFromDb.stream().forEach(obj -> {
					Map<String, String> map = new HashMap<>();
					map.put("productCode", obj.getProductCode());
					map.put("name", obj.getName());
					map.put("description", obj.getDescription());
					map.put("price", obj.getPrice().toString());
					map.put("status", obj.getStatus());
					map.put("categoryId", obj.getCategory().getCategoryCode());
					map.put("categoryName", obj.getCategory().getName());
					mapResponse.add(map);
				});
				return mapResponse;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	private List<Product> getProductByCategory(String categoryCode) {
		Query query = new Query();
		query.addCriteria(Criteria.where("category.categoryCode").is(categoryCode));
		List<Product> products = mongoTemplate.find(query, Product.class);
		return products;
	}

	@Override
	public Map<String, String> getByProductCode(String productCode) {
		try {

			Product Obj = productRepository.findByProductCode(productCode);
			Map<String, String> map = new HashMap<>();
			if (Objects.nonNull(Obj)) {

				map.put("productCode", Obj.getProductCode());
				map.put("name", Obj.getName());
				map.put("description", Obj.getDescription());
				map.put("price", Obj.getPrice().toString());
				map.put("status", Obj.getStatus());
				return map;
			} else {
				map.put("response", "product code not found");
				return map;
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new HashMap<>();
	}

}
