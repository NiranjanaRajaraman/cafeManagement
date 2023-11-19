package com.inn.cafe.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.service.BillService;
import com.inn.cafe.utils.cafeUtils;

@RestController
@RequestMapping(path="/bill")
public class BillController {
	@Autowired
	BillService billService;
	
	@PostMapping(path= "/generateReport")
	ResponseEntity<String> generateReport(@RequestBody Map<String,Object> requestMap){
		try {
			return billService.generateReport(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
