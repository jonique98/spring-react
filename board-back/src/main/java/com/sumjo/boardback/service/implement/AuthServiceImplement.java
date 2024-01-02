package com.sumjo.boardback.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sumjo.boardback.repository.UserRepository;
import com.sumjo.boardback.dto.request.auth.SignUpRequestDto;
import com.sumjo.boardback.dto.response.ResponseDto;
import com.sumjo.boardback.dto.response.auth.SignUpResponseDto;
import com.sumjo.boardback.entity.UserEntity;
import com.sumjo.boardback.service.AuthService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService{

	private final UserRepository userRepository;

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public ResponseEntity<? super SignUpResponseDto> signUp(SignUpRequestDto dto) {

		try{

			String email = dto.getEmail();
			boolean existedEmail = userRepository.existsByEmail(email);
			if(existedEmail) return SignUpResponseDto.duplicateEmail();

			String nickname = dto.getNickname();
			boolean existedNickname = userRepository.existsByNickname(nickname);
			if(existedNickname) return SignUpResponseDto.duplicateNickname();


			String telNumber = dto.getTelNumber();
			boolean existedTelNumber = userRepository.existsByTelNumber(telNumber);
			if(existedTelNumber) return SignUpResponseDto.duplicateTelNumber();

			String password = dto.getPassword();
			String encodedPassword = passwordEncoder.encode(password);
			dto.setPassword(encodedPassword);

			UserEntity UserEntity = new UserEntity(dto);
			userRepository.save(UserEntity);

		} catch (Exception exception) {
			System.out.println("여기 에러");
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}
	
		return SignUpResponseDto.success();
	
	}


}
