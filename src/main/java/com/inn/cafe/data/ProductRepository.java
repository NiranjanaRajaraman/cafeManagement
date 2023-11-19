package com.inn.cafe.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.inn.cafe.domain.Product;

public interface ProductRepository extends MongoRepository<Product, String>, productCustomRepository  {

	Product findByProductCode(String productCode);

	void deleteByProductCode(String productCode);

}
