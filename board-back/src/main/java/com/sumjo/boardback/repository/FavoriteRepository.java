package com.sumjo.boardback.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.sumjo.boardback.entity.FavoriteEntity;
import com.sumjo.boardback.entity.primaryKey.FavoritePk;
import com.sumjo.boardback.repository.resultSet.GetFavoriteListResultSet;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoritePk>{
	FavoriteEntity findByBoardNumberAndUserEmail(Integer boardNumber, String userEmail);

	@Query(
		value =
		"SELECT " +
		"U.email AS email, " +
		"U.nickname AS nickname, " +
		"U.profile_image AS profileImage " +
		"from favorite AS F " +
		"INNER JOIN user AS U " +
		"ON F.user_email = U.email " +
		"where F.board_number = ?1 ",
		nativeQuery = true
	)
	List<GetFavoriteListResultSet> getFavoriteList(Integer boardNumber);
}
