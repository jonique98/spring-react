import React, { ChangeEvent, useEffect, useRef, useState } from 'react'
import './style.css'
import FavoriteItem from 'components/FavoriteItem'
import { Board, CommentListItem, FavoriteListItem } from 'types/interface'
import {commentListMock, favoriteListMock } from 'mocks'
import CommentItem from 'components/CommentItem'
import Pagination from 'components/Pagination/assets'
import defaultProfileImage from 'assets/image/default_profile.png'
import { useLoginUserStore } from 'stores'
import { useNavigate, useParams } from 'react-router-dom'
import { BOARD_PATH, BOARD_UPDATE_PATH, MAIN_PATH, USER_PATH } from 'constant'
import { getBoardRequest, getCommentListRequest, getFavoriteListRequest, increaseViewCountRequest, postCommentRequest, putFavoriteRequest } from 'apis'
import GetBoardResponseDto from 'apis/response/board/get-board.response.dto'
import { ResponseDto } from 'apis/response'
import { GetCommentListResponseDto, GetFavoriteListResponseDto, IncreaseViewCountResponseDto, PostCommentResponseDto, PutFavoriteResponseDto } from 'apis/response/board'

import dayjs from 'dayjs'
import { useCookies } from 'react-cookie'
import { PostCommentRequestDto } from 'apis/request/board'


