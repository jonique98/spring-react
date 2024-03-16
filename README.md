# 블로그 프로젝트 (개발중)

프로젝트 설명 : https://sumjo.tistory.com/12

server : Spring Boot

frontend : React, Zustand

database : Mysql


## 데이터베이스

### ERD

<img src = "https://github.com/jonique98/spring-react/assets/104954561/149a127a-c397-49a8-933f-ad172ee867db">

<details>
<summary>BOARD</summary>
	
```sql
CREATE TABLE board
(
  board_number   INT         NOT NULL AUTO_INCREMENT COMMENT '게시물 번호',
  title          TEXT        NOT NULL COMMENT '게시물 제목',
  content        TEXT        NOT NULL COMMENT '게시물 내용',
  write_datetime DATETIME    NOT NULL COMMENT '게시물 작성 날짜',
  favorite_count INT         NOT NULL DEFAULT 0 COMMENT '좋아요 갯수',
  comment_count  INT         NOT NULL DEFAULT 0 COMMENT '댓글 갯수',
  view_count     INT         NOT NULL DEFAULT 0 COMMENT '게시물 조회수',
  writer_email   VARCHAR(50) NOT NULL COMMENT '게시물 작성자 이메일',
  PRIMARY KEY (board_number)
) COMMENT '게시물';
```
</details>

<details>
<summary>COMMENT</summary>
	
```sql
CREATE TABLE comment
(
  comment_number INT         NOT NULL COMMENT '댓글 번호',
  content        TEXT        NOT NULL COMMENT '댓글 내용',
  write_datetime DATETIME    NOT NULL COMMENT '작성 날짜',
  user_email     VARCHAR(50) NOT NULL COMMENT '사용자 이메일',
  board_number   INT         NOT NULL COMMENT '게시물 번호',
  PRIMARY KEY (comment_number)
) COMMENT '댓글 테이블';
```

</details>
<details>
<summary>USER</summary>
	
```sql
CREATE TABLE user
(
  email          VARCHAR(50)  NOT NULL COMMENT '사용자 이메일',
  password       VARCHAR(100) NOT NULL COMMENT '사용자 비',
  nickname       VARCHAR(20)  NOT NULL COMMENT '닉네임',
  tel_number     VARCHAR(15)  NOT NULL COMMENT '전화번호',
  address        TEXT         NOT NULL COMMENT '주소',
  address_detail TEXT         NULL     COMMENT '상세주소',
  profile_image  TEXT         NULL     COMMENT '프로필 이미지',
  PRIMARY KEY (email)
) COMMENT '유저 정보';
```

</details>


## 회원가입
#### url : /sign-up 
#### Endpoint : api/v1/auth/sign-up

<details>
<summary>명세</summary>

```
signUp(회원가입)

- request
{
	*email: String,
	*password: String,
	*nickname: String,
	*telNumber: String,
	*address: String,
	addressDetail: String
}

- response

성공
Http status - 200 (ok)
{
	code: "SU",
	message: "Success",
	token: "jwt...",
	expiredDate: 123456789
}

실패

- 필수 정보 미입력/ 이메일 포맷 불일치 / 비밀번호 8자 미만 / 전화번호 포멧 불일치

- 이메일 중복
Http status - 400 (Bad Request)
{
	code: "EE",
	message: "Exsited Email"
}

- 데이터베이스 에러
Http Status - 500 (Internal Server Error)
{
	code: "DE",
	message: "Database Error"
}
```
</details>

<details>
<summary>signUp(AuthController)</summary>

```java
private final AuthService authService;
	
@PostMapping("/sign-up")
public ResponseEntity<? super SignUpResponseDto> signUp(
    @RequestBody @Valid SignUpRequestDto requestBody
    ) {
        ResponseEntity<? super SignUpResponseDto> response = authService.signUp(requestBody);
        return response;
}	
```
</details>

<details>
<summary>signUp(AuthService)</summary>

```java
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
```
</details>
<img width="1440" alt="스크린샷 2024-03-17 00 11 28" src="https://github.com/jonique98/spring-react/assets/104954561/c8e9e60f-579a-44c9-83f0-64e0ca590c94">
<img width="1440" alt="스크린샷 2024-03-17 00 11 33" src="https://github.com/jonique98/spring-react/assets/104954561/6625b423-cd0b-4df7-9e9f-d4033b8345f8">
<img width="1440" alt="스크린샷 2024-03-17 00 12 39" src="https://github.com/jonique98/spring-react/assets/104954561/989bfbf1-bf4f-4802-b563-2caafd08cde0">


