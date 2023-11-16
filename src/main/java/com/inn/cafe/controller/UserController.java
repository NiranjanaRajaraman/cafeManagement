package com.inn.cafe.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.cafeUtils;
import com.inn.cafe.wrapper.UserWrapper;


@RestController
@RequestMapping(path="/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping(path= "/signUp")
	public ResponseEntity<String> SignUp(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.signUp(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path= "/login")
	public ResponseEntity<String> login(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.login(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path= "/get")
	public ResponseEntity<List<UserWrapper>> getAllUser(){
		try {
			return userService.getAllUser();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return new ResponseEntity<List<UserWrapper>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path= "/update")
	public ResponseEntity<String> update(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.update(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@GetMapping(path ="/checkToken")
	public ResponseEntity<String> checkToken(){
		try {
			return userService.checkToken();
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path= "/changePassword")
	public ResponseEntity<String> changePassword(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.changePassword(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@PostMapping(path= "/forgotPassword")
	public ResponseEntity<String> forgetPassword(@RequestBody(required=true) Map<String,String> requestMap){
		try {
			return userService.forgotPassword(requestMap);
		}catch(Exception ex) {
			ex.printStackTrace();
			
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}
	

}
