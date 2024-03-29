package com.sumjo.boardback.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sumjo.boardback.dto.response.ResponseDto;
import com.sumjo.boardback.dto.response.user.GetSignInUserResponseDto;
import com.sumjo.boardback.entity.UserEntity;
import com.sumjo.boardback.repository.UserRepository;
import com.sumjo.boardback.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImplement implements UserService {

	private final UserRepository userRepository;

	@Override
	public ResponseEntity<? super GetSignInUserResponseDto> getSignInUser(String email) {

		UserEntity userEntity = null;

		try {
			userEntity = userRepository.findByEmail(email);
			if (userEntity == null) {
				return GetSignInUserResponseDto.notExistUser();
			}
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}

		return GetSignInUserResponseDto.success(userEntity);
	}
	
}