//	component: 게시물 상세 컴포넌트		//
export default function BoardDetail() {

	//		state: 댓글 textarea 참조 상태		//
	const commentRef = useRef<HTMLTextAreaElement | null>(null);

	//		state: 게시물 번호 path variable 상태	//
	const { boardNumber } = useParams();
	//		state:로그인 유저 정보		//
	const { loginUser } = useLoginUserStore();
	//		state: 쿠키 상태		//
	const [cookies, setCookies] = useCookies();


	//		functions: 네비게이트 함수		//
	const navigator = useNavigate();


	//		function: increase view count response 처리		//
	const increaseViewCountResponse = (responseBody : IncreaseViewCountResponseDto | ResponseDto | null) => {
		if(!responseBody) return;
		const { code } = responseBody;
		if(code === 'NB') alert('게시물이 존재하지 않습니다.');
		if(code === 'DBE') alert('데이터베이스 오류가 발생했습니다.');
		return ;
	}

	//		component : 게시물 상세 상단 컴포넌트 //
	const BoardDetailTop = () => {


		//			state: 작성자 여부 상태 			//
		const [isWriter, setIsWriter] = useState<boolean>(false);

		//				state: more버튼 상태 			//
		const [board, setBoard] = useState<Board | null>(null);

		//				state: more버튼 상태 			//
		const [showMore, setShowMore] = useState<boolean>(false);


		//		function: 작성일 포맷 변경 함수		//
		const getWriteDatetimeFormat = () => {
			if (!board) return '';
			const date = dayjs(board.writeDatetime);
			return date.format('YYYY.MM.DD.');
		}

		//		function: get board response 처리		//
		const getBoardResponse = (responseBody : GetBoardResponseDto | ResponseDto | null) => {
			if(!responseBody) return;
			const { code } = responseBody;
			if(code === 'NB') alert('게시물이 존재하지 않습니다.');
			if(code === 'DBE') alert('데이터베이스 오류가 발생했습니다.');
			if(code !== 'SU'){
				navigator(MAIN_PATH());
				return;
			}

			const board: Board = { ...responseBody as GetBoardResponseDto};
			setBoard(board)

			if (!loginUser){
				setIsWriter(false);
				return;
			}
			const isWriter  = loginUser.email === board.writerEmail;
			setIsWriter(isWriter);
		}


		//			event handler: 닉네임 클릭 이벤트 처리		//
		const onNicknameClickHandler = () => {
			if(!board) return;
			navigator(USER_PATH(board.writerEmail));
		}

		//			event handler: more 버튼 클릭 이벤트 처리 //
		const onMoreButtonClickHandler = () => {
			setShowMore(!showMore);
		}

		//			event handler: update 버튼 클릭 이벤트 처리 //
		const onUpdateButtonClickHandler = () => {
			if(!board || !loginUser) return;
			if(loginUser.email !== board.writerEmail) return;
			navigator(BOARD_PATH() + '/' + BOARD_UPDATE_PATH(board.boardNumber));
		}

		//			event handler: 삭제 버튼 클릭 이벤트 처리 //
		const onDeleteButtonClickHandler = () => {
			if(!board || !loginUser) return;
			if(loginUser.email !== board.writerEmail) return;
			// TODO: 삭제 요청 보내기
			navigator(MAIN_PATH());
		}


		//			effect: 게시물 번호 path variable 바뀔 때 마다 게시물 불러오기		//
		useEffect(()=> {
			if(!boardNumber){
				navigator(MAIN_PATH());
				return;
			}

			getBoardRequest(boardNumber).then(getBoardResponse)

		}, [boardNumber])

		// if(!board) return <></>
		//render: 게시물 상세 상단 렌더링//
		return(
			<div id='board-detail-top'>
				<div className='board-detail-top-header'>
					<div className='board-detail-title'>{board?.title}</div>
					<div className='board-detail-top-sub-box'>
						<div className='board-detail-write-info-box'>
							<div className='board-detail-writer-profile-image' style={{ backgroundImage: `url(${board?.writerProfileImage ? board.writerProfileImage : defaultProfileImage})`}}></div>
							<div className='board-detail-writer-nickname' onClick={onNicknameClickHandler}>{board?.writerNickname}</div>
							<div className='board-detail-info-divider'>{'\|'}</div>
							<div className='board-detail-write-date'>{getWriteDatetimeFormat()}</div>
						</div>
						{isWriter && 
							<div className='icon-button' onClick={onMoreButtonClickHandler}>
								<div className='icon more-icon'></div>
							</div>
						}
						{showMore &&
						<div className='board-detail-more-box'>
							<div className='board-detail-update-button' onClick={onUpdateButtonClickHandler}>{'수정'}</div>
							<div className='divider'></div>
							<div className='board-detail-delete-button' onClick={onDeleteButtonClickHandler}>{'삭제'}</div>
						</div>
						}
					</div>
				</div>
				<div className='divider'></div>
				<div className='board-detail-top-main'>
					<div className='board-detail-main-text'>{board?.content}</div>
					{board?.boardImageList.map((image, index) => <img className='board-detail-main-image' src={image}></img>)}
				</div>
			</div>
		)
	}

	//		component : 게시물 상세 하단 컴포넌트 //
	const BoardDetailBottom = () => {

		//				state: 좋아요 리스트 상태			//
		const [favoriteList, setFavoriteList] = useState<FavoriteListItem[]>([]);
		//				state: 댓글 리스트 상태(임시)			//
		const [commentList, setCommentList] = useState<CommentListItem[]>([]);
		//				state: 좋아요 상태			//
		const [isFavorite, setFavorite] = useState<boolean>(false);
		//				state: 좋아요 상자 보기 상태			//
		const [showComment, setShowComment] = useState<boolean>(false);
		//				state: 댓글 상자 보기 상태			//
		const [showFavorite, setShowFavorite] = useState<boolean>(false);
		//				state: 댓글 상태			//
		const [comment, setComment] = useState<string>('');

		//				function: get favorite list 처리 함수		//
		const getFavoriteListResponse = (responseBody : GetFavoriteListResponseDto | ResponseDto | null) => {
			if(!responseBody) return;
			const { code } = responseBody;
			if (code === 'NB') alert ('존재하지 않는 게시물입니다')
			if (code === 'DBE') alert ('데이터베이스 오류가 발생했습니다.')
			if (code !== 'SU') return;

			const { favoriteList } = responseBody as GetFavoriteListResponseDto;
			setFavoriteList(favoriteList);

			if(!loginUser) {
				setFavorite(false);
				return ;
			}

			const isFavorite = favoriteList.findIndex(favorite => favorite.email === loginUser.email) !== -1;
			setFavorite(isFavorite);
		}

		//				function: get comment list response 처리 함수//
		const getCommentListResponse = (responseBody : GetCommentListResponseDto | ResponseDto | null) => {
			if(!responseBody) return;
			const { code } = responseBody;
			if(code === 'NB') alert('게시물이 존재하지 않습니다.');
			if(code === 'DBE') alert('데이터베이스 오류가 발생했습니다.');
			if(code !== 'SU') return;

			const { commentList } = responseBody as GetCommentListResponseDto;
			setCommentList(commentList);
		}

		//				function: put favorite response 처리 함수		//
		const putFavoriteResponse = (responseBody : PutFavoriteResponseDto | ResponseDto | null) => {
			if(!responseBody) return;
			const { code } = responseBody;
			if(code === 'VF') alert ('잘못된 접근입니다');
			if(code === 'NU') alert('존재하지 않는 유저입니다')
			if(code === 'NB') alert('존재하지 않는 게시물입니다')
			if(code === 'AF') alert('인증에 실패했습니다')
			if(code === 'DBE') alert('데이터베이스 오류가 발생했습니다.');
			if(code !== 'SU') return;

			if(!boardNumber) return;
			getFavoriteListRequest(boardNumber).then(getFavoriteListResponse);
		}

		// function: post comment response 처리 함수		//
		const postCommentResponse = (responseBody : PostCommentResponseDto | ResponseDto | null) => {
			if(!responseBody) return;
			const { code } = responseBody;
			if(code === 'NB') alert('게시물이 존재하지 않습니다.');
			if(code === 'DBE') alert('데이터베이스 오류가 발생했습니다.');
			if(code !== 'SU') return;

			if(!boardNumber) return;
			getCommentListRequest(boardNumber).then(getCommentListResponse);
			setComment('');
			if(!commentRef.current) return;
			commentRef.current.style.height = 'auto';
		}


		//event handler: 좋아요 클릭 이벤트 처리//
		const onFavoriteClickHander = () => {
			if(!loginUser || !cookies.accessToken || !boardNumber) return;
			putFavoriteRequest(boardNumber, cookies.accessToken).then(putFavoriteResponse);


		}

		//event handler : 좋아요 상자 보기 클릭 이벤트 처리//
		const onShowFavoriteClickHandler = () => {
			setShowFavorite(!showFavorite);
		}

		//event handler : 댓글 상자 보기 클릭 이벤트 처리//
		const onShowCommentClickHandler = () => {
			setShowComment(!showComment);
		}

		//		event handler : 댓글 작성 버튼 클릭 이벤트 처리//
		const onCommentSubmitButtonCLickHandler = () => {
			if(!comment || !boardNumber || !loginUser || !cookies.accessToken) return;
			const requestBody: PostCommentRequestDto = { content: comment };
			postCommentRequest(boardNumber, requestBody, cookies.accessToken).then(postCommentResponse);
		}

		//event handler : 댓글 입력창 변경 이벤트 처리//
		const onCommentChangeHandler = (event : ChangeEvent<HTMLTextAreaElement>) => {
			setComment(event.target.value);
			if(!commentRef.current) return;
			commentRef.current.style.height = 'auto';
			commentRef.current.style.height = commentRef.current.scrollHeight + 'px';
		}

		//  effect: 게시물 번호 path variable 바뀔 때 마다 좋아요 및 댓글 리스트 불러오기  //
		useEffect(()=> {
			if(!boardNumber) return;
			getFavoriteListRequest(boardNumber).then(getFavoriteListResponse);
			getCommentListRequest(boardNumber).then(getCommentListResponse);
			setCommentList(commentListMock);
		},[])

		//				render: 게시물 상세 하단 렌더링			//
		return(
			<div id = 'board-detail-bottom'>
				<div className='board-detail-bottom-button-box'>
					<div className='board-detail-bottom-button-group'>
						<div className='icon-button' onClick={onFavoriteClickHander}>
							{isFavorite ?
								<div className='icon favorite-fill-icon'></div> :
								<div className='icon favorite-light-icon'></div>	
							}
						</div>
						<div className='board-detail-bottom-board-text'>{`좋아요 ${favoriteList.length}`}</div>
						<div className='icon-button' onClick={onShowFavoriteClickHandler}>
							{showFavorite ?
								<div className='icon down-light-icon'></div> :
								<div className='icon up-light-icon'></div>
							}
						</div>
					</div>
					<div className='board-detail-bottom-button-group'>
						<div className='icon-button'>
							<div className='icon comment-icon'></div>
						</div>
						<div className='board-detail-bottom-board-text'>{`댓글 ${commentList.length}`}</div>
						<div className='icon-button' onClick={onShowCommentClickHandler}>
							{showComment ?
								<div className='icon down-light-icon'></div> :
								<div className='icon up-light-icon'></div>
							}
						</div>
					</div>
				</div>
				{showFavorite &&
					<div className='board-detail-bottom-favorite-box'>
						<div className='board-detail-bottom-favorite-container'>
							<div className='board-detail-bottom-favorite-title'>{'좋아요'}<span className='emphasis'>{12}</span></div>
							<div className='board-detail-bottom-favorite-contents'>
								{favoriteList.map(item => <FavoriteItem favoriteListItem={item}/>)}
							</div>
						</div>
					</div>
				}

				{showComment &&
				<div className='board-detail-bottom-comment-box'>
					<div className='board-detail-bottom-comment-container'>
						<div className='board-detail-bottom-comment-title'>{'댓글'}<span className='emphasis'>{12}</span></div>
						<div className='board-detail-bottom-comment-list-container'>
							{commentList.map(item => <CommentItem commentListItem={item}/>)}						
						</div>
						<div className='divider'></div>
						<div className='board-detail-bottom-comment-pagination-box'>
							<Pagination />
						</div>
					</div>
					{loginUser !== null &&
					<div className='board-detail-bottom-comment-input-box'>
						<div className='board-detail-bottom-comment-input-container'>
							<textarea ref={commentRef} className='board-detail-bottom-comment-textarea' placeholder='댓글을 작성해주세요' value = {comment} onChange={onCommentChangeHandler}/>
							<div className='board-detail-bottom-comment-button-box' onClick={onCommentSubmitButtonCLickHandler}>
								{comment.length > 0 ?
									<div className='black-button'>{'댓글 달기'}</div> :
									<div className='disable-button'>{'댓글 달기'}</div>
								}
							</div>
						</div>
					</div>
					}
				</div>
				}
			</div>
		)
	}

	let effectFlag = true;
	useEffect(()=> {
		if(!boardNumber)
			return;
		if(effectFlag){
			effectFlag = false;
			return;
		}
		increaseViewCountRequest(boardNumber).then(increaseViewCountResponse);
	},[boardNumber])

	//		render: 게시물 상세 렌더링		//
  return (
	<div id='board-detail-wrapper'>
	  <div className='board-detail-container'>
		<BoardDetailTop />
		<BoardDetailBottom />
	  </div>
	</div>
  )
}
