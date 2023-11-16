package com.inn.cafe.data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.inn.cafe.domain.User;
import com.inn.cafe.wrapper.UserWrapper;

public interface UserRepository extends MongoRepository<User,String> {

	User findByEmail(String requestEmailId);
	
	List<User> findByRole(String role);


	 
}
