package com.sumjo.boardback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sumjo.boardback.entity.CommentEntity;
import com.sumjo.boardback.repository.resultSet.GetCommentListResultSet;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer>{

	@Query(
		value=
		"SELECT " +
		" U.nickname AS nickname, " +
		" U.profile_image AS profile_image, " +
		" C.write_datetime AS write_datetime, " +
		" C.content AS content " +
		" from comment as C " +
		" INNER JOIN user as U " +
		" on C.user_email = U.email " +
		" where C.board_number = ?1 " +
		" ORDER BY write_datetime DESC ",
		nativeQuery = true
	)
	List<GetCommentListResultSet> getCommentList(Integer boardNumber);
	
}
