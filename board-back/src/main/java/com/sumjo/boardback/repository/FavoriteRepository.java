package com.sumjo.boardback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumjo.boardback.entity.FavoriteEntity;
import com.sumjo.boardback.entity.primaryKey.FavoritePk;

public interface FavoriteRepository extends JpaRepository<FavoriteEntity, FavoritePk>{
	FavoriteEntity findByBoardNumberAndUserEmail(Integer boardNumber, String userEmail);
}
