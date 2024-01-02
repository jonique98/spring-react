package com.sumjo.boardback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sumjo.boardback.entity.ImgaeEntity;

@Repository
public interface ImageRepository extends JpaRepository<ImgaeEntity, Integer>{
	
}
