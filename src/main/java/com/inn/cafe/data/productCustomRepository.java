package com.inn.cafe.data;

import java.util.List;

import com.inn.cafe.domain.Product;

public interface productCustomRepository {
	
	List<Product> getProductByCategory(String categoryCode);

}
