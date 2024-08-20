package com.abhi.ecom.services.auth;

import com.abhi.ecom.dto.SignupRequest;
import com.abhi.ecom.dto.UserDto;

public interface AuthService {
	UserDto createUser(SignupRequest signupRequest);

	Boolean hasUserWithEmail(String email);

	void createAdminAccount();
}
