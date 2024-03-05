# 블로그 프로젝트

Blog : https://sumjo.tistory.com/12

server : Spring Boot

frontend : React, Zustand

database : Mysql



## 회원가입
#### url : /sign-up 
#### Endpoint : api/v1/auth/sign-up

<details>
<summary>signUp</summary>

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
<img>

## 로그인
#### url : /sign-in
#### Endpoint : api/v1/auth/sign-in
<details>
<summary>signIn</summary>

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
<img>

## 게시글 작성
#### url : /board/write
#### Endpoint : api/v1/board/""
<details>
<summary>postBoard</summary>

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
<img>

## 게시글 조회
#### url : /board/detail/{boardNumber}
#### Endpoint : api/v1/board/{boardNumber}
<details>
<summary>getBoard</summary>

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
<img>






