package com.sumjo.boardback.service.implement;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sumjo.boardback.dto.request.board.PostCommentRequestDto;
import com.sumjo.boardback.dto.request.board.PostboardRequestDto;
import com.sumjo.boardback.dto.response.ResponseDto;
import com.sumjo.boardback.dto.response.board.GetBoardResponseDto;
import com.sumjo.boardback.dto.response.board.GetFavoriteListResponseDto;
import com.sumjo.boardback.dto.response.board.IncreaseViewCountResponseDto;
import com.sumjo.boardback.dto.response.board.PostBoardResponseDto;
import com.sumjo.boardback.dto.response.board.PostCommentResponseDto;
import com.sumjo.boardback.dto.response.board.PutFavoriteResponseDto;
import com.sumjo.boardback.entity.BoardEntity;
import com.sumjo.boardback.entity.CommentEntity;
import com.sumjo.boardback.entity.FavoriteEntity;
import com.sumjo.boardback.entity.ImageEntity;
import com.sumjo.boardback.repository.BoardRepository;
import com.sumjo.boardback.repository.CommentRepository;
import com.sumjo.boardback.repository.FavoriteRepository;
import com.sumjo.boardback.repository.ImageRepository;
import com.sumjo.boardback.repository.UserRepository;
import com.sumjo.boardback.repository.resultSet.GetBoardResultSet;
import com.sumjo.boardback.repository.resultSet.GetFavoriteListResultSet;
import com.sumjo.boardback.service.BoardService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BoardServiceImplement implements BoardService{

	private final UserRepository userRepository;
	private final ImageRepository imageRepository;
	private final BoardRepository boardRepository;
	private final FavoriteRepository favoriteRepository;
	private final CommentRepository commentRepository;

	@Override
	public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber) {

		GetBoardResultSet resultSet = null;
		List<ImageEntity> imageEntities = new ArrayList<>();

		try {
			resultSet = boardRepository.getBoard(boardNumber);
			if (resultSet == null) return GetBoardResponseDto.noExistBoard();

			imageEntities = imageRepository.findByBoardNumber(boardNumber);
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}

		return GetBoardResponseDto.success(resultSet, imageEntities);
	}

	@Override
	public ResponseEntity<? super GetFavoriteListResponseDto> getFavoriteList(Integer boardNumber) {


		List<GetFavoriteListResultSet> resultSets = new ArrayList<>();

		try {
			boolean existedBoard = boardRepository.existsByBoardNumber(boardNumber);
			if (!existedBoard) return GetFavoriteListResponseDto.noExistBoard();
			
			resultSets = favoriteRepository.getFavoriteList(boardNumber);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}

		return GetFavoriteListResponseDto.success(resultSets);
	}

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

	@Override
	public ResponseEntity<? super PostCommentResponseDto> postComment(PostCommentRequestDto dto, Integer boardNumber, String email) {
		try {

			BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
			if (boardEntity == null) return PostCommentResponseDto.noExistBoard();

			boolean existedUser = userRepository.existsByEmail(email);
			if (!existedUser) return PostCommentResponseDto.noExistUser();

			CommentEntity commentEntity = new CommentEntity(dto, boardNumber, email);
			commentRepository.save(commentEntity);

			boardEntity.increaseCommentCount();
			boardRepository.save(boardEntity);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}

		return PostCommentResponseDto.success();
	}

	@Override
	public ResponseEntity<? super PutFavoriteResponseDto> PutFavorite(Integer boardNumber, String email) {

		try {
			
			boolean existedUser = userRepository.existsByEmail(email);
			if (!existedUser) return PutFavoriteResponseDto.noExistUser();

			BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
			if (boardEntity == null) return PutFavoriteResponseDto.noExistBoard();

			FavoriteEntity favoriteEntity = favoriteRepository.findByBoardNumberAndUserEmail(boardNumber, email);
			if (favoriteEntity == null) {
				favoriteEntity = new FavoriteEntity(email, boardNumber);
				favoriteRepository.save(favoriteEntity);
				boardEntity.increaseFavoriteCount();
			} 
			else {
				favoriteRepository.delete(favoriteEntity);
				boardEntity.decreaseFavoriteCount();
			}

			boardRepository.save(boardEntity);

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}
		
		return PutFavoriteResponseDto.success();
	}

	@Override
	public ResponseEntity<? super IncreaseViewCountResponseDto> increaseViewCount(Integer boardNumber) {

		try{
			BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
			if (boardEntity == null) return IncreaseViewCountResponseDto.noExistBoard();
			boardEntity.increaseViewCount();
			boardRepository.save(boardEntity);
		}
		catch (Exception e) {
			e.printStackTrace();
			return ResponseDto.databaseError();
		}

		return IncreaseViewCountResponseDto.success();
	}
	
}
