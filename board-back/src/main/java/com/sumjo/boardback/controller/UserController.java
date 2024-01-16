package com.sumjo.boardback.controller;

import org.apache.tomcat.jni.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumjo.boardback.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/test")
	public ResponseEntity<?> test() {
		return ResponseEntity.ok().body("test");
	}

	@PostMapping("/test1")
	public ResponseEntity<?> test1() {
		return ResponseEntity.ok().body("test1");
	}
}

