package com.inn.cafe.serviceimpl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.inn.cafe.data.BillRepository;
import com.inn.cafe.data.CategoryRepository;
import com.inn.cafe.data.ProductRepository;
import com.inn.cafe.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {
	
	@Autowired
	CategoryRepository categoryRepo;
	
	@Autowired
	ProductRepository productRepo;
	
	@Autowired
	BillRepository billRepo;

	@Override
	public ResponseEntity<Map<String, Object>> getCount() {
		try {
			Map<String,Object > map = new HashMap<>();
			map.put("category", categoryRepo.count());
			map.put("product", productRepo.count());
			map.put("bill", billRepo.count());
			
			return new ResponseEntity<>(map,HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		
		return new ResponseEntity<>(new HashMap<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
