package com.inn.cafe.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.inn.cafe.JWT.Category;

public interface CategoryRepository extends MongoRepository<Category, String> {
	
	List<Category> findAll();

	Category findByCategoryCode(String categoryCode);

}
