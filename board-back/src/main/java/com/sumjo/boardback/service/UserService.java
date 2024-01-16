package com.sumjo.boardback.service;

import org.springframework.http.ResponseEntity;

import com.sumjo.boardback.dto.response.user.GetSignInUserResponseDto;

public interface UserService {
	
	ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email);
}
