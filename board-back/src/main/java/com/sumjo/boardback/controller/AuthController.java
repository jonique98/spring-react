package com.sumjo.boardback.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumjo.boardback.dto.request.auth.SignUpRequestDto;
import com.sumjo.boardback.dto.response.auth.SignUpResponseDto;
import com.sumjo.boardback.service.AuthService;

import lombok.RequiredArgsConstructor;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private final AuthService authService;
	
	@PostMapping("/sign-up")
	public ResponseEntity<? super SignUpResponseDto> signUp(
		@RequestBody @Valid SignUpRequestDto requestBody
		) {
			ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
			return response;
	}	
	
}
