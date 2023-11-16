package com.inn.cafe.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.common.base.Strings;
import com.inn.cafe.JWT.CustomerUserDetailsService;
import com.inn.cafe.JWT.JWTFilter;
import com.inn.cafe.JWT.JWTUtil;
import com.inn.cafe.data.UserRepository;
import com.inn.cafe.domain.CafeConstants;
import com.inn.cafe.domain.User;
import com.inn.cafe.service.UserService;
import com.inn.cafe.utils.EmailUtil;
import com.inn.cafe.utils.cafeUtils;
import com.inn.cafe.wrapper.UserWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	JWTFilter jwtFilter;

	@Autowired
	EmailUtil emailUtil;

	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	CustomerUserDetailsService customerUserDetailsService;

	@Autowired
	JWTUtil jwtUtil;

	private static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

	@Override
	public ResponseEntity<String> signUp(Map<String, String> requestMap) {
		LOGGER.info("Inside SignUp {}", requestMap);
		try {

			if (validateSignUpMap(requestMap)) {
				User userObj = userRepository.findByEmail(requestMap.get("email"));
				if (Objects.isNull(userObj)) {
					User userToBeSaved = getUserFromMap(requestMap);
					userRepository.save(userToBeSaved);
					return cafeUtils.getResponseEntity("SignUp successfull", HttpStatus.OK);
				} else {
					return cafeUtils.getResponseEntity("Email Already exist", HttpStatus.BAD_REQUEST);
				}
			} else {
				return cafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private boolean validateSignUpMap(Map<String, String> requestMap) {

		boolean bool = requestMap.containsKey("name") && requestMap.containsKey("contactNumber")
				&& requestMap.containsKey("email") && requestMap.containsKey("passWord");
		return bool;
	}

	private User getUserFromMap(Map<String, String> requestMap) {
		User user = new User();
		user.setName(requestMap.get("name"));
		user.setContactNumber(requestMap.get("contactNumber"));
		user.setEmail(requestMap.get("email"));
		user.setPassWord(requestMap.get("passWord"));
		user.setRole(requestMap.get("role"));
		user.setStatus(false);
		return user;
	}

	@Override
	public ResponseEntity<String> login(Map<String, String> requestMap) {
		LOGGER.info("Inside login");
		try {

			Authentication auth = authenticationManager.authenticate(
					new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("passWord")));
			if (auth.isAuthenticated()) {
				if (customerUserDetailsService.getUserDetail().getStatus().equals(true)) {
					return new ResponseEntity<String>(
							"{\"token\":\""
									+ jwtUtil.generateToken(customerUserDetailsService.getUserDetail().getEmail(),
											customerUserDetailsService.getUserDetail().getRole())
									+ "\"}",
							HttpStatus.OK);
				} else {
					return new ResponseEntity<String>("{\"message\":\"" + "Wait for admin approval." + "\"}",
							HttpStatus.BAD_REQUEST);
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<String>("{\"message\":\"" + "Bad credentials." + "\"}", HttpStatus.BAD_REQUEST);
	}

	@Override
	public ResponseEntity<List<UserWrapper>> getAllUser() {

		try {
			if (jwtFilter.isAdmin()) {
				List<UserWrapper> UserWrapperList = new ArrayList<>();
				List<User> userFromDb = userRepository.findByRole("User");
				if (Objects.nonNull(userFromDb) || userFromDb.isEmpty()) {
					userFromDb.stream().forEach(obj -> {
						UserWrapper userWrapper = new UserWrapper(obj);
						UserWrapperList.add(userWrapper);
					});
				}
				return new ResponseEntity<>(UserWrapperList, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);

			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> update(Map<String, String> requestMap) {
		try {
			if (jwtFilter.isAdmin()) {
				User userOptional = userRepository.findByEmail(requestMap.get("email"));
				if (Objects.nonNull(userOptional)) {
					userOptional.setStatus(Boolean.valueOf(requestMap.get("status")));
					userRepository.save(userOptional);
					List<User> adminList = userRepository.findByRole("admin");
					sendMailToAllAdmins(Boolean.valueOf(requestMap.get("status")), userOptional.getEmail(), adminList);
					return cafeUtils.getResponseEntity("User Status Updated Successfully!", HttpStatus.OK);
				} else {
					return cafeUtils.getResponseEntity("User Email doesn't exist!", HttpStatus.OK);
				}
			} else {
				return cafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private void sendMailToAllAdmins(Boolean status, String Useremail, List<User> adminList) throws Exception {
		if (Objects.nonNull(adminList)) {
			List<String> emailList = adminList.stream().map(obj -> obj.getEmail()).collect(Collectors.toList());
			emailList.remove(jwtFilter.getCurrentUser());
			if (Objects.nonNull(status) && status == true) {
				emailUtil.sendSimpleMessage(Useremail,
						"USER:- " + Useremail + "\n is approved by \n ADMIN:-" + jwtFilter.getCurrentUser(),
						"Account Approved", emailList);
			} else {
				emailUtil.sendSimpleMessage(Useremail,
						"USER:- " + Useremail + "\n is disabled by \n ADMIN:-" + jwtFilter.getCurrentUser(),
						"Account Disabled", emailList);
			}
		} else {
			throw new Exception("No admin emails found!");
		}

	}

	@Override
	public ResponseEntity<String> checkToken() {
		return cafeUtils.getResponseEntity("true", HttpStatus.OK);
	}

	@Override
	public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
		try {
			User userObj = userRepository.findByEmail(jwtFilter.getCurrentUser());
			if(Objects.nonNull(userObj)) {
				if(userObj.getPassWord().equals(requestMap.get("oldPassword"))) {
					userObj.setPassWord(requestMap.get("newPassword"));
					userRepository.save(userObj);
					return cafeUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
				}
				return cafeUtils.getResponseEntity("Incorrect Old Password!", HttpStatus.INTERNAL_SERVER_ERROR);
			}
			return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
		try {
			User userObj = userRepository.findByEmail(requestMap.get("email"));
			if(Objects.nonNull(userObj) && !Strings.isNullOrEmpty(userObj.getEmail())) {
				emailUtil.forgotMail(userObj.getEmail(),"Credentials by management System", userObj.getPassWord());
			}
			
			return cafeUtils.getResponseEntity("check your mail for credentials", HttpStatus.OK);
		}catch(Exception ex) {
			ex.printStackTrace();
		}
		return cafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
