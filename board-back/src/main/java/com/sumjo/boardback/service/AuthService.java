package com.sumjo.boardback.service;

import org.springframework.http.ResponseEntity;

import com.sumjo.boardback.dto.request.auth.SignInRequestDto;
import com.sumjo.boardback.dto.request.auth.SignUpRequestDto;
import com.sumjo.boardback.dto.response.auth.SignInResponseDto;
import com.sumjo.boardback.dto.response.auth.SignUpResponseDto;

public interface AuthService {
	
	ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto);
	ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto);
}
