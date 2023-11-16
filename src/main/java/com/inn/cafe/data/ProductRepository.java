package com.inn.cafe.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.inn.cafe.domain.Product;

public interface ProductRepository extends MongoRepository<Product, String> {

	Product findByProductCode(String productCode);

}
