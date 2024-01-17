package com.sumjo.boardback.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sumjo.boardback.dto.request.board.PostboardRequestDto;
import com.sumjo.boardback.dto.response.ResponseDto;
import com.sumjo.boardback.dto.response.board.PostBoardResponseDto;
import com.sumjo.boardback.entity.BoardEntity;
import com.sumjo.boardback.entity.ImageEntity;
import com.sumjo.boardback.repository.BoardRepository;
import com.sumjo.boardback.repository.ImageRepository;
import com.sumjo.boardback.repository.UserRepository;
import com.sumjo.boardback.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplement implements BoardService{

	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	private final BoardRepository boardRepository;

	@Override
	public ResponseEntity<? super PostBoardResponseDto> postBoard(PostboardRequestDto dto, String email) {

		try {
			
			boolean existedEmail = userRepository.existsByEmail(email);
			if (!existedEmail)	return PostBoardResponseDto.notExistUser();

			BoardEntity boardEntity = new BoardEntity(dto, email);
			boardRepository.save(boardEntity);

			int boardNumber = boardEntity.getBoardNumber();

			List<String> boardImageList = dto.getBoardImageList();
			List<ImageEntity> imageEntities = new ArrayList<>();

			for (String image: boardImageList) {
				ImageEntity imageEntity = new ImageEntity(boardNumber, image);
				imageEntities.add(imageEntity);
			}
			imageRepository.saveAll(imageEntities);


		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}

		return PostBoardResponseDto.success();
	}
	
}
