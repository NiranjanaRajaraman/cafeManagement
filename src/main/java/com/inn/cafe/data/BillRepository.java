package com.inn.cafe.data;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.inn.cafe.domain.Bill;

public interface BillRepository extends MongoRepository<Bill, String> {

	List<Bill> findByCreatedBy(String currentUser);

	Bill findByUuid(String id);

	void deleteByUuid(String id);

}