## 로그인
#### url : /sign-in
#### Endpoint : api/v1/auth/sign-in

<details>
<summary>명세</summary>

```
signIn(로그인)

- request
{
	*email : String,
	*password : String
}

- response
성공
Http Status - 200
{
	code: "SU",
	message: "Success"
}

실패
- 필수 정보 미입력

Http Status - 401 (Unauthriozed)
{
	code: "SF",
	message: "Sign In Failed"
}

- 데이터베이스 에러
Http Status - 500 (Internal Server Error)
{
	code: "DE",
	message: "Database Error"
}
```
</details>


<details>
<summary>signIn(AuthController)</summary>

```java
@PostMapping("/sign-in")
	public ResponseEntity<? super SignInResponseDto> signIn(
		@RequestBody @Valid SignInRequestDto requestBody
	){
		ResponseEntity<? super SignInResponseDto> response = authService.signIn(requestBody);
		return response;
	}

```
</details>

<details>
<summary>signIn(AuthService)</summary>

```java
@Override
	public ResponseEntity<? super SignInResponseDto> signIn(SignInRequestDto dto) {
		try {
			String email = dto.getEmail();
			UserEntity userEntity = userRepository.findByEmail(email);
			if(userEntity == null) return SignInResponseDto.signInFailed();

			String password = dto.getPassword();
			boolean isMatch = passwordEncoder.matches(password, userEntity.getPassword());
			if(!isMatch) return SignInResponseDto.signInFailed();

		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}
		return SignInResponseDto.success();
	}
```
</details>
<img width="1440" alt="스크린샷 2024-03-17 00 11 12" src="https://github.com/jonique98/spring-react/assets/104954561/531c76ed-773f-4fc8-8791-388b469912fc">

## 게시글 작성
#### url : /board/write
#### Endpoint : api/v1/board/""
<details>
<summary>명세</summary>

```
boardWrite(게시물 작성)

- request
{
	*title: String,
	*content: String,
	boardImageList: String[]
}

- response

성공
Http Status - 200 (ok)
{
	code: "SU",
	message: "Success"
}

실패

-데이터베이스 에러

-존재하지 않는 게시물
Http Status - 400 (Bad Request)
{
	code: "NB",
	message: "Not Exsited Board Number"
}
```
</details>

<details>
<summary>postBoard(BoardController)</summary>


```java
@PostMapping("")
	public ResponseEntity<? super PostBoardResponseDto> postBoard (
		@RequestBody @Valid PostboardRequestDto requsesBody,
		@AuthenticationPrincipal String email
	) {
		ResponseEntity<? super PostBoardResponseDto> response = boardService.postBoard(requsesBody, email);
		return response;
	}
```
</details>

<details>
<summary>postBoard(BoardService)</summary>

```java
@Override
	public ResponseEntity<? super PostBoardResponseDto> postBoard(PostboardRequestDto dto, String email) {
		try {
			UserEntity userEntity = userRepository.findByEmail(email);
			if(userEntity == null) return PostBoardResponseDto.notExsitedUser();

			BoardEntity boardEntity = new BoardEntity(dto, userEntity);
			boardRepository.save(boardEntity);
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}
		return PostBoardResponseDto.success();
	}
```
</details>
<img width="413" alt="스크린샷 2024-03-17 00 27 06" src="https://github.com/jonique98/spring-react/assets/104954561/10576347-85a9-40cf-9578-f5002592f1ce">

<img width="423" alt="스크린샷 2024-03-17 00 27 19" src="https://github.com/jonique98/spring-react/assets/104954561/7768ebc2-4fe2-4767-87e1-ca242b2cf88e">

<img width="1181" alt="스크린샷 2024-03-17 00 28 08" src="https://github.com/jonique98/spring-react/assets/104954561/c5c13f9d-360f-42b4-a1bc-1a4d905f288c">

<img width="421" alt="스크린샷 2024-03-17 00 28 18" src="https://github.com/jonique98/spring-react/assets/104954561/c8b337eb-9cb2-4b00-9d28-c593837f481a">


