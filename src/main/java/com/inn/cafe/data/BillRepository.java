package com.inn.cafe.data;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.inn.cafe.domain.Bill;

public interface BillRepository extends MongoRepository<Bill, String> {

}
