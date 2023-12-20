package com.sumjo.boardback.common;

public interface ResponseMessage {

	String SUCCESS = "Success.";

	// HTTP Status 400
	String VALIDATION_FAILED = "Validation Failed.";
	String DUPLICATE_EMAIL = "Duplicate email.";
	String DUPLICATE_NICKNAME = "Duplicate nickname.";
	String DUPLICATE_TEL_NUMBER = "Duplicate tel number.";
	String NOT_EXISTED_USER = "Not existed user.";
	String NOT_EXISTED_BOARD = "not existed board.";

	// HTTP Status 401
	String SIGN_IN_FAIL = "Login information mismatch.";
	String AUTHORIZATION_FAIL = "Authorization fail.";

	//HTTP Status 403
	String NO_PERMISSION = "Do not have permision";

	//HTTP Status 500
	String DATABASE_ERROR = "Database error.";
}
