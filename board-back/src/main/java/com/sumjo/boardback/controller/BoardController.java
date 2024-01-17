package com.sumjo.boardback.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sumjo.boardback.dto.request.board.PostboardRequestDto;
import com.sumjo.boardback.dto.response.board.PostBoardResponseDto;
import com.sumjo.boardback.service.BoardService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/board")
@RequiredArgsConstructor
public class BoardController {
	
	private final BoardService boardService;

	@PostMapping("")
	public ResponseEntity<? super PostBoardResponseDto> postBoard (
		@RequestBody @Valid PostboardRequestDto requsesBody,
		@AuthenticationPrincipal String email
	) {
		ResponseEntity<? super PostBoardResponseDto> response = boardService.postBoard(requsesBody, email);
		return response;
	}
}
