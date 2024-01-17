package com.sumjo.boardback.service.implement;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sumjo.boardback.repository.UserRepository;
import com.sumjo.boardback.dto.request.auth.SignInRequestDto;
import com.sumjo.boardback.dto.request.auth.SignUpRequestDto;
import com.sumjo.boardback.dto.response.ResponseDto;
import com.sumjo.boardback.dto.response.auth.SignInResponseDto;
import com.sumjo.boardback.dto.response.auth.SignUpResponseDto;
import com.sumjo.boardback.entity.UserEntity;
import com.sumjo.boardback.provider.Jwtprovider;
import com.sumjo.boardback.service.AuthService;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class AuthServiceImplement implements AuthService{

	private final UserRepository userRepository;
	private final Jwtprovider jwtProvider;

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
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}
	
		return SignUpResponseDto.success();
	
	}

	@Override
	public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {

		String token = null;

		try {

			String email = dto.getEmail();
			UserEntity userEntity = userRepository.findByEmail(email);
			if(userEntity == null) return SignInResponseDto.signInFailed();

			String password = dto.getPassword();
			String encodedPassword = userEntity.getPassword();
			boolean isMatched = passwordEncoder.matches(password, encodedPassword);
			if (!isMatched) return SignInResponseDto.signInFailed();

			token = jwtProvider.create(email);


		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}

		return SignInResponseDto.success(token);
	}


}
