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
<summary>AuthController</summary>

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
<summary>AuthService</summary>

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
<img width="695" alt="회원가입" src="https://github.com/jonique98/spring-react/assets/104954561/85870e0d-10e4-42ed-9d7e-bdbff8ea8659">
<img width="736" alt="회원가입 실패" src="https://github.com/jonique98/spring-react/assets/104954561/0d6c0842-ed07-4077-a88b-b4824a09b283">


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
<summary>AuthController</summary>

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
<summary>AuthService</summary>

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
<img width="906" alt="로그인" src="https://github.com/jonique98/spring-react/assets/104954561/8e8972c7-7ce2-4689-aaef-6cc4d97ddf10">

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
<summary>BoardController</summary>


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
<summary>BoardService</summary>

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
<img width="1237" alt="게시글 작성" src="https://github.com/jonique98/spring-react/assets/104954561/abff9f4e-a5b5-438f-a0f2-438bf03c61d7">
<img width="833" alt="스크린샷 2024-03-05 19 29 31" src="https://github.com/jonique98/spring-react/assets/104954561/e8e7ed2c-c78a-474a-bdb3-938b575e25cc">


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
<summary>BoardController</summary>

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
<summary>BoardService</summary>

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
<img width="880" alt="게시글 상세" src="https://github.com/jonique98/spring-react/assets/104954561/541c471b-fcb2-4f7b-a201-4a2a79b36ba5">






