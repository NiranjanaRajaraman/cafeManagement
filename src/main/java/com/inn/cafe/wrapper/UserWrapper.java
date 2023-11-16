package com.inn.cafe.wrapper;

import com.inn.cafe.domain.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {
	
	private String name;
	private String email;
	private String contactNumber;
	private Boolean status;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getContactNumber() {
		return contactNumber;
	}
	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}
	
	public UserWrapper(String name, String email, String contactNumber, Boolean status) {
		super();
		this.name = name;
		this.email = email;
		this.contactNumber = contactNumber;
		this.status = status;
	}
	
	public UserWrapper(User userObj) {
		this.name = userObj.getName();
		this.email = userObj.getEmail();
		this.contactNumber = userObj.getContactNumber();
		this.status = userObj.getStatus();
	}
	

}
