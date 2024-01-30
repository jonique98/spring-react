package com.sumjo.boardback.service;

import org.springframework.http.ResponseEntity;

import com.sumjo.boardback.dto.request.board.PostboardRequestDto;
import com.sumjo.boardback.dto.response.board.GetBoardResponseDto;
import com.sumjo.boardback.dto.response.board.PostBoardResponseDto;
import com.sumjo.boardback.dto.response.board.PutFavoriteResponseDto;

public interface BoardService {
	ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber);
	ResponseEntity<? super PostBoardResponseDto> postBoard(PostboardRequestDto dto, String email);
	ResponseEntity<? super PutFavoriteResponseDto> PutFavorite(Integer boardNumber, String email);
}
