package com.inn.cafe.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.domain.Bill;
import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.service.BillService;
import com.inn.cafe.utils.cafeUtils;

@RestController
@RequestMapping(path = "/bill")
public class BillController {
	@Autowired
	BillService billService;

	@PostMapping(path = "/generateReport")
	ResponseEntity<String> generateReport(@RequestBody Map<String, Object> requestMap) {
		try {
			return billService.generateReport(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping(path = "/getBills")
	ResponseEntity<List<Bill>> getBills() {
		try {
			return billService.getBills();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@PostMapping(path = "/getPdf")
	ResponseEntity<byte[]> getPdf(@RequestBody Map<String, Object> requestMap) {
		try {
			return billService.getPdf(requestMap);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@PostMapping(path = "/deleteBill/{id}")
	ResponseEntity<String> deleteBill(@PathVariable String id) {
		try {
			return billService.deleteBill(id);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}
}
