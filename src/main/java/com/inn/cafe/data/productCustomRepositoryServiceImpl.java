package com.inn.cafe.data;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.inn.cafe.domain.Product;
import com.mongodb.client.MongoClient;

public class productCustomRepositoryServiceImpl implements productCustomRepository {

	@Autowired
	MongoClient client;

	@Autowired
	MongoTemplate mongoTemplate;

	@Autowired
	MongoConverter converter;

	@Override
	public List<Product> getProductByCategory(String categoryCode) {

		Query query = new Query();
		query.addCriteria(Criteria.where("category.categoryCode").is(categoryCode));
		List<Product> products = mongoTemplate.find(query, Product.class);

		return products;
	}

}
