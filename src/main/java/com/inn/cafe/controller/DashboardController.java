package com.inn.cafe.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.service.DashboardService;

@RestController
@RequestMapping(path = "/dashboard")
public class DashboardController {

	@Autowired
	DashboardService dashboardService;

	@GetMapping(path = "/details")
	ResponseEntity<Map<String, Object>> getCount() {
		try {
			return dashboardService.getCount();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new HashMap<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