## 게시글 조회
#### url : /board/detail/{boardNumber}
#### Endpoint : api/v1/board/{boardNumber}

<details>
<summary>명세</summary>

```
boardDetatil(게시물 상세)

성공

Http Status - 200 (ok)
{
	code : "SU",
	message : "Success",
	boardNumber: int,
	title: String,
	content: String,
	boardImage: String[],
	writeDateTime: String,
	writerEmail: String,
	writerNickName: String,
	writerProfileImage: String
}


실패
- 존재하지 않는 게시물
Http Status - 400 (Bad Request)
{
	code: "NB",
	message: "Not Exsited Board Number"
}
```
</details>

<details>
<summary>getBoard(BoardController)</summary>

```java
@GetMapping("/{boardNumber}")
	public ResponseEntity<? super GetBoardResponseDto> getBoard (
		@PathVariable("boardNumber") Integer boardNumber
	){
		ResponseEntity<? super GetBoardResponseDto> response = boardService.getBoard(boardNumber);
		return response;
	}
```

</details>

<details>
<summary>getBoard(BoardService)</summary>

```java
@Override
	public ResponseEntity<? super GetBoardResponseDto> getBoard(Integer boardNumber) {
		try {
			BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
			if(boardEntity == null) return GetBoardResponseDto.notExsitedBoard();

			UserEntity userEntity = boardEntity.getUserEntity();
			GetBoardResponseDto response = new GetBoardResponseDto(boardEntity, userEntity);
			return ResponseEntity.ok(response);
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}
	}
```
</details>
<img width="425" alt="스크린샷 2024-03-17 00 15 20" src="https://github.com/jonique98/spring-react/assets/104954561/82eec903-e73b-4b14-a228-359b68af89fc">

## 댓글 작성
#### url : /board/detail/{boardNumber}
#### Endpoint : api/v1/board/{boardNumber}/comment

<details>
<summary>명세</summary>

```
postComment(댓글 쓰기)

- request
{
	*content: String
}

성공
- response
{
	code: "SU",
	message: "Success",
}

실패
-데이터베이스 에러

-존재하지 않는 게시물
Http Status - 400 (Bad Request)
{
	code: "NB",
	message: "Not Exsited Board Number"
}

-존재하지 않는 유저
Http Status - 400 (Bad Request)
{
	code: "NU",
	message: "Not Exsited User"
}
```

</details>

<details>
<summary>postComment(BoardController)</summary>

```java
@PostMapping("/{boardNumber}/comment")
	public ResponseEntity<? super PostCommentResponseDto> postComment (
		@PathVariable("boardNumber") Integer boardNumber,
		@RequestBody @Valid PostCommentRequestDto requestBody,
		@AuthenticationPrincipal String email
	){
		ResponseEntity<? super PostCommentResponseDto> response = boardService.postComment(boardNumber, requestBody, email);
		return response;
	}
```

</details>

<details>
<summary>postComment(BoardService)</summary>

```java
@Override
	public ResponseEntity<? super PostCommentResponseDto> postComment(Integer boardNumber, PostCommentRequestDto dto, String email) {
		try {
			BoardEntity boardEntity = boardRepository.findByBoardNumber(boardNumber);
			if(boardEntity == null) return PostCommentResponseDto.notExsitedBoard();

			UserEntity userEntity = userRepository.findByEmail(email);
			if(userEntity == null) return PostCommentResponseDto.notExsitedUser();

			CommentEntity commentEntity = new CommentEntity(dto, userEntity, boardEntity);
			commentRepository.save(commentEntity);
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseDto.databaseError();
		}
		return PostCommentResponseDto.success();
	}
```
</details>

<img width="714" alt="스크린샷 2024-03-17 00 15 35" src="https://github.com/jonique98/spring-react/assets/104954561/d58abc0c-3613-411c-b1a8-1e25ed0a3bce">
<img width="692" alt="스크린샷 2024-03-17 00 15 44" src="https://github.com/jonique98/spring-react/assets/104954561/9a9825c9-439e-49c9-b1bc-37de00d55621">
<img width="686" alt="스크린샷 2024-03-17 00 16 55" src="https://github.com/jonique98/spring-react/assets/104954561/2f3cda00-457d-4024-9902-ff7333cdbf63">







