package com.sumjo.boardback.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sumjo.boardback.entity.BoardEntity;

public interface BoardRepository extends JpaRepository<BoardEntity, Integer>{
	
}
