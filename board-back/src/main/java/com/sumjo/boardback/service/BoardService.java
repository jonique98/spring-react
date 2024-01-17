package com.sumjo.boardback.service;

import org.springframework.http.ResponseEntity;

import com.sumjo.boardback.dto.request.board.PostboardRequestDto;
import com.sumjo.boardback.dto.response.board.PostBoardResponseDto;

public interface BoardService {
	ResponseEntity<? super PostBoardResponseDto> postBoard(PostboardRequestDto dto, String email);
}
