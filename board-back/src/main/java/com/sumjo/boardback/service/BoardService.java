package com.sumjo.boardback.service;

import org.springframework.http.ResponseEntity;

import com.sumjo.boardback.dto.request.board.PostCommentRequestDto;
import com.sumjo.boardback.dto.request.board.PostboardRequestDto;
import com.sumjo.boardback.dto.response.board.GetBoardResponseDto;
import com.sumjo.boardback.dto.response.board.GetFavoriteListResponseDto;
import com.sumjo.boardback.dto.response.board.GetCommentListResponseDto;
import com.sumjo.boardback.dto.response.board.IncreaseViewCountResponseDto;
import com.sumjo.boardback.dto.response.board.PostBoardResponseDto;
import com.sumjo.boardback.dto.response.board.PostCommentResponseDto;
import com.sumjo.boardback.dto.response.board.PutFavoriteResponseDto;

public interface BoardService {
	ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber);
	ResponseEntity<? super PostBoardResponseDto> postBoard(PostboardRequestDto dto, String email);
	ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer boardNumber, String email);

	ResponseEntity<? super PutFavoriteResponseDto> PutFavorite(Integer boardNumber, String email);
	ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(Integer boardNumber);
	ResponseEntity<? super GetCommentListResponseDto> getCommentList(Integer boardNumber);

	ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(Integer boardNumber);
}
