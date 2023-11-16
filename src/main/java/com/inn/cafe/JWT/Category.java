package com.inn.cafe.JWT;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Category {

	@Id
	private String _id;
	
	private String name;
	
	private String categoryCode;

	public Category(Category categoryFromDb) {
		this.categoryCode= categoryFromDb.categoryCode;
		this.name= categoryFromDb.name;
	}

	public Category() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}
	
}
